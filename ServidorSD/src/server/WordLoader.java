package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

public class WordLoader extends Thread{
	Map<String, Boolean> map;
	
	WordLoader(Map<String, Boolean> map){
		this.map = map;
	}
	
	public void run() {
		try {
		      File myObj = new File("dicio.txt");
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		    	  String data = myReader.nextLine();
		    	  map.put(data.toUpperCase(), true);
		      }
		      myReader.close();
		} catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		}
		System.out.println("> Arquivo de palavras carregado!");
	}
}
