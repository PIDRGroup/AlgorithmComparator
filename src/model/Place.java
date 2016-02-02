package model;

public class Place {
	private String label;
	private int[] coordinates;
	
	//On crée des id pour que des places différentes puissent avoir le même nom sans être identique
	private int id;
	private static int ID_MAX = 0;
	
	public Place(String label){
		this(label, 0);
	}
	
	public Place(String label, int... coordinates){
		this.label=label;
		id = ID_MAX++;
		
		this.coordinates = new int[coordinates.length];
		for(int i=0;i<coordinates.length;i++){
			this.coordinates[i]=coordinates[i];
		}
	}
	
	public String getLabel(){
		return label;
	}
	
	public int[] getCoordinates(){
		return coordinates;
	}
	
	@Override
	public boolean equals(Object o){
		Place p = (Place) o;
		
		return p.id == id;
	}
}
