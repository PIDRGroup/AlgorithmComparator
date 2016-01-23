package model;

import java.util.ArrayList;

public class AStar<E extends Number> extends Algorithm<E>{
	
	public AStar(Environment<E> env){
		this.world = env;
	}

	@Override
	public void grow(int source, int destination) throws UnknownPlace{
		MaMatrice<Integer> matrice = new MaMatrice<Integer>();
		ArrayList<Integer> noeudouvert = new ArrayList<Integer>();
		ArrayList<Integer> noeudferme = new ArrayList<Integer>();
		noeudferme.add(source);
		ArrayList<Integer> current_path = new ArrayList<Integer>();
		
		ArrayList<Integer> g = new ArrayList<Integer>();
		ArrayList<Integer> f = new ArrayList<Integer>();
		
		while (!noeudouvert.isEmpty()){
			int min = Integer.MAX_VALUE;
			
			for (int i = 0; i < matrice.size(); i++){
				if (f.get(i) < min && noeudouvert.contains(new Integer(i))){
					
				}
			}
		}
	}

	@Override
	public void grow(String src, String dest) throws UnknownPlace {
		
	}

}
