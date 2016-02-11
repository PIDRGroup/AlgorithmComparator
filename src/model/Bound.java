package model;

/**
 * 
 * Repr�sente les bornes des coordonn�es des places.
 *
 */
public class Bound {
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
}
