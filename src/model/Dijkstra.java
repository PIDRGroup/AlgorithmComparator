package model;

import java.util.ArrayList;

public class Dijkstra extends Algorithm {
	
	public Dijkstra(Environment env){
		this.world = env;
	}
	
	public void grow(String src, String dest) throws UnknownPlace{
		if(!world.isLabel(src))
			throw new UnknownPlace(src);
		
		if(!world.isLabel(dest))
			throw new UnknownPlace(dest);
		
		grow(world.indexOf(src), world.indexOf(dest));
	}

	@Override
	public void grow(int source, int destination) throws UnknownPlace {
		
		if(!world.isIndex(source))
			throw new UnknownPlace(source);
		
		if(!world.isIndex(destination))
			throw new UnknownPlace(destination);
		
		MaMatrice<Integer> matrice = this.world.getMatrix();
		ArrayList<Integer> distance = new ArrayList<Integer>();
		ArrayList<Integer> predecessor = new ArrayList<Integer>();
		ArrayList<Integer> banlist = new ArrayList<Integer>();
		
		int dist;
		for (int i = 0; i < matrice.size(); i++){
			try {
				if ((dist = matrice.get(source, i)) < Integer.MAX_VALUE){
					distance.add(dist);
				}else{
					distance.add(Integer.MAX_VALUE);
				}
				predecessor.add(source);
			} catch (UnknownPlace e) {
				System.out.println("Erreur dans le grow de Dijkstra: initialisation");
				e.printStackTrace();
				
			}
		}
		
		while(banlist.size() != matrice.size()){
			int min = Integer.MAX_VALUE;
			int minnode = destination;
			for(int i = 0 ; i < matrice.size() ; i++){
				if (!banlist.contains(i) && distance.get(i) < min){
					min = distance.get(i);
					minnode = i;
				}
			}
			
			banlist.add(minnode);
			int newdistance;
			
			for (int i = 0; i < matrice.size() ; i++){
				try {
					
					if (!banlist.contains(i) && (newdistance = matrice.get(minnode, i)) < Integer.MAX_VALUE){
						newdistance += distance.get(minnode); 
						if (newdistance < distance.get(i)){
							distance.set(i, newdistance);
							predecessor.set(i, minnode);
						}
					}
				} catch (UnknownPlace e) {
					System.out.println("Erreur dans le grow de Dijkstra: mise à jour des noeuds voisins");
					e.printStackTrace();
				}
			}
		}
		
	}

}
