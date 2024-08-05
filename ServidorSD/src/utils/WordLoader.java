package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

import server.TCPServer;

public class WordLoader extends Thread{
	private static HashSet<String> wordSet;
	private static boolean isLoaded;

	public WordLoader(){
		if(wordSet == null) {
			WordLoader.wordSet = new HashSet<>();
			WordLoader.isLoaded = false;
		}
	}

	@Override
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
			      TCPServer.log("ERRO <WordLoader run()>: Arquivo não encontrado");
			      e.printStackTrace();
			}
		}
		TCPServer.log("Arquivo de palavras carregado");
	}

	public static boolean isInWordSet(String word){
		return wordSet.contains(word);
	}

	public static boolean isLoaded() {
		return isLoaded;
	}




}
