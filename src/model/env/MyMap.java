package model.env;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

/**
 * 
 * Réimplémentation de Hashmap, dans laquelle une clé peut avoir plusieurs valeurs.
 * 
 * NOTE : Il n'y a aucun mesure de sécurité ni de vérification des paramètres.
 *
 * @param <T1> Type des clés
 * @param <T2> Type des valeurs
 */
public class MyMap implements Serializable{
	
	private ArrayList<Place> keys;
	private ArrayList<ArrayList<Integer>> values;
	
	public MyMap(){
		keys = new ArrayList<Place>();
		values = new ArrayList<ArrayList<Integer>>();
	}
	
	/**
	 * Ajoute val aux suivants de key.
	 * L'objet key doit exister au préalable
	 * @param key
	 * @param val
	 */
	public void put(int key, int val){
		ArrayList<Integer> vals = values.get(key);
		if(!vals.contains(val)) vals.add(val);
	}
	
	/**
	 * Ajoute l'éléments aux clés de la map et crée une liste vide associée
	 * @param key
	 */
	public void addKey(Place key){
		keys.add(key);
		values.add(new ArrayList<Integer>());
	}
	
	/**
	 * Renvoie la liste des clés.
	 * @return
	 */
	public ArrayList<Place> keyList(){
		return keys;
	}
	
	/**
	 * Renvoie le set des valeurs du point n
	 * @param key Clé dont on veut les valeurs associées.
	 */
	public ArrayList<Integer> valueList(int n){
		return values.get(n);
	}
	
	public ArrayList<ArrayList<Integer>> valueLists(){
		return values;
	}
	
	/**
	 * Dupplique un graphe n fois par des clonages profonds.
	 * @param n Nombre de dupplications que l'on veut
	 * @return ArrayList des versions dupliquées (le premier de la liste est l'original)
	 */
	public ArrayList<MyMap> duplicate(int n) {
		
		ArrayList<MyMap> copie = new ArrayList<MyMap>();
		
		for (int i = 0; i < n; i++) {
			copie.add(this.copy());
		}
		
		return copie;
	}
	
	/**
	 * Add to all indexes the value of adding (handy to change indexes when blending graphes)
	 * @param adding
	 */
	public void addToAllIndexes(int adding){
		
		//On met à jour les indices des places
		for (int i = 0; i < keys.size(); i++) {
			int original_index = keys.get(i).getIndex();
			keys.get(i).setIndex(original_index + adding);
		}
		
		//On met à jour les indices dans les listes d'adjacence
		for (ArrayList<Integer> current_list : values) {
			for (int i = 0; i < current_list.size(); i++) {
				int current_value = current_list.get(i).intValue();
				current_list.set(i, new Integer(current_value + adding));
			}
		}
	}
	
	/**
	 * Lie tous les points du graphe courant à ceux du graphe passé en paramètre.
	 * Les liens créés sont dela forme : this -> graph
	 * Les liens garph -> this ne sont pas générés. La fonction doit-être appelée dans l'autre sens.
	 * 
	 * NOTE : les deux graphes doivent avoir la même taille.
	 * 
	 * @param graph Graphe que l'on veut linker au graphe courant
	 */
	public void link(MyMap graph){
		int size = keys.size(); //Les deux graphes ont la même taille.
		
		for (int i = 0; i < size; i++) {
			Place dest = graph.keys.get(i);
						
			//On fait gaffe à l'indice d'origine : il ne correspond pas encore à la taille réelle de la liste
			this.put(i, dest.getIndex());
		}
	}

	private MyMap copy() {
		
		ArrayList<Place> copie_keys = new ArrayList<Place>();
		
		//On commence par copier les places
		for (int i = 0; i < keys.size(); i++){
			Place copie_place = keys.get(i).copy();
			copie_keys.add(copie_place);
		}
		
		//Puis on copie les listes d'adjacence
		ArrayList<ArrayList<Integer>> copie_liste_adjacence = new ArrayList<ArrayList<Integer>>();
		
		for (int i = 0; i < values.size(); i++) {
			ArrayList<Integer> original = values.get(i);
			ArrayList<Integer> copie_liste = new ArrayList<Integer>();
			
			//On copie les éléments de la liste
			for (int j = 0; j < original.size(); j++) {
				int val_copie = original.get(j).intValue();
				copie_liste.add(new Integer(val_copie));
			}
			
			copie_liste_adjacence.add(copie_liste);
		}
		
		MyMap copie_graph = new MyMap();
		copie_graph.keys = copie_keys;
		copie_graph.values = copie_liste_adjacence;
		
		return copie_graph;
	}
	
	public int getNbElements(){
		return keys.size();
	}
}
