package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Evaluation implements Serializable{
	private int nb_while; // nb de recherche
	private int nb_solution;
	private Date beginning;
	private ArrayList<Integer> nb_while_solutions;
	private ArrayList<Double> cost_solutions;
	private ArrayList<Date> date_solutions; 
	
	public Evaluation() {
		this.nb_while = 0;
		this.nb_solution = 0;
		this.nb_while_solutions = new ArrayList<Integer>();
		this.cost_solutions = new ArrayList<Double>();
		this.date_solutions = new ArrayList<Date>();
	}
	
	public void start(){
		this.beginning = new Date();
	}
	
	public void newWhile(){
		this.nb_while++;
	}
	
	public void gotASolution(Double costnewpath){
		this.nb_solution++;
		this.nb_while_solutions.add(nb_while);
		this.cost_solutions.add(costnewpath);
		this.date_solutions.add(new Date());
	}
	
	public int getNbWhile(){
		return nb_while;
	}
	
	public int getNbSolution(){
		return nb_solution;
	}
	
	public Date getBeginning(){
		return beginning;
	}
	
	public ArrayList<Integer> getNbWhileSolutions(){
		return nb_while_solutions;
	}
	
	public ArrayList<Double> getCostSolutions(){
		return cost_solutions;
	}
	
	public ArrayList<Date> getDateSolutions(){
		return date_solutions;
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
	 
	
}
