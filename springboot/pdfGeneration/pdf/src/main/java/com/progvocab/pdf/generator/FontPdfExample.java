package com.progvocab.pdf.generator;

import java.io.FileNotFoundException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;

public class FontPdfExample  extends PdfExample{

	public FontPdfExample(String string) throws FileNotFoundException, DocumentException {
		super(string);
	}


	public void addFont(String text) throws DocumentException{
		Font f=new Font(FontFamily.TIMES_ROMAN,50.0f,Font.UNDERLINE,BaseColor.BLUE);
		Font f1=new Font(FontFamily.HELVETICA,50.0f,Font.BOLD,BaseColor.GRAY);
		Font f2=new Font(FontFamily.COURIER,50.0f,Font.ITALIC,BaseColor.DARK_GRAY);
		
		document.add(new Paragraph(" Times underline ",f));
		document.add(new Paragraph(" Havel bold ",f1));
		document.add(new Paragraph(" Courier italics ",f2));
		 
	}
}
