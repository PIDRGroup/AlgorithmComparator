package model;

import java.util.ArrayList;
import java.util.HashMap;

public class AStar extends Algorithm{
	
	public AStar(Environment env){
		this.world = env;
		path = new ArrayList<Place>();
	}

	@Override
	public void grow(Place src, Place dest) throws UnknownPlaceException{
		nb_visited_nodes = 0;
		estimated_time = 0;
		
		ArrayList<Integer> noeudouvert = new ArrayList<Integer>();
		ArrayList<Integer> noeudferme = new ArrayList<Integer>();
		noeudouvert.add(world.indexOf(src));
		HashMap<Integer,Integer> predecesseur = new HashMap<Integer,Integer>();
		
		ArrayList<Integer> g = new ArrayList<Integer>();
		ArrayList<Integer> f = new ArrayList<Integer>();
		
		for (int i =0; i < world.size(); i++){
			g.add(Integer.MAX_VALUE);
			f.add(Integer.MAX_VALUE);
		}
		g.set(world.indexOf(src), 0);
		f.set(world.indexOf(src), h(src));
		
		while (!noeudouvert.isEmpty()){
			int min = Integer.MAX_VALUE;
			int current = 0;
			
			for (int i = 0; i < world.size(); i++){
				//On recherche le noeud n'appartenant pas au noeud ouvert tel que f est minimal
				if (f.get(i) < min && noeudouvert.contains(new Integer(i))){
					min = f.get(i);
					current = i;
				}
			}
			
			if (current == world.indexOf(dest)){
				path.add(world.getPlace(current));
				while (predecesseur.containsKey(current)){
					current = predecesseur.get(current);
					path.add(world.getPlace(current));
				}
				break;
			}
						
			noeudouvert.remove(new Integer(current));
			noeudferme.add(current);
			int dist;
			
			for (int i = 0; i < world.size(); i++){
				if (world.get(world.getPlace(current), world.getPlace(i)) < Integer.MAX_VALUE){
					
					if (noeudferme.contains(new Integer(i))){
						continue;
					}
					
					dist = (int) (g.get(current) + world.get(world.getPlace(current), world.getPlace(i)));
					if (!noeudouvert.contains(new Integer(i))){
						noeudouvert.add(i);
						nb_visited_nodes++;
					}else if (dist >= g.get(i)){
						continue;
					}
					
					predecesseur.put(i,current);
					g.set(i, dist);
					f.set(i, g.get(i) + h(world.getPlace(i)));
				}
			}
		}
		
		if (noeudouvert.isEmpty()){
			System.out.println("Aucun chemin trouvé!");
		}
	}
	
	int h(Place current){
		return 0;
	}

	@Override
	public String getName() {
		return "A*";
	}
}
