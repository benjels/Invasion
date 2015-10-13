package main;

import java.io.File;

import storage.XMLParser;
import storage.XMLParser2;

public class LoadingMain {
	
	public static void main(String[] args){
		XMLParser parser = new XMLParser();
		parser.parse(new File("Standard-Entities.xml"));
	}

}
