package com.progvocab.pdf.generator;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;

public class PdfUtil {

	public static Font L_BOLD = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
	public static Font S_NORNAL = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
	public static Font M_BOLD = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
	public static Font S_BOLD = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
	
	 public static void addEmptyLine(Paragraph paragraph, int number) {
	        for (int i = 0; i < number; i++) {
	            paragraph.add(new Paragraph(" "));
	        }
	    }
}
