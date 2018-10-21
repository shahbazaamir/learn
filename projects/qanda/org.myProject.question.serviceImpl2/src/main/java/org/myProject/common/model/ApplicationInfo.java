package org.myProject.common.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the application_info database table.
 * 
 */
@Entity
@Table(name="application_info")
@NamedQuery(name="ApplicationInfo.findAll", query="SELECT a FROM ApplicationInfo a")
public class ApplicationInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, length=10)
	private String identifier;

	@Column(name="event_desc", length=200)
	private String eventDesc;

	@Column(name="event_id", length=10)
	private String eventId;

	public ApplicationInfo() {
	}

	public String getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getEventDesc() {
		return this.eventDesc;
	}

	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}

	public String getEventId() {
		return this.eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

}