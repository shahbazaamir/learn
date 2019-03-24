package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the P_USER_CONF database table.
 * 
 */
@Entity
@Table(name="P_USER_CONF")
@NamedQuery(name="PUserConf.findAll", query="SELECT p FROM PUserConf p")
public class PUserConf implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="USER_ID", unique=true, nullable=false, length=50)
	private String userId;

	@Column(name="USER_CLASS", length=1)
	private String userClass;

	@Column(name="USER_PASSWORD", length=50)
	private String userPassword;

	@Column(name="USER_STATUS", length=1)
	private String userStatus;

	//bi-directional many-to-one association to PUser
	@ManyToOne
	@JoinColumn(name="USER_ID", nullable=false, insertable=false, updatable=false)
	private PUser PUser;

	public PUserConf() {
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserClass() {
		return this.userClass;
	}

	public void setUserClass(String userClass) {
		this.userClass = userClass;
	}

	public String getUserPassword() {
		return this.userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserStatus() {
		return this.userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public PUser getPUser() {
		return this.PUser;
	}

	public void setPUser(PUser PUser) {
		this.PUser = PUser;
	}

}