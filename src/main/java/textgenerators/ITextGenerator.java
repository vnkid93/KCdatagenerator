package textgenerators;

import java.util.Date;
import java.util.Random;

public abstract class ITextGenerator {
	protected Random rand;
	protected boolean nationalChar;
	protected ITextGenerator(){
		rand = new Random();
		nationalChar = true;
	}
	
	public void setNationalChar(boolean nationalChar){
		this.nationalChar = nationalChar;
	}
	
	public abstract String genWords(int numOfWords);
	
	public abstract String genParagraph(int numOfPar);
	
	public abstract String genSentence(int numOfSentences);
	
	public abstract String genSubject();
	
	public abstract String genShortContent();
	
	public abstract String genTelNumber();
	
	public abstract String genLoginName();
	
	public abstract String genFirstName();
	
	public abstract String genLastName();
	
	public abstract String genFullName();
	
	public abstract String genWebsite();
	
	public abstract String genPassword(boolean complex);
	
	public abstract Date genBirthDay();
	
	public abstract Date genAnniversary();
	
	public abstract String genMiddleName();
	
	public abstract String genNickName();
	
	public abstract String genEmail();
	
	public abstract String genEmail(String firstName, String lastName);
	
	public abstract String genLocation();
}
