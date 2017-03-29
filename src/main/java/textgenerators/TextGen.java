package textgenerators;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.thedeanda.lorem.LoremIpsum;

import engine.SimpleAddress;

public class TextGen extends ITextGenerator{
	
	//final static Logger logger = Logger.getLogger(TextGen.class);
	private LoremIpsum loremIpsum;
	private static TextGen instance = null;
	
	private String[] cs_firstNames = null;
	private String[] cs_lastNames = null;
	private String[] us_firstNames = null;
	private String[] us_lastNames = null;
	private String[] countries = null;
	
	private static final String[] CZ_CITIES = {"Plzeň", "České Budějovice", "Mariánské Lázně", "Hradec Králové", "Telč", "Litoměřice", "Děčín", "Ústí nad Labem"};
	private static final String[] OTHER_CITIES = {"New York", "Los Angeles", "Praha", "Hanoi", "Chicago", "London", "Brno", "Saigon"};
	
	
	final String[] domains = {".eu", ".com", ".cz", ".ru", ".vn", ".org", ".biz", ".io", ".net"};
	final String[] companies = {"Kerio", "Google", "Yahoo", "Microsoft", "Facebook", "Apple", "Samsung", "Seznam"};
	final String[] jobTitles = {"Tester", "Manager", "Technical Writer", "Developer" , "Supporter", "Secretary", "Administrator", "Designer", "Production Coordinator", "Marketing Manager"};

	final String[] nickNames = {"Jack", "Katie", "Johny", "Abby", "Sara", "Fiona", "Sam", "Shrek", "Juan"};
	final String[] names = {"Mae", "Marie", "May", "Lee", "Ann", "Mary", "Duc"};
	
	private String textBook;
	private int textBookLength;
	
	private TextGen(){
		super();
		//logger.debug("Start initializing");
		cs_firstNames = fillNames("src/main/resources/cs_firstname.txt");
		cs_lastNames = fillNames("src/main/resources/cs_lastname.txt");
		us_firstNames = fillNames("src/main/resources/us_firstname.txt");
		us_lastNames = fillNames("src/main/resources/us_lastname.txt");
		fillText();
		countries = fillCountries("src/main/resources/countries.txt");
		this.loremIpsum = LoremIpsum.getInstance();
		//logger.debug("Initializing ended");
	}
	
	
	private String[] fillCountries(String filePath){
		//logger.debug("Starting to fill countries");
		String[] countries = null;
		try {
			InputStreamReader in = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
			
			BufferedReader br = new BufferedReader(in);
			List<String> list = new ArrayList<String>();
			
			String line;
			while((line=br.readLine()) != null){
				list.add(line);
			}
			countries = list.toArray(new String[list.size()]);
			br.close();
			in.close();
		} catch (FileNotFoundException e) {
			//logger.error("File not found: "+filePath);
			e.printStackTrace();
		} catch (IOException e) {
			//logger.error("IO Exception during reading file "+filePath);
			e.printStackTrace();
		}
		return countries;
	}
	
	private String[] fillNames(String filePath){
		//logger.debug("Starting to fill names");
		String[] nameArr = null;
		try {
			InputStreamReader in = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
			BufferedReader br = new BufferedReader(in);
			List<String> list = new ArrayList<String>();
			String line;
			while((line=br.readLine()) != null){
				list.add(line);
			}
			
			nameArr = list.toArray(new String[list.size()]);
			br.close();
			in.close();
		} catch (FileNotFoundException e) {
			//logger.error("File not found: "+filePath);
			e.printStackTrace();
		} catch (IOException e) {
			//logger.error("IO Exception during reading file "+filePath);
			e.printStackTrace();
		}
		return nameArr;
	}
	
