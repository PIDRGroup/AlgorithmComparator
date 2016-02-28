package model;

import java.util.ArrayList;
import java.util.Observable;

public abstract class Algorithm extends Observable{
	
	protected  ArrayList<Place> path;
	
	//Attrbuts pour juger de la performance de l'algo
	protected long estimated_time;
	protected int nb_visited_nodes;
	
	/**
	 * pply an algorithm to the environment
	 * @param src
	 * @param dest
	 * @throws UnknownPlaceException
	 */
	public abstract void grow(Environment world, Place src, Place dest) throws UnknownPlaceException;
	
	public ArrayList<Place> getPath(){
		return path;
	}
	
	public long getDuration(){
		return estimated_time;
	}
	
	public int getNbNodes(){
		return nb_visited_nodes;
	}
	
	public abstract String getName();
}
