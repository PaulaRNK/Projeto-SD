package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import game.Player;

public class Connection extends Thread {
	DataInputStream in;
	DataOutputStream out;
	Socket clientSocket;
	public Player player;
	
	public Connection (Socket aClientSocket) {
	    try {
			clientSocket = aClientSocket;
			in = new DataInputStream( clientSocket.getInputStream());
			out =new DataOutputStream( clientSocket.getOutputStream());
	     } catch(IOException e)  {System.out.println("Connection:"+e.getMessage());}
	}
	
	public void welcomeConnection() {
		sendMessage("Digite o seu nome:");
		String playerName = "";
		try {
			playerName = in.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		TCPServer.connections.add(this);
		this.player = new Player(playerName, !TCPServer.gameStarted);
		
		if(TCPServer.gameStarted) {
			sendMessage(playerName +", você está em modo espectador...");
		}
		else{
			sendMessage(playerName +", o jogo começará em breve...");
		}
		
		this.run();
	}
	
	public void sendMessage (String message) {
		if(clientSocket.isClosed()) return;
		try {
			out.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void run() {
		String message;
		try {
			while((message = in.readUTF()) != null) {
				if(player.isTurnActive()) player.setLastWord(message);
			}
			clientSocket.close();
			TCPServer.connections.remove(this);
			if(player.isPlaying()) TCPServer.players.remove(this);
			player.setPlaying(false);
			TCPServer.announceMessageAll(player.getPlayerName() + " se desconectou.");
		} catch (IOException e) {
			System.out.println("IO:"+e.getMessage());
		}
	}
	
	
}

