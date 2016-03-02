package model;

import java.util.ArrayList;

public class Node{
	private Place stat;
	private double path_cost;
	private ArrayList<Node> solvation = new ArrayList<Node>();
	
	public Node(Place state, double path_cost, ArrayList<Node> solvation){
		this.stat = state;
		this.path_cost=path_cost;
		if(solvation != null)
			this.solvation=solvation;
		else
			this.solvation=new ArrayList<Node>();
	}
	
	public boolean isSuperior(Node noeud){
		return this.getstat() == noeud.getstat() && this.getpathcost() < noeud.getpathcost();
	}

	public Place getstat(){
		return stat;
	}
	
	public double getpathcost() {
		return this.path_cost;
	}
	
	public void setpathcost(double path_cost){
		this.path_cost = path_cost;
	}
	
	public ArrayList<Node> getsolvation(){
		return this.solvation;
	}


}
