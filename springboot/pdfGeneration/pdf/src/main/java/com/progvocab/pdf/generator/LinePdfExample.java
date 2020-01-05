package com.progvocab.pdf.generator;

import java.io.FileNotFoundException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfContentByte;

public class LinePdfExample extends PdfExample{

	public LinePdfExample(String string) throws FileNotFoundException, DocumentException {
		super(string);
	}

	public void addLine() {
		PdfContentByte canvas = writer.getDirectContent();
		CMYKColor magentaColor = new CMYKColor(0.f, 1.f, 0.f, 0.f);
		canvas.setColorStroke(magentaColor);
		canvas.moveTo(72, 720);
		canvas.lineTo(172, 720);
		 
		canvas.closePathStroke();
	}
}
