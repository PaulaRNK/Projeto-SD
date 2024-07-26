package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public class WordLoader extends Thread{
	private static HashSet<String> wordSet;
	private static boolean isLoaded;
	
	public WordLoader(){
		if(wordSet == null) {
			WordLoader.wordSet = new HashSet<String>();
			WordLoader.isLoaded = false;
		}
	}
	
	public void run() {
		if(wordSet.isEmpty()) {
			try {
			      File myObj = new File("dicio.txt");
			      Scanner myReader = new Scanner(myObj);
			      while (myReader.hasNextLine()) {
			    	  String data = myReader.nextLine();
			    	  wordSet.add(data.toUpperCase());
			      }
			      myReader.close();
			      WordLoader.isLoaded = true;
			} catch (FileNotFoundException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			}
		}
		System.out.println("> Arquivo de palavras carregado!");
	}
	
	public static boolean isInWordSet(String word){
		return wordSet.contains(word);
	}
	
	public static boolean isLoaded() {
		return isLoaded;
	}
	
	
	
	
}
