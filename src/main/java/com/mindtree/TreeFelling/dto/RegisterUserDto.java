package com.mindtree.TreeFelling.dto;

public class RegisterUserDto {

	private String name;
	private String mobileNo;
	private String userType;
	private String aadharNo;
	private String email;
	private String address;
	private String password;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAadharNo() {
		return aadharNo;
	}

	public void setAadharNo(String aadharNo) {
		this.aadharNo = aadharNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public RegisterUserDto(String name, String aadharNo, String address, String mobileNo,
			String password, String userType, String email) {
		super();
		this.name = name;
		this.aadharNo = aadharNo;
		this.address = address;
		this.mobileNo = mobileNo;
		this.password = password;
		this.userType = userType;
		this.email = email;
	}

	public RegisterUserDto() {
		super();
		// TODO Auto-generated constructor stub
	}

}
