package model;

/**
 * 
 * Mod�lise un couple destination / poids
 *
 */
public class Link<E extends Number>{
	
	/**
	 * Label de la source et de la destination du lien
	 */
	private int destination;
	
	/**
	 * Valeur du lien
	 */
	private E value;
	
	public Link(int dest, E val){
		destination = dest;
		value=val;
	}
	
	/**
	 * Cr�e un lien fix� arbitrairement � 1
	 * @param src Source du lien
	 * @param dest Destination du lien
	 */
	public Link(int dest){
		this(dest, (E) (Integer) 1);
	}
	
	@Override
	public boolean equals(Object o){
		Link l = (Link) o;
		
		return l.destination == destination;
	}
	
	public int getDest(){
		return destination;
	}
	
	public E getVal(){
		return value;
	}
	
	public void setVal(E val){
		value=val;
	}
	
	public String toString(){
		return destination + " / " + value;
	}
}
