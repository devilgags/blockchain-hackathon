package com.mindtree.TreeFelling.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.mindtree.TreeFelling.dto.LandDto;
import com.mindtree.TreeFelling.dto.LandsDto;
import com.mindtree.TreeFelling.dto.RegisterUserDto;
import com.mindtree.TreeFelling.dto.ReturnUserDto;
import com.mindtree.TreeFelling.dto.UserDto;

public interface UserService {

	public String registerUser(RegisterUserDto registerUserDto);
	
	public UserDto getUserById(String userID);
	
	public List<LandsDto> getLandsByUserId(String userId);
	
	public String registerLand(LandDto landDto,String userId) throws FileNotFoundException, IOException;
}
