package model;

import java.util.ArrayList;
import java.util.Observable;

public abstract class Algorithm<E extends Number> extends Observable implements Runnable{
	
	protected Environment<E> world;
	protected  ArrayList<Integer> path;
	protected int source, destination;
	
	//Attrbuts pour juger de la performance de l'algo
	protected long estimated_time;
	protected int nb_visited_nodes;
	
	/**
	 * Modify the environment depending on the current algorithm.
	 * Each algorithm has its own instance of the environment
	 */
	public abstract void grow(int source, int destination) throws UnknownPlaceException;
	
	public void grow(String src, String dest) throws UnknownPlaceException{
		if(!world.isLabel(src))
			throw new UnknownPlaceException(src);
		
		if(!world.isLabel(dest))
			throw new UnknownPlaceException(dest);
		
		grow(world.indexOf(src), world.indexOf(dest));
	}
	
	public void grow() throws UnknownPlaceException{
		grow(destination, source);
	}
	
	public ArrayList<Integer> getPathIndexes(){
		return path;
	}
	
	public ArrayList<String> getPathLabels() throws UnknownPlaceException{
		ArrayList<String> labels = new ArrayList<String>();
		
		for(int pl : path){
			labels.add(world.labelOf(pl));
		}
		
		return labels;
	}
	
	public void setDest(int dest){
		destination = dest;
	}
	
	public void setSrc(int src){
		source=src;
	}
	
	public void setDest(String dest) throws UnknownPlaceException{
		destination = world.indexOf(dest);
	}
	
	public void setSrc(String src) throws UnknownPlaceException{
		source=world.indexOf(src);
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
