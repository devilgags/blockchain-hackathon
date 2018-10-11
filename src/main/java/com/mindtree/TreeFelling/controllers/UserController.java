package com.mindtree.TreeFelling.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mindtree.TreeFelling.dto.LandDto;
import com.mindtree.TreeFelling.dto.LandsDto;
import com.mindtree.TreeFelling.dto.RegisterUserDto;
import com.mindtree.TreeFelling.dto.ReturnUserDto;
import com.mindtree.TreeFelling.dto.UserDto;
import com.mindtree.TreeFelling.filters.JwtFilter;
import com.mindtree.TreeFelling.services.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;

@RestController
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@ApiOperation(value = "Register a land", notes = "Register a land")
	@ApiResponse(code = 200, message = "REGISTER LAND")
	@RequestMapping(value = "/api/registerLand", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> registerLand(
			@ApiParam(name = "register the Land", value = "Item to be persisted", required = true) @RequestBody LandDto landDto,
			@RequestHeader String authorization) throws FileNotFoundException, IOException {
		String userId = JwtFilter.userId;
		return ResponseEntity.ok(userService.registerLand(landDto,userId));
	}
	

	@ApiOperation(value = "Register a user", notes = "Register a user")
	@ApiResponse(code = 200, message = "REGISTER")
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> register(
			@ApiParam(name = "register the user", value = "Item to be persisted", required = true) @RequestBody RegisterUserDto registerUserDto) {

		return ResponseEntity.ok(userService.registerUser(registerUserDto));
	}


	@ApiOperation(value = "Get user by Id", notes = "Get user by Id")
	@ApiResponse(code = 200, message = "POST")
	@RequestMapping(value = "/api/user/", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<UserDto> getCompanyById(@RequestHeader String authorization, @RequestBody String userId) {

		return ResponseEntity.ok(userService.getUserById(userId));
	}
	
	@ApiOperation(value = "Get all lands for user", notes = "Get all lands")
	@ApiResponse(code = 200, message = "REGISTER")
	@RequestMapping(value = "/api/users/lands", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<LandsDto>> getAlLLandsforUser(@RequestHeader String authorization) {

		String userId = JwtFilter.userId;
		return ResponseEntity.ok(userService.getLandsByUserId(userId));
	}

}
