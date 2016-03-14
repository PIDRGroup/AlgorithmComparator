package model;

import java.util.ArrayList;
import java.util.Random;

public class Seed {
	private long seed;
	private ArrayList<Bound> bounds;
	private Random generator;
	private int nb_places;
	
	public Seed(long s, int nbPlaces, ArrayList<Bound> b){
		seed = s;
		bounds = b;
		generator = new Random(seed);
		nb_places = nbPlaces;
	}
	
	public Place nextPlace(){
		ArrayList<Long> list = new ArrayList<Long>();
		
		for (int i = 0; i < bounds.size(); i++) {
			list.add(new Long(generator.nextInt(bounds.get(i).max()-bounds.get(i).min()) + bounds.get(i).min()));
		}
		
		return new Place(list);
	}
	
	public Place nextLink(ArrayList<Place> places){
		int i = -1;
		
		i = generator.nextInt(places.size());
		
		return places.get(i);
	}
	
	public int nbDim(){
		return bounds.size();
	}
	
	public ArrayList<Bound> getBounds(){
		return bounds;
	}
	
	public int nbPlaces(){
		return nb_places;
	}
}
