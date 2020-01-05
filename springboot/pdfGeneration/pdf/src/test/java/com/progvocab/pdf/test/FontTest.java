package com.progvocab.pdf.test;

import com.progvocab.pdf.generator.FontPdfExample;

public class FontTest {

	public static void main(String [] a) {
		try(FontPdfExample pdfgen = new FontPdfExample("./"+"test8"+".pdf")){
			pdfgen.addText("Example");
			pdfgen.addFont("testing");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
