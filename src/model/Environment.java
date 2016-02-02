package model;

import java.util.ArrayList;
import java.util.Observable;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;

public class Environment<E extends Number> extends Observable{
	
	private ArrayList<ArrayList<Link<E>>> successors;
	private ArrayList<String> labels;
		
	public Environment(){
		successors = new ArrayList<ArrayList<Link<E>>>();
		labels = new ArrayList<String>();
	}
	
	/**
	 * Méthode pour ajouter une place à l'environnement.
	 * Surtout utile pour les environnements variables.
	 * 
	 * @param label Label de la nouvelle place.
	 * @throws MultiplePlaceException Si la place que l'on essaie d'ajouter se trouve déjà dans l'environnement
	 */
	public void addPlace(String label) throws MultiplePlaceException{
		if(labels.contains(label))
			throw new MultiplePlaceException(label);
		
		labels.add(label);
		successors.add(new ArrayList<>());
	}
	
	/**
	 * Méthode pour supprimer une place par son label.
	 * Supprime aussi tous les liens avec les autres places.
	 * 
	 * @param label Label de la place
	 * @throws UnknownPlaceException Si le label n'est pas reconnu
	 */
	public void delete(String label) throws UnknownPlaceException{
		this.delete(labels.indexOf(label));
	}
	
	/**
	 * Méthode pour supprimer une place par son indice.
	 * Supprime aussi tous les liens avec les autres places.
	 * 
	 * @param index Indice de la place
	 * @throws UnknownPlaceException Si l'indice n'appartient pas à la matrice
	 */
	public void delete(int index) throws UnknownPlaceException{
		if(index < 0 || index >= labels.size())
			throw new UnknownPlaceException(index);
		
		labels.remove(index);
		
		//Suppression dans la liste de successeurs
		successors.remove(index);
		for(ArrayList<Link<E>> l : successors){
			for(Link<E> el : l){
				if(el.getDest() == index){
					l.remove(el);
				}
			}
		}
	}
	
	/**
	 * Méthode pour créer un lien entre une source et une destination dont les labels
	 * sont passés en paramètre.
	 * 
	 * @param src Label de la source
	 * @param dest Label de la destination
	 * @param weight Poids du lien
	 * @throws UnknownPlaceException 
	 */
	public void addLink(String src, String dest, E weight) throws UnknownPlaceException{
		this.addLink(labels.indexOf(src), labels.indexOf(dest), weight);
	}
	
	/**
	 * Méthode pour créer un lien entre uen source et une destination dont
	 * les indices sont passés en paramètres
	 * 
	 * @param src Indice de la source
	 * @param dest Indice de la destination
	 * @param weight Poids du lien
	 * @throws UnknownPlaceException 
	 */
	public void addLink(int src, int dest, E weight) throws UnknownPlaceException{
		if(src < 0 || src > labels.size())
			throw new UnknownPlaceException(src);
		
		if(dest < 0 || dest > labels.size())
			throw new UnknownPlaceException(dest);
				
		successors.get(src).add(new Link<E>(dest, weight));
	}
	
	/**
	 * Méthode pour récupérer la valeur d'un lien entre uen source et une destination.
	 * Si la valeur est de MAX_VALUE, alors le lien n'existe pas.
	 * 
	 * @param src Label de la source
	 * @param dest Label de la destination
	 * @return Poids du lien
	 * @throws UnknownPlaceException Si le label de la source ou de la destination est inconnu
	 */
	public E get(String src, String dest) throws UnknownPlaceException{
		return this.get(labels.indexOf(src), labels.indexOf(dest));
	}
	
