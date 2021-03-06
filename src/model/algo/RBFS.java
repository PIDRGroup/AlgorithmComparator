package model.algo;

import java.util.ArrayList;

import model.env.Environment;
import model.env.Place;
import model.env.UnknownPlaceException;

public class RBFS extends Algorithm{

	private Environment world;
	private Place destination;
	private ArrayList<Place> noeudenvisage;
	private double previouscost;
	
	public RBFS(){
		path = new ArrayList<Place>();
		eval = new Evaluation();
		noeudenvisage = new ArrayList<Place>();
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
				current = new Node(world.getByIndex(i), Double.MAX_VALUE ,null);
			}	
				noeud.add(current);
		}
		
		this.eval.start();		
		this.eval.newVisite(source);
		recursive_BFS(noeud, sourcenode, Double.MAX_VALUE);
		
	}
	
	public RBFSreturn recursive_BFS(ArrayList<Node> noeud, Node current, double f_limit){
		
		Node noeudcopie;
		Node copienoeud;
		ArrayList<Node> noeudclone = new ArrayList<Node>();
		
		for (int i = 0; i < noeud.size(); i ++){
			noeudcopie = noeud.get(i);
			copienoeud = new Node(noeudcopie.getstat(), noeudcopie.getpathcost(), (ArrayList<Node>) noeudcopie.getsolvation().clone());
			copienoeud.setG(noeudcopie.getG());
			noeudclone.add(copienoeud);
		}
		
		if (!this.noeudenvisage.contains(current.getstat())){
			this.noeudenvisage.add(current.getstat());
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
		
		for (int i = 0; i < noeudclone.size(); i++){
			try {
				if(world.get(current.getstat().getIndex(), noeudclone.get(i).getstat().getIndex()) < Double.MAX_VALUE){
					successors.add(noeudclone.get(i));
				}
			} catch (UnknownPlaceException e) {
				System.out.println("Problème de lecture du monde dans la fonction de récursivité de RBFS");;
			}	
		}
		
		if (successors.isEmpty()){
			return new RBFSreturn(true, Double.MAX_VALUE);
		}
		
		double maxi = 0.0;
		
		for (int i = 0; i < successors.size(); i++){
			try {
				successors.get(i).setG(current.getG()+world.get(current.getstat().getIndex(), successors.get(i).getstat().getIndex()));
				maxi = Math.max(successors.get(i).getG()+h(successors.get(i).getstat()), current.getpathcost());
				successors.get(i).setpathcost(maxi);
				this.eval.newVisite(successors.get(i).getstat());
				
			} catch (UnknownPlaceException e) {
				System.out.println("Problème dans la mise a jour des valeurs d'un noeud");
			}
			
		}
		
		while(true){
			
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
			
			sortednode.get(0).setSolvation(currentsolvation);
			this.eval.newVisite(sortednode.get(0).getstat());
			
			if (sortednode.get(0).getpathcost() > f_limit) return new RBFSreturn(true, sortednode.get(0).getpathcost());
			
			RBFSreturn result = recursive_BFS(noeudclone, sortednode.get(0), Math.min(f_limit, sortednode.get(1).getpathcost()));
			sortednode.get(0).setpathcost(result.getBestF());
			sortednode.get(1).setSolvation(currentsolvation);
			
			
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
