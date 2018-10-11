package com.mindtree.TreeFelling.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.mindtree.TreeFelling.dto.LandsDto;
import com.mindtree.TreeFelling.dto.TreeDto;
import com.mindtree.TreeFelling.dto.TreePermitDto;

public interface TreeFellingService {
	
	public String addTree(TreeDto treeDto, String userId) throws FileNotFoundException, IOException;
	
	public LandsDto getLandByLandId(String landId,String userId);
	
	public List<TreeDto> getTreesByLandId(String landId,String userId);

	public String createPermitRequest(TreePermitDto permitDto, String userId);

	public TreePermitDto getPermitById(String permitId, String userId);

	public List<TreePermitDto> getAllPermits(String userId);

	public String updatePermitRequest(String permitId, String action, String userId);

}
