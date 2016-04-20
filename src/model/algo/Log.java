package model.algo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

import model.env.Environment;

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
		
		this.lignes = this.name_algo.toUpperCase()+":\n";
		
		for (int i =0 ; i < eval.getNbSolution(); i++){
			lignes += "**"+(i+1)+"** dist="+nb_while_solutions.get(i)+", ";
			lignes += "t="+time_solutions.get(i)+ " ms, ";
			lignes += "nb_boucles="+cost_solutions.get(i)+"\n";
		}
	}
	
	public Log(Algorithm algo, Environment env){
		this.name_algo = algo.getName();
		this.eval = algo.getEval();
		
		ArrayList<Integer> nb_while_solutions = eval.getNbWhileSolutions();
		ArrayList<Double> cost_solutions = eval.getCostSolutions();
		ArrayList<Long> time_solutions = eval.getTimeSolutions();
		
		this.lignes = this.name_algo.toUpperCase()+":\n";
		this.lignes+="N="+env.size()+", dim="+env.nbDim()+"\n";
		
		for (int i =0 ; i < eval.getNbSolution(); i++){
			lignes += "**"+(i+1)+"** dist="+nb_while_solutions.get(i)+", ";
			lignes += "t="+time_solutions.get(i)+ " ms, ";
			lignes += "nb_boucles="+cost_solutions.get(i)+"\n";
		}
	}
	
	/**
	 * Rajoute le log à la fin du fichier dont le chemin est passé en paramètre.
	 * @param path : chemin
	 * @throws IOException 
	 */
	public void write(String path) throws IOException{
		Files.write(Paths.get(path), this.lignes.getBytes(), StandardOpenOption.APPEND);
	}
	
	@Override
	public String toString(){
		return lignes;
	}
}
