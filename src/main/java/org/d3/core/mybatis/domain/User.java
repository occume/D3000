package org.d3.core.mybatis.domain;

import java.io.Serializable;

public class User implements Serializable{
	
	private static final long serialVersionUID = 4184871401453031406L;
	private long	id;
	private String 	name;
	private String 	password;
	private String 	email;
	
	public User(){}
	
	public User(String name, String password) {
		super();
		this.name = name;
		this.password = password;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public User(long id, String name, String email) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
	}

}
