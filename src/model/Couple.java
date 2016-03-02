package model;

public class Couple<E, F>{
	private E left;
	private F right;
	
	public Couple(E e, F f){
		left = e;
		right = f;
	}
	
	public E left(){
		return left;
	}
	
	public F right(){
		return right;
	}
}
