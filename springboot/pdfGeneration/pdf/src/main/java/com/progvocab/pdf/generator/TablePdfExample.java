package com.progvocab.pdf.generator;

import java.io.FileNotFoundException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class TablePdfExample  extends PdfExample{

	public TablePdfExample(String string) throws FileNotFoundException, DocumentException {
		super(string);
	}


	public void addTable() throws DocumentException{
		PdfPTable table = new PdfPTable(3); 
	      
	      // Adding cells to the table       
	      table.addCell(new PdfPCell( new Phrase("Name")));       
	      table.addCell(new PdfPCell( new Phrase("Raju")));       
	      table.addCell(new PdfPCell( new Phrase("Id")));       
	      table.addCell(new PdfPCell( new Phrase("1001")));       
	      table.addCell(new PdfPCell( new Phrase("Designation")));       
	      table.addCell(new PdfPCell( new Phrase("Programmer")));     
	      document.add(table);
	}
}
