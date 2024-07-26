package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class WaitPlayers extends Thread{
	private ArrayList<GameConnection> connections;
	ArrayList<GameConnection> players;
	private int serverPort;
	private boolean isWaiting;
	
	public WaitPlayers (int serverPort, ArrayList<GameConnection> connections, 
			ArrayList<GameConnection> players) {
		this.connections = connections;
		this.players = players;
		this.serverPort = serverPort;
		this.isWaiting = true;
		this.start();
	}
	
	public void run () {
		try{
			// netstat -ano | findstr :yourPortNumber
			// taskkill /pid yourid /f
			ServerSocket listenSocket = new ServerSocket(serverPort);
			while(true){
				Socket clientSocket = listenSocket.accept();
				System.out.println("> Nova conexão");
				GameConnection c = new GameConnection(clientSocket, isWaiting);
				connections.add(c);
				if(isWaiting) players.add(c);
				c.start();
			}
			
		} 
		catch(IOException e) {System.out.println("Listen :"+e.getMessage());}
	}
	
	void setIsWaiting(boolean isWaiting){
		this.isWaiting = isWaiting;
		if(isWaiting) System.out.println("Aceitando novos players");
		else System.out.println("Não aceitando novos players");
	}
}
