package model;

import java.util.ArrayList;

/**
 * 
 * A resizable matrix (we can add or delete rows and columns)
 * Matrice carr�e
 *
 */
public class MaMatrice<E extends Number>{
	
	private ArrayList<String> labels;
	private ArrayList<ArrayList<E>> data;
	
	public void delete(String label){
		this.delete(labels.indexOf(label));
	}
	
	public void delete(int index){
		labels.remove(index);
		data.remove(index);
		
		for(ArrayList<E> l : data){
			l.remove(index);
		}
	}
	
	public void addPlace(String label){
		labels.add(label);
		
		//On ajoute l'ArrayList correspondant aux pr�d�cesseurs du nouvel �l�ments avec uen taille
		//correspondant au nombre de places + la nouvelle place.
		ArrayList<E> new_list = new ArrayList<E>();
		data.add(new_list);
		
		//on initialise tous les �l�ments de la nouvelle colonne � 0
		for(int i=0; i<data.size(); i++){
			new_list.add((E)((Integer) 0));
		}
		
		//On parcourt chaque colonne pour rajouter un �l�ment en ligne (le nouvel �l�ment en tant que successeur)
		for(ArrayList<E> l : data){
			l.add(l.size(), ((E)((Integer) 0)));
		}
	}
	
	public void addLink(String src, String dest, E weight){
		int index_src = labels.indexOf(src);
		int index_dest = labels.indexOf(dest);
		
		this.addLink(index_src, index_dest, weight);
	}

	public void addLink(int src, int dest, E weight){
		data.get(src).set(dest, weight);
	}
	
	public E get(String src, String dest){
		int index_src = labels.indexOf(src);
		int index_dest = labels.indexOf(dest);
		
		return this.get(index_src, index_dest);
	}
	
	public E get(int src, int dest){
		return data.get(src).get(dest);
	}
	
	//Taille de la matrice carr�e
	public int size(){
		return data.size();
	}
}
