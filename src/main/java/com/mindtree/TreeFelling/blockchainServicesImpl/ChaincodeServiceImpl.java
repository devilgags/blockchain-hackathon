/**
 * 
 */
package com.mindtree.TreeFelling.blockchainServicesImpl;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Hex;
import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.hyperledger.fabric.sdk.BlockchainInfo;
import org.hyperledger.fabric.sdk.ChaincodeEndorsementPolicy;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.ChannelConfiguration;
import org.hyperledger.fabric.sdk.EventHub;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.InstallProposalRequest;
import org.hyperledger.fabric.sdk.InstantiateProposalRequest;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;
import org.hyperledger.fabric.sdk.SDKUtils;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.TransactionEventException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.mindtree.TreeFelling.blockchainConfig.ConnectionConfig;
import com.mindtree.TreeFelling.blockchainConfig.ConnectionUtil;
import com.mindtree.TreeFelling.blockchainConfig.SampleOrg;
import com.mindtree.TreeFelling.blockchainConfig.SampleStore;
import com.mindtree.TreeFelling.blockchainConfig.SampleUser;
import com.mindtree.TreeFelling.blockchainServices.ChaincodeService;
import com.mindtree.TreeFelling.exception.BlockchainException;



@PropertySource("hyperledger.properties")
@Service
public class ChaincodeServiceImpl implements ChaincodeService {

	private static final Logger logger = LoggerFactory.getLogger(ChaincodeServiceImpl.class);

	private static final ConnectionConfig connectionConfig = ConnectionConfig.getConfig();

	// private static String FIXTURES_PATH = System.getProperty("user.dir");

	@Value("${ADMIN_NAME}")
	private String ADMIN_NAME;

	// @Value("${FIXTURES_PATH}")
	// private String FIXTURES_PATH;

	@Value("${CHAIN_CODE_PATH}")
	private String CHAIN_CODE_PATH;

	@Value("${CHAIN_CODE_VERSION}")
	private String CHAIN_CODE_VERSION;

	@Value("${CHANNEL_NAME}")
	private String CHANNEL_NAME;

	int counter = 0;
	int enrollmentCheckFlag = 0;

	String TxID = null; // save the CC invoke TxID and use in queries

	// private final ConnectionConfigHelper configHelper = new
	// ConnectionConfigHelper();

	ChaincodeID chaincodeID;

	private Collection<SampleOrg> SampleOrgs;

	HFClient client = HFClient.createNewInstance();

	Collection<ProposalResponse> responses;
	Collection<ProposalResponse> successful = new LinkedList<>();
	Collection<ProposalResponse> failed = new LinkedList<>();

	static void out(String format, Object... args) {
		System.err.flush();
		System.out.flush();
		System.out.println(format(format, args));
		System.err.flush();
		System.out.flush();

	}

	private void waitOnFabric(int additional) {
		// wait a few seconds for the peers to catch up with each other via the
		// gossip network.
		// Another way would be to wait on all the peers event hubs for the
		// event containing the transaction TxID
		// try {
		// out("Wait %d milliseconds for peers to sync with each other",
		// gossipWaitTime + additional);
		// TimeUnit.MILLISECONDS.sleep(gossipWaitTime + additional);
		// } catch (InterruptedException e) {
		// fail("should not have jumped out of sleep mode. No other threads
		// should be running");
		// }
	}

	public ChaincodeID getChaincodeId(String name) {
		chaincodeID = ChaincodeID.newBuilder().setName(name).setVersion(CHAIN_CODE_VERSION).setPath(CHAIN_CODE_PATH)
				.build();
		return chaincodeID;
	}

	public void checkConfig() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException, MalformedURLException, BlockchainException {

		SampleOrgs = connectionConfig.getSampleOrgs();
		if (counter == 0) {
			try {
				client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
				counter++;
			} catch (CryptoException | InvalidArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new BlockchainException("Exception in config");
			}
		}

		// Set up hfca for each sample org
		for (SampleOrg sampleOrg : SampleOrgs) {
			sampleOrg.setCAClient(HFCAClient.createNewInstance(sampleOrg.getCALocation(), sampleOrg.getCAProperties()));
		}
	}

