package game;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import game.Rules.Rule;
import game.Rules.RuleStrings;
import server.Connection;
import server.TCPServer;
import utils.GameUtils;
import utils.WordLoader;

public class Game extends Thread{
	private Collection<Connection> players;
	private int secondsToAnswer;

	public Game(Collection<Connection> players) {
		this.players = Collections.synchronizedList(new ArrayList<>(players));
		this.secondsToAnswer = 15;
	}

	private boolean validateWord(Connection playerConnection, Rule rule, String validator, String triedWord, HashSet<String> usedWords) {
		triedWord = triedWord.strip().toUpperCase();
		if(triedWord.isBlank()) {
			playerConnection.sendMessage("Escreva alguma coisa!");
			return false;
		}
		if(!triedWord.matches("[a-zA-ZçáéíóúÁÉÍÓÚãõÃÕâêîôûÂÊÎÔÛÀÈÌÒÙàèìòùçÇ]+")) {
			playerConnection.sendMessage("Sua palavra não pode conter caractéres especiais!");
			return false;
		}
		if(!rule.getVerification(triedWord, validator)) {
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

	@Override
	public void run () {
		if(players.size()==0) {
			TCPServer.log("GAME: Não há jogadores suficientes para começar a partida");
			TCPServer.gameStarted = false;
			return;
		}
		
		HashSet<String> usedWords = new HashSet<>();
		String lastWord = "ROLETA";
		boolean isValid = false;
		long startingTime;
		String triedWord = "";
		Rule rule;
		RuleStrings ruleStrings;
		Iterator<Connection> i;
		synchronized (players) {
			i = players.iterator();
			String playersString = "";
			while(i.hasNext()) {
				playersString += i.next().getInfo().getPlayerName() + "\n";
			}
			TCPServer.announceMessageAll("JOGADORES: " + playersString);
			
			while(players.size() > 1) {
				i = players.iterator();
				while(i.hasNext()) {
					Connection player = i.next();
					if(!player.isConnected()) {
						i.remove();
						continue;
					}

					player.setTurnActive(true);

					TCPServer.announceMessageAllExcept("É a vez de " + player.getInfo().getPlayerName() + "!", player);
					player.sendMessage(player.getInfo().getPlayerName() + ", é a sua vez!");

					rule = GameUtils.getValidRule(lastWord.toUpperCase());
					ruleStrings = rule.getRule(lastWord.toUpperCase());

					TCPServer.announceMessageAllExcept("\n" + player.getInfo().getPlayerName() + " deverá escrever uma palavra que " + ruleStrings.getRuleText(), player);
					player.sendMessage("\n" + player.getInfo().getPlayerName() + ", escreva uma palavra que " + ruleStrings.getRuleText());

					GameUtils.waitMilliseconds(800);

					startingTime = System.currentTimeMillis();
					isValid = false;
					while(	(!isValid)
							&& (System.currentTimeMillis()<= (startingTime+secondsToAnswer*1000))
							&& (player.isConnected())) {
						if(player.sentNewTry()) {
							triedWord = player.getLastTry();
							isValid = validateWord(player,
								rule,
								ruleStrings.getRuleVerificationText(),
								triedWord,
								usedWords);
							player.setSentNewTry(false);
						}
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if(isValid) {
						TCPServer.log("GAME: " + player.getInfo().getPlayerName() + " enviou a palavra válida \"" + triedWord + "\"");
						lastWord = triedWord.toUpperCase();
						usedWords.add(lastWord);
						player.sendMessage("VOCÊ CONSEGUIU!");
						player.getInfo().addPoints();
						TCPServer.announceMessageAllExcept(player.getInfo().getPlayerName() + " CONSEGUIU COM A PALAVRA: " + triedWord, player);
					}
					else{
						if(player.isConnected()) {
							TCPServer.log("GAME: " + player.getInfo().getPlayerName() + " perdeu");
							player.sendMessage("VOCÊ MORREU! PONTUAÇÃO FINAL: " + player.getInfo().getPoints());
						}
						else 
							TCPServer.log("GAME: " + player.getInfo().getPlayerName() + " se desconectou durante a sua vez");
						TCPServer.announceMessageAllExcept(player.getInfo().getPlayerName() + " MORREU. PONTUAÇÃO FINAL: " + player.getInfo().getPoints(), player);
						i.remove();
						player.setPlaying(false);
					}
					player.setTurnActive(false);
				}
			}
			i = players.iterator();
			if(i.hasNext()) {
				Connection player = i.next();
				TCPServer.announceMessageAll("\n" + player.getInfo().getPlayerName() + " VENCEU!!! PONTUAÇÃO FINAL: " + player.getInfo().getPoints());
			}
			else {
				TCPServer.announceMessageAll("JOGO FINALIZADO.");
			}
			players.clear();
			TCPServer.gameStarted = false;
			TCPServer.log("GAME: Partida finalizada");
		}

	}
}
