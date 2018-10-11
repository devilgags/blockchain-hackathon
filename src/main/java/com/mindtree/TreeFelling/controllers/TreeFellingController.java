package com.mindtree.TreeFelling.controllers;


import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mindtree.TreeFelling.dto.LandsDto;
import com.mindtree.TreeFelling.dto.ReturnUserDto;
import com.mindtree.TreeFelling.dto.TreeDto;
import com.mindtree.TreeFelling.dto.TreePermitDto;
import com.mindtree.TreeFelling.filters.JwtFilter;
import com.mindtree.TreeFelling.services.DocUtilService;
import com.mindtree.TreeFelling.services.TreeFellingService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;


@RestController
public class TreeFellingController {
	
	@Autowired
	TreeFellingService treeFellingService;
	
	@Autowired
	private DocUtilService docUtilService;
	
	
	@RequestMapping(value = "/docs/{hash}", method = RequestMethod.GET, produces = "application/pdf")
	public ResponseEntity<InputStreamResource> downloadPDFFile(@PathVariable String hash)
	        throws IOException {

//	     ClassPathResource pdfFile = new ClassPathResource("pdf-sample.pdf");
		 InputStream is = new ByteArrayInputStream(docUtilService.getDocBytes(hash));

	    return ResponseEntity
	            .ok()
	            .contentType(
	                    MediaType.parseMediaType("application/octet-stream"))
	            .body(new InputStreamResource(is));
	}
	
	
	@ApiOperation(value = "Add a tree", notes = "Add a tree")
	@ApiResponse(code = 200, message = "ADD TREE")
	@RequestMapping(value = "/api/trees", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> addTree(
			@ApiParam(name = "add tree", value = "Item to be persisted", required = true) @RequestBody TreeDto treeDto,
			@RequestHeader String authorization) throws FileNotFoundException, IOException {
		String userId = JwtFilter.userId;
		String treeId=String.valueOf((new Date().getTime())+6258798983L);
		
		String surveyNo=String.valueOf((new Date().getTime())+8545678987L);
		
		treeDto.setTreeId(treeId);
		treeDto.setSurveyNo(surveyNo);
		return ResponseEntity.ok(treeFellingService.addTree(treeDto,userId));
	}
	
	@ApiOperation(value = "Get land by land id", notes = "Get land by land id")
	@ApiResponse(code = 200, message = "GET LAND INFO")
	@RequestMapping(value = "/api/lands/{landId}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<LandsDto> registerGETALL(@RequestHeader String authorization,@PathVariable String landId) {

		String userId = JwtFilter.userId;
		return ResponseEntity.ok(treeFellingService.getLandByLandId(landId, userId));
	}
	
	
	@ApiOperation(value = "Get trees by land id", notes = "Get trees by land id")
	@ApiResponse(code = 200, message = "GET TREES INFO")
	@RequestMapping(value = "/api/lands/{landId}/trees", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<TreeDto>> getTreesByLandId(@RequestHeader String authorization,@PathVariable String landId) {

		String userId = JwtFilter.userId;
		return ResponseEntity.ok(treeFellingService.getTreesByLandId(landId, userId));
	}
	
	
	@ApiOperation(value = "Create Tree Cutting permit Request", notes = "Create Tree Cutting permit Request")
	@ApiResponse(code = 200, message = "CREATE PERMIT REQUEST")
	@RequestMapping(value = "/api/treePermits", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> createPermitRequest(
			@ApiParam(name = "add permit", value = "Item to be persisted", required = true) @RequestBody TreePermitDto permitDto,
			@RequestHeader String authorization) {
		String userId = JwtFilter.userId;
		return ResponseEntity.ok(treeFellingService.createPermitRequest(permitDto,userId));
	}
	
	@ApiOperation(value = "Get all permits", notes = "Get all permits")
	@ApiResponse(code = 200, message = "GET ALL PERMITS' INFO")
	@RequestMapping(value = "/api/permits", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<TreePermitDto>> getAllPermits(@RequestHeader String authorization) {

		String userId = JwtFilter.userId;
		return ResponseEntity.ok(treeFellingService.getAllPermits(userId));
	}
	
	@ApiOperation(value = "Get permit by permit id", notes = "Get permit")
	@ApiResponse(code = 200, message = "GET PERMIT INFO")
	@RequestMapping(value = "/api/permits/{permitId}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<TreePermitDto> getPermitById(@RequestHeader String authorization,@PathVariable String permitId) {

		String userId = JwtFilter.userId;
		return ResponseEntity.ok(treeFellingService.getPermitById(permitId, userId));
	}
	
	@ApiOperation(value = "Update Tree Permit", notes = "Update Tree Permit")
	@ApiResponse(code = 200, message = "Update Permit")
	@RequestMapping(value = "/api/treePermits/action/{permitId}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> updatePermitRequest( @PathVariable String permitId,
			@RequestParam String action,@RequestHeader String authorization) {
		String userId = JwtFilter.userId;
		return ResponseEntity.ok(treeFellingService.updatePermitRequest(permitId,action,userId));
	}
	
	

}
