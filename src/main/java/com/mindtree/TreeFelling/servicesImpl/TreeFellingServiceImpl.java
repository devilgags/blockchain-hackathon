package com.mindtree.TreeFelling.servicesImpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mindtree.TreeFelling.blockchainServices.ChaincodeService;
import com.mindtree.TreeFelling.dto.LandsDto;
import com.mindtree.TreeFelling.dto.TreeDto;
import com.mindtree.TreeFelling.dto.TreePermitDto;
import com.mindtree.TreeFelling.exception.BlockchainException;
import com.mindtree.TreeFelling.exception.DatabaseException;
import com.mindtree.TreeFelling.exception.JSONParsingException;
import com.mindtree.TreeFelling.services.DocUtilService;
import com.mindtree.TreeFelling.services.TreeFellingService;

@PropertySource("hyperledger.properties")
@Service
public class TreeFellingServiceImpl implements TreeFellingService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Value("${CHAINCODE_NAME}")
	private String CHAINCODE_NAME;

	@Autowired
	private ChaincodeService chaincodeService;

	@Autowired
	private DocUtilService docUtilService;

	@Override
	public String addTree(TreeDto treeDto, String userId) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		treeDto.setStatus("UNCUT");
		treeDto.setCreatedOn(new Date().toGMTString());
		treeDto.setRevenueId(String.valueOf((new Date().getTime())+56887668L));
		String surveyDocHash = docUtilService.createSurveyDoc(treeDto, userId);
		String revenueDocHash = docUtilService.createRevenueDoc(treeDto, userId);

		String result = null;
		try {

			String args[] = new String[11];
			args[0] = treeDto.getTreeId();
			args[1] = treeDto.getLocation();
			args[2] = treeDto.getSurveyNo();
			args[3] = treeDto.getApproxTreeArea();
			args[4] = treeDto.getYearOfPlantation();
			args[5] = treeDto.getStatus();
			args[6] = treeDto.getLandId();
			// args[7] = "acasjhcasyc76df23d23" + String.valueOf(new
			// Date().getTime());
			args[7] = revenueDocHash;

			// args[8] = "a2r23r23r34t36df25h5" + String.valueOf(new
			// Date().getTime())
			args[8] = surveyDocHash;
			args[9] = String.valueOf((new Date().getTime()) + 2745237842L);
			args[10] = String.valueOf(new Date().getTime());

			try {
				// BlockchainService
				chaincodeService.enrollAndRegister(userId);
				result = chaincodeService.invokeChaincode(CHAINCODE_NAME, "addTree", args, userId);
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new BlockchainException("Exception in invokechaincode in UserServiceImpl");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new DatabaseException("Exception in saving user in userServiceImpl");
		}
		if (result != null)
			return result;
		else
			return "Not Added";

	}

	@Override
	public LandsDto getLandByLandId(String landId, String userId) {
		// TODO Auto-generated method stub
		System.out.println("USER ID : " + userId);
		System.out.println("LAND ID : " + landId);
		String args[] = new String[1];
		args[0] = landId;
		String payload = "";
		try {
			// blockchain service
			System.out.println("UserID:::" + userId);
			chaincodeService.enrollAndRegister(userId);
			payload = chaincodeService.queryChaincode(CHAINCODE_NAME, "getLandsByLandId", args);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new BlockchainException("Exception in query chaincode in userserviceimpl");
		}
		System.out.println("RESULTANT PAYLOAD : " + payload);
		LandsDto landsDto = new LandsDto();
		try {
			JSONObject obj = new JSONObject(payload);
			landsDto.setUserId(userId);
			landsDto.setDistrict(obj.getString("District"));
			landsDto.setGPS(obj.getString("GPS"));
			landsDto.setLandId(landId);
			landsDto.setTaluk(obj.getString("Taluk"));
			landsDto.setTotalExtent(obj.getString("TotalExtent"));
			landsDto.setLandDocHash(obj.getString("LandDocHash"));
			landsDto.setCreatedOn(obj.getString("CreatedOn"));

		} catch (JSONException e) {
			logger.error(e.getMessage());
			throw new JSONParsingException("Exception in parsing JSON in userserviceimpl");
		}
		return landsDto;

	}

	@Override
	public List<TreeDto> getTreesByLandId(String landId, String userId) {
		// TODO Auto-generated method stub
		System.out.println("USER ID : " + userId);
		System.out.println("LAND ID : " + landId);
		String args[] = new String[1];
		args[0] = landId;
		String payload = "";
		try {
			// blockchain service
			System.out.println("UserID:::" + userId);
			chaincodeService.enrollAndRegister(userId);
			payload = chaincodeService.queryChaincode(CHAINCODE_NAME, "getTreesByLandId", args);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new BlockchainException("Exception in query chaincode in userserviceimpl");
		}
		System.out.println("RESULTANT PAYLOAD : " + payload);
		List<TreeDto> treeDTOs = new ArrayList<>();
		try {
			JSONArray jsonArr = new JSONArray(payload);
			if (jsonArr.length() <= 0) {
				return new ArrayList<TreeDto>();
			} else {
				for (int i = 0; i < jsonArr.length(); i++) {
					JSONObject obj = (JSONObject) jsonArr.get(i);
					treeDTOs.add(new TreeDto(obj.getString("TreeId"), obj.getString("Location"),
							obj.getString("SurveyNo"), obj.getString("ApproxTreeArea"), obj.getString("PermitId"),
							obj.getString("YearOfPlantation"), obj.getString("Status"), obj.getString("LandId"),
							obj.getString("RevenueDocHash"), obj.getString("SurveyDocHash"), obj.getString("RevenueId"),
							obj.getString("CreatedOn")));
				}

			}
		} catch (JSONException e) {
			logger.error(e.getMessage());
			throw new JSONParsingException("Exception in parsing JSON in userserviceimpl");
		}
		return treeDTOs;
	}

	@Override
	public String createPermitRequest(TreePermitDto permitDto, String userId) {

		String result = null;
		try {

			String args[] = new String[17];
			args[0] = String.valueOf(new Date().getTime());
			args[1] = userId;
			args[2] = "REQUESTED";
			if (permitDto.getTreeId().length > 0) {
				String treeIds = "";
				for (int i = 0; i < permitDto.getTreeId().length; i++) {
					if (i == permitDto.getTreeId().length - 1) {
						treeIds = treeIds + permitDto.getTreeId()[i];
					} else {
						treeIds = treeIds + permitDto.getTreeId()[i] + ",";
					}
				}
				args[3] = treeIds;
			} else {
				args[3] = "";
			}
			args[4] = permitDto.getLandId();
			args[5] = permitDto.getPurpose();
			args[6] = permitDto.getSurveyDocHash();
			args[7] = permitDto.getLandDocHash();
			args[8] = permitDto.getRevenueDocHash();
			args[9] = "";
			args[10] = "";
			args[11] = "";
			args[12] = "";
			args[13] = "";
			args[14] = String.valueOf(new Date().getTime());
			args[15] = "";

			args[16] = "";
			// args[9] = "a2r23r23r34t36df25h5" + String.valueOf(new
			// Date().getTime());
			if (permitDto.getSurveyNo().length > 0) {
				String surveyNos = "";
				for (int i = 0; i < permitDto.getSurveyNo().length; i++) {
					if (i == permitDto.getSurveyNo().length - 1) {
						surveyNos = surveyNos + permitDto.getSurveyNo()[i];
					} else {
						surveyNos = surveyNos + permitDto.getSurveyNo()[i] + ",";
					}
				}
				args[16] = surveyNos;
			} else {
				args[16] = "";
			}

			try {
				// BlockchainService
				chaincodeService.enrollAndRegister(userId);
				result = chaincodeService.invokeChaincode(CHAINCODE_NAME, "createPermit", args, userId);
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new BlockchainException("Exception in invokechaincode in tree service");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new DatabaseException("Exception in saving user in tree service");
		}
		if (result != null)
			return result;
		else
			return "Not Added";
	}

	@Override
	public TreePermitDto getPermitById(String permitId, String userId) {
		// TODO Auto-generated method stub
		System.out.println("USER ID : " + userId);
		System.out.println("PERMIT ID : " + permitId);
		String args[] = new String[1];
		args[0] = permitId;
		String payload = "";
		try {
			// blockchain service
			System.out.println("UserID:::" + userId);
			chaincodeService.enrollAndRegister(userId);
			payload = chaincodeService.queryChaincode(CHAINCODE_NAME, "getPermitByPermitId", args);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new BlockchainException("Exception in query chaincode in treefelling service impl");
		}
		System.out.println("RESULTANT PAYLOAD : " + payload);
		TreePermitDto treePermitDto = new TreePermitDto();
		try {
			JSONObject obj = new JSONObject(payload);
			treePermitDto.setPermitId(obj.getString("PermitId"));
			treePermitDto.setUserId(obj.getString("UserId"));
			treePermitDto.setStatus(obj.getString("Status"));
			treePermitDto.setTreeId(obj.getString("TreeId").split(","));
			treePermitDto.setSurveyNo(obj.getString("SurveyNo").split(","));
			treePermitDto.setLandId(obj.getString("LandId"));
			treePermitDto.setPurpose(obj.getString("Purpose"));
			treePermitDto.setSurveyDocHash(obj.getString("SurveyDocHash"));
			treePermitDto.setLandDocHash(obj.getString("LandDocHash"));
			treePermitDto.setRevenueDocHash(obj.getString("RevenueDocHash"));
			treePermitDto.setPoliceDocHash(obj.getString("PoliceDocHash"));
			treePermitDto.setRTODocHash(obj.getString("RTODocHash"));
			treePermitDto.setForestDocHash(obj.getString("ForestDocHash"));
			treePermitDto.setSawMillDocHash(obj.getString("SawMillDocHash"));
			treePermitDto.setBescomDocHash(obj.getString("BESCOMDocHash"));
			treePermitDto.setCreatedOn(obj.getString("CreatedOn"));
			treePermitDto.setGrantedOn(obj.getString("GrantedOn"));

		} catch (JSONException e) {
			logger.error(e.getMessage());
			throw new JSONParsingException("Exception in parsing JSON in tree felling service impl");
		}
		return treePermitDto;
	}

	@Override
	public List<TreePermitDto> getAllPermits(String userId) {
		// TODO Auto-generated method stub
		System.out.println("USER ID : " + userId);
		String args[] = new String[1];
		args[0] = userId;
		String payload = "";
		try {
			// blockchain service
			System.out.println("UserID:::" + userId);
			chaincodeService.enrollAndRegister(userId);
			payload = chaincodeService.queryChaincode(CHAINCODE_NAME, "getAllPermits", args);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new BlockchainException("Exception in query chaincode in userserviceimpl");
		}
		System.out.println("RESULTANT PAYLOAD : " + payload);
		List<TreePermitDto> permitDtos = new ArrayList<>();
		try {
			JSONArray jsonArr = new JSONArray(payload);
			if (jsonArr.length() <= 0) {
				return new ArrayList<TreePermitDto>();
			} else {
				for (int i = 0; i < jsonArr.length(); i++) {
					JSONObject obj = (JSONObject) jsonArr.get(i);
					permitDtos.add(new TreePermitDto(obj.getString("PermitId"), obj.getString("UserId"),
							obj.getString("Status"), obj.getString("TreeId").split(","), obj.getString("LandId"),
							obj.getString("SurveyDocHash"), obj.getString("LandDocHash"),
							obj.getString("RevenueDocHash"), obj.getString("PoliceDocHash"),
							obj.getString("RTODocHash"), obj.getString("ForestDocHash"),
							obj.getString("SawMillDocHash"), obj.getString("BESCOMDocHash"), obj.getString("CreatedOn"),
							obj.getString("GrantedOn"), obj.getString("Purpose"),
							obj.getString("SurveyNo").split(",")));
				}

			}
		} catch (JSONException e) {
			logger.error(e.getMessage());
			throw new JSONParsingException("Exception in parsing JSON in userserviceimpl");
		}
		return permitDtos;
	}

	@Override
	public String updatePermitRequest(String permitId, String action, String userId) {
		String result = null;
		String status = "";
		if (action.equalsIgnoreCase("grant")) {
			status = "GRANTED";
		} else {
			status = "REJECTED";
		}
		try {
			System.out.println("permit id : " + permitId + "   status : " + status + "   userId : " + userId);
			String args[] = new String[4];
			args[0] = permitId;
			args[1] = status;
			args[2] = "hvd3f3478vwedy348f6d83vyd3478" + String.valueOf(new Date().getTime());
			args[3] = String.valueOf(new Date().getTime());

			try {
				// BlockchainService
				chaincodeService.enrollAndRegister(userId);
				result = chaincodeService.invokeChaincode(CHAINCODE_NAME, "updatePermit", args, userId);
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new BlockchainException("Exception in invokechaincode in Tree Felling Service");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new DatabaseException("Exception in saving user in Tree Felling Service");
		}
		
		if (result != null){
			String resultFromBloche=callBlocheHook(status, userId);
			System.out.println("RESULT FROM BLOCHE : "+resultFromBloche);
			return result;
		}
		else
			return "Not Updated";
	}
	
	
	private String callBlocheHook(String status,String userId){
		RestTemplate restTemplate = new RestTemplate();
		String message="";
		if(status.equalsIgnoreCase("GRANTED")){
			message="Hi "+ userId+ ", Your request for permit to cut trees has been granted by the forest department !";
		}
		else{
			message="Hi "+ userId+ ", Your request for permit to cut trees has been rejected by the forest department !";

		}
		String url = "https://1398d960.ngrok.io/api/customMessages";
		JSONObject request = new JSONObject();
		request.put("message", message);

		// set headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);

		// send request and parse result
		ResponseEntity<String> hookResponse = restTemplate
		  .exchange(url, HttpMethod.POST, entity, String.class);
		if (hookResponse.getStatusCode() == HttpStatus.OK) {
			System.out.println("HOOK RESPONSE IS OK.");
		} else if (hookResponse.getStatusCode() == HttpStatus.UNAUTHORIZED) {
		  // nono... bad credentials
			System.out.println("erro in hook call");
		}
		return hookResponse.getBody();
	}

}
