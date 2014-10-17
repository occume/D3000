package org.d3.core.mybatis.domain;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable{
	
	private static final long serialVersionUID = 4184871401453031406L;
	private long	id;
	private int		type;
	private int		uid1;
	private int		uid2;
	private int		readed;
	private int		result;
	private String 	info;
	private Date	happenTime = new Date();
	private	Date	lastModifyTime = new Date();
	
	public Message(){}

	public Message(int type, int uid1, int uid2, String info) {
		this.type = type;
		this.uid1 = uid1;
		this.uid2 = uid2;
		this.info = info;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getUid1() {
		return uid1;
	}

	public void setUid1(int uid1) {
		this.uid1 = uid1;
	}

	public int getUid2() {
		return uid2;
	}

	public void setUid2(int uid2) {
		this.uid2 = uid2;
	}

	public int getReaded() {
		return readed;
	}

	public void setReaded(int readed) {
		this.readed = readed;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Date getHappenTime() {
		return happenTime;
	}

	public void setHappenTime(Date happenTime) {
		this.happenTime = happenTime;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	@Override
	public String toString() {
		return "Message [type=" + type + ", uid1=" + uid1 + ", uid2=" + uid2
				+ ", readed=" + readed + ", result=" + result + ", info="
				+ info + ", happenTime=" + happenTime + "]";
	}

}
