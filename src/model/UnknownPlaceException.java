package model;

/**
 * 
 * Exception lev�e quand la place est inconnue
 *
 */
public class UnknownPlaceException extends Exception{
	
	public UnknownPlaceException(int indice){
		super("L'indice de la place est inconnu : "+indice);
	}
	
	public UnknownPlaceException(String label){
		super("Le label de la place est inconnu : "+label);
	}
	
}