package game;

public class Info {
	private String playerName;
	private int points;

	public Info(String playerName) {
		this.setPlayerName(playerName);
		this.resetPoints();
	}

	public void addPoints() {
		this.points++;
	}
	public int getPoints() {
		return points;
	}
	public void resetPoints() {
		this.points = 0;
	}

	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

}
