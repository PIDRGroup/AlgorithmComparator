package model;

import java.util.Observable;

public class Environment<E extends Number> extends Observable{
	
	private MaMatrice<E> matrix;
	
	public Environment(){
		matrix = new MaMatrice<E>();
	}
	
	public MaMatrice<E> getMatrix(){
		return matrix;
	}

	public Environment<E> duplicate(){
		Environment<E> copy = new Environment<E>();
		
		copy.matrix = this.matrix.duplicate();
		
		return copy;
	}
	
	public String toString(){
		return matrix.toString();
	}
}
