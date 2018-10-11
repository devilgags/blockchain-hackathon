package com.mindtree.TreeFelling.services;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.mindtree.TreeFelling.dto.LandDto;
import com.mindtree.TreeFelling.dto.TreeDto;

public interface DocUtilService {
	
	public String createLandDoc(LandDto dto) throws FileNotFoundException, IOException;
	
	public byte[] getDocBytes(String hash) throws IOException;
	
	public String createSurveyDoc(TreeDto dto,String userId) throws FileNotFoundException, IOException;
	
	public String createRevenueDoc(TreeDto dto,String userId) throws FileNotFoundException, IOException;
	
//	public String createLandDoc()

}
