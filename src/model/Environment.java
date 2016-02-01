package model;

import java.util.ArrayList;
import java.util.Observable;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;

public class Environment<E extends Number> extends Observable{
	
	private ArrayList<ArrayList<E>> successors;
	private ArrayList<ArrayList<E>> predecessors;
	private ArrayList<String> labels;
		
	public Environment(){
		successors = new ArrayList<ArrayList<E>>();
		predecessors = new ArrayList<ArrayList<E>>();
		labels = new ArrayList<String>();
	}
	
	public ArrayList<ArrayList<E>> getPredecessors(){
		return predecessors;
	}
	
	public ArrayList<ArrayList<E>> getSuccessors(){
		return successors;
	}

	public Environment<E> duplicate(){
		Environment<E> copy = new Environment<E>();
		
		ArrayList<ArrayList<E>> succ_copy = new ArrayList<ArrayList<E>>();
		ArrayList<ArrayList<E>> pred_copy = new ArrayList<ArrayList<E>>();
		
		for(ArrayList<E> l : successors){
			ArrayList<E> list = new ArrayList<E>();
			
			for(E el : l){
				list.add(el);
			}
			
			succ_copy.add(list);
		}
		
		copy.predecessors = pred_copy;
		copy.successors = succ_copy;
		
		return copy;
	}
	
	public String toString(){
		String s = "";
		
		s+="Predessors : ";
		for(ArrayList<E> list : predecessors){
			s+="[";
			
			for(E el : list){
				s+=el + " / ";
			}
			
			s+="]";
		}
		
		return s;
	}
	
	public boolean isLabel(String label){
		return labels.contains(label);
	}
	
	public boolean isIndex(int index){
		return index > 0 && index < labels.size();
	}
	
	public int indexOf(String label) throws UnknownPlace{
		if(!labels.contains(label))
			throw new UnknownPlace(label);
		
		return labels.indexOf(label);
	}
	
	public String labelOf(int i) throws UnknownPlace{
		if(i < 0 || i > labels.size())
			throw new UnknownPlace(i);
		
		return labels.get(i);
	}
	
	public Graph<String, E> toGraph(){
		Graph<String, E> g = new DirectedSparseMultigraph<String, E>();
		
		//On crée toutes les places
		for(int i=0; i<labels.size(); i++){
			g.addVertex(labels.get(i));
		}
		
		//Puis on crée les flux
		for(int i=0; i<successors.size(); i++){
			for(int j=0; j<successors.get(i).size(); j++){
				if(successors.get(i).get(j).intValue()<Integer.MAX_VALUE)
					g.addEdge(successors.get(i).get(j), labels.get(i), labels.get(j));
			}
		}
		
		return g;
	}
}
