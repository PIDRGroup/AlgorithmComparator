package model;

import java.util.Random;

/**
 * 
 * Une place dans le graphe.
 * On peut calculer sa distance par rapport � une autre place et activer une dimension de contrainte.
 *
 */
public class Place {
	
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
	public double distance(Place p){
		double dist = 0;
		
		//On fait la somme des carr�s des diff�rences de chaque coordonn�es
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
		
		//On regarde si le label est nul ou non. Si non, on v�rifie que les labels soient identiques.
		ret = (label == null) ? p.label == null : label.equals(p.label);
		
		int i=0;
		while(ret && i<coordinates.length){
			ret = coordinates[i] == p.coordinates[i];
			i++;
		}
		
		return ret;
	}
}
