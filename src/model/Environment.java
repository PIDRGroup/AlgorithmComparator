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
	
	public boolean isLabel(String label){
		return matrix.isLabel(label);
	}
	
	public boolean isIndex(int index){
		return matrix.isIndex(index);
	}
	
	public int indexOf(String label){
		return matrix.indexOf(label);
	}
}
