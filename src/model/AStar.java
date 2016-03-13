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
		
		eval.start();
		
		ArrayList<Node> noeudouvert = new ArrayList<Node>();
		ArrayList<Node> noeudferme = new ArrayList<Node>();
		Node source = new Node(src, h(src), null);
		
		source.setG(0.0);
		noeudouvert.add(source);
		
		HashMap<Node,Node> predecesseur = new HashMap<Node,Node>();
		
		ArrayList<Node> noeud = new ArrayList<Node>();
		
		for (int i =0; i < world.size(); i++){
			if (world.indexOf(src) == i){
				noeud.add(source);
			}
			else noeud.add(new Node(world.getPlace(i), Double.MAX_VALUE, null));
		}
		
		while (!noeudouvert.isEmpty()){
			
			eval.newWhile();
			
			double min = Double.MAX_VALUE;
			Node current = null;
			
			for (int i = 0; i < noeud.size(); i++){
				//On recherche le noeud n'appartenant pas au noeud ouvert tel que f est minimal
				if (noeud.get(i).getpathcost() < min && noeudouvert.contains(noeud.get(i))){
					min = noeud.get(i).getpathcost();
					current = noeud.get(i);
				}
			}
			
			if (current == null){
				System.out.println("Le noeud courant est null");
				break;
			}
			
			if (current.getstat().equals(dest)){
				
				double cost = 0.0;
				
				path.add(current.getstat());
				cost += current.getpathcost();
				while (predecesseur.containsKey(current)){
					current = predecesseur.get(current);
					path.add(current.getstat());
					cost += current.getpathcost();
				}
				
				eval.gotASolution(cost);
				
				break;
			}
						
			noeudouvert.remove(current);
			noeudferme.add(current);
			double dist;
			
			for (int i = 0; i < noeud.size(); i++){
				if (world.get(current.getstat(), world.getPlace(i)) < Integer.MAX_VALUE){
					
					if (noeudferme.contains(noeud.get(i))){
						continue;
					}
					
					dist = current.getG() + world.get(current.getstat(), noeud.get(i).getstat());
					if (!noeudouvert.contains(noeud.get(i))){
						noeudouvert.add(noeud.get(i));
					}else if (dist >= noeud.get(i).getG()){
						continue;
					}
					
					predecesseur.put(noeud.get(i),current);
					noeud.get(i).setG(dist);
					noeud.get(i).setpathcost(noeud.get(i).getG()+ h (noeud.get(i).getstat()));
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
