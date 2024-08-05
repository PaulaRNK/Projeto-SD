package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import game.Info;

public class Connection extends Thread {
	DataInputStream in;
	DataOutputStream out;
	Socket clientSocket;
	private Info info;
	private boolean isConnected;
	private boolean isPlaying;
	private boolean isTurnActive;
	private boolean sentNewTry;
	private String lastTry = "";

	public Connection (Socket aClientSocket) {
	    try {
			clientSocket = aClientSocket;
			in = new DataInputStream(clientSocket.getInputStream());
			out = new DataOutputStream(clientSocket.getOutputStream());
			isConnected = true;
	    } catch(IOException e)  {
	    	 TCPServer.log("ERRO <Connection>:"+e.getMessage());
	    }
	}

	public void welcomeConnection() {
		sendMessage("Digite o seu nome:");
		String playerName = "";
		try {
			playerName = in.readUTF();
		} catch (IOException e) {
			TCPServer.log("ERRO <Connection welcomeConnection>: " + e.getMessage());
		}
		
		TCPServer.log("Conexão (" + clientSocket.getInetAddress().getHostAddress() + " -> " + playerName + ") adicionada à lista");
		TCPServer.connections.add(this);
		this.info = new Info(playerName);
		this.setPlaying(TCPServer.gameStarted);

		if(TCPServer.gameStarted) {
			sendMessage(playerName +", você está em modo espectador...");
		}
		else{
			sendMessage(playerName +", o jogo começará em breve...");
		}
	}

	public void sendMessage (String message) {
		if(clientSocket.isClosed()  || !this.isConnected) {
			return;
		}
		try {
			out.writeUTF(message + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void run() {
		String message = "";
		try {
			this.welcomeConnection();
			while((message = in.readUTF())!=null && message!="/exit") {
				TCPServer.log(clientSocket.getInetAddress().getHostAddress() + " -> " + this.info.getPlayerName() + " enviou \"" + message + "\"");
				if(this.isTurnActive()) {
					this.setLastTry(message);
					this.setSentNewTry(true);
					System.out.println("> Nova tentativa com " + this.getLastTry());
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			this.clientSocket.close();
		} catch (IOException e) {
			TCPServer.log("ERRO <Connection run>: " + e.getMessage());
		} finally {
			TCPServer.announceMessageAll(this.info.getPlayerName() + " se desconectou.");
			TCPServer.log("Conexão (" + clientSocket.getInetAddress().getHostAddress() + " -> " + this.info.getPlayerName() + ") se desconectou");
			isConnected = false;
		}
	}


	public Info getInfo() {
		return info;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	public boolean isTurnActive() {
		return isTurnActive;
	}

	public void setTurnActive(boolean isTurnActive) {
		this.isTurnActive = isTurnActive;
	}

	public boolean sentNewTry() {
		return sentNewTry;
	}

	public void setSentNewTry(boolean sentNewTry) {
		this.sentNewTry = sentNewTry;
	}

	public String getLastTry() {
		return lastTry;
	}

	public void setLastTry(String lastTry) {
		this.lastTry = lastTry;
	}

	public boolean isConnected() {
		return isConnected;
	}

}

