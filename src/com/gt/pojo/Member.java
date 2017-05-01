package com.gt.pojo;

public class Member {
	private String address;
	private String age;
	private String appid;
	private String email;
	private String fullname;
	private int idmember;
	private String phone;
	private String startlatituted;
	private String startlongitude;
	private String markerpic;

	public Member(int idmember, String fullname, String age, String phone, String email,
			String address, String appid) {
		super();
		this.idmember = idmember;
		this.fullname = fullname;
		this.age = age;
		this.phone = phone;
		this.email = email;
		this.address = address;
		this.appid = appid;
	}

	public Member() {
	}

	public String getAddress() {
		return address;
	}

	public String getAge() {
		return age;
	}

	public String getAppid() {
		return appid;
	}

	public String getEmail() {
		return email;
	}

	public String getFullname() {
		return fullname;
	}

	public int getIdmember() {
		return idmember;
	}

	public String getPhone() {
		return phone;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public void setIdmember(int idmember) {
		this.idmember = idmember;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStartlatituted() {
		return startlatituted;
	}

	public void setStartlatituted(String startlatituted) {
		this.startlatituted = startlatituted;
	}

	public String getStartlongitude() {
		return startlongitude;
	}

	public void setStartlongitude(String startlongitude) {
		this.startlongitude = startlongitude;
	}

	public String getMarkerpic() {
		return markerpic;
	}

	public void setMarkerpic(String markerpic) {
		this.markerpic = markerpic;
	}

	@Override
	public String toString() {
		return "Member [address=" + address + ", age=" + age + ", appid=" + appid + ", email="
				+ email + ", fullname=" + fullname + ", idmember=" + idmember + ", phone=" + phone
				+ ", startlatituted=" + startlatituted + ", startlongitude=" + startlongitude
				+ ", markerpic=" + markerpic + "]";
	}
}
