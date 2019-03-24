package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the P_USER database table.
 * 
 */
@Entity
@Table(name="P_USER")
@NamedQuery(name="PUser.findAll", query="SELECT p FROM PUser p")
public class PUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="USER_ID", unique=true, nullable=false, length=50)
	private String userId;

	@Column(name="USER_CLASS", length=1)
	private String userClass;

	@Column(name="USER_NAME", length=100)
	private String userName;

	//bi-directional many-to-one association to PUserConf
	@OneToMany(mappedBy="PUser")
	private List<PUserConf> PUserConfs;

	//bi-directional one-to-one association to PWorkflow
	@OneToOne(mappedBy="PUser")
	private PWorkflow PWorkflow;

	public PUser() {
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

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<PUserConf> getPUserConfs() {
		return this.PUserConfs;
	}

	public void setPUserConfs(List<PUserConf> PUserConfs) {
		this.PUserConfs = PUserConfs;
	}

	public PUserConf addPUserConf(PUserConf PUserConf) {
		getPUserConfs().add(PUserConf);
		PUserConf.setPUser(this);

		return PUserConf;
	}

	public PUserConf removePUserConf(PUserConf PUserConf) {
		getPUserConfs().remove(PUserConf);
		PUserConf.setPUser(null);

		return PUserConf;
	}

	public PWorkflow getPWorkflow() {
		return this.PWorkflow;
	}

	public void setPWorkflow(PWorkflow PWorkflow) {
		this.PWorkflow = PWorkflow;
	}

}