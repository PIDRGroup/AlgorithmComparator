package model;

import java.util.ArrayList;
import java.util.Observable;

public abstract class Algorithm<E extends Number> extends Observable implements Runnable{
	
	protected Environment<E> world;
	protected  ArrayList<Integer> path;
	protected int source, destination;
	
	/**
	 * Modify the environment depending on the current algorithm.
	 * Each algorithm has its own instance of the environment
	 */
	public abstract void grow(int source, int destination) throws UnknownPlace;
	
	public void grow(String src, String dest) throws UnknownPlace{
		if(!world.isLabel(src))
			throw new UnknownPlace(src);
		
		if(!world.isLabel(dest))
			throw new UnknownPlace(dest);
		
		grow(world.indexOf(src), world.indexOf(dest));
	}
	
	public void grow() throws UnknownPlace{
		grow(destination, source);
	}
	
	public ArrayList<Integer> getPathIndexes(){
		return path;
	}
	
	public ArrayList<String> getPathLabels(){
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
	
	public void setDest(String dest){
		destination = world.indexOf(dest);
	}
	
	public void setSrc(String src){
		source=world.indexOf(src);
	}
	
	public void run(){
		try {
			grow(source, destination);
		} catch (UnknownPlace e) {
			e.printStackTrace();
		}
	}
}
