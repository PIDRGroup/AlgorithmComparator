package model.algo;

import java.util.ArrayList;
import java.util.HashMap;

import model.env.Environment;
import model.env.Place;
import model.env.UnknownPlaceException;

public class AStar extends Algorithm{
	Place dest;
	
	public AStar(){
		path = new ArrayList<Place>();
		eval = new Evaluation();
	}

	@Override
	public void grow(Environment world, Place src, Place dest) throws UnknownPlaceException{
		
		this.dest = dest;
		
		
		ArrayList<Node> noeudouvert = new ArrayList<Node>();
		ArrayList<Node> noeudferme = new ArrayList<Node>();
		Node source = new Node(src, h(src), null);
		
		source.setG(0.0);
		noeudouvert.add(source);
		this.eval.newNoeudEnvisage();
		this.eval.newVisite(src);
		
		HashMap<Node,Node> predecesseur = new HashMap<Node,Node>();
		
		ArrayList<Node> noeud = new ArrayList<Node>();
		
		for (int i =0; i < world.size(); i++){
			if (src.getIndex() == i){
				noeud.add(source);
			}
			else noeud.add(new Node(world.getByIndex(i), Double.MAX_VALUE, null));
		}
		
		this.eval.start();
		
		double previouscost = Double.MAX_VALUE;
		
		while (!noeudouvert.isEmpty()){
			
			double min = Double.MAX_VALUE;
			Node current = null;
			
			for (int i = 0; i < noeud.size(); i++){
				//On recherche le noeud n'appartenant pas au noeud ouvert tel que f est minimal
				if (noeud.get(i).getpathcost() < min && noeudouvert.contains(noeud.get(i))){
					this.eval.newVisite(noeud.get(i).getstat());
					min = noeud.get(i).getpathcost();
					current = noeud.get(i);
				}
			}
			
			if (current == null){
				System.out.println("Le noeud courant est null");
				break;
			}
			
			//Version normale
			
			if (current.getstat().equals(dest)){
				
				double cost = 0.0;
				
				path.add(current.getstat());
				cost += current.getpathcost();
				
				while (predecesseur.containsKey(current)){
					current = predecesseur.get(current);
					path.add(current.getstat());
				}
				
				this.eval.gotASolution(cost, path.size());
				
				break;
			}
				
			//Version augmentee
			
			if (world.get(current.getstat().getIndex(), dest.getIndex()) < Double.MAX_VALUE){
				
				double cost = 0.0;
				int count = 1;
				cost += current.getpathcost()+ world.get(current.getstat().getIndex(), dest.getIndex());
				
				if(cost < previouscost){
					previouscost = cost;
					
					
					while (predecesseur.containsKey(current)){
						current = predecesseur.get(current);
						count++;
					}
					
					this.eval.gotASolution(cost , count);
				}
				
						
			}
			
			noeudouvert.remove(current);
			noeudferme.add(current);
			double dist;
			
			for (int i = 0; i < noeud.size(); i++){
				if (world.get(current.getstat().getIndex(), i) < Double.MAX_VALUE){
					
					this.eval.newVisite(world.getByIndex(i));
					
					if (noeudferme.contains(noeud.get(i))){
						continue;
					}
					dist = current.getG() + world.get(current.getstat().getIndex(), noeud.get(i).getstat().getIndex());
					if (!noeudouvert.contains(noeud.get(i))){
						noeudouvert.add(noeud.get(i));
						this.eval.newNoeudEnvisage();
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
			System.out.println("Liste noeud ouvert vide");
		}
	}
	
	private double h(Place current){
		return current.distanceEuclidienne(this.dest);
	}

	@Override
	public String getName() {
		return "A*";
	}
}
