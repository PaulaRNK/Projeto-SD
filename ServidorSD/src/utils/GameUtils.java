package utils;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import game.Rules;
import game.Rules.Rule;

public abstract class GameUtils {
	
	public static void waitMilliseconds(int time){
		if(time<0) return;
		try {
			TimeUnit.MILLISECONDS.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static Rule getValidRule(String lastWord) {
		Random random = new Random();
		int pos = random.nextInt(Rules.allRules.length);
		while(lastWord.length()<3 && pos==4) pos = random.nextInt(Rules.allRules.length);
		return Rules.allRules[pos];
	}
}
