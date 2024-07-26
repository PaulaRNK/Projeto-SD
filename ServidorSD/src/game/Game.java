package game;


import java.util.HashSet;
import game.Rules.Rule;
import game.Rules.RuleStrings;
import server.Connection;
import server.TCPServer;
import utils.GameUtils;
import utils.WordLoader;

public class Game extends Thread{
	private boolean validateWord(Connection playerConnection, Rule rule, String validator, String triedWord, HashSet<String> usedWords) {
		triedWord = triedWord.strip().toUpperCase();
		if(triedWord.isBlank()) {
			playerConnection.sendMessage("Escreva alguma coisa!");
			return false;
		}
		if(!triedWord.matches("[a-zA-ZçáéíóúÁÉÍÓÚãõâêîôûàèìòùçÇ]+")) {
			playerConnection.sendMessage("Sua palavra não pode conter caractéres especiais!");
			return false;
		}
		if(!rule.getVerification(validator, triedWord)) {
			playerConnection.sendMessage("Sua palavra não segue a regra!");
			return false;
		}
		if (usedWords.contains(triedWord)){
			playerConnection.sendMessage("Essa palavra já foi usada!");
			return false;
		}
		if (!WordLoader.isInWordSet(triedWord)) {
			playerConnection.sendMessage("Essa palavra não existe!");
			return false;
		}
		return true;
	}
	
	public void run () {
		if(TCPServer.players.size()==0) {
			System.out.println("Não há players para jogar");
			TCPServer.gameStarted = false;
			return;
		}
		
		String lastWord = "ROLETA";
		boolean isValid = false;
		HashSet<String> usedWords = new HashSet<>();
		Rule rule;
		RuleStrings ruleStrings;
		int secondsToAnswer = 5;
		long startingTime;
		String triedWord = "";
		
		while(TCPServer.players.size() > 1) {
			for(Connection playerConnection : TCPServer.players) {
				TCPServer.announceMessageAllExcept("É a vez de " + playerConnection.player.getPlayerName() + "!", playerConnection);
				playerConnection.sendMessage(playerConnection.player.getPlayerName() + ", é a sua vez!");
				
				rule = GameUtils.getValidRule(lastWord);
				ruleStrings = rule.getRule(lastWord);
				
				TCPServer.announceMessageAllExcept("\n" + playerConnection.player.getPlayerName() + "deverá escrever uma palavra que " + ruleStrings.getRuleText(), playerConnection);
				playerConnection.sendMessage("\n" + playerConnection.player.getPlayerName() + ", escreva uma palavra que " + ruleStrings.getRuleText());
				
				
				/*
				TimeTick timeTick = new TimeTick();
				isValid = false;
				timeTick.setTime(System.currentTimeMillis());
				timeTick.setPlayer(player);
				timeTick.start();*/
				
				startingTime = System.currentTimeMillis();
				isValid = false;
				playerConnection.player.setTurnActive(true);
				while(!isValid && System.currentTimeMillis()>startingTime+secondsToAnswer*1000 && playerConnection.player.isPlaying()) {
					triedWord = playerConnection.player.getLastWord();
					isValid = validateWord(playerConnection, 
							rule, 
							ruleStrings.getRuleVerificationText(), 
							triedWord, 
							usedWords);
				}
				if(isValid) {  
					playerConnection.sendMessage("VOCÊ CONSEGUIU!");
					playerConnection.player.addPoints();
					TCPServer.announceMessageAllExcept(playerConnection.player.getPlayerName() + " CONSEGUIU COM A PALAVRA: " + triedWord, playerConnection);
				}
				else{
					playerConnection.sendMessage("VOCÊ MORREU! PONTUAÇÃO FINAL: " + playerConnection.player.getPoints());
					TCPServer.announceMessageAllExcept(playerConnection.player.getPlayerName() + " MORREU. PONTUAÇÃO FINAL: " + playerConnection.player.getPoints(), playerConnection);
					TCPServer.players.remove(playerConnection);
					playerConnection.player.setPlaying(false);
				}
				playerConnection.player.setTurnActive(false);
			}
		}
		TCPServer.announceMessageAll(TCPServer.players.get(0).player.getPlayerName() + " VENCEU!!! PONTUAÇÃO FINAL: " + TCPServer.players.get(0).player.getPoints());
		TCPServer.players.remove(TCPServer.players.get(0));
		TCPServer.gameStarted = false;
	}
}
