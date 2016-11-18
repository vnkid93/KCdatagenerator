package textgenerators;

import java.util.Random;

public abstract class ITextGenerator {
	protected Random rand;
	
	protected ITextGenerator(){
		rand = new Random();
	}
	
	public abstract String genWords(boolean nationalChar, int numOfWords);
	
	public abstract String genParagraph(boolean nationalChar, int numOfPar);
	
	public abstract String genSentence(boolean nationalChar, int numOfSentences);
	
	public abstract String genSubject(boolean nationalChar);
	
	public abstract String genShortContent(boolean nationalChar);
	
	public abstract String genTelNumber();
	
	public abstract String genLoginName();
	
	public abstract String genFirstName(boolean nationalChar);
	
	public abstract String genLastName(boolean nationalChar);
	
	public abstract String genFullName(boolean nationalChar);
	
	public abstract String genWebsite();
	
	public abstract String genPassword(boolean complex);
}
