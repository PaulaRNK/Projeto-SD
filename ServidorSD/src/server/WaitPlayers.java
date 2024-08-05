package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class WaitPlayers extends Thread{
	private int serverPort;

	public WaitPlayers (int serverPort) {
		this.serverPort = serverPort;
	}

	@Override
	public void run () {
		try (ServerSocket listenSocket = new ServerSocket(serverPort)) {
			while(true) {
				TCPServer.log("Aguardando conexão");
				Socket clientSocket = listenSocket.accept();
				TCPServer.log("Nova conexão (" + clientSocket.getInetAddress().getHostAddress() + ")");
				Connection c = new Connection(clientSocket);
				c.start();
			}

		}
		catch(IOException e) {
			TCPServer.log("ERRO <WaitPlayers run>:"+e.getMessage());
		}
	}
}
