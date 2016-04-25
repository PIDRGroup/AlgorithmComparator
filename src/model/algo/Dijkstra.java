package model.algo;

import java.util.ArrayList;

import model.env.Environment;
import model.env.Place;
import model.env.UnknownPlaceException;

public class Dijkstra extends Algorithm{
	
	public Dijkstra(){
		path = new ArrayList<Place>();
		eval = new Evaluation();
	}

	@Override
	public void grow(Environment world, Place src, Place dest) throws UnknownPlaceException {
				
		if(!world.isPlace(src))
			throw new UnknownPlaceException(src);
		
		if(!world.isPlace(dest))
			throw new UnknownPlaceException(dest);
		
		eval.start();
		
		ArrayList<Double> distance = new ArrayList<Double>();
		ArrayList<Integer> predecessor = new ArrayList<Integer>();
		ArrayList<Integer> banlist = new ArrayList<Integer>();
		double dist;
		
		for (int i = 0; i < world.size(); i++){
			try {
				if ((dist = world.get(src, world.getByIndex(i))) < Double.MAX_VALUE){
					distance.add(dist);
					//On initialise les noeuds à une distance infini de la src
				}else{
					distance.add(new Double(Double.MAX_VALUE));
					//On initialise les noeuds adjacents à la src, par leur distance à la src
				}
				
				predecessor.add(world.indexOf(src));
				//On ajoute à chaque noeud comme prédécesseur, eux-mêmes
				
			} catch (UnknownPlaceException e) {
				System.out.println("Erreur dans le grow de Dijkstra: initialisation");
			}
		}
		
		while(banlist.size() != world.size()){
			// Tant que tous les noeuds n'ont pas été parcourus
			
			double min = Double.MAX_VALUE;
			int minnode = world.indexOf(dest);
			
			//On cherche le noeud n'ont encore parcourus avec la plus petit distance à la src
			for(int i = 0 ; i < world.size() ; i++){
				if (!banlist.contains(i) && distance.get(i) < min){
					eval.newVisite(world.getByIndex(i));
					min = distance.get(i);
					minnode = i;
				}
			}
			
			//On indique qu'on l'a parcouru
			banlist.add(minnode);
			this.eval.newNoeudEnvisage();
			
			double newdistance;
			
			for (int i = 0; i < world.size(); i++){
				try {
					
					//On fais une mise à jour de la distance à la source pour les noeuds connectés au noeud courant
					if (!banlist.contains(i) && (newdistance = world.get(world.getByIndex(minnode), world.getByIndex(i))) < Integer.MAX_VALUE){
						this.eval.newVisite(world.getByIndex(i));
						newdistance += distance.get(minnode).intValue(); 
						if (newdistance < distance.get(i).intValue()){
							/*Si la distance à la source d'un noeud connecté change,
							cela signifie que le noeud à parcourir avec ce noeud dans la cadre
							d'un plus cours chemin est le noeud courant
							*/
							distance.set(i, newdistance);
							predecessor.set(i, minnode);
							
							if(world.getByIndex(i).equals(dest)){
								
								path.add(dest);
								
								int current = world.indexOf(dest);
								
								//On rétablit le plus court chemin jusqu'à la dest d'après la liste des prédécesseurs
								while (!world.getByIndex(current).equals(src)){
									int pred = predecessor.get(current);
									path.add(world.getByIndex(pred));
									current = pred;
								}
								eval.gotASolution(newdistance, path.size());
							}
						}
					}
				} catch (UnknownPlaceException e) {
					System.out.println("Erreur dans le grow de Dijkstra: mise à jour des noeuds voisins");
					break;
				}
			}
		}
	
		
		
	}

	@Override
	public String getName() {
		return "Dijkstra";
	}
	
}
