package com.progvocab.pdf.test;

import com.progvocab.pdf.generator.TablePdfExample;

public class TableTest {

	public static void main(String [] a) {
		try(TablePdfExample pdfgen = new TablePdfExample("./"+"test9"+".pdf")){
			pdfgen.addText("Example");
			pdfgen.addTable();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
