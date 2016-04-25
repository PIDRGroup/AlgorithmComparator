package model.algo;

import java.util.ArrayList;

import model.env.Place;

public class Node{
	private Place stat;
	private double f;
	private double g;
	private ArrayList<Node> solvation = new ArrayList<Node>();
	
	public Node(Place state, double f, ArrayList<Node> solvation){
		this.stat = state;
		this.f=f;
		this.g=Double.MAX_VALUE;
		if(solvation != null)
			this.solvation=solvation;
		else
			this.solvation=new ArrayList<Node>();
	}
	
	public boolean isSuperior(Node noeud){
		return this.getpathcost() > noeud.getpathcost();
	}

	public Place getstat(){
		return stat;
	}
	
	public double getpathcost() {
		return this.f;
	}
	
	public double getG(){
		return this.g;
	}
	
	public void setpathcost(double f){
		this.f = f;
	}
	
	public void setG(double g){
		this.g = g;
	}
	
	public ArrayList<Node> getsolvation(){
		return this.solvation;
	}


}
