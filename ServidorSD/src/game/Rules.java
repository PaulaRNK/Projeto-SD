package game;

import java.util.Random;
import java.util.Scanner;


public class Rules {
	public static class RuleStrings{
		private String ruleText;
		private String ruleVerificationText;

		public String getRuleText() {
			return ruleText;
		}
		public void setRuleText(String ruleText) {
			this.ruleText = ruleText;
		}
		public String getRuleVerificationText() {
			return ruleVerificationText;
		}
		public void setRuleVerificationText(String ruleVerificationText) {
			this.ruleVerificationText = ruleVerificationText;
		}
	}


	private static RuleStrings rule1 (String a) {
		RuleStrings ruleStrings = new RuleStrings();
		String ruleStr = "";

		Random random = new Random();
		int qtd = random.nextInt(2);

		String text = "COMECE com ";
		if(qtd==1) {
			text +=  "\"" + a.charAt(0) + a.charAt(1) + "\""+ "!";
			ruleStr += "" + a.charAt(0) + a.charAt(1);
		}
		else {
			text +=  "'" + a.charAt(0) + "'"+ "!";
			ruleStr += "" + a.charAt(0);
		}

		ruleStrings.setRuleText(text);
		ruleStrings.setRuleVerificationText(ruleStr);
		return ruleStrings;
	}
	private static boolean ruleVerification1 (String a, String ruleStr) {
		return a.startsWith(ruleStr);
	}


	private static RuleStrings rule2 (String a) {
		RuleStrings ruleStrings = new RuleStrings();
		String ruleStr = "";

		Random random = new Random();
		int qtd = a.length()>3 ? random.nextInt(a.length()/2 - 1) + 1 : 1;

		String text = "CONTENHA ";
		if(qtd>1) {
			text += "AS LETRAS";
		} else {
			text += "A LETRA";
		}

		while(qtd>0) {
			int pos = random.nextInt(a.length());
			text += " '" + a.charAt(pos) + "'";
			ruleStr += "" + a.charAt(pos);
			qtd--;
		}
		text += "!";

		ruleStrings.setRuleText(text);
		ruleStrings.setRuleVerificationText(ruleStr);
		return ruleStrings;
	}
	private static boolean ruleVerification2 (String a, String ruleStr) {
		for(int i=0 ; i<ruleStr.length() ; i++) {
			if (a.lastIndexOf(ruleStr.charAt(i))==-1) {
				return false;
			}
		}
		return true;
	}


	private static RuleStrings rule3 (String a) {
		RuleStrings ruleStrings = new RuleStrings();

		String text = "seja MAIOR que " +
				  "\"" + a + "\""+ "!";

		ruleStrings.setRuleText(text);
		ruleStrings.setRuleVerificationText(a);
		return ruleStrings;
	}
	private static boolean ruleVerification3 (String a, String ruleStr) {
		return a.length() > ruleStr.length();
	}


	private static RuleStrings rule4 (String a) {
		RuleStrings ruleStrings = new RuleStrings();

		String text = "seja MENOR que " +
				  "\"" + a + "\""+ "!";

		ruleStrings.setRuleText(text);
		ruleStrings.setRuleVerificationText(a);
		return ruleStrings;
	}
	private static boolean ruleVerification4 (String a, String ruleStr) {
		return a.length() < ruleStr.length();
	}


	private static RuleStrings rule5 (String a) {
		RuleStrings ruleStrings = new RuleStrings();

		String text = "tenha o MESMO TAMANHO que " +
				  "\"" + a + "\""+ "!";

		ruleStrings.setRuleText(text);
		ruleStrings.setRuleVerificationText(a);
		return ruleStrings;
	}
	private static boolean ruleVerification5 (String a, String ruleStr) {
		return a.length() == ruleStr.length();
	}


	private static RuleStrings rule6 (String a) {
		RuleStrings ruleStrings = new RuleStrings();
		String ruleStr = "";

		Random random = new Random();
		int qtd = random.nextInt(2);

		String text = "TERMINE com ";
		if(qtd==1) {
			text +=  "\"" + a.charAt(a.length()-2) + a.charAt(a.length()-1) + "\""+ "!";
			ruleStr +=  "" + a.charAt(a.length()-2) + a.charAt(a.length()-1);
		}
		else {
			text +=  "'" + a.charAt(a.length()-1) + "'"+ "!";
			ruleStr += "" + a.charAt(a.length()-1);
		}

		ruleStrings.setRuleText(text);
		ruleStrings.setRuleVerificationText(ruleStr);
		return ruleStrings;
	}
	private static boolean ruleVerification6 (String a, String ruleStr) {
		return a.endsWith(ruleStr);
	}


	public interface Rule {
		RuleStrings getRule(String a);
		boolean getVerification(String a, String ruleStr);
	}

	public static Rule[] allRules = new Rule[] {
			new Rule() {@Override
			public RuleStrings getRule(String a) {return Rules.rule1(a);}
			@Override
			public boolean getVerification(String a, String ruleStr) {return Rules.ruleVerification1(a,ruleStr);} },

			new Rule() {@Override
			public RuleStrings getRule(String a) {return Rules.rule2(a);}
			@Override
			public boolean getVerification(String a, String ruleStr) {return Rules.ruleVerification2(a, ruleStr);} },

			new Rule() {@Override
			public RuleStrings getRule(String a) {return Rules.rule3(a);}
			@Override
			public boolean getVerification(String a, String ruleStr) {return Rules.ruleVerification3(a, ruleStr);} },

			new Rule() {@Override
			public RuleStrings getRule(String a) {return Rules.rule4(a);}
			@Override
			public boolean getVerification(String a, String ruleStr) {return Rules.ruleVerification4(a, ruleStr);} },

			new Rule() {@Override
			public RuleStrings getRule(String a) {return Rules.rule5(a);}
			@Override
			public boolean getVerification(String a, String ruleStr) {return Rules.ruleVerification5(a, ruleStr);} },

			new Rule() {@Override
			public RuleStrings getRule(String a) {return Rules.rule6(a);}
			@Override
			public boolean getVerification(String a, String ruleStr) {return Rules.ruleVerification6(a, ruleStr);} }
	};

	public static void main(String args[]) {
		String previousWord = "cair";
		try (Scanner scanner = new Scanner(System.in)) {
			while(true) {
				Random random = new Random();
				int ruleNumber = random.nextInt(allRules.length);
				RuleStrings ruleStrings = Rules.allRules[ruleNumber].getRule(previousWord);
				System.out.println(ruleStrings.getRuleText());
				previousWord = scanner.nextLine();
				while(!Rules.allRules[ruleNumber].getVerification(previousWord, ruleStrings.getRuleVerificationText())) {
					System.out.println("Não segue a regra:" + ruleStrings.getRuleVerificationText());
					previousWord = scanner.nextLine();
				}
			}
		}
	}


}
