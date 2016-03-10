package model;

import java.util.ArrayList;

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
				if ((dist = world.get(src, world.getPlace(i))) < Integer.MAX_VALUE){
					distance.add(dist);
					//On initialise les noeuds à une distance infini de la src
				}else{
					distance.add(new Double(Integer.MAX_VALUE));
					//On initialise les noeuds adjacents à la src, par leur distance à la src
				}
				predecessor.add(world.indexOf(src));
				//On ajoute à chaque noeud comme prédécesseur, eux-mêmes
			} catch (UnknownPlaceException e) {
				System.out.println("Erreur dans le grow de Dijkstra: initialisation");
				e.printStackTrace();
			}
		}
		
		while(banlist.size() != world.size()){
			// Tant que tous les noeuds n'ont pas été parcourus
			
			eval.newWhile();
			
			int min = Integer.MAX_VALUE;
			int minnode = world.indexOf(dest);
			
			//On cherche le noeud n'ont encore parcourus avec la plus petit distance à la src
			for(int i = 0 ; i < world.size() ; i++){
				if (!banlist.contains(i) && distance.get(i).intValue() < min){
					min = distance.get(i).intValue();
					minnode = i;
				}
			}
			
			//On indique qu'on l'a parcouru
			banlist.add(minnode);
			double newdistance;
			
			for (int i = 0; i < world.size() ; i++){
				try {
					
					//On fais une mise à jour de la distance à la source pour les noeuds connectés au noeud courant
					if (!banlist.contains(i) && (newdistance = world.get(world.getPlace(minnode), world.getPlace(i))) < Integer.MAX_VALUE){
						newdistance += distance.get(minnode).intValue(); 
						if (newdistance < distance.get(i).intValue()){
							/*Si la distance à la source d'un noeud connecté change,
							cela signifie que le noeud à parcourir avec ce noeud dans la cadre
							d'un plus cours chemin est le noeud courant
							*/
							distance.set(i, newdistance);
							predecessor.set(i, minnode);
							if(world.getPlace(i).equals(dest)){
								eval.gotASolution(newdistance);
							}
						}
					}
				} catch (UnknownPlaceException e) {
					System.out.println("Erreur dans le grow de Dijkstra: mise à jour des noeuds voisins");
					e.printStackTrace();
				}
			}
		}
	
		path.add(dest);
		
		int current = world.indexOf(dest);
		
		//On rétablit le plus court chemin jusqu'à la dest d'après la liste des prédécesseurs
		while (!world.getPlace(current).equals(src)){
			int pred = predecessor.get(current);
			path.add(world.getPlace(pred));
			current = pred;
		}
		
	}

	@Override
	public String getName() {
		return "Dijkstra";
	}
	
}
