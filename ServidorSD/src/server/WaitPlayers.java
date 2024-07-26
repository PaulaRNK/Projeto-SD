package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class WaitPlayers extends Thread{
	private int serverPort;
	
	public WaitPlayers (int serverPort) {
		this.serverPort = serverPort;
		this.start();
	}
	
	public void run () {
		try (
		ServerSocket listenSocket = new ServerSocket(serverPort)){
			while(true){
				Socket clientSocket = listenSocket.accept();
				System.out.println("> Nova conexão");
				Connection c = new Connection(clientSocket);
				c.welcomeConnection();
			}
			
		} 
		catch(IOException e) {System.out.println("Listen:"+e.getMessage());}
	}
}
