/**
 * 
 */
package com.mindtree.TreeFelling.services;


import com.mindtree.TreeFelling.dto.LoginDto;
import com.mindtree.TreeFelling.dto.LoginReturnDto;



public interface LoginService {

	public LoginReturnDto checkCredentials(LoginDto loginDto);
}
