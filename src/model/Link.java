package model;

/**
 * 
 * Modélise un lien entre deux places
 *
 */
public class Link<E extends Number>{
	private String source, destination;
	private E value;
	
	public Link(String src, String dest, E val){
		source = src;
		destination = dest;
		value=val;
	}
	
	public Link(String src, String dest){
		this(src, dest, (E) (Integer) 1);
	}
	
	@Override
	public boolean equals(Object o){
		Link l = (Link) o;
		
		return l.source.equals(source) && l.destination.equals(destination);
	}
	
	public String getSrc(){
		return source;
	}
	
	public String getDest(){
		return destination;
	}
	
	public E getVal(){
		return value;
	}
	
	public void setVal(E val){
		value=val;
	}
}
