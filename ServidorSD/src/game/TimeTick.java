package game;

import server.Connection;
import utils.GameUtils;

public class TimeTick extends Thread {
	Connection player;
	private boolean isSafe;
	private boolean isFinished;

	
	public void setPlayer(Connection player) {
		this.player = player;
	}
	
	public void setIsSafe(boolean isSafe) {
		this.isSafe = isSafe;
	}
	
	public boolean getIsFinished() {
		return this.isFinished;
	}
	
	public void run () {
		isSafe = false;
		isFinished = false;
		int timer = 5;
		while(!isSafe) {
			if(timer>=0) {
				GameUtils.waitMilliseconds(1000);
				//player.sendMessage(Integer.toString(timer) + '!' );
				timer--;
			}
			else
			isFinished = true;
			
		}
		
	}
	
	
}
