package com.progvocab.pdf.test;

import com.progvocab.pdf.generator.BoxPdfExample;

public class BoxTest {

	public static void main(String [] a) {
		try(BoxPdfExample pdfgen = new BoxPdfExample("./"+"test7"+".pdf")){
			pdfgen.addText("Example");
			pdfgen.addBox();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
