package engine;

import java.util.Random;

public class Probability {

	private Random rand;
	private static Probability instance = null;
	
	private Probability(){
		rand = new Random();
	}
	
	public static Probability getInstance(){
		if(instance == null){
			instance = new Probability();
		}
		return instance;
	}
	
	/**
	 * Get boolean from probability passed as argument.
	 * @param percentage integer value from 0 to 100, which acts as percentage
	 * @return boolean result of the probability
	 */
	public boolean tryLuck(int percentage){
		return rand.nextInt(100) < percentage;
	}
	
	public Random getRadom(){
		return rand;
	}
	
	
	
}
