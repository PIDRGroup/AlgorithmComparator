package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * Réimplémentation de Hashmap, dans laquelle une clé peut avoir plusieurs valeurs.
 * 
 * NOTE : Il n'y a aucun mesure de sécurité ni de vérification des paramètres.
 *
 * @param <T1> Type des clés
 * @param <T2> Type des valeurs
 */
public class MyMap<E, F> implements Serializable{
	
	private ArrayList<E> keys;
	private ArrayList<ArrayList<F>> values;
	
	public MyMap(){
		keys = new ArrayList<E>();
		values = new ArrayList<ArrayList<F>>();
	}
	
	public boolean containsKey(E key){
		return keys.contains(key);
	}
	
	/**
	 * Supprime la clé et les valeurs associées
	 * @param key
	 */
	public void remove(E key){
		values.remove(keys.indexOf(key));
		keys.remove(key);
	}
	
	/**
	 * Ajoute vala aux suivants de key.
	 * L'objet key doit exister au préalable
	 * @param key
	 * @param val
	 */
	public void put(E key, F val){
		ArrayList<F> vals = values.get(keys.indexOf(key));
		if(!vals.contains(val)) vals.add(val);
	}
	
	/**
	 * Ajoute l'éléments aux clés de la map et crée une liste vide associée
	 * @param key
	 */
	public void addKey(E key){
		keys.add(key);
		values.add(new ArrayList<F>());
	}
	
	/**
	 * Supprime l'élément target des éléments associés à key
	 * @param key
	 * @param target
	 */
	public void remove(E key, F target){
		values.get(keys.indexOf(key)).remove(target);
	}
	
	/**
	 * Renvoie la liste des clés.
	 * @return
	 */
	public ArrayList<E> keyList(){
		return keys;
	}
	
	/**
	 * Renvoie le set des valeurs de la clé passée en paramètre
	 * @param key Clé dont on veut les valeurs associées.
	 */
	public ArrayList<F> valueList(E key){
		return values.get(keys.indexOf(key));
	}
	
	/**
	 * Réalise une copie en semi-profondeur de l'objet
	 * @return
	 */
	public MyMap<E, F> dupplicate(){
		MyMap<E, F> copy = new MyMap<E, F>();
		
		copy.keys = new ArrayList<E>();
		copy.values = new ArrayList<ArrayList<F>>();
		for(int i=0; i<keys.size(); i++){
			copy.keys.add(keys.get(i));
			copy.values.add(new ArrayList<F>());
			
			for(F val : values.get(i)){
				copy.values.get(i).add(val);
			}
		}
		
		return copy;
	}

	/**
	 * Retourne l'index de l'élément dans la liste des clés.
	 * -1 si l'élément n'est pas présent
	 * @return
	 */
	public int indexOf(E key){
		return keys.indexOf(key);
	}
	
}
