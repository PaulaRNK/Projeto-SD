package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Game extends Thread{
	String palavra_secreta = "CAULE";
	
	public static void waitMilliseconds(int time){
		if(time<0) return;
		try {
			TimeUnit.MILLISECONDS.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void run () {
		String triedWord = "";
		String lastWord = "ROLETA";
		boolean isValid = false;
		Map<String, Boolean> usedWords = new HashMap<>();
		
		ArrayList <String> rules;
		while(TCPServer.players.size() > 1) {
			for(GameConnection player : TCPServer.players) {
				TCPServer.announceMessageAll("É a vez de " + player.getPlayerName() + "!");
				player.sendMessage(player.getPlayerName() + ", é a sua vez!");
				
				Random random = new Random();
				int pos = random.nextInt(Rules.allRules.length);
				while(lastWord.length()<3 && pos==4) pos = random.nextInt(Rules.allRules.length);
				rules = Rules.allRules[pos].getRule(lastWord);
				waitMilliseconds(300);
				TCPServer.announceMessageAll("A missão de " + player.getPlayerName() + " é: " + rules.get(0));
				player.sendMessage(player.getPlayerName() + ", " + rules.get(0));
				
				TimeTick timeTick = new TimeTick();
				isValid = false;
				timeTick.setTime(System.currentTimeMillis());
				timeTick.setPlayer(player);
				timeTick.start();
				while(!isValid) {
					triedWord = player.receiveMessage().strip().toUpperCase();
					if(timeTick.getIsFinished()) break;
					if(triedWord.isBlank()) {
						player.sendMessage("Escreva alguma coisa!");
					}
					else if(Rules.allRules[pos].getVerification(triedWord, rules.get(1))){
						if(!TCPServer.map.containsKey(triedWord)) {
							player.sendMessage("Essa palavra não existe!");
						}
						else if (usedWords.containsKey(triedWord)){
							player.sendMessage("Essa palavra já foi usada!");
						}
						else {
							isValid = true;
							lastWord = triedWord;
							usedWords.put(triedWord, true);
							timeTick.setIsSafe(true);
						}
					}
					else {
						player.sendMessage("Essa palavra não obedece à regra!");
					}
					System.out.println(player.getPlayerName() + ": " + triedWord);
				}
				
				if(isValid) {  
					player.sendMessage("VOCÊ CONSEGUIU!");
					TCPServer.announceMessageAll(player.getPlayerName() + " mandou a palavra: " + triedWord);
				}
				else {
					player.sendMessage("VOCÊ MORREU");
					TCPServer.announceMessageAll(player.getPlayerName() + " MORREU.");
					TCPServer.players.remove(player);
					player.disconnect();
				}
			}
		}
		TCPServer.announceMessageAll(TCPServer.players.get(0).getPlayerName() + " VENCEU!!!");
	}
}
