/**
 * 
 */
package com.mindtree.TreeFelling.controllers;

import java.util.List;

import org.hyperledger.fabric.sdk.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mindtree.TreeFelling.blockchainServices.ChaincodeService;
import com.mindtree.TreeFelling.dto.ChaincodeNameDto;
import com.mindtree.TreeFelling.dto.FunctionAndArgsDto;
import com.mindtree.TreeFelling.filters.JwtFilter;



/**
 * 
 * @author m1039224
 *
 */

@RestController
public class BlockchainController {

	private static final Logger logger = LoggerFactory.getLogger(BlockchainController.class);

	@Autowired
	private ChaincodeService chaincodeService;



	/**
	 * Return the channel that has been created , it takes the JWT token of the
	 * user that is constructing it.
	 * 
	 * @param Authorization
	 * @returns the channel that has been created
	 */
	@RequestMapping(value = "/api/construct", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Channel createChannel(@RequestHeader String Authorization) throws Exception {
		String uname = JwtFilter.userId;
		logger.debug(uname);
		String result = chaincodeService.enrollAndRegister(uname);
		if (result != "Failed to enroll user") {

			return chaincodeService.constructChannel();
		} else {
			return null;
		}
	}

	/**
	 * 
	 * Return the channel that has been recreated , it takes the JWT token of
	 * the user that is constructing it.
	 * 
	 * @param Authorization
	 * @returns the channel that has been recreated
	 */
	@RequestMapping(value = "/api/reconstruct", method = RequestMethod.PUT)
	public Channel recreateChannel(@RequestHeader String Authorization) throws Exception {

		String uname = JwtFilter.userId;

		String result = chaincodeService.enrollAndRegister(uname);
		if (result != "Failed to enroll user") {

			return chaincodeService.reconstructChannel();
		} else {
			return null;
		}
	}

	/**
	 * takes as input chaincode name and authorization token and returns status
	 * message as string for installation of chaincode.
	 * 
	 * @param chaincodeName
	 * @param Authorization
	 * @return the status as string
	 * @throws Exception
	 */
	@RequestMapping(value = "/api/install", method = RequestMethod.POST)
	public String installChaincode(@RequestBody ChaincodeNameDto chaincodeName, @RequestHeader String Authorization)
			throws Exception {
		String uname = JwtFilter.userId;
		String result = chaincodeService.enrollAndRegister(uname);
		if (result != "Failed to enroll user") {
			return chaincodeService.installChaincode(chaincodeName.getChaincodeName());
		} else {
			return null;
		}
	}

	/**
	 * takes input as function name (init), arguments , chaincode name and
	 * authorization token.
	 * 
	 * @param Authorization
	 * @return status as string
	 */
	@RequestMapping(value = "/api/instantiate", method = RequestMethod.POST)
	public String instantiateChaincode(@RequestBody FunctionAndArgsDto chaincodeDto,
			@RequestHeader String Authorization) throws Exception {

		if ((chaincodeDto.getFunction()) == null) {
			return "function not present in method body";
		}
		if (chaincodeDto.getArgs() == null) {
			return "args not present in method body";
		}
		String uname = JwtFilter.userId;
		String result = chaincodeService.enrollAndRegister(uname);
		if (result != "Failed to enroll user") {
			return chaincodeService.instantiateChaincode(chaincodeDto.getChaincodeName(), chaincodeDto.getFunction(),
					chaincodeDto.getArgs());
		} else {
			return null;
		}
	}


}
