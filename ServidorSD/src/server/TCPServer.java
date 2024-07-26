package server;


import java.util.ArrayList;
import java.util.Scanner;
import game.Game;
import utils.WordLoader;


public class TCPServer extends Thread{
	public static ArrayList<Connection> connections;
	public static ArrayList<Connection> players;
	public static boolean gameStarted;
	
	TCPServer(){
		TCPServer.connections = new ArrayList<Connection>();
		TCPServer.players = new ArrayList<Connection>();
		TCPServer.gameStarted = false;
	}
	
	public static void announceMessageAll (String message) {
		for (Connection connection : connections) {
			connection.sendMessage(message);
		}
	}
	
	public static void announceMessageAllExcept(String message, Connection except) {
		for (Connection connection : connections) {
			if(connection != except)
				connection.sendMessage(message);
		}
	}
	
	private static void setAllConnectionsPlayers() {
		for (Connection connection : connections) {
			players.add(connection);
			connection.sendMessage("Você está participando do jogo!");
		}
	}
	
	
	public void run () {
		// 49666
		// netstat -ano | findstr :yourPortNumber
		// taskkill /pid yourid /f
		try (Scanner scanner = new Scanner(System.in)) {
			String msg;
			System.out.println("> Iniciando server");
			new WaitPlayers(49666);
			
			while(true) {
				if(!TCPServer.gameStarted) {
					msg = "";
					while(!msg.equals("start")) {
			    		msg = scanner.nextLine();
			    	}
			    	
			    	setAllConnectionsPlayers();
			    	
			    	announceMessageAll("Jogo começando!");
			    	System.out.println("> Jogo iniciado");
			    	
			    	Game game = new Game();
			    	game.start();
			    	TCPServer.gameStarted = true;
				}
				else {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
    	
	}
	
	
    public static void main (String[] args) {
    	TCPServer server = new TCPServer();
    	server.start();
    	WordLoader wordLoader = new WordLoader();
    	wordLoader.run();
    }
}