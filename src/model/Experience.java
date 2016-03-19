package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import model.algo.Algorithm;
import model.algo.Evaluation;
import model.algo.Log;
import model.env.Environment;
import model.env.Place;
import model.env.UnknownPlaceException;

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
		if(!algos.contains(a))
			algos.add(a);
	}
	
	public void addAlgos(ArrayList<Algorithm> alg){
		for(Algorithm a : alg){
			this.addAlgo(a);
		}
	}
	
	public void launch() throws UnknownPlaceException{
		for(Algorithm a : algos){
			System.out.println("LAUNCHING -- "+a.getName()+" -- ");
			a.grow(env, source, dest);
			Log l = new Log(a);
			System.out.println(l);
			System.out.println("FINISHED -- "+a.getName()+" -- ");
		}
	}
	
	public Place getSrc(){
		return source;
	}
	
	public Place getDest(){
		return dest;
	}
	
	public ArrayList<Evaluation> getEvals(){
		ArrayList<Evaluation> list = new ArrayList<Evaluation>();
		
		for (Algorithm al : algos) {
			list.add(al.getEval());
		}
		
		return list;
	}
	
	public ArrayList<Algorithm> getAlgos(){
		return algos;
	}
	
	public Environment getEnv(){
		return env;
	}
	
	public void save(String path) throws IOException{
		FileOutputStream fos = new FileOutputStream(new File(path));
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(this);
		oos.flush();
		oos.close();
	}
	
	public static Experience load(String path) throws IOException, ClassNotFoundException{
		FileInputStream fos = new FileInputStream(new File(path));
		ObjectInputStream oos = new ObjectInputStream(fos);
		Experience exp = (Experience) oos.readObject();
		oos.close();
		return exp;
	}
}
