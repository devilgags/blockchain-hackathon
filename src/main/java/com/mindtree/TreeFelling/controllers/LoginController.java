/**
 * 
 */
package com.mindtree.TreeFelling.controllers;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mindtree.TreeFelling.dto.LoginDto;
import com.mindtree.TreeFelling.dto.LoginReturnDto;
import com.mindtree.TreeFelling.exception.ControllerException;
import com.mindtree.TreeFelling.services.LoginService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private LoginService loginService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<LoginReturnDto> login(@RequestBody LoginDto user) {

		LoginReturnDto returnUser = loginService.checkCredentials(user);
		if (returnUser != null) {

			String jwtToken = "";

			if (user.getuserId() == null) {
				return new ResponseEntity<>(new LoginReturnDto(), HttpStatus.BAD_REQUEST);
			}

			String userId = user.getuserId();

			jwtToken = Jwts.builder().setSubject(userId).claim("roles", "user").setIssuedAt(new Date())
					.signWith(SignatureAlgorithm.HS256, "secretkey").compact();

			logger.info("token: " + jwtToken);
			returnUser.setJwtToken("Bearer " + jwtToken);
			if(returnUser.getUserName().equalsIgnoreCase("NA")){
				returnUser.setJwtToken("");
			}
			return ResponseEntity.ok(returnUser);
		} else {
			throw new ControllerException("Token not verified");
		}

	}

}
