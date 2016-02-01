package model;

public class MultiplePlaceException extends Exception {
	public MultiplePlaceException(String place){
		super("La place '"+place+"' se trouve déjà dans l'environnement !");
	}
}
