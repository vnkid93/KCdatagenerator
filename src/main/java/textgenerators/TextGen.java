package textgenerators;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import de.svenjacobs.loremipsum.LoremIpsum;

public class TextGen extends ITextGenerator{
	private static final int NAME_COUNT = 100;
	private LoremIpsum loremIpsum;
	private static TextGen instance = null;
	
	private final String[] cs_firstNames = new String[NAME_COUNT];
	private final String[] cs_lastNames = new String[NAME_COUNT];
	private final String[] us_firstNames = new String[NAME_COUNT];
	private final String[] us_lastNames = new String[NAME_COUNT];
	

	private String textBook;
	private int textBookLength;
	
	public TextGen(){
		super();
		fillNames(cs_firstNames, "src/main/resources/cs_firstname.txt");
		fillNames(cs_lastNames, "src/main/resources/cs_lastname.txt");
		fillNames(us_firstNames, "src/main/resources/us_firstname.txt");
		fillNames(us_lastNames, "src/main/resources/us_lastname.txt");
		fillText();
	}
	
	
	private void fillNames(String[] nameArr, String filePath){
		try {
			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			for (int i = 0; i < nameArr.length; i++) {
				nameArr[i] = br.readLine();
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void fillText(){
		try {
			FileReader fr = new FileReader("src/main/resources/cs_text.txt");
			BufferedReader br = new BufferedReader(fr);
			String line;
			textBook = "";
			while((line=br.readLine()) != null){
				textBook += line;
			}
			textBookLength = textBook.length();
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Singleton - getInstance method
	 * @return the instance of this class
	 */
	public static TextGen getInstance(){
		if(instance == null){
			instance = new TextGen();
		}
		return instance;
	}
	
	
	public String genWords(boolean nationalChar, int numOfWords){
		return loremIpsum.getWords(numOfWords);
	}
	
	public String genParagraph(boolean nationalChar, int numOfPar){
		final int parSize = 1000;
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < numOfPar; i++) {
			int seek = rand.nextInt((int)(textBookLength*0.5));
			seek = textBook.indexOf('.', seek); // wtf
			//textBook.index
			//TODO do while to check the length
			int nextIndex = textBook.indexOf('.', seek + (rand.nextInt(parSize / 4 * 1) + parSize / 4 * 3));
			str.append(textBook.substring(seek+2, nextIndex+1));
			str.append("\n");
		}
		return str.toString();
	}
	
	public String genSentence(boolean nationalChar, int numOfSentences){
		StringBuilder str = new StringBuilder();
		int preLastIndex = textBook.lastIndexOf('.', textBookLength-2);
		for (int i = 0; i < numOfSentences; i++) {
			int seek = textBook.indexOf('.', rand.nextInt(preLastIndex));
			str.append(textBook.substring(seek+2, textBook.indexOf('.', seek+1)));
			str.append(". ");
		}
		return str.toString().trim();
	}
	
	public String genTelNumber(){
		final int CONST = 100000000;
		int num = (int)(rand.nextFloat() * (10 * CONST));
		if(num < CONST) num += CONST;
		return "+420"+num;
	}
	
	public String genFirstName(boolean nationalChar){
		return nationalChar ? cs_firstNames[rand.nextInt(NAME_COUNT)] : us_firstNames[rand.nextInt(NAME_COUNT)];
	}
	
	public String genLastName(boolean nationalChar){
		return nationalChar ? cs_lastNames[rand.nextInt(NAME_COUNT)] : us_lastNames[rand.nextInt(NAME_COUNT)];
	}
	
	public String genFullName(boolean nationalChar){
		return genFirstName(nationalChar)+ " " + genLastName(nationalChar);
	}
	
	public String genWebsite(){
		StringBuilder str = new StringBuilder();
		final String[] domains = {".eu", ".com", ".cz", ".ru", ".vn", ".org", ".biz", ".io", ".net"};
		str.append("www.");
		int count = rand.nextInt(20)+5;
		
		for (int i = 0; i < count; i++) {
			str.append((char)(rand.nextInt('z'-'a') + 'a'));
			if(rand.nextFloat() < 0.1 && i>0 && i+1 < count){
				str.append('-');
			}
		}
		str.append(domains[rand.nextInt(domains.length)]);
		return str.toString();
	}
	
	public String genPassword(boolean complex){
		return complex ? "ABCabc132" : "a";
	}


	@Override
	public String genLoginName() {
		
		return "FIX_THIS";
	}
	
	@Override
	public String genSubject(boolean nationalChar) {
		return genSentence(nationalChar, 1);
	}

	@Override
	public String genShortContent(boolean nationalChar) {
		// TODO Auto-generated method stub
		return genParagraph(nationalChar, rand.nextInt(3)+1);
	}
	
	
}
