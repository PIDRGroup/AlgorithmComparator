package model.env;

import java.io.Serializable;

/**
 * 
 * Repr�sente les bornes des coordonn�es des places.
 *
 */
public class Bound implements Serializable{
	
	public static final int INIT_BOUND = 300;
	private int min, max;
	
	public Bound(int min, int max){
		this.min = min;
		this.max = max;
	}
	
	public int min(){
		return min;
	}
	
	public int max(){
		return max;
	}
	
	public int size(){
		return max-min;
	}
}
