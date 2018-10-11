package com.mindtree.TreeFelling.dto;

public class LandDto {

	private String LandId;
	private String UserId;
	private String District;
	private String Taluk;
	private String GPS;
	private String TotalExtent;
	private String createdOn;
	private String landDocHash;

	
	
	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getLandDocHash() {
		return landDocHash;
	}

	public void setLandDocHash(String landDocHash) {
		this.landDocHash = landDocHash;
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

	

	public LandDto(String landId, String userId, String district, String taluk, String gPS, String totalExtent,
			String createdOn, String landDocHash) {
		super();
		LandId = landId;
		UserId = userId;
		District = district;
		Taluk = taluk;
		GPS = gPS;
		TotalExtent = totalExtent;
		this.createdOn = createdOn;
		this.landDocHash = landDocHash;
	}

	public LandDto() {
		super();
		// TODO Auto-generated constructor stub
	}

}
