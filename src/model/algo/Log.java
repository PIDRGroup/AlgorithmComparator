package model.algo;

import java.util.ArrayList;
import java.util.Date;

public class Log {
	String name_algo;
	Evaluation eval;
	String lignes;
	
	public Log(Algorithm algo){
		this.name_algo = algo.getName();
		this.eval = algo.getEval();
		
		ArrayList<Integer> nb_while_solutions = eval.getNbWhileSolutions();
		ArrayList<Double> cost_solutions = eval.getCostSolutions();
		ArrayList<Long> time_solutions = eval.getTimeSolutions();
		
		this.lignes = this.name_algo+" a fournit "+ eval.getNbSolution()+" resultats:\n";
		for (int i =0 ; i < eval.getNbSolution(); i++){
			lignes += "**"+(i+1)+"** Solution de distance "+nb_while_solutions.get(i)+" ";
			lignes += "obtenu au bout de "+time_solutions.get(i)+ " mms";
			lignes += "et apres "+cost_solutions.get(i)+" tours de boucle\n";
		}
	}
	
	@Override
	public String toString(){
		return lignes;
	}
}
