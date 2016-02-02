package model;

import java.util.ArrayList;

public class GreedybestfirstSearch<E extends Number> extends Algorithm<E> {

	@Override
	public void grow(int source, int destination) throws UnknownPlaceException {
		// TODO Auto-generated method stub

		ArrayList<Integer> noeudouvert = new ArrayList<Integer>();
		ArrayList<Integer> noeudferme = new ArrayList<Integer>();
		noeudouvert.add(source);
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
