package server;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.*;
public class TCPServer extends Thread{
	static ArrayList<GameConnection> connections;
	static ArrayList<GameConnection> players;
	static Map<String, Boolean> map = new HashMap<>();
	
	TCPServer(){
		TCPServer.connections = new ArrayList<GameConnection>();
		TCPServer.players = new ArrayList<GameConnection>();
	}
	
	public static void announceMessageAll (String message) {
		for (GameConnection connection : connections) {
			connection.sendMessage(message);
		}
	}
	
	
	public void run () {
		 // 49666
    	Scanner scanner = new Scanner(System.in);
    	String msg = "";
    	System.out.println("> Iniciando server");
    	WaitPlayers start = new WaitPlayers(49666, connections, players);
    	
    	while(!msg.equals(new String("start"))) {
    		msg = scanner.nextLine();
    	}
    	
    	start.setIsWaiting(false);
    	announceMessageAll("Jogo começando!");
    	System.out.println("> Jogo iniciado");
    	
    	Game game = new Game();
    	game.start();
    	
    	while(true) {
    		announceMessageAll(scanner.nextLine());
    	}
    	
	}
	
	
    public static void main (String[] args) {
    	TCPServer server = new TCPServer();
    	server.start();
    	WordLoader wl = new WordLoader(map);
    	wl.start();
    }
}