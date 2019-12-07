package com.progvocab.pdf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.progvocab.pdf.entity.Mytable1;
import com.progvocab.pdf.repository.Mytable1Repository;

@Service
public class PdfService {
	
	@Autowired
	private Mytable1Repository mytable1Repository ;
	
	public void generatePdf() {
		List<Mytable1> mytable1 =mytable1Repository.findAll();
		
		mytable1.stream().forEach( entity -> System.out.println(entity) );
	}
	
}
