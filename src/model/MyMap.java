package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * R�impl�mentation de Hashmap, dans laquelle une cl� peut avoir plusieurs valeurs.
 * 
 * NOTE : Il n'y a aucun mesure de s�curit� ni de v�rification des param�tres.
 *
 * @param <T1> Type des cl�s
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
	 * Supprime la cl� et les valeurs associ�es
	 * @param key
	 */
	public void remove(E key){
		values.remove(keys.indexOf(key));
		keys.remove(key);
	}
	
	/**
	 * Ajoute vala aux suivants de key.
	 * L'objet key doit exister au pr�alable
	 * @param key
	 * @param val
	 */
	public void put(E key, F val){
		ArrayList<F> vals = values.get(keys.indexOf(key));
		if(!vals.contains(val)) vals.add(val);
	}
	
	/**
	 * Ajoute l'�l�ments aux cl�s de la map et cr�e une liste vide associ�e
	 * @param key
	 */
	public void addKey(E key){
		keys.add(key);
		values.add(new ArrayList<F>());
	}
	
	/**
	 * Supprime l'�l�ment target des �l�ments associ�s � key
	 * @param key
	 * @param target
	 */
	public void remove(E key, F target){
		values.get(keys.indexOf(key)).remove(target);
	}
	
	/**
	 * Renvoie la liste des cl�s.
	 * @return
	 */
	public ArrayList<E> keyList(){
		return keys;
	}
	
	/**
	 * Renvoie le set des valeurs de la cl� pass�e en param�tre
	 * @param key Cl� dont on veut les valeurs associ�es.
	 */
	public ArrayList<F> valueList(E key){
		return values.get(keys.indexOf(key));
	}
	
	/**
	 * R�alise une copie en semi-profondeur de l'objet
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
	 * Retourne l'index de l'�l�ment dans la liste des cl�s.
	 * -1 si l'�l�ment n'est pas pr�sent
	 * @return
	 */
	public int indexOf(E key){
		return keys.indexOf(key);
	}
	
}
