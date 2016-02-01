package model;

public class MultiplePlaceException extends Exception {
	public MultiplePlaceException(String place){
		super("La place '"+place+"' se trouve d�j� dans l'environnement !");
	}
}
