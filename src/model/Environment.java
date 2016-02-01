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
	
	/**
	 * Méthode pour ajouter une place à l'environnement.
	 * Surtout utile pour les environnements variables.
	 * 
	 * @param label Label de la nouvelle place.
	 * @throws MultiplePlace Si la place que l'on essaie d'ajouter se trouve déjà dans l'environnement
	 */
	public void addPlace(String label) throws MultiplePlace{
		labels.add(label);
		successors.add(new ArrayList<E>());
		predecessors.add(new ArrayList<E>());
	}
	
	/**
	 * Méthode pour supprimer une place par son label.
	 * Supprime aussi tous les liens avec les autres places.
	 * 
	 * @param label Label de la place
	 * @throws UnknownPlace Si le label n'est pas reconnu
	 */
	public void delete(String label) throws UnknownPlace{
		
	}
	
	/**
	 * Méthode pour supprimer une place par son indice.
	 * Supprime aussi tous les liens avec les autres places.
	 * 
	 * @param index Indice de la place
	 * @throws UnknownPlace Si l'indice n'appartient pas à la matrice
	 */
	public void delete(int index) throws UnknownPlace{
		
	}
	
	/**
	 * Méthode pour créer un lien entre une source et une destination dont les labels
	 * sont passés en paramètre.
	 * 
	 * @param src Label de la source
	 * @param dest Label de la destination
	 * @param weight Poids du lien
	 * @throws UnknownPlace 
	 */
	public void addLink(String src, String dest, E weight) throws UnknownPlace{
		
	}
	
	/**
	 * Méthode pour créer un lien entre uen source et une destination dont
	 * les indices sont passés en paramètres
	 * 
	 * @param src Indice de la source
	 * @param dest Indice de la destination
	 * @param weight Poids du lien
	 * @throws UnknownPlace 
	 */
	public void addLink(int src, int dest, E weight) throws UnknownPlace{
		
	}
	
	/**
	 * Méthode pour récupérer la valeur d'un lien entre uen source et une destination.
	 * Si la valeur est de MAX_VALUE, alors le lien n'existe pas.
	 * 
	 * @param src Label de la source
	 * @param dest Label de la destination
	 * @return Poids du lien
	 * @throws UnknownPlace Si le label de la source ou de la destination est inconnu
	 */
	public E get(String src, String dest) throws UnknownPlace{
		
	}
	
	/**
	 * Méthode pour récupérer la valeur d'un lien entre uen source et une destination.
	 * Si la valeur est de MAX_VALUE, alors le lien n'existe pas.
	 * 
	 * @param src indice de la source
	 * @param dest indice de la destination
	 * @return Poids du lien
	 * @throws UnknownPlace Si l'indice n'appartient pas à la matrice
	 */
	public E get(int src, int dest) throws UnknownPlace{
		
	}
	
	/**
	 * Taille de la matrice d'adjacence.
	 * Celle-ci étant forcément carrée, on ne renvoie qu'un entier.
	 * 
	 * @return Taille de la matrice.
	 */
	public int size(){
		return labels.size();
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
