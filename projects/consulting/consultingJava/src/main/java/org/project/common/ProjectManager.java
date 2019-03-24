package org.project.common;

import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.framework.config.ConfigVO;
import org.framework.xml.XMLFileVO;
import org.framework.xml.XMLWrapper;
import org.project.log.LogWriter;

public class ProjectManager {
	public static ConfigVO getConfigVO (){
		XMLFileVO xmlFile = XMLWrapper.getXMLObj("F:\\shahbaz\\work\\keplWork\\Consulting\\WebContent\\Test\\project.xml");
		XMLEventReader xmlEvent = xmlFile.getXmlReader();
		ConfigVO configVO = new ConfigVO() ;
		try {
			while(xmlEvent.hasNext()){
		
			  XMLEvent event = xmlEvent.nextEvent();
			  //xmlEvent.
			  //if (event.isStartElement()) {
		          StartElement startElement = event.asStartElement();
		          LogWriter.writeLog(startElement.getName().getLocalPart());
		          Iterator<Attribute> attributes = startElement.getAttributes();
		          //if(startElement.getName().getLocalPart()=="project"){
		        	  LogWriter.writeLog("inside if");
		        	  while (attributes.hasNext()) {
		        		  Attribute attribute = attributes.next();
		        		  LogWriter.writeLog("attr"+attribute.getName().toString());
		        		  LogWriter.writeLog("attrname"+attribute.getValue().toString());
		        		  //fetchConfigVO()
		        		  //attribute.
		        		  /*
		        		  if (attribute.getName().toString().equals("")) {
		                      item.setDate(attribute.getValue());
		                    }
*/
		        	  }
		          //}
			 // }
		  }
		}catch(Exception e){
			e.printStackTrace();
		}
		return configVO ;
	}
	public static void main(String[] args) {
		
	}

}
