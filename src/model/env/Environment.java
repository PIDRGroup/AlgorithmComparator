package model.env;

import java.io.Serializable;
import java.util.*;

public abstract class Environment extends Observable implements Serializable{
	
	/**
	 * Les places en cl�s correspondent � l'ensemble des places du graphe, 
	 * tandis que les places en valeur sont les successeurs de ces places.
	 */
	protected MyMap graph;
	protected Seed seed;
	
	/**
	 * M�thode pour cr�er un lien entre uen source et une destination
	 * pass�es en param�tres
	 * 
	 * @param src Place source
	 * @param dest Place destination
	 * @throws UnknownPlaceException 
	 */
	public void addLink(int src, int dest){	
		graph.put(src, dest);
	}
	
	public void addPlace(Place p){
		graph.addKey(p);
	}
	
	/**
	 * M�thode pour r�cup�rer la valeur d'un lien entre une source et une destination.
	 * Si la valeur est de MAX_VALUE, alors le lien n'existe pas.
	 * 
	 * @param src Place source
	 * @param dest Place destination
	 * @return Poids du lien (distance entre les deux places)
	 * @throws UnknownPlaceException Si les places n'appartiennent pas au graph
	 */
	public double get(int src, int dest) throws UnknownPlaceException{		
		//Si val vaut null, la distance est infinie
		ArrayList<Integer> l = graph.valueList(src);
		
		double val = 0;
		int i=0;
		
		while(i < l.size() && val == 0){
			if(l.get(i).equals(dest)){
				val = graph.keyList().get(src).distanceEuclidienne(graph.keyList().get(dest));
			}else{
				i++;
			}
		}
		
		val = (val == 0) ? Double.MAX_VALUE : val;
		
		return val;
	}
	
	/**
	 * Nombre de places.
	 * Celle-ci �tant forc�ment carr�e, on ne renvoie qu'un entier.
	 * 
	 * @return Taille de la matrice.
	 */
	public int size(){
		return graph.keyList().size();
	}
	
	public String toString(){
		String s = "";
		
		s+="Successors : ";
		
		for (int i = 0; i < graph.keyList().size(); i++) {
			s+="[ ";
			for(int j=0; j < graph.valueList(i).size() ; j++){
				s+=graph.valueList(i).get(j)+" | ";
			}
			s+=" ]";
		}
				
		return s;
	}

	
	/**
	 * Retourne la liste de toutes les places du graphe
	 * @return
	 */
	public ArrayList<Place> getPlaces(){
		return graph.keyList();
	}
	
	public int nbDim(){
		return seed.getNbDim();
	}
	
	/**
	 * Tirer une place au hasard.
	 * @return
	 */
	public Place alea(){
		return graph.keyList().get((new Random()).nextInt(graph.keyList().size()));
	}
	
	public ArrayList<Integer> getLinks(int src) throws UnknownPlaceException{
		return graph.valueList(src);
	}

	public Seed getSeed() {
		return seed;
	}
	
	public Place getByIndex(int index){
		return graph.keyList().get(index);
	}
}
