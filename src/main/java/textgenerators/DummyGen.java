package textgenerators;

import java.util.Date;

public class DummyGen extends ITextGenerator{
	private final String CZECH_SENTENCE = "Vidíte to, fakt vostrý to krůta, prso, uděláme vějířky a k tomu, ať je bezpracná, to dávají do šejku, Klára ji do změknutí nechat.";
	private final String US_SENTENCE = "Hello, how are you";
	private final String CZECH_WORD = "černá";
	private final String US_WORD = "black";
	
	public DummyGen(){
		super();
	}

	public String genWords(int numOfWords) {
		StringBuilder str = new StringBuilder();
		String append = (nationalChar) ? CZECH_WORD : US_WORD;
		for (int i = 0; i < numOfWords; i++) {
			str.append(append+ " ");
		}
		return str.toString();
	}

	public String genParagraph(int numOfPar) {
		StringBuilder str = new StringBuilder();
		String append = (nationalChar) ? CZECH_SENTENCE : US_SENTENCE;
		for (int i = 0; i < numOfPar; i++) {
			int numOfSentences = rand.nextInt(10);
			for (int j = 0; j < numOfSentences; j++) {
				str.append(append + ". ");
			}
			str.append("\n");
		}
		return str.toString();
	}

	public String genSentence(int numOfSentences) {
		StringBuilder str = new StringBuilder();
		String append = (nationalChar) ? CZECH_SENTENCE : US_SENTENCE;
		for (int j = 0; j < numOfSentences; j++) {
			str.append(append + ". ");
		}
		return str.toString();
	}

	public String genTelNumber() {
		return "+420777123456";
	}

	public String genFirstName() {
		// TODO Auto-generated method stub
		return (nationalChar) ? "Aněžda" : "John";
	}

	public String genLastName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String genFullName() {
		return (nationalChar) ? "Dvořáková" : "Brown";
	}

	public String genWebsite() {
		// TODO Auto-generated method stub
		return "www.gugle.com";
	}

	public String genPassword(boolean complex) {
		return complex ? "ABCabc132" : "a";
	}

	@Override
	public String genLoginName() {
		return null;
	}

	@Override
	public String genSubject() {
		return genSentence(1);
	}

	@Override
	public String genShortContent() {
		// TODO Auto-generated method stub
		return genParagraph(rand.nextInt(3)+1);
	}

	@Override
	public Date genBirthDay() {
		return new Date();
	}

	@Override
	public Date genAnniversary() {
		// TODO Auto-generated method stub
		return new Date();
	}

	@Override
	public String genMiddleName() {
		return "Duc";
	}

	@Override
	public String genNickName() {
		return "Jack";
	}

	@Override
	public String genEmail() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String genEmail(String firstName, String lastName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String genLocation() {
		// TODO Auto-generated method stub
		return (nationalChar) ? "Třebíč" : "Prague";
	}

}
