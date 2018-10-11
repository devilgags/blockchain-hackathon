/**
 * 
 */
package com.mindtree.TreeFelling.dto;


public class ReturnUserDto {

	private String id;
	private String userName;
	private String companyName;
	private String userType;

	/**
	 * 
	 */
	public ReturnUserDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param userName
	 * @param companyName
	 * @param userType
	 */
	public ReturnUserDto(String id, String userName, String companyName, String userType) {
		super();
		this.id = id;
		this.userName = userName;
		this.companyName = companyName;
		this.userType = userType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ReturnUserDto other = (ReturnUserDto) obj;
		if (companyName == null) {
			if (other.companyName != null)
				return false;
		} else if (!companyName.equals(other.companyName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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

	@Override
	public String toString() {
		return "ReturnCompanydto [id=" + id + ", userName=" + userName + ", companyName=" + companyName + ", userType="
				+ userType + "]";
	}

}
