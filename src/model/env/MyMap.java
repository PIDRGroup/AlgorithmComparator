package model.env;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

/**
 * 
 * R�impl�mentation de Hashmap, dans laquelle une cl� peut avoir plusieurs valeurs.
 * 
 * NOTE : Il n'y a aucun mesure de s�curit� ni de v�rification des param�tres.
 *
 * @param <T1> Type des cl�s
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
	 * L'objet key doit exister au pr�alable
	 * @param key
	 * @param val
	 */
	public void put(int key, int val){
		ArrayList<Integer> vals = values.get(key);
		if(!vals.contains(val)) vals.add(val);
	}
	
	/**
	 * Ajoute l'�l�ments aux cl�s de la map et cr�e une liste vide associ�e
	 * @param key
	 */
	public void addKey(Place key){
		keys.add(key);
		values.add(new ArrayList<Integer>());
	}
	
	/**
	 * Renvoie la liste des cl�s.
	 * @return
	 */
	public ArrayList<Place> keyList(){
		return keys;
	}
	
	/**
	 * Renvoie le set des valeurs du point n
	 * @param key Cl� dont on veut les valeurs associ�es.
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
	 * @return ArrayList des versions dupliqu�es (le premier de la liste est l'original)
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
		
		//On met � jour les indices des places
		for (int i = 0; i < keys.size(); i++) {
			int original_index = keys.get(i).getIndex();
			keys.get(i).setIndex(original_index + adding);
		}
		
		//On met � jour les indices dans les listes d'adjacence
		for (ArrayList<Integer> current_list : values) {
			for (int i = 0; i < current_list.size(); i++) {
				int current_value = current_list.get(i).intValue();
				current_list.set(i, new Integer(current_value + adding));
			}
		}
	}
	
	/**
	 * Lie tous les points du graphe courant � ceux du graphe pass� en param�tre.
	 * Les liens cr��s sont dela forme : this -> graph
	 * Les liens garph -> this ne sont pas g�n�r�s. La fonction doit-�tre appel�e dans l'autre sens.
	 * 
	 * NOTE : les deux graphes doivent avoir la m�me taille.
	 * 
	 * @param graph Graphe que l'on veut linker au graphe courant
	 */
	public void link(MyMap graph){
		int size = keys.size(); //Les deux graphes ont la m�me taille.
		
		for (int i = 0; i < size; i++) {
			Place dest = graph.keys.get(i);
						
			//On fait gaffe � l'indice d'origine : il ne correspond pas encore � la taille r�elle de la liste
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
			
			//On copie les �l�ments de la liste
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
