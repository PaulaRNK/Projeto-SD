package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

class GameConnection extends Thread {
	DataInputStream in;
	DataOutputStream out;
	Socket clientSocket;
	ArrayList<String> palavras;
	boolean allowedToSend = false;
	boolean isPlaying;
	String playerName;
	
	
	
	public GameConnection (Socket aClientSocket, boolean isPlaying) {
		this.isPlaying = isPlaying;
	    try {
			clientSocket = aClientSocket;
			in = new DataInputStream( clientSocket.getInputStream());
			out =new DataOutputStream( clientSocket.getOutputStream());
			welcome();
	     } catch(IOException e)  {System.out.println("Connection:"+e.getMessage());}
	}
	
	public void welcome() {
		sendMessage("Digite o seu nome:");
		this.playerName = this.receiveMessage();
		if(isPlaying) sendMessage(playerName +", o jogo começará em breve...");
		else sendMessage(playerName +", você está em modo espectador...");
	}
	
	public void sendMessage (String message) {
		try {
			out.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String receiveMessage() {
		String message = "";
		try {
			message = in.readUTF();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
	
	public void disconnect() {
		try {
			in.close();
			out.close();
			clientSocket.close();
		}
		catch(IOException e) {
	    	System.out.println("IO:"+e.getMessage());
	    }
		
		System.out.println("Conexão fechada");
	}
	
	public String getPlayerName() {
		return this.playerName;
	}
}

