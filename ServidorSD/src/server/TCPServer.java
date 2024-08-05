package server;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

import game.Game;
import utils.WordLoader;


public class TCPServer extends Thread{
	public static Collection<Connection> connections;
	public static boolean gameStarted;

	TCPServer(){
		TCPServer.connections = Collections.synchronizedList(new ArrayList<>());
		TCPServer.gameStarted = false;
	}

	public static void announceMessageAll (String message) {
		Iterator<Connection> i = connections.iterator();
		synchronized (connections) {
			while(i.hasNext()) {
				Connection connection = i.next();
				if(!connection.isConnected()) {
					i.remove();
					continue;
				}
				connection.sendMessage(message);
			}
		}
		TCPServer.log("ANUNCIADO: \"" + message + "\"");
	}

	public static void announceMessageAllExcept(String message, Connection except) {
		Iterator<Connection> i = connections.iterator();
		synchronized (connections) {
			while(i.hasNext()) {
				Connection connection = i.next();
				if(!connection.isConnected()) {
					i.remove();
					continue;
				}
				if(!connection.equals(except)) {
					connection.sendMessage(message);
				}
			}
		}
		TCPServer.log("ANUNCIADO: \"" + message + "\" EXCETO PARA: "+ except.getInfo().getPlayerName());
	}

	private static Collection<Connection> getPlayersFromConnections() {
		Iterator<Connection> i = connections.iterator();
		Collection<Connection> players = Collections.synchronizedList(new ArrayList<>());
		synchronized (connections) {
			while(i.hasNext()) {
				Connection connection = i.next();
				if(!connection.isConnected()) {
					i.remove();
					continue;
				}
				connection.getInfo().resetPoints();
				connection.setPlaying(true);
				connection.setTurnActive(false);
				connection.setSentNewTry(false);
				connection.setLastTry("");
				connection.sendMessage("Você está participando do jogo!");
				players.add(connection);
			}
		}
		return players;
	}
	
	public static void log(String message) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		System.out.println(dtf.format(now) + " > " + message);
	}


	@Override
	public void run () {
		// 49666
		// netstat -ano | findstr :yourPortNumber
		// taskkill /pid yourid /f
		try (Scanner scanner = new Scanner(System.in)) {
			String msg;
			TCPServer.log("Iniciando server");
			WaitPlayers waitPlayers = new WaitPlayers(49666);
			waitPlayers.start();
			Collection<Connection> players;

			while(true) {
				if(!TCPServer.gameStarted) {
					msg = "";
					announceMessageAll("Um novo jogo iniciará em breve!");
					TCPServer.log("Digite \"start\" para iniciar um jogo");
					while(!msg.equals("start")) {
			    		msg = scanner.nextLine();
			    	}

			    	players = getPlayersFromConnections();

			    	announceMessageAll("Jogo começando!");
			    	TCPServer.log("Jogo iniciado");

			    	Game game = new Game(players);
			    	game.start();
			    	TCPServer.gameStarted = true;
				}
				else {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
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