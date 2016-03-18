package model.algo;

import java.util.ArrayList;

import model.env.Environment;
import model.env.Place;
import model.env.UnknownPlaceException;

public class RBFS extends Algorithm{

	private Environment world;
	private Place destination;
	
	public RBFS(){
		path = new ArrayList<Place>();
		eval = new Evaluation();
	}
	
	@Override
	public void grow(Environment world, Place source, Place destination) throws UnknownPlaceException {
		
		this.world = world;
		this.destination = destination;
		
		if(!world.isPlace(source))
			throw new UnknownPlaceException(source);
		
		if(!world.isPlace(destination))
			throw new UnknownPlaceException(destination);
		
		
		ArrayList<Node> noeud = new ArrayList<Node>();
		
		for (int i = 0; i < world.size(); i++){
			noeud.add(new Node(world.getPlace(i),Double.MAX_VALUE,null));
		}
		
	}
	
	public Double recursive_BFS(ArrayList<Node> noeud, Node current, double f_limit){
		
		if (current.getstat() == destination){
			return current.getpathcost();
		}
		
		
		ArrayList<Node> successors = new ArrayList<Node>();
		
		double dist;
		
		for (int i = 0; i < noeud.size(); i++){
			try {
				if((dist = world.get(current.getstat(), noeud.get(i).getstat())) < Integer.MAX_VALUE){
					successors.add(noeud.get(i));
				}
			} catch (UnknownPlaceException e) {
				System.out.println("Problème de lecture du monde dans la fonction de récursivité de RBFS");;
			}	
		}
		
		try{
			if (successors.isEmpty()){
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("La liste des successeurs de RBFS est vide");
		}
		
		for (int i = 0; i < successors.size(); i++){
			try {
				successors.get(i).setG(current.getG()+world.get(current.getstat(), successors.get(i).getstat()));
				successors.get(i).setpathcost(successors.get(i).getG()+h(successors.get(i).getstat()));
			} catch (UnknownPlaceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		while(true){
			
			double min = Double.MAX_VALUE;
			int currentsize;
			ArrayList<Node> sortednode = new ArrayList<Node>();
			Node current_best = null;
			
			for (int i = 0; i < 2; i ++){
				
				currentsize =successors.size();
				
				for (int j = 0; j < currentsize; j++){
					if (successors.get(i).getpathcost() < min){
						current_best = successors.get(i);
						min = successors.get(i).getpathcost();
					}
				}
				
				sortednode.add(current_best);
				successors.remove(current_best);
			}
			
			if (sortednode.get(0).getpathcost() > f_limit) return null;
			
			return recursive_BFS(noeud, sortednode.get(0), Math.min(f_limit, sortednode.get(1).getpathcost()));
			
			
		}
		
	}

	public double h(Place current){
		return 0;
	}
	
	@Override
	public String getName() {
		return "Recursive Best First Search";
	}

	
	

}
