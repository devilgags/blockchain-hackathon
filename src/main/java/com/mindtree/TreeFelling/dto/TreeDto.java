package com.mindtree.TreeFelling.dto;

public class TreeDto {

	private String TreeId;
	private String Location;
	private String SurveyNo;
	private String ApproxTreeArea;
	private String PermitId;
	private String YearOfPlantation;
	private String Status;
	private String LandId;
	private String RevenueDocHash;
	private String SurveyDocHash;
	private String RevenueId;
	private String createdOn;

	public String getRevenueDocHash() {
		return RevenueDocHash;
	}

	public void setRevenueDocHash(String revenueDocHash) {
		RevenueDocHash = revenueDocHash;
	}

	public String getSurveyDocHash() {
		return SurveyDocHash;
	}

	public void setSurveyDocHash(String surveyDocHash) {
		SurveyDocHash = surveyDocHash;
	}

	public String getRevenueId() {
		return RevenueId;
	}

	public void setRevenueId(String revenueId) {
		RevenueId = revenueId;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getTreeId() {
		return TreeId;
	}

	public void setTreeId(String treeId) {
		TreeId = treeId;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public String getSurveyNo() {
		return SurveyNo;
	}

	public void setSurveyNo(String surveyNo) {
		SurveyNo = surveyNo;
	}

	public String getApproxTreeArea() {
		return ApproxTreeArea;
	}

	public void setApproxTreeArea(String approxTreeArea) {
		ApproxTreeArea = approxTreeArea;
	}

	public String getPermitId() {
		return PermitId;
	}

	public void setPermitId(String permitId) {
		PermitId = permitId;
	}

	public String getYearOfPlantation() {
		return YearOfPlantation;
	}

	public void setYearOfPlantation(String yearOfPlantation) {
		YearOfPlantation = yearOfPlantation;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getLandId() {
		return LandId;
	}

	public void setLandId(String landId) {
		LandId = landId;
	}

	public TreeDto(String treeId, String location, String surveyNo, String approxTreeArea, String permitId,
			String yearOfPlantation, String status, String landId, String revenueDocHash, String surveyDocHash,
			String revenueId, String createdOn) {
		super();
		TreeId = treeId;
		Location = location;
		SurveyNo = surveyNo;
		ApproxTreeArea = approxTreeArea;
		PermitId = permitId;
		YearOfPlantation = yearOfPlantation;
		Status = status;
		LandId = landId;
		RevenueDocHash = revenueDocHash;
		SurveyDocHash = surveyDocHash;
		RevenueId = revenueId;
		this.createdOn = createdOn;
	}

	public TreeDto() {
		super();
		// TODO Auto-generated constructor stub
	}

}
