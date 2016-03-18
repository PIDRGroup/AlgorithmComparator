package model.env;

/**
 * 
 * Exception g�n�r�e quand un lien est inexistant
 *
 */
public class UnknownLinkException extends Exception{
	public UnknownLinkException(String src, String dest){
		super("Lien inconnu entre "+src+" et "+dest);
	}
}
