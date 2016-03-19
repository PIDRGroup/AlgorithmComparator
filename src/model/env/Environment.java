package model.env;

import java.io.Serializable;
import java.util.*;

public abstract class Environment extends Observable implements Serializable{
	
	/**
	 * Les places en clés correspondent à l'ensemble des places du graphe, 
	 * tandis que les places en valeur sont les successeurs de ces places.
	 */
	protected MyMap<Place, Place> graph;
	protected Seed seed;

	/**
	 * Méthode pour supprimer une place par sa position.
	 * Supprime aussi tous les liens avec les autres places.
	 * 
	 * @param positions Coordonnées de la place à supprimer
	 * @throws UnknownPlaceException Si l'indice n'appartient pas à la matrice
	 */
	public void delete(int... positions) throws UnknownPlaceException{
		Place target = new Place(positions);
		this.delete(target);
	}
	
	/**
	 * Méthode pour supprimer une place par sa position.
	 * Supprime aussi tous les liens avec les autres places.
	 * 
	 * @param target Place à supprimer
	 * @throws UnknownPlaceException
	 */
	public void delete(Place target) throws UnknownPlaceException{
		
		if(!graph.containsKey(target))
			throw new UnknownPlaceException(target);
		
		//On supprime la place du graphe
		graph.remove(target);
		
		//On supprime les liens de la place
		for(Place key : graph.keyList()){
			graph.remove(key, target);
		}
	}
	
	/**
	 * Méthode pour créer un lien entre uen source et une destination
	 * passées en paramètres
	 * 
	 * @param src Place source
	 * @param dest Place destination
	 * @throws UnknownPlaceException 
	 */
	public void addLink(Place src, Place dest) throws UnknownPlaceException{
		if(!graph.containsKey(src))
			throw new UnknownPlaceException(src);
		
		if(!graph.containsKey(dest))
			throw new UnknownPlaceException(dest);
				
		graph.put(src, dest);
	}
	
	public void addPlace(Place p){
		graph.addKey(p);
	}
	
	/**
	 * Méthode pour récupérer la valeur d'un lien entre une source et une destination.
	 * Si la valeur est de MAX_VALUE, alors le lien n'existe pas.
	 * 
	 * @param src Place source
	 * @param dest Place destination
	 * @return Poids du lien (distance entre les deux places)
	 * @throws UnknownPlaceException Si les places n'appartiennent pas au graph
	 */
	public double get(Place src, Place dest) throws UnknownPlaceException{
		if(!graph.containsKey(src))
			throw new UnknownPlaceException(src);
		
		if(!graph.containsKey(dest))
			throw new UnknownPlaceException(dest);
		
		//Si val vaut null, la distance est infinie
		ArrayList<Place> l = graph.valueList(src);
		
		double val = 0;
		int i=0;
		
		while(i < l.size() && val == 0){
			if(l.get(i).equals(dest)){
				val = src.distanceEuclidienne(dest);
			}else{
				i++;
			}
		}
		
		val = (val == 0) ? Integer.MAX_VALUE : val;
		
		return val;
	}
	
	/**
	 * Nombre de places.
	 * Celle-ci étant forcément carrée, on ne renvoie qu'un entier.
	 * 
	 * @return Taille de la matrice.
	 */
	public int size(){
		return graph.keyList().size();
	}
	
	public String toString(){
		String s = "";
		
		s+="Successors : ";
		
		for(Place src : graph.keyList()){
			s+="[ ";
			for(Place dest : graph.valueList(src)){
				s+=dest+" | ";
			}
			s+=" ]";
		}
				
		return s;
	}
	
	/**
	 * Vérifie si une place fait partie de l'environnement
	 * @param place Place à checker
	 * @return Vrai si la place existe, faux sinon
	 */
	public boolean isPlace(Place place){
		return graph.keyList().contains(place);
	}
	
	public int indexOf(Place p){
		return graph.indexOf(p);
	}
	
	public Place getPlace(int... coordinates) throws UnknownPlaceException{
		
		if(coordinates.length != nbDim())throw new UnknownPlaceException(new Place(coordinates));
		
		Place p = null;
		ArrayList<Place> places = graph.keyList();
		
		for (int i = 0; i < places.size() && p == null; i++) {
			Place current = places.get(i);
			boolean eq = true;
			
			for (int j = 0; j < coordinates.length && eq == true; j++) {
				if(current.getCoordinate(j) != coordinates[j]) eq = false;
			}
			
			if(eq == true)p = current;
			
		}
		
		return p;
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
	 * Tier une place au hasard.
	 * @return
	 */
	public Place alea(){
		return graph.keyList().get((new Random()).nextInt(graph.keyList().size()));
	}
	
	public ArrayList<Place> getLinks(Place src) throws UnknownPlaceException{
		return graph.valueList(src);
	}

	public Seed getSeed() {
		return seed;
	}
	
	public int degre(Place p){
		return graph.valueList(p).size();
	}
	
	public Place getByIndex(int index){
		return graph.keyList().get(index);
	}
}
