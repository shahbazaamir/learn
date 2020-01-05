package com.progvocab.pdf.generator;

import java.io.FileNotFoundException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;

public class BackgroundPdfExample  extends PdfExample{

	public BackgroundPdfExample(String string) throws FileNotFoundException, DocumentException {
		super(string);
	}


	public void addBox(){
		PdfContentByte canvas = writer.getDirectContent();
		Rectangle rect = new Rectangle(10, 10, 580, 820);
		rect.setBorder(Rectangle.BOX);
		//rect.setBorderWidth(2);
		rect.setBackgroundColor(new BaseColor(220, 220, 220));
		canvas.rectangle(rect);
		 
	}
}
