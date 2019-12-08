package com.progvocab.pdf.controller;
 

import org.springframework.web.bind.annotation.RestController;

import com.progvocab.pdf.service.PdfService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class PdfController {
	
	@Autowired
	private PdfService pdfService;
	
	
	@CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping("/index")
    public String index() {
        return "[{\"id\":\"1\",\"questionDesc\":\"What is java?\"}]";
    }
	
	
	@CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping("/pdf")
    public String generatePdf() {
		pdfService.generatePdf();
		return "success";
    }
    
}