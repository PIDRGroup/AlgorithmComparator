package model;

import java.util.ArrayList;
import java.util.HashMap;

public class AStar extends Algorithm{
	
	public AStar(){
		path = new ArrayList<Place>();
		eval = new Evaluation();
	}

	@Override
	public void grow(Environment world, Place src, Place dest) throws UnknownPlaceException{
		
		if(!world.isPlace(src))
			throw new UnknownPlaceException(src);
		
		if(!world.isPlace(dest))
			throw new UnknownPlaceException(dest);
		
		ArrayList<Integer> noeudouvert = new ArrayList<Integer>();
		ArrayList<Integer> noeudferme = new ArrayList<Integer>();
		noeudouvert.add(world.indexOf(src));
		HashMap<Integer,Integer> predecesseur = new HashMap<Integer,Integer>();
		
		ArrayList<Double> g = new ArrayList<Double>();
		ArrayList<Double> f = new ArrayList<Double>();
		
		for (int i =0; i < world.size(); i++){
			g.add(Double.MAX_VALUE);
			f.add(Double.MAX_VALUE);
		}
		g.set(world.indexOf(src), 0.0);
		f.set(world.indexOf(src), h(src));
		
		while (!noeudouvert.isEmpty()){
			double min = Double.MAX_VALUE;
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
			double dist;
			
			for (int i = 0; i < world.size(); i++){
				if (world.get(world.getPlace(current), world.getPlace(i)) < Integer.MAX_VALUE){
					
					if (noeudferme.contains(new Integer(i))){
						continue;
					}
					
					dist = (int) (g.get(current) + world.get(world.getPlace(current), world.getPlace(i)));
					if (!noeudouvert.contains(new Integer(i))){
						noeudouvert.add(i);
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
	
	private double h(Place current){
		return 0;
	}

	@Override
	public String getName() {
		return "A*";
	}
}
