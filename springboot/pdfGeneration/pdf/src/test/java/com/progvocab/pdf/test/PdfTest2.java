package com.progvocab.pdf.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.progvocab.pdf.generator.GeneratePdf;
import com.progvocab.pdf.generator.PdfExample;

public class PdfTest2 {
	
	public static void main(String [] a) {
		try(PdfExample pdfgen = new PdfExample("./"+"test4"+".pdf")){
			pdfgen.addText("Example");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
