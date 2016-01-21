package model;

public class MultiplePlace extends Exception {
	public MultiplePlace(){
		super("Cette place se trouve déjà dans l'environnement !");
	}
}
