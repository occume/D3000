package org.d3.module.chat;

public class ChatInfo {
	
	private String name;
	private String target;
	private String message;
	
	public ChatInfo(){}
	
	public ChatInfo(String name, String target, String message) {
		super();
		this.name = name;
		this.target = target;
		this.message = message;
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
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
