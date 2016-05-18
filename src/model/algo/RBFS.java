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
		
		ArrayList<Node> noeud = new ArrayList<Node>();
		Node current;
		Node sourcenode = null;
		
		for (int i = 0; i < world.size(); i++){
			
			if (this.world.getByIndex(i).equals(source)){
				current = new Node(world.getByIndex(i),h(source),null);
				current.setG(0);
				sourcenode = current;
			}else{
				current = new Node(world.getByIndex(i),world.get(source.getIndex(), i),null);
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
		
		if(current.getpathcost() > f_limit){
			return new RBFSreturn(true, current.getpathcost());
		}
		
		if (current.getstat().equals(destination)){			
			for (Node n:current.getsolvation()){
				this.path.add(n.getstat());
			}
			
			this.path.add(destination);
			this.eval.gotASolution(current.getpathcost(), path.size());
			return new RBFSreturn(false, current.getpathcost());
		}
		
		try {
			if (world.get(current.getstat().getIndex(), destination.getIndex()) < Double.MAX_VALUE){
				
				double cost = 0.0;
				int count = current.getsolvation().size() + 1;
				cost += current.getpathcost()+ world.get(current.getstat().getIndex(), destination.getIndex());
				
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
		
		try {
			for (int index : world.getLinks(current.getstat().getIndex())) {
				successors.add(noeud.get(index));
			}
		} catch (UnknownPlaceException e) {
			e.printStackTrace();
		}
		
		if (successors.isEmpty()){
			return new RBFSreturn(true, Double.MAX_VALUE);
		}
		
		for (int i = 0; i < successors.size(); i++){
			try {
				successors.get(i).setG(current.getG()+world.get(current.getstat().getIndex(), successors.get(i).getstat().getIndex()));
				successors.get(i).setpathcost(Math.max(current.getpathcost(), successors.get(i).getG()+h(successors.get(i).getstat())));
				
				this.eval.newVisite(successors.get(i).getstat());
				
			} catch (UnknownPlaceException e) {
				System.out.println("Problème dans la mise a jour des valeurs d'un noeud");
			}
			
		}
		
		double min;
		ArrayList<Node> sortednode = new ArrayList<Node>();
		ArrayList<Node> alreadysorted = new ArrayList<Node>();
		Node current_best = null;
		
		for (int i = 0; i < successors.size(); i ++){
			min = Double.MAX_VALUE;
			
			for (int j = 0; j < successors.size(); j++){
				
				this.eval.newVisite(successors.get(j).getstat());
				
				if (!alreadysorted.contains(successors.get(j)) && successors.get(j).getpathcost() < min){
					current_best = successors.get(j);
					min = successors.get(j).getpathcost();
				}
				
			}				
			
			sortednode.add(current_best);
			alreadysorted.add(current_best);
			
		}
		
		while(sortednode.get(0).getpathcost() <= f_limit && sortednode.get(0).getpathcost() < Integer.MAX_VALUE){
			
			sortednode.get(0).setpathcost(recursive_BFS(noeud, sortednode.get(0), Math.min(f_limit, sortednode.get(1).getpathcost())).getBestF());
			
			for (int i = 0; i < successors.size(); i ++){
				min = Double.MAX_VALUE;
				
				for (int j = 0; j < successors.size(); j++){
					
					this.eval.newVisite(successors.get(j).getstat());
					
					if (!alreadysorted.contains(successors.get(j)) && successors.get(j).getpathcost() < min){
						current_best = successors.get(j);
						min = successors.get(j).getpathcost();
					}
					
				}				
				
				sortednode.add(current_best);
				alreadysorted.add(current_best);
				
			}
		}
		
		return new RBFSreturn(false, sortednode.get(0).getpathcost());
	}

	public double h(Place current){
		return current.distanceEuclidienne(this.destination);
	}
	
	@Override
	public String getName() {
		return "Recursive Best First Search";
	}

	
	

}
