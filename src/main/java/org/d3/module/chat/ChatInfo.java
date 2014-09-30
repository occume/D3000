package org.d3.module.chat;

public class ChatInfo {
	
	private String type;
	private String name;
	private String target;
	private String info;
	
	public ChatInfo(){}
	
	public ChatInfo(String type, String name, String target, String message) {
		this.type = type;
		this.name = name;
		this.target = target;
		this.info = message;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
