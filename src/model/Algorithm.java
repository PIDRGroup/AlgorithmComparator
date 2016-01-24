package model;

import java.util.ArrayList;

public abstract class Algorithm<E extends Number> extends Thread{
	
	protected Environment<E> world;
	protected  ArrayList<Integer> path;
	
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
}
