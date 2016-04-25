package model.algo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import model.env.Place;

public class Evaluation implements Serializable{
	private int nb_solution;
	private int premiere_visite_noeud;
	private int visite_noeud;
	private int noeud_envisage;
	private Date beginning;
	private ArrayList<Double> cost_solutions;
	private ArrayList<Integer> nb_noeud_solutions;
	private ArrayList<Date> date_solutions; 
	private ArrayList<Integer> premiere_visite_solutions;
	private ArrayList<Integer> visite_noeud_solutions;
	private ArrayList<Integer> noeud_visitee_solutions;
	private ArrayList<Place> noeud_visite;
	
	public Evaluation() {
		this.nb_solution = 0;
		this.premiere_visite_noeud = 0;
		this.visite_noeud = 0;
		this.noeud_envisage = 0;
		this.cost_solutions = new ArrayList<Double>();
		this.nb_noeud_solutions = new ArrayList<Integer>();
		this.date_solutions = new ArrayList<Date>();
		this.premiere_visite_solutions = new ArrayList<Integer>();
		this.visite_noeud_solutions = new ArrayList<Integer>();
		this.noeud_visitee_solutions = new ArrayList<Integer>();
		this.noeud_visite = new ArrayList<Place>();
	}
	
	public void start(){
		this.beginning = new Date();
	}
	
	public void newVisite(Place place){
		if (!this.noeud_visite.contains(place)){
			this.premiere_visite_noeud++;
			this.noeud_visite.add(place);
		}		
		this.visite_noeud++;
	}
	
	public void newNoeudEnvisage(){
		this.noeud_envisage++;
	}
	
	public void gotASolution(Double costnewpath, Integer nbnoeud){
		this.nb_solution++;
		this.cost_solutions.add(costnewpath);
		this.nb_noeud_solutions.add(nbnoeud);
		this.date_solutions.add(new Date());
		this.premiere_visite_solutions.add(this.premiere_visite_noeud);
		this.visite_noeud_solutions.add(this.visite_noeud);
		this.noeud_visitee_solutions.add(this.noeud_envisage);
	}
	
	public int getNbSolution(){
		return this.nb_solution;
	}
	
	public int getPremiereVisite(){
		return this.premiere_visite_noeud;
	}
	
	public int getVisiteNoeud(){
		return this.visite_noeud;
	}
	
	public int getNoeudEnvisage(){
		return this.noeud_envisage;
	}
	
	public Date getBeginning(){
		return this.beginning;
	}
	
	public ArrayList<Double> getCostSolutions(){
		return this.cost_solutions;
	}
	
	public ArrayList<Integer> getNbNoeudSolutions(){
		return this.nb_noeud_solutions;
	}
	
	public ArrayList<Date> getDateSolutions(){
		return this.date_solutions;
	}
	
	public ArrayList<Long> getTimeSolutions(){
		ArrayList<Long> time_solutions = new ArrayList<Long>();
		Long beginning_time = this.beginning.getTime();
		Long current;
		
		for(Date d : date_solutions){
			current = d.getTime() - beginning_time;
			time_solutions.add(current);
		}
		
		return time_solutions;
	}
	
	public ArrayList<Integer> getPremiereVisiteSolutions(){
		return this.premiere_visite_solutions;
	}
	
	public ArrayList<Integer> getVisiteNoeudSolutions(){
		return this.visite_noeud_solutions;
	}
	
	public ArrayList<Integer> getNoeudEnvisageSolutions(){
		return this.noeud_visitee_solutions;
	}
	 
	
}
