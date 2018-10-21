package org.myProject.question.serviceImpl;

import java.io.IOException;

import org.myProject.service.question.ImageDetail;
import org.myProject.service.question.ImageService;

import pics.PicsDAO;

public class ImagesServiceImpl implements ImageService{

	public void saveImageDetails(ImageDetail imageDetail) {
		PicsDAO p=new PicsDAO();
		try {
			p.saveImage(imageDetail.getImageName());
		} catch (IOException e) {
			imageDetail.setStatus("fail");
			Logger.printStackTrace(e);
		}
		
	}

	public void getImageDetail(ImageDetail imageDetail) {
		PicsDAO p=new PicsDAO();
		try {
			p.getImage(imageDetail.getImageName());
		} catch (IOException e) {
			imageDetail.setStatus("fail");
			Logger.printStackTrace(e);
		}
	}

}
