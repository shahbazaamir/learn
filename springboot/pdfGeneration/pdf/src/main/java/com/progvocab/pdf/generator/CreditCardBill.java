package com.progvocab.pdf.generator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class CreditCardBill {


	

	public static void init(String filename,Document document,PdfWriter pdfWriter) throws FileNotFoundException, DocumentException {

		document = new Document();
		pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(filename));
		document.open();

	}
	
	public static void setBackground(String filename,Document document,PdfWriter pdfWriter) throws FileNotFoundException, DocumentException {
		PdfContentByte canvas = pdfWriter.getDirectContent();
		Rectangle rect = new Rectangle(10, 10, 580, 820);
		rect.setBorder(Rectangle.BOX);
		//rect.setBorderWidth(2);
		rect.setBackgroundColor(new BaseColor(220, 220, 220));
		canvas.rectangle(rect);
	}
	public static void addHeading(Document document) {
		
	}
	public static void addBox(PdfWriter pdfWriter){
		PdfContentByte canvas = pdfWriter.getDirectContent();
		Rectangle rect = new Rectangle(50,760, 470, 810);
		rect.setBorder(Rectangle.BOX);
		//rect.setBorderWidth(2);
		rect.setBackgroundColor(new BaseColor(0, 220, 0));
		canvas.rectangle(rect);
		 
	}
	
	public static void addTable(Document document)  throws DocumentException{
		
		Paragraph p = new Paragraph(" ");
		p.setSpacingAfter(500);
		
		document.add(p);
		PdfPTable table = new PdfPTable(3); 
		 
		 
		 
		table.setSpacingBefore(50);
		table.setWidthPercentage(40);
		//table.
	      // Adding cells to the table       
	      table.addCell(new PdfPCell( new Phrase("Name")));     
	      table.addCell(new PdfPCell( new Phrase("Id")));  
	      table.addCell(new PdfPCell( new Phrase("Designation")));       
	      table.addCell(new PdfPCell( new Phrase("Raju")));       
	           
	      table.addCell(new PdfPCell( new Phrase("1001")));       
	      table.addCell(new PdfPCell( new Phrase("Programmer")));     
	      
	      table.addCell(cell("Shahbaz "));
	      table.addCell(cell(" 007 "));
	      table.addCell(cell(" Architect "));
	      document.add(table);
	}
	
	public static PdfPCell cell(String content) {
		PdfPCell cell = new PdfPCell();
		Phrase phrase = new Phrase(content);
		cell.addElement(phrase);
		BaseColor color = new BaseColor(0,220,0); 
		cell.setBackgroundColor(color);
		cell.setPadding(7f);
		cell.setBorderWidth(1);
		cell.setBorderColor( new BaseColor(255,255,255) );
		return cell;
	}
	
	public static void close(Document document) {
		document.close();
	}
	
	public static void main(String[] args) throws Exception{
		Document document = null;
		String filename = "./pdf/CreditCardBill.pdf";
		PdfWriter pdfWriter = null ;
		document = new Document();
		pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(filename));
		document.open();

		setBackground(filename, document, pdfWriter);
		addBox(pdfWriter);
		addTable(document);
		close(document);
	}
}
