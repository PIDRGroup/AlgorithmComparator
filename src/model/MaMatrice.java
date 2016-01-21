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
	
	public MaMatrice(){
		labels = new ArrayList<String>();
		data = new ArrayList<ArrayList<E>>();
	}
	
	/**
	 * M�thode pour supprimer une place par son label.
	 * Supprime aussi tous les liens avec les autres places.
	 * 
	 * @param label Label de la place
	 * @throws UnknownPlace Si le label n'est pas reconnu
	 */
	public void delete(String label) throws UnknownPlace{
		int ind = labels.indexOf(label);
		
		if(ind == -1)
			throw new UnknownPlace(label);
		
		this.delete(ind);
	}
	
	/**
	 * M�thode pour supprimer une place par son indice.
	 * Supprime aussi tous les liens avec les autres places.
	 * 
	 * @param index Indice de la place
	 * @throws UnknownPlace Si l'indice n'appartient pas � la matrice
	 */
	public void delete(int index) throws UnknownPlace{
		
		if(index < 0 || index >= data.size())
			throw new UnknownPlace(index);
		
		labels.remove(index);
		data.remove(index);
		
		for(ArrayList<E> l : data){
			l.remove(index);
		}
	}
	
	/**
	 * M�thode pour ajouter une place � l'environnement.
	 * Surtout utile pour les environnements variables.
	 * 
	 * @param label Label de la nouvelle place.
	 * @throws MultiplePlace Si la place que l'on essaie d'ajouter se trouve d�j� dans l'environnement
	 */
	public void addPlace(String label) throws MultiplePlace{
		
		//On v�rifie que le place n'existe pas encore.
		if(labels.indexOf(label) != -1)
			throw new MultiplePlace();
		
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
	
	/**
	 * M�thode pour cr�er un lien entre une source et une destination dont les labels
	 * sont pass�s en param�tre.
	 * 
	 * @param src Label de la source
	 * @param dest Label de la destination
	 * @param weight Poids du lien
	 */
	public void addLink(String src, String dest, E weight){
		int index_src = labels.indexOf(src);
		int index_dest = labels.indexOf(dest);
		
		this.addLink(index_src, index_dest, weight);
	}
	
	/**
	 * M�thode pour cr�er un lien entre uen source et une destination dont
	 * les indices sont pass�s en param�tres
	 * 
	 * @param src Indice de la source
	 * @param dest Indice de la destination
	 * @param weight Poids du lien
	 */
	public void addLink(int src, int dest, E weight){
		data.get(src).set(dest, weight);
	}
	
	/**
	 * M�thode pour r�cup�rer la valeur d'un lien entre uen source et une destination.
	 * Si la valeur est de 0, alors le lien n'existe pas.
	 * 
	 * @param src Label de la source
	 * @param dest Label de la destination
	 * @return Poids du lien
	 * @throws UnknownPlace Si le label de la source ou de la destination est inconnu
	 */
	public E get(String src, String dest) throws UnknownPlace{
		int index_src = labels.indexOf(src);
		int index_dest = labels.indexOf(dest);
		
		if(index_src == -1)
			throw new UnknownPlace(src);
		
		if(index_dest == -1)
			throw new UnknownPlace(dest);
		
		return this.get(index_src, index_dest);
	}
	
	/**
	 * M�thode pour r�cup�rer la valeur d'un lien entre uen source et une destination.
	 * Si la valeur est de 0, alors le lien n'existe pas.
	 * 
	 * @param src indice de la source
	 * @param dest indice de la destination
	 * @return Poids du lien
	 * @throws UnknownPlace Si l'indice n'appartient pas � la matrice
	 */
	public E get(int src, int dest) throws UnknownPlace{
		
		if(src < 0 || src >= data.size())
			throw new UnknownPlace(src);
		
		if(dest < 0 || dest >= data.size())
			throw new UnknownPlace(dest);
		
		return data.get(src).get(dest);
	}
	
	/**
	 * Taille de la matrice d'adjacence.
	 * Celle-ci �tant forc�ment carr�e, on ne renvoie qu'un entier.
	 * 
	 * @return Taille de la matrice.
	 */
	public int size(){
		return data.size();
	}
	
	/**
	 * Cr�e une copie en profondeur afin d'�viter les inf�rences de pointeur.
	 * @return
	 */
	public MaMatrice<E> duplicate(){
		MaMatrice<E> copy = new MaMatrice<>();
		
		copy.labels = new ArrayList<String>();
		for(String l : this.labels){
			copy.labels.add(l);
		}
		
		copy.data = new ArrayList<ArrayList<E>>();
		for (ArrayList<E> al : data){
			ArrayList<E> list = new ArrayList<E>();
			for(E el : al){
				list.add(el);
			}
			copy.data.add(list);
		}
		
		return copy;
	}
}
