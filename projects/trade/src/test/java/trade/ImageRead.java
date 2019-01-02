package trade;

import java.io.File;

import org.junit.Test;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

public class ImageRead {

//@Test
public static void main(String args[]) {//void crackImage() {  
	String filePath=    "C:\\tmp\\animation\\download.png";
	File imageFile = new File(filePath);  
	    ITesseract instance = new Tesseract();  
	    try {  
	    	instance.setDatapath(LoadLibs.extractTessResources("tessdata").getParent());

	    	String result = instance.doOCR(imageFile);  
	        System.out.println( result);  
	    } catch (TesseractException e) {  
	        System.err.println(e.getMessage());  
	        System.out.println( "Error while reading image");  
	    }  
	}
}


