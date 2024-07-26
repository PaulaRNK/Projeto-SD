package server;

public class TimeTick extends Thread {
	private long time;
	GameConnection player;
	private boolean isSafe;
	private boolean isFinished;

	public void setTime(long time) {
		this.time = time;
	}
	
	public void setPlayer(GameConnection player) {
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
				Game.waitMilliseconds(1000);
				//player.sendMessage(Integer.toString(timer) + '!' );
				timer--;
			}
			else
			isFinished = true;
			
		}
		
	}
	
	
}
