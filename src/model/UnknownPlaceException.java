package model;

/**
 * 
 * Exception levée quand la place est inconnue
 *
 */
public class UnknownPlaceException extends Exception{
	
	public UnknownPlaceException(Place p){
		super(toMess(p));
	}
	
	private static String toMess(Place p){
		String msg = "La place est inconnue : ("+p.getLabel()+", coordonnées : [";
		
		for (int i = 0; i < p.getCoordinates().length; i++) {
			msg+=p.getCoordinate(i)+" ";
		}
		
		msg+=" ] )";
		
		return msg;
	}
	
}
