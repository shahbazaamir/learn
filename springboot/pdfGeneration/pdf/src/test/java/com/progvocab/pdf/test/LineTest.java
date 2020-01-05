package com.progvocab.pdf.test;

import com.progvocab.pdf.generator.LinePdfExample;

public class LineTest {

	public static void main(String [] a) {
		try(LinePdfExample pdfgen = new LinePdfExample("./"+"test5"+".pdf")){
			pdfgen.addText("Example");
			pdfgen.addLine();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
