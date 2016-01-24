package model;

import java.util.Observable;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;

public class Environment<E extends Number> extends Observable{
	
	private MaMatrice<E> matrix;
	
	public Environment(){
		matrix = new MaMatrice<E>();
	}
	
	public MaMatrice<E> getMatrix(){
		return matrix;
	}

	public Environment<E> duplicate(){
		Environment<E> copy = new Environment<E>();
		
		copy.matrix = this.matrix.duplicate();
		
		return copy;
	}
	
	public String toString(){
		return matrix.toString();
	}
	
	public boolean isLabel(String label){
		return matrix.isLabel(label);
	}
	
	public boolean isIndex(int index){
		return matrix.isIndex(index);
	}
	
	public int indexOf(String label){
		return matrix.indexOf(label);
	}
	
	public String labelOf(int i){
		return matrix.labelOf(i);
	}
	
	public Graph<String, E> toGraph(){
		Graph<String, E> g = new DirectedSparseMultigraph<String, E>();
		
		//On crée toutes les places
		for(int i=0; i<matrix.size(); i++){
			g.addVertex(matrix.labelOf(i));
		}
		
		//Puis on crée les flux
		for(int i=0; i<matrix.size(); i++){
			for(int j=0; j<matrix.size(); j++){
				try {
					if(matrix.get(i, j).intValue()<Integer.MAX_VALUE)
						g.addEdge(matrix.get(i, j), matrix.labelOf(i), matrix.labelOf(j));
				} catch (UnknownPlace e) {
					e.printStackTrace();
				}
			}
		}
		
		return g;
	}
}
