package com.progvocab.pdf.generator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfExample implements AutoCloseable{

	protected Document document = null;
	protected PdfWriter writer = null;
	public PdfExample() {
		
	}
	
	public PdfExample(String filename) throws FileNotFoundException, DocumentException {
		Rectangle pageSize = new Rectangle(PageSize.A4);
		pageSize.setBackgroundColor(new BaseColor(235, 235, 235));
		document = new Document( pageSize );
		
		writer =PdfWriter.getInstance(document, new FileOutputStream(filename));
		document.open();
	
		 
	}

	public void addText(String text) throws DocumentException {
		document.add(new Paragraph(text));
	}

	@Override
	public void close() throws Exception {
		System.out.println("close called");
		if(document != null )document.close();
	}

}
