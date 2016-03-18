package model.env;

/**
 * 
 * Exception g�n�r�e quand plusieurs places identiques sont ajout�es � un environnement
 *
 */
public class MultiplePlaceException extends Exception {
	public MultiplePlaceException(String place){
		super("La place '"+place+"' se trouve d�j� dans l'environnement !");
	}
}
