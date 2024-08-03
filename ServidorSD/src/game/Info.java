package game;

public class Player {
	private String playerName;
	private boolean isPlaying;
	private boolean isTurnActive;
	private String lastWord;
	private int points;
	
	public Player(String playerName, boolean isPlaying) {
		this.setPlayerName(playerName);
		this.setPlaying(isPlaying);
		this.setTurnActive(false);
		this.setPoints(0);
	}
	
	public void addPoints() {
		this.points++;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
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

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}


	public String getLastWord() {
		return lastWord;
	}


	public void setLastWord(String lastWord) {
		this.lastWord = lastWord;
	}
}
