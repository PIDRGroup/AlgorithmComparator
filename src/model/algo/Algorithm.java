package model.algo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import model.env.Environment;
import model.env.Place;
import model.env.UnknownPlaceException;

public abstract class Algorithm extends Observable implements Serializable{
	
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
	public Evaluation getEval(){return eval;}
}
