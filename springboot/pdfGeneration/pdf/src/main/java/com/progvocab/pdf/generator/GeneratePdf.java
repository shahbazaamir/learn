package com.progvocab.pdf.generator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class GeneratePdf implements AutoCloseable{

	Document document = null;
	public GeneratePdf(String filename) throws FileNotFoundException, DocumentException {
		
			document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(filename));
			document.open();
			//addMetaData(document);
			//addTitlePage(document);
			//addContent(document);
	}
	
	

	@Override
	public void close() throws Exception {
		System.out.println("close called");
		document.close();
		
	}

	public void generateTable(String string) {
		// TODO Auto-generated method stub
		
	}

	
	public  void createTable()
            throws DocumentException {
        PdfPTable table = new PdfPTable(3);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Table Header 1"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 2"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 3"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        table.addCell("1.0");
        table.addCell("1.1");
        table.addCell("1.2");
        table.addCell("2.1");
        table.addCell("2.2");
        table.addCell("2.3");

        document.add(table);

    }
}
