package model;

import java.util.ArrayList;

public class Dijkstra<E extends Number> extends Algorithm<E>{
	
	public Dijkstra(Environment env){
		this.world = env;
	}

	@Override
	public void grow(int source, int destination) throws UnknownPlace {
		
		if(!world.isIndex(source))
			throw new UnknownPlace(source);
		
		if(!world.isIndex(destination))
			throw new UnknownPlace(destination);
		
		MaMatrice<E> matrice = this.world.getMatrix();
		ArrayList<E> distance = new ArrayList<E>();
		ArrayList<Integer> predecessor = new ArrayList<Integer>();
		ArrayList<Integer> banlist = new ArrayList<Integer>();
		
		E dist;
		for (int i = 0; i < matrice.size(); i++){
			try {
				if ((dist = matrice.get(source, i)).intValue() < Integer.MAX_VALUE){
					distance.add(dist);
					//On initialise les noeuds à une distance infini de la source
				}else{
					distance.add((E)(Integer)Integer.MAX_VALUE);
					//On initialise les noeuds adjacents à la source, par leur distance à la source
				}
				predecessor.add(source);
				//On ajoute à chaque noeud comme prédécesseur, eux-mêmes
			} catch (UnknownPlace e) {
				System.out.println("Erreur dans le grow de Dijkstra: initialisation");
				e.printStackTrace();
				
			}
		}
		
		while(banlist.size() != matrice.size()){
			// Tant que tous les noeuds n'ont pas été parcourus
			
			int min = Integer.MAX_VALUE;
			int minnode = destination;
			
			//On cherche le noeud n'ont encore parcourus avec la plus petit distance à la source
			for(int i = 0 ; i < matrice.size() ; i++){
				if (!banlist.contains(i) && distance.get(i).intValue() < min){
					min = distance.get(i).intValue();
					minnode = i;
				}
			}
			
			//On indique qu'on l'a parcouru
			banlist.add(minnode);
			int newdistance;
			
			for (int i = 0; i < matrice.size() ; i++){
				try {
					
					//On fais une mise à jour de la distance à la source pour les noeuds connectés au noeud courant
					if (!banlist.contains(i) && (newdistance = (Integer) matrice.get(minnode, i)) < Integer.MAX_VALUE){
						newdistance += distance.get(minnode).intValue(); 
						if (newdistance < distance.get(i).intValue()){
							/*Si la distance à la source d'un noeud connecté change,
							cela signifie que le noeud à parcourir avec ce noeud dans la cadre
							d'un plus cours chemin est le noeud courant
							*/
							distance.set(i, (E) (Integer) newdistance);
							predecessor.set(i, minnode);
						}
					}
				} catch (UnknownPlace e) {
					System.out.println("Erreur dans le grow de Dijkstra: mise à jour des noeuds voisins");
					e.printStackTrace();
				}
			}
		}
		
		this.path = new ArrayList<Integer>();
		path.add(destination);
		int current = destination;
		
		//On rétablit le plus court chemin jusqu'à la destination d'après la liste des prédécesseurs
		while (current != source){
			int pred = predecessor.get(current);
			path.add(pred);
			current = pred;
		}
		
	}
	
	

}
