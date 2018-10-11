package com.mindtree.TreeFelling.dto;

public class LandsDto {
	
	private String LandId;
	private String UserId;
	private String District;
	private String Taluk;
	private String GPS;
	private String TotalExtent;
	private String[] TreeId;
	private String CreatedOn;
	private String LandDocHash;
	
	
	
	public String getCreatedOn() {
		return CreatedOn;
	}
	public void setCreatedOn(String createdOn) {
		this.CreatedOn = createdOn;
	}
	public String getLandDocHash() {
		return LandDocHash;
	}
	public void setLandDocHash(String landDocHash) {
		this.LandDocHash = landDocHash;
	}
	public String getLandId() {
		return LandId;
	}
	public void setLandId(String landId) {
		LandId = landId;
	}
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		UserId = userId;
	}
	public String getDistrict() {
		return District;
	}
	public void setDistrict(String district) {
		District = district;
	}
	public String getTaluk() {
		return Taluk;
	}
	public void setTaluk(String taluk) {
		Taluk = taluk;
	}
	public String getGPS() {
		return GPS;
	}
	public void setGPS(String gPS) {
		GPS = gPS;
	}
	public String getTotalExtent() {
		return TotalExtent;
	}
	public void setTotalExtent(String totalExtent) {
		TotalExtent = totalExtent;
	}
	public String[] getTreeId() {
		return TreeId;
	}
	public void setTreeId(String[] treeId) {
		TreeId = treeId;
	}
	public LandsDto(String landId, String userId, String district, String taluk, String gPS, String totalExtent,
			String[] treeId,String createdOn, String landDocHash) {
		super();
		LandId = landId;
		UserId = userId;
		District = district;
		Taluk = taluk;
		GPS = gPS;
		TotalExtent = totalExtent;
		TreeId = treeId;
		CreatedOn=createdOn;
		LandDocHash=landDocHash;
	}
	public LandsDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
