package org.myProject.service.question;

import java.io.Serializable;

public class ImageDetail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2205243813455244320L;
	private String imageName;
	private String status;
	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
