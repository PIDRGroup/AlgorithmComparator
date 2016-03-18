package model.env;

/**
 * 
 * Exception générée quand plusieurs places identiques sont ajoutées à un environnement
 *
 */
public class MultiplePlaceException extends Exception {
	public MultiplePlaceException(String place){
		super("La place '"+place+"' se trouve déjà dans l'environnement !");
	}
}
