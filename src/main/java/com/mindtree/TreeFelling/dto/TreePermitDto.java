package com.mindtree.TreeFelling.dto;

public class TreePermitDto {

	private String PermitId;
	private String UserId;
	private String Status;
	private String[] TreeId;
	private String LandId;
	private String SurveyDocHash;
	private String LandDocHash;
	private String RevenueDocHash;
	private String PoliceDocHash;
	private String RTODocHash;
	private String ForestDocHash;
	private String SawMillDocHash;
	private String BescomDocHash;
	private String CreatedOn;
	private String GrantedOn;
	private String Purpose;
	private String[] SurveyNo;
	
	
	

	public String getPermitId() {
		return PermitId;
	}
	public void setPermitId(String permitId) {
		PermitId = permitId;
	}
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		UserId = userId;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	
	
	public String[] getTreeId() {
		return TreeId;
	}
	public void setTreeId(String[] treeId) {
		TreeId = treeId;
	}
	public String[] getSurveyNo() {
		return SurveyNo;
	}
	public void setSurveyNo(String[] surveyNo) {
		SurveyNo = surveyNo;
	}
	public String getLandId() {
		return LandId;
	}
	public void setLandId(String landId) {
		LandId = landId;
	}
	public String getSurveyDocHash() {
		return SurveyDocHash;
	}
	public void setSurveyDocHash(String surveyDocHash) {
		SurveyDocHash = surveyDocHash;
	}
	public String getLandDocHash() {
		return LandDocHash;
	}
	public void setLandDocHash(String landDocHash) {
		LandDocHash = landDocHash;
	}
	public String getRevenueDocHash() {
		return RevenueDocHash;
	}
	public void setRevenueDocHash(String revenueDocHash) {
		RevenueDocHash = revenueDocHash;
	}
	public String getPoliceDocHash() {
		return PoliceDocHash;
	}
	public void setPoliceDocHash(String policeDocHash) {
		PoliceDocHash = policeDocHash;
	}
	public String getRTODocHash() {
		return RTODocHash;
	}
	public void setRTODocHash(String rTODocHash) {
		RTODocHash = rTODocHash;
	}
	public String getForestDocHash() {
		return ForestDocHash;
	}
	public void setForestDocHash(String forestDocHash) {
		ForestDocHash = forestDocHash;
	}
	public String getSawMillDocHash() {
		return SawMillDocHash;
	}
	public void setSawMillDocHash(String sawMillDocHash) {
		SawMillDocHash = sawMillDocHash;
	}
	public String getBescomDocHash() {
		return BescomDocHash;
	}
	public void setBescomDocHash(String bescomDocHash) {
		BescomDocHash = bescomDocHash;
	}
	public String getCreatedOn() {
		return CreatedOn;
	}
	public void setCreatedOn(String createdOn) {
		CreatedOn = createdOn;
	}
	public String getGrantedOn() {
		return GrantedOn;
	}
	public void setGrantedOn(String grantedOn) {
		GrantedOn = grantedOn;
	}
	public String getPurpose() {
		return Purpose;
	}
	public void setPurpose(String purpose) {
		Purpose = purpose;
	}
	public TreePermitDto(String permitId, String userId, String status, String[] treeId, String landId,
			String surveyDocHash, String landDocHash, String revenueDocHash, String policeDocHash, String rTODocHash,
			String forestDocHash, String sawMillDocHash, String bescomDocHash, String createdOn, String grantedOn,
			String purpose, String[] surveyNo) {
		super();
		PermitId = permitId;
		UserId = userId;
		Status = status;
		TreeId = treeId;
		LandId = landId;
		SurveyDocHash = surveyDocHash;
		LandDocHash = landDocHash;
		RevenueDocHash = revenueDocHash;
		PoliceDocHash = policeDocHash;
		RTODocHash = rTODocHash;
		ForestDocHash = forestDocHash;
		SawMillDocHash = sawMillDocHash;
		BescomDocHash = bescomDocHash;
		CreatedOn = createdOn;
		GrantedOn = grantedOn;
		Purpose = purpose;
		SurveyNo = surveyNo;
	}
	public TreePermitDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
	
}
