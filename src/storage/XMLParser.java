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

	// public WorldGameState parse(File file) {
	// try {
	// InputStream in = new FileInputStream(file);
	// // InputStream in = new FileInputStream(new File("test.xml"));
	// PrintWriter out = new PrintWriter(new File("loadingTest.txt"));
	// XMLStreamReader xmlreader = XMLInputFactory.newInstance()
	// .createXMLStreamReader(in);
	//
	// while (xmlreader.hasNext()) {
	// // fill this in with each individual case if it comes to a
	// // worldstate tag, tile tag, room tag, entity tag etc
	//
	// int event = xmlreader.next();
	// /*
	// * Look at XMLStreamConstants.class if needing to add in other
	// * values
	// */
	// System.out.println(event);
	// switch (event) {
	// case 1:
	// // event is start element
	// out.println("Start: <" + xmlreader.getLocalName() + ">");
	// break;
	// case 2:
	// // event is an end element
	// out.println("End: </" + xmlreader.getLocalName() + ">");
	//
	// break;
	// case 4:
	// // event is characters /*not sure if i need to use */
	// out.print(event);
	// out.println(xmlreader.getText());
	// if (xmlreader.hasName()) {
	//
	// out.println("@" + xmlreader.getText());
	// }
	// // out.println(xmlreader.getElementText());
	// break;
	// case 5:
	// // event is a comment /*will probably just ignore these
	// // aswell*/
	// break;
	// case 6:
	// // event is ignorable whitespace
	// out.print("WHITESPACE");
	// break;
	// case 7:
	// // event is start of document
	// out.println("Start of document");
	// break;
	// case 8:
	// // event is end of document
	// out.println("End of document");
	// break;
	// case 10:
	// // event is an attribute
	// break;
	// case 11:
	// // event is a DTD
	// out.println("DTD");
	// break;
	// case 13:
	// // event is NameSpace
	// out.println("NameSpace");
	// break;
	// default:
	// }
	//
	// }
	//
	// } catch (FileNotFoundException | XMLStreamException
	// | FactoryConfigurationError e) {
	// e.printStackTrace(System.out);
	// }
	// return null;
	//
	// }

	public WorldGameState parse(File file) {
		try {
			InputStream in = new FileInputStream(file);
			// InputStream in = new FileInputStream(new File("test.xml"));
			PrintWriter out = new PrintWriter(new File("loadingTest.txt"));
			XMLEventReader xmlreader = XMLInputFactory.newInstance()
					.createXMLEventReader(in);

			while (xmlreader.hasNext()) {
				XMLEvent event = xmlreader.nextEvent();

				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					String elemName = startElement.getName().getLocalPart();

					if (elemName.equals("worldstate")) {
						out.println("worldstate");
						continue;
					}
					if (elemName.equals("rooms")) {
						out.println("rooms");
						HashMap<Integer, RoomState> rooms = new HashMap<Integer, RoomState>();
						continue;
					}
					if (elemName.equals("room")) {
						// RoomState current = new RoomState();
						event = xmlreader.nextEvent();
						String[] properties = event.asCharacters().getData()
								.split(" ");
						for (int i = 0; i < properties.length; i++) {
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

		return null;
	}

}
