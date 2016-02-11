package model;

import java.util.ArrayList;

public abstract class Algorithm implements Runnable{
	
	protected Environment world;
	protected  ArrayList<Place> path;
	protected Place source, destination;
	
	//Attrbuts pour juger de la performance de l'algo
	protected long estimated_time;
	protected int nb_visited_nodes;
	
	/**
	 * pply an algorithm to the environment
	 * @param src
	 * @param dest
	 * @throws UnknownPlaceException
	 */
	public abstract void grow(Place src, Place dest) throws UnknownPlaceException;
	
	public void grow() throws UnknownPlaceException{
		grow(destination, source);
	}
	
	public ArrayList<Place> getPath(){
		return path;
	}
	
	public ArrayList<String> getPathLabels() throws UnknownPlaceException{
		ArrayList<String> labels = new ArrayList<String>();
		
		for(Place pl : path){
			labels.add(pl.getLabel());
		}
		
		return labels;
	}
	
	public void setDest(Place dest){
		destination = dest;
	}
	
	public void setSrc(Place src){
		source=src;
	}
	
	public void run(){
		long first_time = System.currentTimeMillis() / 1000;
		
		try {
			grow(source, destination);
		} catch (UnknownPlaceException e) {
			e.printStackTrace();
		}
		
		estimated_time = System.currentTimeMillis() / 1000 - first_time;
	}
	
	public long getDuration(){
		return estimated_time;
	}
	
	public int getNbNodes(){
		return nb_visited_nodes;
	}
	
	public abstract String getName();
}
