package com.progvocab.pdf.generator;

import java.io.FileOutputStream;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import static com.progvocab.pdf.generator.PdfUtil.addEmptyLine;
import static com.progvocab.pdf.generator.PdfUtil.S_NORNAL;

public class GenerateFirstPdf {


	public static void generate(String file) {
		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();
			//addMetaData(document);
			addTitlePage(document);
			//addContent(document);
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	 private static void addTitlePage(Document document)
	            throws DocumentException {
	        Paragraph preface = new Paragraph();
	        // We add one empty line
	        addEmptyLine(preface, 1);
	        // Lets write a big header
	        preface.add(new Paragraph("Title of the document", S_NORNAL));

	        addEmptyLine(preface, 1);
	        // Will create: Report generated by: _name, _date
	        preface.add(new Paragraph(
	                "Report generated by: " + System.getProperty("user.name") + ", " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	                S_NORNAL));
	        addEmptyLine(preface, 3);
	        preface.add(new Paragraph(
	                "This document describes something which is very important ",
	                S_NORNAL));

	        addEmptyLine(preface, 8);

	         

	        document.add(preface);
	        // Start a new page
	        document.newPage();
	    }

}