	public String loadUserFromPersistence(String name) throws BlockchainException {

		// File sampleStoreFile = new File(FIXTURES_PATH +
		// "/util/HyperledgerEnroll.properties");
		File sampleStoreFile = new File("util/HyperledgerEnroll.properties");
		//File sampleStoreFile = new File("C:\\Users\\M1039224\\Documents\\Smart-Retail_Services\\util\\HyperledgerEnroll.properties");		

		final SampleStore sampleStore = new SampleStore(sampleStoreFile);
		for (SampleOrg sampleOrg : SampleOrgs) {

			final String orgName = sampleOrg.getName();
			SampleUser admin = sampleStore.getMember(ADMIN_NAME, orgName);
			sampleOrg.setAdmin(admin); // The admin of this org.

			// No need to enroll or register all done in End2endIt !
			SampleUser user = sampleStore.getMember(name, orgName);
			sampleOrg.addUser(user); // Remember user belongs to this Org

			sampleOrg.setPeerAdmin(sampleStore.getMember(orgName + "Admin", orgName));

			final String sampleOrgName = sampleOrg.getName();
			final String sampleOrgDomainName = sampleOrg.getDomainName();

			SampleUser peerOrgAdmin;
			try {
				peerOrgAdmin = sampleStore.getMember(sampleOrgName + "Admin", sampleOrgName, sampleOrg.getMSPID(),
						ConnectionUtil.findFileSk(Paths.get(connectionConfig.getTestChannelPath(),
								"crypto-config/peerOrganizations/", sampleOrgDomainName,
								format("/users/Admin@%s/msp/keystore", sampleOrgDomainName)).toFile()),
						Paths.get(connectionConfig.getTestChannelPath(), "crypto-config/peerOrganizations/",
								sampleOrgDomainName, format("/users/Admin@%s/msp/signcerts/Admin@%s-cert.pem",
										sampleOrgDomainName, sampleOrgDomainName))
								.toFile());

				sampleOrg.setPeerAdmin(peerOrgAdmin);
			} catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new BlockchainException("Exception in loading from persistence");
			}

		}
		return "Successfully loaded member from persistence";

	}


	@Override
	public Channel reconstructChannel() throws Exception {
		// TODO Auto-generated method stub
		checkConfig();
		SampleOrg sampleOrg = connectionConfig.getSampleOrg("peerOrg1");

		client.setUserContext(sampleOrg.getPeerAdmin());
		Channel newChannel = client.newChannel(CHANNEL_NAME);

		for (String orderName : sampleOrg.getOrdererNames()) {
			newChannel.addOrderer(client.newOrderer(orderName, sampleOrg.getOrdererLocation(orderName),
					connectionConfig.getOrdererProperties(orderName)));
		}

		for (String peerName : sampleOrg.getPeerNames()) {
			String peerLocation = sampleOrg.getPeerLocation(peerName);
			Peer peer = client.newPeer(peerName, peerLocation, connectionConfig.getPeerProperties(peerName));

			// Query the actual peer for which channels it belongs to and check
			// it belongs to this channel
			Set<String> channels = client.queryChannels(peer);
			if (!channels.contains(CHANNEL_NAME)) {
				out(format("Peer %s does not appear to belong to channel %s", peerName, CHANNEL_NAME));
			}

			newChannel.addPeer(peer);
			sampleOrg.addPeer(peer);
		}

		for (String eventHubName : sampleOrg.getEventHubNames()) {
			EventHub eventHub = client.newEventHub(eventHubName, sampleOrg.getEventHubLocation(eventHubName),
					connectionConfig.getEventHubProperties(eventHubName));
			newChannel.addEventHub(eventHub);
		}

		newChannel.initialize();

		return newChannel;
	}


	@Override
	public String enrollAndRegister(String userId) throws BlockchainException {

		// TODO Auto-generated method stub
		try {
			checkConfig();

			// File sampleStoreFile = new File(FIXTURES_PATH +
			// "/util/HyperledgerEnroll.properties");
			
			File sampleStoreFile = new File("util/HyperledgerEnroll.properties");
			//File sampleStoreFile = new File("C:\\Users\\M1039224\\Documents\\Smart-Retail_Services\\util\\HyperledgerEnroll.properties");
			

			final SampleStore sampleStore = new SampleStore(sampleStoreFile);
			for (SampleOrg sampleOrg : SampleOrgs) {

				HFCAClient ca = sampleOrg.getCAClient();
				final String orgName = sampleOrg.getName();
				final String mspid = sampleOrg.getMSPID();
				ca.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
				SampleUser admin = sampleStore.getMember(ADMIN_NAME, orgName);
				if (!admin.isEnrolled()) { // Preregistered admin only needs to
					// be enrolled with Fabric caClient.
					admin.setEnrollment(ca.enroll(admin.getName(), "adminpw"));
					admin.setMspId(mspid);
				}

				sampleOrg.setAdmin(admin); // The admin of this org --

				if (sampleStore.hasMember(userId, sampleOrg.getName())) {
					String result = loadUserFromPersistence(userId);
					return result;
				}
				SampleUser user = sampleStore.getMember(userId, sampleOrg.getName());

				if (!user.isRegistered()) { // users need to be registered AND
					// enrolled
					RegistrationRequest rr = new RegistrationRequest(user.getName(), "org1.department1");

					user.setEnrollmentSecret(ca.register(rr, admin));
					enrollmentCheckFlag++;

				}
				if (!user.isEnrolled()) {
					user.setEnrollment(ca.enroll(user.getName(), user.getEnrollmentSecret()));
					user.setMspId(mspid);
				}
				sampleOrg.addUser(user); // Remember user belongs to this Org

				final String sampleOrgName = sampleOrg.getName();
				final String sampleOrgDomainName = sampleOrg.getDomainName();

				// src/test/fixture/sdkintegration/e2e-2Orgs/channel/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore/

				SampleUser peerOrgAdmin = sampleStore.getMember(sampleOrgName + "Admin", sampleOrgName,
						sampleOrg.getMSPID(),
						ConnectionUtil.findFileSk(Paths.get(connectionConfig.getTestChannelPath(),
								"crypto-config/peerOrganizations/", sampleOrgDomainName,
								format("/users/Admin@%s/msp/keystore", sampleOrgDomainName)).toFile()),
						Paths.get(connectionConfig.getTestChannelPath(), "crypto-config/peerOrganizations/",
								sampleOrgDomainName, format("/users/Admin@%s/msp/signcerts/Admin@%s-cert.pem",
										sampleOrgDomainName, sampleOrgDomainName))
								.toFile());

				sampleOrg.setPeerAdmin(peerOrgAdmin); // A special user that can
				// create channels, join
				// peers and install
				// chaincode
				if (enrollmentCheckFlag != 0)
					return "User " + user.getName() + " Enrolled Successfuly";
				else {
					return "Successfully loaded " + user.getName() + " from persistence";
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new BlockchainException("Failed to enroll user");

		}
		throw new BlockchainException("Failed to enroll user in chaincodeserviceimpl");
	}


	@Override
	public Channel constructChannel() throws Exception {
		// TODO Auto-generated method stub
		checkConfig();

		SampleOrg sampleOrg = connectionConfig.getSampleOrg("peerOrg1");

		out("Constructing channel %s", CHANNEL_NAME);

		// Only peer Admin org
		client.setUserContext(sampleOrg.getPeerAdmin());

		Collection<Orderer> orderers = new LinkedList<>();

		for (String orderName : sampleOrg.getOrdererNames()) {

			Properties ordererProperties = connectionConfig.getOrdererProperties(orderName);

			// example of setting keepAlive to avoid timeouts on inactive http2
			// connections.
			// Under 5 minutes would require changes to server side to accept
			// faster ping rates.
			// ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveTime",
			// new Object[] {5L, TimeUnit.MINUTES});
			// ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveTimeout",
			// new Object[] {8L, TimeUnit.SECONDS});

			orderers.add(client.newOrderer(orderName, sampleOrg.getOrdererLocation(orderName), ordererProperties));
		}

		// Just pick the first orderer in the list to create the channel.

		Orderer anOrderer = orderers.iterator().next();
		orderers.remove(anOrderer);

		// ChannelConfiguration channelConfiguration = new ChannelConfiguration(
		// new File(FIXTURES_PATH + "/e2e-2Orgs/channel/" + CHANNEL_NAME +
		// ".tx"));
		ChannelConfiguration channelConfiguration = new ChannelConfiguration(
				new File("artifacts/channel/" + CHANNEL_NAME + ".tx"));
		// Create channel that has only one signer that is this orgs peer admin.
		// If channel creation policy needed more signature they would need to
		// be added too.
		Channel newChannel = client.newChannel(CHANNEL_NAME, anOrderer, channelConfiguration,
				client.getChannelConfigurationSignature(channelConfiguration, sampleOrg.getPeerAdmin()));

		out("Created channel %s", CHANNEL_NAME);

		for (String peerName : sampleOrg.getPeerNames()) {
			String peerLocation = sampleOrg.getPeerLocation(peerName);

			Properties peerProperties = connectionConfig.getPeerProperties(peerName); // test
			// properties
			// for
			// peer..
			// if
			// any.
			if (peerProperties == null) {
				peerProperties = new Properties();
			}
			// Example of setting specific options on grpc's NettyChannelBuilder
			// peerProperties.put("grpc.NettyChannelBuilderOption.maxInboundMessageSize",
			// 9000000);

			Peer peer = client.newPeer(peerName, peerLocation, peerProperties);
			newChannel.joinPeer(peer);
			out("Peer %s joined channel %s", peerName, CHANNEL_NAME);
			sampleOrg.addPeer(peer);
		}

		for (Orderer orderer : orderers) { // add remaining orderers if any.
			newChannel.addOrderer(orderer);
		}

		for (String eventHubName : sampleOrg.getEventHubNames()) {

			final Properties eventHubProperties = connectionConfig.getEventHubProperties(eventHubName);
			EventHub eventHub = client.newEventHub(eventHubName, sampleOrg.getEventHubLocation(eventHubName),
					eventHubProperties);
			newChannel.addEventHub(eventHub);
		}

		newChannel.initialize();

		out("Finished initialization channel %s", CHANNEL_NAME);

		return newChannel;
	}

	@Override
	public String installChaincode(String chaincodeName) {
		// TODO Auto-generated method stub
		try {
			checkConfig();

			chaincodeID = getChaincodeId(chaincodeName);
			SampleOrg sampleOrg = connectionConfig.getSampleOrg("peerOrg1");
			Channel channel = reconstructChannel();
			final String channelName = channel.getName();
			out("Running channel %s", channelName);

			client.setUserContext(sampleOrg.getPeerAdmin());
			out("Creating install proposal");
			InstallProposalRequest installProposalRequest = client.newInstallProposalRequest();
			installProposalRequest.setChaincodeID(chaincodeID);
			//installProposalRequest.setChaincodeSourceLocation(new File("C:\\Users\\M1039224\\Documents\\Smart-Retail_Services\\chaincode"));
			installProposalRequest.setChaincodeSourceLocation(new File("chaincode"));
			installProposalRequest.setChaincodeVersion(CHAIN_CODE_VERSION);
			out("Sending install proposal");
			int numInstallProposal = 0;

			Set<Peer> peersFromOrg = sampleOrg.getPeers();
			numInstallProposal = numInstallProposal + peersFromOrg.size();
			responses = client.sendInstallProposal(installProposalRequest, peersFromOrg);
			for (ProposalResponse response : responses) {
				if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
					out("Successful install proposal response Txid: %s from peer %s", response.getTransactionID(),
							response.getPeer().getName());
					successful.add(response);
				} else {
					failed.add(response);
				}
			}
			SDKUtils.getProposalConsistencySets(responses);
			// }
			out("Received %d install proposal responses. Successful+verified: %d . Failed: %d", numInstallProposal,
					successful.size(), failed.size());

			if (failed.size() > 0) {
				ProposalResponse first = failed.iterator().next();
				return "Not enough endorsers for install :" + successful.size() + ".  " + first.getMessage();
			}

			return "Chaincode installed successfully";

		} catch (Exception e) {
			e.printStackTrace();
			throw new BlockchainException("Chaincode installation failed in chaincodeserviceimpl");
		}

	}

	@Override
	public String instantiateChaincode(String chaincodeName, String chaincodeFunction, String[] chaincodeArgs)
			throws BlockchainException {
		// TODO Auto-generated method stub
		try {
			checkConfig();

			chaincodeID = getChaincodeId(chaincodeName);
			SampleOrg sampleOrg = connectionConfig.getSampleOrg("peerOrg1");
			Channel channel = reconstructChannel();
			final String channelName = channel.getName();
			boolean isFooChain = CHANNEL_NAME.equals(channelName);
			Collection<Orderer> orderers = channel.getOrderers();
			InstantiateProposalRequest instantiateProposalRequest = client.newInstantiationProposalRequest();
			instantiateProposalRequest.setProposalWaitTime(connectionConfig.getProposalWaitTime());
			instantiateProposalRequest.setChaincodeID(chaincodeID);
			instantiateProposalRequest.setFcn(chaincodeFunction);
			instantiateProposalRequest.setArgs(chaincodeArgs);
			Map<String, byte[]> tm = new HashMap<>();
			tm.put("HyperLedgerFabric", "InstantiateProposalRequest:JavaSDK".getBytes(UTF_8));
			tm.put("method", "InstantiateProposalRequest".getBytes(UTF_8));
			instantiateProposalRequest.setTransientMap(tm);
			ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
			// chaincodeEndorsementPolicy
			// .fromYamlFile(new File(FIXTURES_PATH +
			// "/chaincodeendorsementpolicy.yaml"));
			chaincodeEndorsementPolicy.fromYamlFile(new File("util/chaincodeendorsementpolicy.yaml"));
			//chaincodeEndorsementPolicy.fromYamlFile(new File("C:\\Users\\M1039224\\Documents\\Smart-Retail_Services\\util\\chaincodeendorsementpolicy.yaml"));
			
			instantiateProposalRequest.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);

			successful.clear();
			failed.clear();

			if (isFooChain) { // Send responses both ways with specifying peers
				// and by using those on the channel.
				responses = channel.sendInstantiationProposal(instantiateProposalRequest, channel.getPeers());
			} else {
				responses = channel.sendInstantiationProposal(instantiateProposalRequest);

			}
			for (ProposalResponse response : responses) {
				if (response.isVerified() && response.getStatus() == ProposalResponse.Status.SUCCESS) {
					successful.add(response);
					out("Succesful instantiate proposal response Txid: %s from peer %s", response.getTransactionID(),
							response.getPeer().getName());
				} else {
					failed.add(response);
				}
			}
			out("Received %d instantiate proposal responses. Successful+verified: %d . Failed: %d", responses.size(),
					successful.size(), failed.size());
			if (failed.size() > 0) {
				ProposalResponse first = failed.iterator().next();
			}

			///////////////
			/// Send instantiate transaction to orderer
			out("orderers", orderers);
			channel.sendTransaction(successful, orderers).thenApply(transactionEvent -> {

				waitOnFabric(0);

				out("transaction event is valid", transactionEvent.isValid()); // must
				// be
				// valid
				// to
				// be
				// here.
				out("Finished instantiate transaction with transaction id %s", transactionEvent.getTransactionID());

				return null;
			}).exceptionally(e -> {
				if (e instanceof TransactionEventException) {
					BlockEvent.TransactionEvent te = ((TransactionEventException) e).getTransactionEvent();
					if (te != null) {
						out("Transaction with txid %s failed. %s", te.getTransactionID(), e.getMessage());
					}
				}
				out("Test failed with %s exception %s", e.getClass().getName(), e.getMessage());
				return null;
			}).get(connectionConfig.getTransactionWaitTime(), TimeUnit.SECONDS);

			return "Chaincode instantiated Successfully";

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new BlockchainException("Chaincode instantiated failed in chaincodeserviceimpl");

		}
	}


	@Override
	public String invokeChaincode(String chaincodename, String chaincodeFunction, String[] chaincodeArgs,
			String userId) throws BlockchainException {
		// TODO Auto-generated method stub
		try {
			checkConfig();

			chaincodeID = getChaincodeId(chaincodename);
			SampleOrg sampleOrg = connectionConfig.getSampleOrg("peerOrg1");
			Channel channel = reconstructChannel();
			successful.clear();
			failed.clear();

			if (client.getUserContext() == null)
				client.setUserContext(sampleOrg.getUser(userId));

			///////////////
			/// Send transaction proposal to all peers
			TransactionProposalRequest transactionProposalRequest = client.newTransactionProposalRequest();
			transactionProposalRequest.setChaincodeID(chaincodeID);
			transactionProposalRequest.setFcn(chaincodeFunction);
			transactionProposalRequest.setProposalWaitTime(connectionConfig.getProposalWaitTime());
			transactionProposalRequest.setArgs(chaincodeArgs);

			Map<String, byte[]> tm2 = new HashMap<>();
			tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
			tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
			tm2.put("result", ":)".getBytes(UTF_8)); /// This should be returned
			/// see chaincode.
			transactionProposalRequest.setTransientMap(tm2);
			Collection<ProposalResponse> transactionPropResp = channel
					.sendTransactionProposal(transactionProposalRequest, channel.getPeers());
			for (ProposalResponse response : transactionPropResp) {
				if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
					out("Successful transaction proposal response Txid: %s from peer %s", response.getTransactionID(),
							response.getPeer().getName());
					successful.add(response);
				} else {
					failed.add(response);
				}
			}
			Collection<Set<ProposalResponse>> proposalConsistencySets = SDKUtils
					.getProposalConsistencySets(transactionPropResp);
			if (proposalConsistencySets.size() != 1) {
				out(format("Expected only one set of consistent proposal responses but got %d",
						proposalConsistencySets.size()));
			}

			out("Received %d transaction proposal responses. Successful+verified: %d . Failed: %d",
					transactionPropResp.size(), successful.size(), failed.size());
			if (failed.size() > 0) {
				ProposalResponse firstTransactionProposalResponse = failed.iterator().next();
				out("Not enough endorsers for invoke(move a,b,100):" + failed.size() + " endorser error: "
						+ firstTransactionProposalResponse.getMessage() + ". Was verified: "
						+ firstTransactionProposalResponse.isVerified());
			}
			out("Successfully received transaction proposal responses.");
			ProposalResponse resp = transactionPropResp.iterator().next();
			byte[] x = resp.getChaincodeActionResponsePayload(); // This is the
			// data
			// returned
			// by the
			// chaincode.
			String resultAsString = null;
			if (x != null) {
				resultAsString = new String(x, "UTF-8");
			}

			ChaincodeID cid = resp.getChaincodeID();

			////////////////////////
			// Send Transaction Transaction to orderer
			logger.info("Sending transaction");
			channel.sendTransaction(successful).thenApply(transactionEvent -> {

				waitOnFabric(0);

				logger.info("transaction event is valid", transactionEvent.isValid()); // must
				// be
				// valid
				// to
				// be
				// here.
				logger.info("Finished invoke transaction with transaction id %s", transactionEvent.getTransactionID());

				return "Chaincode invoked successfully " + transactionEvent.getTransactionID();
			}).exceptionally(e -> {
				if (e instanceof TransactionEventException) {
					BlockEvent.TransactionEvent te = ((TransactionEventException) e).getTransactionEvent();
					if (te != null) {
						out("Transaction with txid %s failed. %s", te.getTransactionID(), e.getMessage());
					}
				}
				return "Error";
			}).get(connectionConfig.getTransactionWaitTime(), TimeUnit.SECONDS);
		} catch (Exception e) {
			out("Caught an exception while invoking chaincode");
			logger.error(e.getMessage());
			throw new BlockchainException("Caught an exception while invoking chaincode");
		}
		

		return "Transaction invoked successfully";
	}

	@Override
	public String queryChaincode(String name, String chaincodeFunction, String[] chaincodeArgs)
			throws BlockchainException {
		// TODO Auto-generated method stub
		try {
			checkConfig();

			SampleOrg sampleOrg = connectionConfig.getSampleOrg("peerOrg1");

			chaincodeID = getChaincodeId(name);
			Channel channel = reconstructChannel();
			QueryByChaincodeRequest queryByChaincodeRequest = client.newQueryProposalRequest();
			queryByChaincodeRequest.setArgs(chaincodeArgs);
			queryByChaincodeRequest.setFcn(chaincodeFunction);
			queryByChaincodeRequest.setChaincodeID(chaincodeID);

			Map<String, byte[]> tm2 = new HashMap<>();
			tm2.put("HyperLedgerFabric", "QueryByChaincodeRequest:JavaSDK".getBytes(UTF_8));
			tm2.put("method", "QueryByChaincodeRequest".getBytes(UTF_8));
			queryByChaincodeRequest.setTransientMap(tm2);

			Collection<ProposalResponse> queryProposals = channel.queryByChaincode(queryByChaincodeRequest,
					channel.getPeers());
			for (ProposalResponse proposalResponse : queryProposals) {
				if (!proposalResponse.isVerified() || proposalResponse.getStatus() != ProposalResponse.Status.SUCCESS) {
					out("Failed query proposal from peer " + proposalResponse.getPeer().getName() + " status: "
							+ proposalResponse.getStatus() + ". Messages: " + proposalResponse.getMessage()
							+ ". Was verified : " + proposalResponse.isVerified());
				} else {
					String payload = proposalResponse.getProposalResponse().getResponse().getPayload().toStringUtf8();
					return payload;
				}

			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new BlockchainException("Exception in querychaincode in chaincodeserviceimpl");
		}
		return null;
	}


	@Override
	public void blockchainInfo(SampleOrg sampleOrg, Channel channel) throws BlockchainException {
		// TODO Auto-generated method stub

		try {
			checkConfig();

			String channelName = channel.getName();
			Set<Peer> peerSet = sampleOrg.getPeers();
			// Peer queryPeer = peerSet.iterator().next();
			// out("Using peer %s for channel queries", queryPeer.getName());

			BlockchainInfo channelInfo = channel.queryBlockchainInfo();
			out("Channel info for : " + channelName);
			out("Channel height: " + channelInfo.getHeight());

			String chainCurrentHash = Hex.encodeHexString(channelInfo.getCurrentBlockHash());
			String chainPreviousHash = Hex.encodeHexString(channelInfo.getPreviousBlockHash());
			out("Chain current block hash: " + chainCurrentHash);
			out("Chainl previous block hash: " + chainPreviousHash);

			// Query by block number. Should return latest block, i.e. block
			// number 2
			BlockInfo returnedBlock = channel.queryBlockByNumber(channelInfo.getHeight() - 1);
			String previousHash = Hex.encodeHexString(returnedBlock.getPreviousHash());
			out("queryBlockByNumber returned correct block with blockNumber " + returnedBlock.getBlockNumber()
					+ " \n previous_hash " + previousHash);

			// Query by block hash. Using latest block's previous hash so should
			// return block number 1
			byte[] hashQuery = returnedBlock.getPreviousHash();
			returnedBlock = channel.queryBlockByHash(hashQuery);
			out("queryBlockByHash returned block with blockNumber " + returnedBlock.getBlockNumber());

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new BlockchainException("Exception in blockchaininfo in chaincodeserviceimpl");
		}

	}

}
