/**
 * 
 */
package com.mindtree.TreeFelling.dto;

public class LoginReturnDto {

	private String userName;
	private String jwtToken;
	private String userId;
	private String userType;
	private String phoneNo;
	private String address;
	private String aadharNo;


	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAadharNo() {
		return aadharNo;
	}

	public void setAadharNo(String aadharNo) {
		this.aadharNo = aadharNo;
	}

	/**
	 * 
	 */
	public LoginReturnDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param userName
	 * @param jwtToken
	 * @param companyId
	 * @param userType
	 * @param phoneNo
	 */
	public LoginReturnDto(String userName, String jwtToken, String userId, String userType, String phoneNo) {
		super();
		this.userName = userName;
		this.jwtToken = jwtToken;
		this.userId = userId;
		this.userType = userType;
		this.phoneNo = phoneNo;

	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jwtToken == null) ? 0 : jwtToken.hashCode());
		result = prime * result + ((phoneNo == null) ? 0 : phoneNo.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((userType == null) ? 0 : userType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoginReturnDto other = (LoginReturnDto) obj;
		if (jwtToken == null) {
			if (other.jwtToken != null)
				return false;
		} else if (!jwtToken.equals(other.jwtToken))
			return false;
		if (phoneNo == null) {
			if (other.phoneNo != null)
				return false;
		} else if (!phoneNo.equals(other.phoneNo))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		if (userType == null) {
			if (other.userType != null)
				return false;
		} else if (!userType.equals(other.userType))
			return false;
		return true;
	}

}