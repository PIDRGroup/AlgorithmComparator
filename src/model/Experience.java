package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * 
 * Représente une expérience avec son environnement et les algorithmes qui se déroulent dessus.
 *
 */
public class Experience implements Serializable{
	
	private Environment env;
	private Place source, dest;
	private ArrayList<Algorithm> algos;
	private String name;
	private String date;
	
	public Experience(String name, String date, Environment e, Place src, Place dst){
		this.name=name;
		this.date=date;
		env=e;
		source=src;
		dest=dst;
		this.algos = new ArrayList<Algorithm>();
	}
	
	public Experience(String name, String date, Environment e){
		
		this.name=name;
		this.date=date;
		env=e;
		source=e.alea();
		dest=e.alea();
		this.algos = new ArrayList<Algorithm>();
	}
	
	public void addAlgo(Algorithm a){
		algos.add(a);
	}
	
	public void launch() throws UnknownPlaceException{
		for(Algorithm a : algos){
			a.grow(env, source, dest);
		}
	}
	
	public Place getSrc(){
		return source;
	}
	
	public Place getDest(){
		return dest;
	}
}
