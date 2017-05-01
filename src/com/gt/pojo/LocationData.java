package com.gt.pojo;

public class LocationData {
	int idlocation = 0;
	int memberid = 0;
	String accuaracy = "";

	String date = "";

	String direction = "";

	String distance = "";

	String eventtype = "";

	String extrainfo = "";

	String latitude = "";

	String locationMethod = "";

	String longitude = "";

	String phonenumber = "";

	String sessionid = "";

	String speed = "";

	String username = "";

	String place = "";

	public String getAccuaracy() {
		return accuaracy;
	}

	public int getIdlocation() {
		return idlocation;
	}

	public void setIdlocation(int idlocation) {
		this.idlocation = idlocation;
	}

	public int getMemberid() {
		return memberid;
	}

	public void setMemberid(int memberid) {
		this.memberid = memberid;
	}

	public String getDate() {
		return date;
	}

	public String getDirection() {
		return direction;
	}

	public String getDistance() {
		return distance;
	}

	public String getEventtype() {
		return eventtype;
	}

	public String getExtrainfo() {
		return extrainfo;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLocationMethod() {
		return locationMethod;
	}

	public String getLongitude() {
		return longitude;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public String getSessionid() {
		return sessionid;
	}

	public String getSpeed() {
		return speed;
	}

	public String getUsername() {
		return username;
	}

	public void setAccuaracy(String accuaracy) {
		this.accuaracy = accuaracy;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public void setEventtype(String eventtype) {
		this.eventtype = eventtype;
	}

	public void setExtrainfo(String extrainfo) {
		this.extrainfo = extrainfo;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public void setLocationMethod(String locationMethod) {
		this.locationMethod = locationMethod;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	@Override
	public String toString() {
		return "LocationData [idlocation=" + idlocation + ", memberid=" + memberid + ", accuaracy="
				+ accuaracy + ", date=" + date + ", direction=" + direction + ", distance="
				+ distance + ", eventtype=" + eventtype + ", extrainfo=" + extrainfo
				+ ", latitude=" + latitude + ", locationMethod=" + locationMethod + ", longitude="
				+ longitude + ", phonenumber=" + phonenumber + ", sessionid=" + sessionid
				+ ", speed=" + speed + ", username=" + username + ", place=" + place + "]";
	}

}