	/**
	 * Méthode pour récupérer la valeur d'un lien entre uen source et une destination.
	 * Si la valeur est de MAX_VALUE, alors le lien n'existe pas.
	 * 
	 * @param src indice de la source
	 * @param dest indice de la destination
	 * @return Poids du lien
	 * @throws UnknownPlaceException Si l'indice n'appartient pas à la matrice
	 */
	public E get(int src, int dest) throws UnknownPlaceException{
		if(src < 0 || src >= labels.size())
			throw new UnknownPlaceException(src);
		
		if(dest < 0 || dest >= labels.size())
			throw new UnknownPlaceException(dest);
		
		//Si val vaut null, la distance est infinie
		ArrayList<Link<E>> l = successors.get(src);
		
		E val = null;
		int i=0;
		
		while(i < l.size() && val == null){
			if(l.get(i).getDest() == dest){
				val = l.get(i).getVal();
			}else{
				i++;
			}
		}
		
		val = (val == null) ? (E) (Integer) Integer.MAX_VALUE : val;
		
		return val;
	}
	
	/**
	 * Nombre de places.
	 * Celle-ci étant forcément carrée, on ne renvoie qu'un entier.
	 * 
	 * @return Taille de la matrice.
	 */
	public int size(){
		return labels.size();
	}
	
	/**
	 * Retourne la liste des successeurs
	 * @return Liste des liens de l'environnement
	 */
	public ArrayList<ArrayList<Link<E>>> getLinks(){
		return successors;
	}
	
	/**
	 * Crée une copie en profondeur d'un environnement
	 * @return Copie en profondeur
	 */
	public Environment<E> duplicate(){
		Environment<E> copy = new Environment<E>();
		
		ArrayList<ArrayList<Link<E>>> l_copy = new ArrayList<ArrayList<Link<E>>>();
		
		for(ArrayList<Link<E>> l : successors){
			ArrayList<Link<E>> waiting_list=new ArrayList<Link<E>>();
			
			for(Link<E> el : l){
				waiting_list.add(el);
			}
			
			l_copy.add(waiting_list);
		}
		
		ArrayList<String> labs = new ArrayList<String>();
		for(String s : labels){
			labs.add(new String(s));
		}
		
		copy.labels = labs;
		copy.successors = l_copy;
		
		return copy;
	}
	
	public String toString(){
		String s = "";
		
		s+="Successors : ";
		
		for(int i=0; i<successors.size();i++){
			ArrayList<Link<E>> l = successors.get(i);
			s+="[";
						
			for(int j=0; j<l.size(); j++){
				try {
					E v=this.get(i, l.get(j).getDest());
					if(v.doubleValue() < Integer.MAX_VALUE)
						s+="("+labels.get(i)+"-"+v.doubleValue()+"->"+labels.get(l.get(j).getDest())+"), ";
				} catch (UnknownPlaceException e) {
					e.printStackTrace();
				}
			}
			s+="] ";
		}
				
		return s;
	}
	
	/**
	 * Vérifie si un label fait partie de l'environnement
	 * @param label Label à checker
	 * @return Vrai si le label est une place, faux sinon
	 */
	public boolean isLabel(String label){
		return labels.contains(label);
	}
	
	/**
	 * vérifie qu'un index représente une palce dans l'enrivonnement
	 * @param index
	 * @return
	 */
	public boolean isIndex(int index){
		return index > 0 && index < labels.size();
	}
	
	/**
	 * Retourne l'index du label
	 * @param label 
	 * @return
	 * @throws UnknownPlaceException
	 */
	public int indexOf(String label) throws UnknownPlaceException{
		if(!labels.contains(label))
			throw new UnknownPlaceException(label);
		
		return labels.indexOf(label);
	}
	
	/**
	 * Renvoie le label d'un index
	 * @param i
	 * @return
	 * @throws UnknownPlaceException
	 */
	public String labelOf(int i) throws UnknownPlaceException{
		if(i < 0 || i > labels.size())
			throw new UnknownPlaceException(i);
		
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
			ArrayList<Link<E>> l = successors.get(i);
			for(int j=0; j<l.size(); j++){
				Link<E> el = l.get(j);
				if(el.getVal().doubleValue() < Integer.MAX_VALUE){
					try{
						g.addEdge(el.getVal(), labels.get(i), labels.get(j));
					}catch(Throwable th){}
				}
			}
		}

		
		return g;
	}
}
