package model;

import java.util.Random;

/**
 * 
 * Une place dans le graphe.
 * On peut calculer sa distance par rapport à une autre place et activer une dimension de contrainte.
 *
 */
public class Place {
	
	/**
	 * Optionnel : label de la place
	 */
	private String label = null;
	
	/**
	 * Coordonnées de la place
	 */
	private int[] coordinates;
	
	/**
	 * Dimension de contrainte supplémentaire pour ajouter une part d'aléa.
	 * De base, dimension désactivée.
	 */
	private int w = 0;
	
	/**
	 * Constructeur d'une place avec label
	 * @param label Label de la place 
	 * @param coordinates Coordonnées de la place (détermine son nombre de dimension)
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
	 * @param coordinates Coordonnées de la place (détermine son nombre de dimension)
	 */
	public Place(int... coordinates){
		this(null, coordinates);
	}
	
	/**
	 * Renvoie le tableau des coordonnées
	 * @return
	 */
	public int[] getCoordinates(){
		return coordinates;
	}
	
	/**
	 * Renvoie la coordonnée de la dimension x
	 * @param x Dimension de la coordonnée dont on veut la valeur
	 * @return
	 */
	public int getCoordinate(int x){
		return coordinates[x];
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
	 * Crée une valeur aléatoire entre min et max.
	 */
	public void activateW(int min, int max){
		w = (new Random()).nextInt(max-min) + min;
	}
	
	/**
	 * Désactive la dimension W.
	 */
	public void deactivateW(){
		w = 0;
	}
	
	/**
	 * Calcul de la distance euclidienne entre deux points
	 * @param p Point dont on veut connaître la distance au point courant
	 * @return Valeur de la distance
	 */
	public double distance(Place p){
		double dist = 0;
		
		//On fait la somme des carrés des différences de chaque coordonnées
		for (int i = 0; i < coordinates.length; i++) {
			dist += Math.pow(coordinates[i] - p.coordinates[i], 2);
		}
		
		//On n'oublie pas la dimension de contrainte
		dist += Math.pow(w - p.w, 2);
		
		return Math.sqrt(dist);
	}
	
	@Override
	public boolean equals(Object o){
		Place p = (Place) o;
		boolean ret = true;
		
		//On regarde si le label est nul ou non. Si non, on vérifie que les labels soient identiques.
		ret = (label == null) ? p.label == null : label.equals(p.label);
		
		int i=0;
		while(ret && i<coordinates.length){
			ret = coordinates[i] == p.coordinates[i];
			i++;
		}
		
		return ret;
	}
}
