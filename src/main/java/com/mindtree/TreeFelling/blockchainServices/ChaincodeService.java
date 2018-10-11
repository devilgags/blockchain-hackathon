/**
 * 
 */
package com.mindtree.TreeFelling.blockchainServices;

import org.hyperledger.fabric.sdk.Channel;
import org.json.JSONObject;

import com.mindtree.TreeFelling.blockchainConfig.SampleOrg;



public interface ChaincodeService {

	public Channel reconstructChannel() throws Exception;

	public String enrollAndRegister(String uname);

	public Channel constructChannel() throws Exception;

	public String installChaincode(String chaincodeName);

	public String instantiateChaincode(String chaincodeName, String chaincodeFunction, String[] chaincodeArgs);

	public String invokeChaincode(String chaincodename, String chaincodeFunction, String[] chaincodeArgs, String userName);

	public String queryChaincode(String chaincodename, String chaincodeFunction, String[] chaincodeArgs);

	public void blockchainInfo(SampleOrg sampleOrg, Channel channel);

}
