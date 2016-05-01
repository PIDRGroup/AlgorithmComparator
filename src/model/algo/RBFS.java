package model.algo;

import java.util.ArrayList;

import model.env.Environment;
import model.env.Place;
import model.env.UnknownPlaceException;

public class RBFS extends Algorithm{

	private Environment world;
	private Place destination;
	private ArrayList<Node> noeudenvisage;
	private double previouscost;
	
	public RBFS(){
		path = new ArrayList<Place>();
		eval = new Evaluation();
		noeudenvisage = new ArrayList<Node>();
		previouscost = Double.MAX_VALUE;
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
		Node current;
		Node sourcenode = null;
		
		for (int i = 0; i < world.size(); i++){
			
			if (this.world.getByIndex(i).equals(source)){
				current = new Node(world.getByIndex(i),h(source),null);
				current.setG(0);
				sourcenode = current;
			}else{
				current = new Node(world.getByIndex(i),world.get(source, world.getByIndex(i)),null);
			}	
				noeud.add(current);
		}
		
		this.eval.start();		
		this.eval.newVisite(source);
		recursive_BFS(noeud, sourcenode, Double.MAX_VALUE);
		
	}
	
	public RBFSreturn recursive_BFS(ArrayList<Node> noeud, Node current, double f_limit){
		
		if (!this.noeudenvisage.contains(current)){
			this.noeudenvisage.add(current);
			this.eval.newNoeudEnvisage();
		}
		
		if (current.getstat() == destination){			
			for (Node n:current.getsolvation()){
				this.path.add(n.getstat());
			}
			
			this.path.add(destination);
			this.eval.gotASolution(current.getpathcost(), path.size());
			return new RBFSreturn(false, current.getpathcost());
		}
		
		try {
			if (world.get(current.getstat(), destination) < Double.MAX_VALUE){
				
				double cost = 0.0;
				int count = current.getsolvation().size() + 1;
				cost += current.getpathcost()+ world.get(current.getstat(), destination);
				
				if(cost < previouscost){
					previouscost = cost;					
					
					this.eval.gotASolution(cost , count);
				}
					
			}
		} catch (UnknownPlaceException e1) {
			System.out.println("Place non trouve dans la recherche de nouvelles solutions");
		}
		
		ArrayList<Node> successors = new ArrayList<Node>();
		ArrayList<Node> currentsolvation = current.getsolvation();
		currentsolvation.add(current);
		
		for (int i = 0; i < noeud.size(); i++){
			try {
				if(world.get(current.getstat(), noeud.get(i).getstat()) < Double.MAX_VALUE){
					successors.add(noeud.get(i));
				}
			} catch (UnknownPlaceException e) {
				System.out.println("Problème de lecture du monde dans la fonction de récursivité de RBFS");;
			}	
		}
		
		if (successors.isEmpty()){
			return new RBFSreturn(true, Double.MAX_VALUE);
		}
		
		for (int i = 0; i < successors.size(); i++){
			try {
				successors.get(i).setG(current.getG()+world.get(current.getstat(), successors.get(i).getstat()));
				successors.get(i).setpathcost(successors.get(i).getG()+h(successors.get(i).getstat()));
				
				this.eval.newVisite(successors.get(i).getstat());
				
			} catch (UnknownPlaceException e) {
				System.out.println("Problème dans la mise a jour des valeurs d'un noeud");
			}
			
		}
		
		while(true){
			
			double min;
			int currentsize;
			ArrayList<Node> sortednode = new ArrayList<Node>();
			Node current_best = null;
			
			for (int i = 0; i < 2; i ++){
				
				min = Double.MAX_VALUE;
				currentsize = successors.size();
				
				for (int j = 0; j < currentsize; j++){
					
					this.eval.newVisite(successors.get(j).getstat());
					
					if (successors.get(j).getpathcost() < min){
						current_best = successors.get(j);
						min = successors.get(j).getpathcost();
					}
					
				}				
				
				sortednode.add(current_best);
				
				successors.remove(current_best);
				
			}
			
			if(sortednode.get(0) == null){
				return new RBFSreturn(true, Double.MAX_VALUE);
			}else{
				sortednode.get(0).setSolvation(currentsolvation);
			}
			
			this.eval.newVisite(sortednode.get(0).getstat());
			
			if (sortednode.get(0).getpathcost() > f_limit) return new RBFSreturn(true, sortednode.get(0).getpathcost());
			
			RBFSreturn result;
			
			if(sortednode.get(1) == null){
				result = recursive_BFS(noeud, sortednode.get(0), f_limit);
			}else{
				result = recursive_BFS(noeud, sortednode.get(0), Math.min(f_limit, sortednode.get(1).getpathcost()));
				sortednode.get(1).setSolvation(currentsolvation);
			}
			
			if (!result.isFailure()) return result;
			
		}
		
	}

	public double h(Place current){
		return current.distanceEuclidienne(this.destination);
	}
	
	@Override
	public String getName() {
		return "Recursive Best First Search";
	}

	
	

}
