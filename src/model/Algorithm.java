package model;

import java.util.ArrayList;
import java.util.Observable;

public abstract class Algorithm extends Observable{
	
	protected ArrayList<Place> path;
	protected Evaluation eval;
	
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
	
	public abstract String getName();
}
