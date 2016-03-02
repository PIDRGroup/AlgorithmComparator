package model;

import java.io.Serializable;

public class Evaluation implements Serializable{
	private int nb_while; // nb de recherche
	private int first_solution; //nb de recherche avant de trouver une premiere solution
	private int best_solution_fine_while;//nb de recherche pour trouvé le meilleur noeud
	
	public Evaluation() {
		// TODO Auto-generated constructor stub
		nb_while = 0;
		first_solution = -1;
		best_solution_fine_while = 0;
	}
	
	public int getnb_while(){
		return this.nb_while;
	}
	
	public int getfirst_solution(){
		return this.first_solution;
	}
	
	public int getbest_solution(){
		return this.best_solution_fine_while;
	}
	
	public void new_while(){
		this.nb_while++;
	}
	
	public void gotasolution(){
		if (first_solution == -1){
			first_solution = nb_while;
		}
		
		best_solution_fine_while = nb_while;
		
	}
	
}
