package model;

import java.util.ArrayList;

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
			noeud.add(new Node(world.getPlace(i),Integer.MAX_VALUE,null));
		}
		
	}
	
	public void recursive_BFS(ArrayList<Node> noeud, Node current, double f_limit){
		
		if (current.getstat() == destination){
			//return solution
		}
		
		
		ArrayList<Integer> successors = new ArrayList<Integer>();
		
		double dist;
		
		for (int i = 0; i < world.size(); i++){
			try {
				if((dist = world.get(current.getstat(), world.getPlace(i))) < Integer.MAX_VALUE){
					successors.add(i);
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
		
		for (int i = 0; i < noeud.size(); i++){
			//noeud.get(i).setpathcost(max());
		}
		
		while(true){
			
			double min = Integer.MAX_VALUE;
			Node current_best = new Node(null,0,null);
			
			for (int i = 0; i < noeud.size() ; i++){
				if (noeud.get(i).getpathcost() < min){
					current_best = noeud.get(i);
					min = noeud.get(i).getpathcost();
				}
			}
			
			if (current_best.getpathcost() > f_limit){
				// return faillure
			}
			
			
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
