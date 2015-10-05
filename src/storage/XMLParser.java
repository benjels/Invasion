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
				
				xmlreader.next();
				//Shows that it is a start tag
				if (xmlreader.getEventType() == xmlreader.START_ELEMENT){
					String tagName = xmlreader.getLocalName();
					System.out.println(tagName);
					if (tagName.equals("worldstate")){
						System.out.println("this is a worldstate");
						break;
					}
					if (tagName.equals("rooms")){
						System.out.println("this is the rooms");
						break;
					}
					if (tagName.equals("room")){
						System.out.println("this is the room");
						break;
					}
					if (tagName.equals("tile")){
						System.out.println("this is a tile");
						break;
					}
					
				}
				
				
				
				
			}

		} catch (FileNotFoundException | XMLStreamException
				| FactoryConfigurationError e) {
			e.printStackTrace(System.out);
		}
		return null;

	}

}
