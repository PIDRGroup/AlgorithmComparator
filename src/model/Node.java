package model;

import java.util.ArrayList;

public class Node<E extends Number>{
	private int stat;
	private E path_cost;
	private ArrayList<Node> solvation = new ArrayList<Node>();
	
	public Node(int state, E path_cost, ArrayList<Node> solvation){
		this.stat = stat;
		this.path_cost=path_cost;
		this.solvation=solvation;
	}
	
	public boolean isSuperior(Node noeud){
		return this.getstat() == noeud.getstat() && (Integer)this.getpathcost() < (Integer)noeud.getpathcost();
	}

	public int getstat(){
		return stat;
	}
	
	public E getpathcost() {
		// TODO Auto-generated method stub
		return this.path_cost;
	}
	
	public ArrayList<Node> getsolvation(){
		return this.solvation;
	}


}
