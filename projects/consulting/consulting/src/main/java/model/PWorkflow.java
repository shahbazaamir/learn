package model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the P_WORKFLOW database table.
 * 
 */
@Entity
@Table(name="P_WORKFLOW")
@NamedQuery(name="PWorkflow.findAll", query="SELECT p FROM PWorkflow p")
public class PWorkflow implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="USER_ID", unique=true, nullable=false, length=50)
	private String userId;

	@Column(name="USER_RANK", precision=22)
	private BigDecimal userRank;

	//bi-directional one-to-one association to PUser
	@OneToOne
	@JoinColumn(name="USER_ID", nullable=false, insertable=false, updatable=false)
	private PUser PUser;

	public PWorkflow() {
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public BigDecimal getUserRank() {
		return this.userRank;
	}

	public void setUserRank(BigDecimal userRank) {
		this.userRank = userRank;
	}

	public PUser getPUser() {
		return this.PUser;
	}

	public void setPUser(PUser PUser) {
		this.PUser = PUser;
	}

}