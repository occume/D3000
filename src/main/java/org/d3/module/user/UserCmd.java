package org.d3.module.user;

public class UserCmd {
	
	private int type;
	private String name;
	private String target;
	private String date;
	
	public UserCmd(){};
	
	public UserCmd(int type, String name, String target, String date) {
		super();
		this.type = type;
		this.name = name;
		this.target = target;
		this.date = date;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}
