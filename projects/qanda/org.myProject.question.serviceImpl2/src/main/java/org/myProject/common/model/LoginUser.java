package org.myProject.common.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the LOGIN_USER database table.
 * 
 */
@Entity
@Table(name="LOGIN_USER")
@NamedQuery(name="LoginUser.findAll", query="SELECT l FROM LoginUser l")
public class LoginUser implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column(name="password")
	private String password;
	@Id
	@Column(name="USER_ID")
	private String userId;

	@Column(name="USER_NAME")
	private String userName;

	public LoginUser() {
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}