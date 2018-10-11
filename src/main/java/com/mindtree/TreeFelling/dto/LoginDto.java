/**
 * 
 */
package com.mindtree.TreeFelling.dto;

public class LoginDto {
	private String userId;
	private String password;

	/**
	 * 
	 */
	public LoginDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param userId
	 * @param password
	 */
	public LoginDto(String userId, String password) {
		super();
		this.userId = userId;
		this.password = password;
	}

	public String getuserId() {
		return userId;
	}

	public void setuserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		LoginDto other = (LoginDto) obj;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LoginDto [userId=" + userId + ", password=" + password + "]";
	}

}
