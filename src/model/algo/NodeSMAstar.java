package model.algo;

import java.util.ArrayList;
import model.env.Place;

public class NodeSMAstar {
	private Place place;
	private double f;
	private double g;
	private int depth;
	private ArrayList<Integer> indexofsuccessors;
	private ArrayList<NodeSMAstar> successors;
	private NodeSMAstar parent;
	private int cursor;
	private double secondbest;
	
	public NodeSMAstar(Place place, double f, double g, int depth, NodeSMAstar parent) {
		this.place = place;
		this.f = f;
		this.g = g;
		this.depth = depth;
		this.indexofsuccessors = new ArrayList<Integer>();
		this.successors = new ArrayList<NodeSMAstar>();
		this.parent = parent;
		this.cursor = 0;
		this.secondbest = Double.MAX_VALUE; 
	}
	
	public void setF(double f){
		this.f = f;
	}
	
	public void setG(double g){
		this.g = g;
	}
	
	public void setSecondBest(double secondbest){
		this.secondbest = secondbest;
	}
	
	public Place getPlace(){
		return this.place;
	}
	
	public double getF(){
		return this.f;
	}
	
	public double getG(){
		return this.g;
	}
	
	public int getDepth(){
		return this.depth;
	}
	
	public ArrayList<Integer> getIndexOfSuccessors(){
		return this.indexofsuccessors;
	}
	
	public ArrayList<NodeSMAstar> getSuccessors(){
		return this.successors;
	}
	
	public NodeSMAstar getParent(){
		return this.parent;
	}
	
	public double getSecondBest(){
		return this.secondbest;
	}
	
	public void addSuccessor(Integer index){
		this.indexofsuccessors.add(index);
	}
	
	public Integer nextSuccessor(){
		if (this.indexofsuccessors.size() == this.cursor){
			return -1;
		}else{
			this.cursor++;
			return this.indexofsuccessors.get(cursor-1);
		}
	}
	
	public void addSuccessorMemory(NodeSMAstar node){
		this.successors.add(node);
	}
	
	public void rmSuccessorMemory(NodeSMAstar node){
		this.successors.remove(node);
	}
	
	public boolean isAllSuccessorGenerated(){
		return this.indexofsuccessors.size() == this.cursor;
	}
	
	public boolean isAllSuccessorInMemory(){
		return this.indexofsuccessors.size() == this.successors.size();
	}
}
