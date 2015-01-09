package org.d3.core.mybatis.domain;

import java.io.Serializable;

public class UserRelation implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 661151252607250221L;
	private int uid1;
	private int uid2;
	private int type;
	
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "UserRelation [uid1=" + uid1 + ", uid2=" + uid2 + ", type="
				+ type + "]";
	}
	
}
