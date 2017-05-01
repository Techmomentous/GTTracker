package com.gt.pojo;

public class PushMessage {
	private String flagnew;
	private int idmessages;
	private int memberid;

	private String message;

	private String name;

	private String type;
	private String time;

	public String getFlagnew() {
		return flagnew;
	}

	public int getIdmessages() {
		return idmessages;
	}

	public int getMemberid() {
		return memberid;
	}

	public String getMessage() {
		return message;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setFlagnew(String flagnew) {
		this.flagnew = flagnew;
	}

	public void setIdmessages(int idmessages) {
		this.idmessages = idmessages;
	}

	public void setMemberid(int memberid) {
		this.memberid = memberid;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Message [idmessages=" + idmessages + ", memberid=" + memberid + ", type=" + type
				+ ", message=" + message + ", name=" + name + ", flagnew=" + flagnew + "]";
	}
}
