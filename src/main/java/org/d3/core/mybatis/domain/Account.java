package org.d3.core.mybatis.domain;

import java.io.Serializable;

public class Account implements Serializable {

  private static final long serialVersionUID = 8751282105532159742L;
  
  private int	 id;
  private String username;
  private String password;
  private String email;
  
//  	@CacheKeyMethod
  	public int getId() {
  		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

//  @Validate(required=true, on={"newAccount", "editAccount"})
  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

//  @Validate(required=true, on={"newPassword", "editPassword"})
  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
  
}
