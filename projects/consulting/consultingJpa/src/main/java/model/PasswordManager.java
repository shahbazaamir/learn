package model;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the PASSWORD_MANAGER database table.
 * 
 */
@Entity
@Table(name="PASSWORD_MANAGER")
public class PasswordManager implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="APP_CODE", unique=true, nullable=false, length=20)
	private String appCode;

	@Column(name="APP_EMAIL", length=50)
	private String appEmail;

	@Column(name="APP_NAME", length=20)
	private String appName;

	@Column(name="APP_PASSWORD", length=50)
	private String appPassword;

	@Column(name="APP_USER_ID", length=50)
	private String appUserId;

	public PasswordManager() {
	}

	public String getAppCode() {
		return this.appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getAppEmail() {
		return this.appEmail;
	}

	public void setAppEmail(String appEmail) {
		this.appEmail = appEmail;
	}

	public String getAppName() {
		return this.appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppPassword() {
		return this.appPassword;
	}

	public void setAppPassword(String appPassword) {
		this.appPassword = appPassword;
	}

	public String getAppUserId() {
		return this.appUserId;
	}

	public void setAppUserId(String appUserId) {
		this.appUserId = appUserId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{ \"appCode\":\"");
		builder.append(appCode);
		builder.append("\", appEmail\":\"");
		builder.append(appEmail);
		builder.append("\", appName\":\"");
		builder.append(appName);
		builder.append("\", appPassword\":\"");
		builder.append(appPassword);
		builder.append("\", appUserId\":\"");
		builder.append(appUserId);
		builder.append("\"}");
		return builder.toString();
	}

}