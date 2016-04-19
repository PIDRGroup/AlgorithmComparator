package model.algo;

import java.io.*;
import java.util.*;

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
			lignes += "obtenu au bout de "+time_solutions.get(i)+ " ms ";
			lignes += "et apres "+cost_solutions.get(i)+" tours de boucle\n";
		}
	}
	
	/**
	 * Rajoute le log à la fin du fichier dont le chemin est passé en paramètre.
	 * @param path : chemin
	 * @throws IOException 
	 */
	public void write(String path) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path)));
		bw.newLine();
		bw.write(this.lignes);
		bw.close();
	}
	
	@Override
	public String toString(){
		return lignes;
	}
}
