package model;

import java.util.Observable;

public class Environment<E extends Number> extends Observable{
	
	private MaMatrice<E> matrix;
	
	public Environment(){
		
	}

	public Environment(Environment<E> env){
		
	}
	
	public MaMatrice<E> getMatrix(){
		return matrix;
	}

}
