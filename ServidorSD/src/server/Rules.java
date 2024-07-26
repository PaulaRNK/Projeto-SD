package server;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Rules {
	private static ArrayList<String> rule1 (String a) {
		ArrayList<String> rules = new ArrayList<String>(2);
		String ruleStr = "";
		
		Random random = new Random();
		int qtd = random.nextInt(2);
		
		String text = "Escreva uma palavra que COMECE com ";
		if(qtd==1) {
			text +=  "\"" + a.charAt(0) + a.charAt(1) + "\""+ "!";
			ruleStr += "" + a.charAt(0) + a.charAt(1);
		}
		else {
			text +=  "'" + a.charAt(0) + "'"+ "!";
			ruleStr += "" + a.charAt(0);
		}
		
		rules.add(text);
		rules.add(ruleStr);
		
		return rules;
	}
	private static boolean ruleVerification1 (String a, String ruleStr) {
		System.out.println("Enviado:" + a);
		return a.startsWith(ruleStr);
	}
	
	
	private static ArrayList<String> rule2 (String a) {
		ArrayList<String> rules = new ArrayList<String>(2);
		String ruleStr = "";
		
		Random random = new Random();
		int qtd = a.length()>3 ? random.nextInt(a.length()/2 - 1) + 1 : 1;
		
		String text = "Escreva uma palavra que CONTENHA ";
		if(qtd>1) text += "AS LETRAS";
		else text += "A LETRA";
		
		while(qtd>0) {
			int pos = random.nextInt(a.length());
			text += " '" + a.charAt(pos) + "'";
			ruleStr += "" + a.charAt(pos);
			qtd--;
		}
		text += "!";
		
		rules.add(text);
		rules.add(ruleStr);
				
		return rules;
	}
	private static boolean ruleVerification2 (String a, String ruleStr) {
		for(int i=0 ; i<ruleStr.length() ; i++) {
			if (a.lastIndexOf(ruleStr.charAt(i))==-1) return false;
		}
		return true;
	}
	
	
	private static ArrayList<String> rule3 (String a) {
		ArrayList<String> rules = new ArrayList<String>(2);
		
		String text = "Escreva uma palavra que seja MAIOR que " +
				  "\"" + a + "\""+ "!";
		rules.add(text);
		rules.add(a);
		
		return rules;
	}
	private static boolean ruleVerification3 (String a, String ruleStr) {
		return a.length() > ruleStr.length();
	}
	
	
	private static ArrayList<String> rule4 (String a) {
		ArrayList<String> rules = new ArrayList<String>(2);
		
		String text = "Escreva uma palavra que seja MENOR que " +
				  "\"" + a + "\""+ "!";
		rules.add(text);
		rules.add(a);
		
		return rules;
	}
	private static boolean ruleVerification4 (String a, String ruleStr) {
		return a.length() < ruleStr.length();
	}
	
	private static ArrayList<String> rule5 (String a) {
		ArrayList<String> rules = new ArrayList<String>(2);
		
		String text = "Escreva uma palavra que tenha o MESMO TAMANHO que " +
				  "\"" + a + "\""+ "!";
		rules.add(text);
		rules.add(a);
		
		return rules;
	}
	private static boolean ruleVerification5 (String a, String ruleStr) {
		return a.length() == ruleStr.length();
	}
	
	private static ArrayList<String> rule6 (String a) {
		ArrayList<String> rules = new ArrayList<String>(2);
		String ruleStr = "";
		
		Random random = new Random();
		int qtd = random.nextInt(2);
		
		String text = "Escreva uma palavra que TERMINE com ";
		if(qtd==1) {
			text +=  "\"" + a.charAt(a.length()-2) + a.charAt(a.length()-1) + "\""+ "!";
			ruleStr +=  "" + a.charAt(a.length()-2) + a.charAt(a.length()-1);
		}
		else {
			text +=  "'" + a.charAt(a.length()-1) + "'"+ "!";
			ruleStr += "" + a.charAt(a.length()-1);
		}
		
		rules.add(text);
		rules.add(ruleStr);
		
		return rules;
	}
	private static boolean ruleVerification6 (String a, String ruleStr) {
		return a.endsWith(ruleStr);
	}
	
	
	interface Rule {
		ArrayList<String> getRule(String a);
		boolean getVerification(String a, String ruleStr);
	}
	
	public static Rule[] allRules = new Rule[] {
			new Rule() {public ArrayList<String> getRule(String a) {return Rules.rule1(a);} 
			public boolean getVerification(String a, String ruleStr) {return Rules.ruleVerification1(a,ruleStr);} },
			
			new Rule() {public ArrayList<String> getRule(String a) {return Rules.rule2(a);} 
			public boolean getVerification(String a, String ruleStr) {return Rules.ruleVerification2(a, ruleStr);} },
			
			new Rule() {public ArrayList<String> getRule(String a) {return Rules.rule3(a);} 
			public boolean getVerification(String a, String ruleStr) {return Rules.ruleVerification3(a, ruleStr);} },
			
			new Rule() {public ArrayList<String> getRule(String a) {return Rules.rule4(a);} 
			public boolean getVerification(String a, String ruleStr) {return Rules.ruleVerification4(a, ruleStr);} },
			
			new Rule() {public ArrayList<String> getRule(String a) {return Rules.rule5(a);} 
			public boolean getVerification(String a, String ruleStr) {return Rules.ruleVerification5(a, ruleStr);} },
			
			new Rule() {public ArrayList<String> getRule(String a) {return Rules.rule6(a);} 
			public boolean getVerification(String a, String ruleStr) {return Rules.ruleVerification6(a, ruleStr);} }
	};
	
	public static void main(String args[]) {
		String previousWord = "cair";
		ArrayList<String> rules;
		Scanner scanner = new Scanner(System.in);
		System.out.println(scanner.nextLine().toUpperCase());
		while(true) {
			Random random = new Random();
			int ruleNumber = random.nextInt(allRules.length);
			rules = Rules.allRules[ruleNumber].getRule(previousWord);
			System.out.println(rules.get(0));
			previousWord = scanner.nextLine();
			while(!Rules.allRules[ruleNumber].getVerification(previousWord, rules.get(1))) {
				System.out.println("Não segue a regra:" + rules.get(1));
				previousWord = scanner.nextLine();
			}
			
		}
	}
	
	
}
