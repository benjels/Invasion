package storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import gamelogic.RoomState;
import gamelogic.WorldGameState;

public class XMLParser {
	
	public WorldGameState parse(File file){
		parseTiles();
		parseEntities(file);
		
		return null;
	}

	public RoomState[] parseTiles() {
		
		RoomState[] rooms = null;
		try {
			InputStream in = new FileInputStream(new File("tiles.xml"));
			PrintWriter out = new PrintWriter(new File("loadingTest.txt"));
			XMLEventReader xmlreader = XMLInputFactory.newInstance()
					.createXMLEventReader(in);

			while (xmlreader.hasNext()) {
				XMLEvent event = xmlreader.nextEvent();

				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					String elemName = startElement.getName().getLocalPart();

					if (elemName.equals("worldState")) {
						System.out.println("worldState");
						continue;
					}
					if (elemName.equals("rooms")) {
						System.out.println("rooms");
						continue;
					}
					if (elemName.equals("room")) {
						event = xmlreader.nextEvent();
						String[] properties = event.asCharacters().getData()
								.split(" ");
						
						//properties.length -1 because otherwise there is an empty property
						for (int i = 0; i < properties.length-1; i++) {
							if (!properties[i].equals("")) {
								System.out.println(properties[i]);
							}
						}

					}
				}

			}
		} catch (FileNotFoundException | XMLStreamException e) {
			e.printStackTrace(System.out);
		}

		return rooms;
	}
	
	public RoomState[] parseEntities(File file) {
		RoomState[] rooms = null;
		try {
			//InputStream in = new FileInputStream(file);
			InputStream in = new FileInputStream(new File("test.xml"));
			PrintWriter out = new PrintWriter(new File("loadingTest.txt"));
			XMLEventReader xmlreader = XMLInputFactory.newInstance()
					.createXMLEventReader(in);

			while (xmlreader.hasNext()) {
				XMLEvent event = xmlreader.nextEvent();

				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					String elemName = startElement.getName().getLocalPart();

					if (elemName.equals("worldState")) {
						System.out.println("worldState");
						continue;
					}
					if (elemName.equals("rooms")) {
						System.out.println("rooms");
						continue;
					}
					if (elemName.equals("room")) {
						// RoomState current = new RoomState();
						event = xmlreader.nextEvent();
						String[] properties = event.asCharacters().getData()
								.split(" ");
						
						//properties.length -1 because otherwise there is an empty property
						for (int i = 0; i < properties.length-1; i++) {
							if (!properties[i].equals("")) {
								System.out.println(properties[i]);
							}
						}

					}
				}

			}
		} catch (FileNotFoundException | XMLStreamException e) {
			e.printStackTrace(System.out);
		}

		return rooms;
	}

}