	private void fillText(){
		final String fileName = "cs_text.txt";
		//logger.debug("Starting to fill text: "+fileName);
		try {
			InputStreamReader in = new InputStreamReader(new FileInputStream("src/main/resources/"+fileName), "UTF-8");
			BufferedReader br = new BufferedReader(in);
			String line;
			textBook = "";
			while((line=br.readLine()) != null){
				textBook += line;
			}
			textBookLength = textBook.length();
			br.close();
			in.close();
		} catch (FileNotFoundException e) {
			//logger.error("File not found: "+fileName);
			e.printStackTrace();
		} catch (IOException e) {
			//logger.error("IO Exception during reading file "+fileName);
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
	
	
	public String genWords(int numOfWords){
		return loremIpsum.getWords(numOfWords);
	}
	
	public String genParagraph(int numOfPar){
		StringBuilder str = new StringBuilder();
		if(nationalChar){
			final int parSize = 1000;
			for (int i = 0; i < numOfPar; i++) {
				int seek = rand.nextInt((int)(textBookLength*0.5));
				seek = textBook.indexOf('.', seek); // wtf
				//textBook.index
				//TODO do while to check the length
				int nextIndex = textBook.indexOf('.', seek + (rand.nextInt(parSize / 4 * 1) + parSize / 4 * 3));
				str.append(textBook.substring(seek+2, nextIndex+1));
				str.append("\n");
			}			
		}else{
			str.append(loremIpsum.getParagraphs(numOfPar, numOfPar));
		}
		
		return str.toString();
	}
	
	public String genSentence(int numOfSentences){
		StringBuilder str = new StringBuilder();
		if(nationalChar){
			int preLastIndex = textBook.lastIndexOf('.', textBookLength-2);
			for (int i = 0; i < numOfSentences; i++) {
				int seek = textBook.indexOf('.', rand.nextInt(preLastIndex));
				str.append(textBook.substring(seek+2, textBook.indexOf('.', seek+1)));
				str.append(". ");
			}			
		}else{
			for (int i = 0; i < numOfSentences; i++) {
				str.append(loremIpsum.getWords(rand.nextInt(20) + 5) + ". ");
			}
		}
		return str.toString().trim();
	}
	
	public String genTelNumber(){
		//logger.debug("Generating telephone number");
		final int CONST = 100000000;
		int num = (int)(rand.nextFloat() * (10 * CONST));
		if(num < CONST) num += CONST;
		return "+420"+num;
	}
	
	public String genFirstName(){
		return nationalChar ? cs_firstNames[rand.nextInt(cs_firstNames.length)] : us_firstNames[rand.nextInt(us_firstNames.length)];
	}
	
	public String genLastName(){
		return nationalChar ? cs_lastNames[rand.nextInt(cs_lastNames.length)] : us_lastNames[rand.nextInt(cs_lastNames.length)];
	}
	
	public String genFullName(){
		return genFirstName()+ " " + genLastName();
	}
	
	public String genWebsite(){
		StringBuilder str = new StringBuilder();
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
		return complex ? "ABCabc123" : "a";
	}


	@Override
	public String genLoginName() {
		return "testxx";
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
	
	public String genJobTitle(){
		return jobTitles[rand.nextInt(jobTitles.length)];
	}
	
	public String genCompany(){
		
		return companies[rand.nextInt(companies.length)];
	}


	@Override
	public Date genBirthDay() {
		DateTime birthDay = new DateTime();
		birthDay = birthDay.minusYears(rand.nextInt(40)+20);
		birthDay = birthDay.minusMonths(rand.nextInt(12));
		birthDay = birthDay.minusDays(rand.nextInt(31));
		return birthDay.toDate();
	}

	@Override
	public Date genAnniversary() {
		DateTime birthDay = new DateTime();
		birthDay = birthDay.minusYears(rand.nextInt(20)+5);
		birthDay = birthDay.minusMonths(rand.nextInt(12));
		birthDay = birthDay.minusDays(rand.nextInt(31));
		return birthDay.toDate();
	}


	@Override
	public String genMiddleName() {
		return names[rand.nextInt(names.length)];
	}


	@Override
	public String genNickName() {
		return nickNames[rand.nextInt(nickNames.length)];
	}

	@Override
	public String genEmail() {
		return genEmail(genFirstName(), genLastName());
	}

	@Override
	public String genEmail(String firstName, String lastName) {
		String firstPart = firstName.toLowerCase() + "." + lastName.toLowerCase();
		firstPart = Normalizer.normalize(firstPart, Normalizer.Form.NFD).replaceAll("[^\\x00-\\x7F]", "");
		return firstPart + (rand.nextInt(99) + 1)+ "@" + genCompany().toLowerCase() + ".kom";
	}
	
	public String genLocation(){
		String []cities = (nationalChar) ? CZ_CITIES : OTHER_CITIES;
		return cities[rand.nextInt(cities.length)];
	}
	
	public String genCountry(){
		return countries[rand.nextInt(countries.length)];
	}
	
	public SimpleAddress genAddress(){
		String street = us_lastNames[rand.nextInt(us_lastNames.length)] + " " + (rand.nextInt(200)+1);
		String city = genLocation();
		String zip = (rand.nextInt(300)+100)+" "+(rand.nextInt(100));
		String country = genCountry();
		
		return new SimpleAddress(street, city, country, zip);
	}
	
	
}
