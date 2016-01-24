package model;

import java.util.ArrayList;

public class Dijkstra<E extends Number> extends Algorithm<E>{
	
	public Dijkstra(Environment<E> env){
		this.world = env;
		path = new ArrayList<Integer>();
	}

	@Override
	public void grow(int src, int dest) throws UnknownPlace {
		
		if(!world.isIndex(src))
			throw new UnknownPlace(src);
		
		if(!world.isIndex(dest))
			throw new UnknownPlace(dest);
		
		MaMatrice<E> matrice = this.world.getMatrix();
		ArrayList<E> distance = new ArrayList<E>();
		ArrayList<Integer> predecessor = new ArrayList<Integer>();
		ArrayList<Integer> banlist = new ArrayList<Integer>();
		
		E dist;
		for (int i = 0; i < matrice.size(); i++){
			try {
				if ((dist = matrice.get(src, i)).intValue() < Integer.MAX_VALUE){
					distance.add(dist);
					//On initialise les noeuds à une distance infini de la src
				}else{
					distance.add((E)(Integer)Integer.MAX_VALUE);
					//On initialise les noeuds adjacents à la src, par leur distance à la src
				}
				predecessor.add(src);
				//On ajoute à chaque noeud comme prédécesseur, eux-mêmes
			} catch (UnknownPlace e) {
				System.out.println("Erreur dans le grow de Dijkstra: initialisation");
				e.printStackTrace();
				
			}
		}
		
		while(banlist.size() != matrice.size()){
			// Tant que tous les noeuds n'ont pas été parcourus
			
			int min = Integer.MAX_VALUE;
			int minnode = dest;
			
			//On cherche le noeud n'ont encore parcourus avec la plus petit distance à la src
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
	
		path.add(dest);
		this.setChanged();
		this.notifyObservers();
		
		int current = dest;
		
		//On rétablit le plus court chemin jusqu'à la dest d'après la liste des prédécesseurs
		while (current != src){
			int pred = predecessor.get(current);
			path.add(pred);
			current = pred;
			this.setChanged();
			this.notifyObservers();
		}
		
	}
	
}
