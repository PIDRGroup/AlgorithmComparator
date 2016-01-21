package model;

public class UnknownPlace extends Exception{
	
	public UnknownPlace(int indice){
		super("L'indice de la place est inconnu : "+indice);
	}
	
	public UnknownPlace(String label){
		super("Le label de la place est inconnu : "+label);
	}
	
}
