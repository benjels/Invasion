package main;

import java.io.File;

import storage.XMLParser;

public class LoadingMain {
	
	public static void main(String[] args){
		XMLParser parser = new XMLParser();
		parser.parse(new File("test.xml"));
	}

}
