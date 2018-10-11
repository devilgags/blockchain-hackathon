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
import org.springframework.stereotype.Service;

import com.mindtree.TreeFelling.blockchainServices.ChaincodeService;
import com.mindtree.TreeFelling.dto.LandDto;
import com.mindtree.TreeFelling.dto.LandsDto;
import com.mindtree.TreeFelling.dto.RegisterUserDto;
import com.mindtree.TreeFelling.dto.ReturnUserDto;
import com.mindtree.TreeFelling.dto.UserDto;
import com.mindtree.TreeFelling.exception.BlockchainException;
import com.mindtree.TreeFelling.exception.DatabaseException;
import com.mindtree.TreeFelling.exception.JSONParsingException;
import com.mindtree.TreeFelling.exception.ObjectNotFoundDatabase;
import com.mindtree.TreeFelling.exception.ServiceException;
import com.mindtree.TreeFelling.services.DocUtilService;
import com.mindtree.TreeFelling.services.UserService;

@PropertySource("hyperledger.properties")
@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Value("${CHAINCODE_NAME}")
	private String CHAINCODE_NAME;

	

	@Autowired
	private ChaincodeService chaincodeService;

	@Autowired
	private DocUtilService docUtilService;

	@Override
	public String registerUser(RegisterUserDto registerUserDto) throws ServiceException {
		// TODO Auto-generated method stub

		String result = null;
		try {

			String args[] = new String[7];
			args[0] = registerUserDto.getEmail();
			args[1] = registerUserDto.getName();
			args[2] = registerUserDto.getMobileNo();
			args[3] = registerUserDto.getUserType();
			args[4] = registerUserDto.getAadharNo();
			args[5] = registerUserDto.getAddress();
			args[6] = registerUserDto.getPassword();

			try {
				// BlockchainService
				chaincodeService.enrollAndRegister(registerUserDto.getEmail());
				result = chaincodeService.invokeChaincode(CHAINCODE_NAME, "addUser", args, registerUserDto.getEmail());
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
	public UserDto getUserById(String userId) throws ServiceException {
		// TODO Auto-generated method stub

		String args[] = new String[1];
		args[0] = userId;
		String payload = "";
		try {
			// blockchain service
			System.out.println("UserID:::" + userId);
			chaincodeService.enrollAndRegister(userId);
			payload = chaincodeService.queryChaincode(CHAINCODE_NAME, "getUserById", args);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new BlockchainException("Exception in query chaincode in userserviceimpl");
		}

		UserDto userDto = new UserDto();
		try {
			JSONObject obj = new JSONObject(payload);
			userDto.setId(obj.getString("UserId"));
			userDto.setUserName(obj.getString("UserName"));
			userDto.setPhoneNo(obj.getString("PhoneNo"));
			userDto.setPurpose(obj.getString("purpose"));

		} catch (JSONException e) {
			logger.error(e.getMessage());
			throw new JSONParsingException("Exception in parsing JSON in userserviceimpl");
		}
		return userDto;
	}

	@Override
	public List<LandsDto> getLandsByUserId(String userId) {
		// TODO Auto-generated method stub
		String[] args = new String[1];
		args[0] = userId;
		String payload = "";
		try {
			// blockchain service
			chaincodeService.enrollAndRegister(userId);
			payload = chaincodeService.queryChaincode(CHAINCODE_NAME, "getLandsByUserId", args);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new BlockchainException("Exception in quering chaincode in productsServiceImpl");
		}
		System.out.println(">>>>>>>>>PAYLOAD : " + payload);
		List<LandsDto> lands = new ArrayList<>();
		try {
			JSONArray jsonArr = new JSONArray(payload);
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject obj = (JSONObject) jsonArr.get(i);
				System.out.println("*****" + obj);
				// JSONArray treeIdArray = new JSONArray(obj.get("TreeId"));
				// String treeIds[] = new String[treeIdArray.length()];
				//
				// for (int j = 0; j < jsonArr.length(); j++) {
				// JSONObject obj1 = (JSONObject) treeIdArray.get(j);
				// treeIds[j] = obj1.toString();
				// }

				lands.add(new LandsDto(obj.getString("LandId"), obj.getString("UserId"), obj.getString("District"),
						obj.getString("Taluk"), obj.getString("GPS"), obj.getString("TotalExtent"), new String[1],
						obj.getString("CreatedOn"), obj.getString("LandDocHash")));
			}

		} catch (JSONException e) {
			System.out.println(">>>>>>>>>>>>>>>>>" + e.getMessage());
			throw new JSONParsingException("Exception in parsing JSON in productsServiceImpl");
		}
		return lands;
	}

	@Override
	public String registerLand(LandDto landDto, String userId) throws FileNotFoundException, IOException {

		// TODO Auto-generated method stub
		landDto.setLandId(String.valueOf((new Date().getTime()) + 7653739L));
		landDto.setUserId(userId);
		landDto.setCreatedOn(new Date().toGMTString());
		String landDocHash = docUtilService.createLandDoc(landDto);
		String result = null;
		try {

			System.out.println();
			String args[] = new String[8];
			args[0] = landDto.getLandId();
			args[1] = userId;
			args[2] = landDto.getDistrict();
			args[3] = landDto.getTaluk();
			args[4] = landDto.getGPS();
			args[5] = landDto.getTotalExtent();
			args[6] = landDocHash;
			args[7] = String.valueOf(new Date().getTime());

			for (int i = 0; i < args.length; i++) {
				System.out.println(">>>>>" + args[i]);
			}
			try {
				// BlockchainService
				chaincodeService.enrollAndRegister(userId);
				result = chaincodeService.invokeChaincode(CHAINCODE_NAME, "registerLand", args, userId);
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

}
