package model;

public class UnknownLinkException extends Exception{
	public UnknownLinkException(String src, String dest){
		super("Lien inconnu entre "+src+" et "+dest);
	}
}
