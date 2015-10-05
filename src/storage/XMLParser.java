package storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import gamelogic.WorldGameState;

public class XMLParser {

	public WorldGameState parse(File file) {
		try {
			InputStream in = new FileInputStream(file);
			//InputStream in = new FileInputStream(new File("test.xml"));
			XMLStreamReader xmlreader = XMLInputFactory.newInstance().createXMLStreamReader(in);
			
			while(xmlreader.hasNext()){
				//fill this in with each individual case if it comes to a worldstate tag, tile tag, room tag, entity tag etc
				
				int event = xmlreader.next();
				
				//Shows that it is a start tag
				if (event == xmlreader.START_ELEMENT){
					try {
					String text = xmlreader.getElementText();
					String tagName = xmlreader.getLocalName();
					System.out.println("Local Name: " + tagName);
					System.out.println("Text: " + text);
					} catch(XMLStreamException e){
						//
					}
				}
				
				if (event == xmlreader.CHARACTERS){
					System.out.println("type is characters");
				}
				
				
				
				
			}

		} catch (FileNotFoundException | XMLStreamException
				| FactoryConfigurationError e) {
			e.printStackTrace(System.out);
		}
		return null;

	}

}
