package model;

import java.util.ArrayList;

/**
 * 
 * A resizable matrix (we can add or delete rows and columns)
 * Matrice carrée
 *
 */
public class MaMatrice<E>{
	
	private ArrayList<String> labels;
	private ArrayList<ArrayList<E>> data;
	
	public void delete(){
		
	}
	
	public void addPlace(String label){
		
	}
	
	public void addLink(String src, String dest, int weight){
		
	}

	public void addLink(int src, int dest, int weight){
		
	}
	
	public E get(String src, String dest){
		
	}
	
	public E get(int src, int dest){
		
	}
	
	//Taille de la matrice carrée
	public int size(){
		return data.size();
	}
}
