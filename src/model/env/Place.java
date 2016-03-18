package model.env;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * 
 * Une place dans le graphe.
 * On peut calculer sa distance par rapport � une autre place et activer une dimension de contrainte.
 * On peut aussi g�n�rer un ensemble de points par des contraintes.
 *
 */
public class Place implements Serializable{
	
	/**
	 * Optionnel : label de la place
	 */
	private String label = null;
	
	/**
	 * Coordonn�es de la place
	 */
	private int[] coordinates;
	
	/**
	 * Dimension de contrainte suppl�mentaire pour ajouter une part d'al�a.
	 * De base, dimension d�sactiv�e.
	 */
	private int w = 0;
	
	/**
	 * Constructeur d'une place avec label
	 * @param label Label de la place 
	 * @param coordinates Coordonn�es de la place (d�termine son nombre de dimension)
	 */
	public Place(String label, int... coordinates){
		this.label = label;
		this.coordinates= new int[coordinates.length];
		
		for (int i = 0; i < coordinates.length; i++) {
			this.coordinates[i] = coordinates[i];
		}
	}
	
	/**
	 * Constructeur d'une place sans label
	 * @param coordinates Coordonn�es de la place (d�termine son nombre de dimension)
	 */
	public Place(int... coordinates){
		this(null, coordinates);
	}
	
	public Place(ArrayList<Long> coordinates){
		this.coordinates = new int[coordinates.size()];
		for (int i = 0; i < this.coordinates.length; i++) this.coordinates[i] = coordinates.get(i).intValue();
	}
	
	/**
	 * G�n�re un lot d'un certain nombre de places anonymes avec un nombre de dimensions 
	 * bounds.length. Toute dimension d a des coordonn�es comprises entre bounds[i].min()
	 * et bounds[i].max().
	 * 
	 * Le param�tre w_activation permet de contr�ler l'activation de la dimension de contrainte.
	 * 
	 * @param nb_places
	 * @return
	 */
	public static Place[] generate(int nb_places, Bound[] bounds, boolean w_activation){
		Place[] places = new Place[nb_places];
		
		for (int i = 0; i < places.length; i++) {
			
			int [] coordinates = new int[bounds.length];
			
			for(int j=0; j<bounds.length; j++){
				Bound b = bounds[j];
				coordinates[j] = (new Random()).nextInt(b.max()-b.min()) + b.min();
			}
			
			places[i] = new Place(coordinates);
			
			//Si on active w, on calcule la dimension avec les plus petites bornes
			if(w_activation){
				int min_max = bounds[0].max(), min_min = bounds[0].min();
				
				//On calcule la moyenne des bornes
				for(int j=1; j<bounds.length; j++){
					if (min_max > bounds[j].max()) min_max = bounds[j].max();
					if (min_min > bounds[j].min()) min_min += bounds[j].min();
				}
				
				places[i].activateW(min_min, min_max);
			}
		}
		
		return places;
	}
	
	/**
	 * Renvoie le tableau des coordonn�es
	 * @return
	 */
	public int[] getCoordinates(){
		return coordinates;
	}
	
	/**
	 * Renvoie la coordonn�e de la dimension x
	 * @param x Dimension de la coordonn�e dont on veut la valeur
	 * @return
	 */
	public int getCoordinate(int x){
		return coordinates[x];
	}
	
	public void setCoordinate(int dim, int value){
		coordinates[dim] = value;
	}
	
	/**
	 * Retourne le label ou une chaine vide si la place n'a pas de label
	 * @return Le label de la place
	 */
	public String getLabel(){
		if(label == null) return "";
		
		return label;
	}
	
	/**
	 * Retourne la dimension de contrainte.
	 * @return
	 */
	public int dimensionW(){
		return w;
	}
	
	/**
	 * Active la dimension W.
	 * Cr�e une valeur al�atoire entre min et max.
	 */
	public void activateW(int min, int max){
		w = (new Random()).nextInt(max-min) + min;
	}
	
	/**
	 * D�sactive la dimension W.
	 */
	public void deactivateW(){
		w = 0;
	}
	
	/**
	 * Calcul de la distance euclidienne entre deux points
	 * @param p Point dont on veut conna�tre la distance au point courant
	 * @return Valeur de la distance
	 */
	public double distanceEuclidienne(Place p){
		double dist = 0;
		
		//On fait la somme des carr�s des diff�rences de chaque coordonn�es
		for (int i = 0; i < coordinates.length; i++) {
			dist += Math.pow(coordinates[i] - p.coordinates[i], 2);
		}
		
		//On n'oublie pas la dimension de contrainte
		dist += Math.pow(w - p.w, 2);
		
		return Math.sqrt(dist);
	}
	
	public String toString(){
		String s = "(";
		
		for (int i = 0; i < coordinates.length; i++) {
			s+=coordinates[i];
			if(i != coordinates.length-1) s+=",";
		}
		
		return s+")";
	}
	
	@Override
	public boolean equals(Object o){
		Place p = (Place) o;
		boolean ret = true;
		
		int i=0;
		while(ret && i<coordinates.length){
			ret = coordinates[i] == p.coordinates[i];
			i++;
		}
		
		return ret;
	}
}
