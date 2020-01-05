package com.progvocab.pdf.generator;

import java.io.FileNotFoundException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;

public class BoxPdfExample  extends PdfExample{

	public BoxPdfExample(String string) throws FileNotFoundException, DocumentException {
		super(string);
	}


	public void addBox(){
		PdfContentByte canvas = writer.getDirectContent();
		Rectangle rect = new Rectangle(50,760, 470, 810);
		rect.setBorder(Rectangle.BOX);
		rect.setBorderWidth(2);
		rect.setBackgroundColor(new BaseColor(0, 220, 0));
		canvas.rectangle(rect);
		 
	}
}
