package com.progvocab.pdf;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.progvocab.pdf.generator.GeneratePdf;

@SpringBootTest
class PdfApplicationTests {

	@Test
	void contextLoads() {
		try(GeneratePdf pdfgen = new GeneratePdf("./"+"filename"+".pdf")){
			pdfgen.createTable();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
