package model;

import java.util.ArrayList;
import java.util.HashMap;

public class AStar<E extends Number> extends Algorithm<E>{
	
	public AStar(Environment<E> env){
		this.world = env;
		path = new ArrayList<Integer>();
	}

	@Override
	public void grow(int src, int dest) throws UnknownPlace{
		MaMatrice<Integer> matrice = (MaMatrice<Integer>) world.getMatrix();
		ArrayList<Integer> noeudouvert = new ArrayList<Integer>();
		ArrayList<Integer> noeudferme = new ArrayList<Integer>();
		noeudouvert.add(src);
		HashMap<Integer,Integer> predecesseur = new HashMap<Integer,Integer>();
		
		ArrayList<Integer> g = new ArrayList<Integer>();
		ArrayList<Integer> f = new ArrayList<Integer>();
		
		for (int i =0; i < matrice.size(); i++){
			g.add(Integer.MAX_VALUE);
			f.add(Integer.MAX_VALUE);
		}
		g.set(src, 0);
		f.set(src, h(src));
		
		while (!noeudouvert.isEmpty()){
			int min = Integer.MAX_VALUE;
			int current = 0;
			
			for (int i = 0; i < matrice.size(); i++){
				//On recherche le noeud n'appartenant pas au noeud ouvert tel que f est minimal
				if (f.get(i) < min && noeudouvert.contains(new Integer(i))){
					min = f.get(i);
					current = i;
				}
			}
			
			if (current == dest){
				path.add(current);
				setChanged();
				notifyObservers();
				while (predecesseur.containsKey(current)){
					current = predecesseur.get(current);
					path.add(current);
					setChanged();
					notifyObservers();
				}
				break;
			}
						
			noeudouvert.remove(new Integer(current));
			noeudferme.add(current);
			int dist;
			
			for (int i = 0; i < matrice.size(); i++){
				if (matrice.get(current, i) < Integer.MAX_VALUE){
					if (noeudferme.contains(new Integer(i))){
						continue;
						
					}
					
					dist = g.get(current) + matrice.get(current, i);
					if (!noeudouvert.contains(new Integer(i))){
						noeudouvert.add(i);
					}else if (dist >= g.get(i)){
						continue;
					}
					
					predecesseur.put(i,current);
					g.set(i, dist);
					f.set(i, g.get(i) + h(i));
				}
			}
		}
		
		if (noeudouvert.isEmpty()){
			System.out.println("Aucun chemin trouvé!");
		}
	}
	
	int h(int current){
		return 0;
	}

	@Override
	public String getName() {
		return "A*";
	}
}
