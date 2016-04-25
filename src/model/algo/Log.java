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
		
		ArrayList<Double> cost_solutions = eval.getCostSolutions();
		ArrayList<Long> time_solutions = eval.getTimeSolutions();
		ArrayList<Integer> premiere_visite_solutions = eval.getPremiereVisiteSolutions();
		ArrayList<Integer> visite_solutions = eval.getVisiteNoeudSolutions();
		ArrayList<Integer> noeud_envisage_solutions = eval.getNoeudEnvisageSolutions();
		
		this.lignes = this.name_algo.toUpperCase()+": ";
		this.lignes += "nb_total_1er_visite="+this.eval.getPremiereVisite()+", ";
		this.lignes += "nb_total_visite="+this.eval.getVisiteNoeud()+", ";
		this.lignes += "nb_total_noeud_env="+this.eval.getNoeudEnvisage()+"\n";
		
		for (int i =0 ; i < eval.getNbSolution(); i++){
			lignes += "**"+(i+1)+"** dist="+cost_solutions.get(i)+", ";
			lignes += "t="+time_solutions.get(i)+ " ms\n";
			lignes += "***** nb_1er_visite="+premiere_visite_solutions.get(i)+", ";
			lignes += "nb_visite="+visite_solutions.get(i)+", "; 
			lignes += "nb_noeud_env="+noeud_envisage_solutions.get(i)+"\n";
		}
	}
	
	public Log(Algorithm algo, Environment env){
		this.name_algo = algo.getName();
		this.eval = algo.getEval();
		
		ArrayList<Double> cost_solutions = eval.getCostSolutions();
		ArrayList<Long> time_solutions = eval.getTimeSolutions();
		ArrayList<Integer> premiere_visite_solutions = eval.getPremiereVisiteSolutions();
		ArrayList<Integer> visite_solutions = eval.getVisiteNoeudSolutions();
		ArrayList<Integer> noeud_envisage_solutions = eval.getNoeudEnvisageSolutions();
		
		this.lignes = this.name_algo.toUpperCase()+":\n";
		this.lignes += "nb_total_1er_visite="+this.eval.getPremiereVisite()+"\n";
		this.lignes += "nb_total_visite="+this.eval.getVisiteNoeud()+"\n";
		this.lignes += "nb_total_noeud_env="+this.eval.getNoeudEnvisage()+"\n";
		this.lignes+="N="+env.size()+", dim="+env.nbDim()+"\n";
		
		for (int i =0 ; i < eval.getNbSolution(); i++){
			lignes += "**"+(i+1)+"** dist="+cost_solutions.get(i)+", ";
			lignes += "t="+time_solutions.get(i)+ " ms\n";
			lignes += "***** nb_1er_visite="+premiere_visite_solutions.get(i)+", ";
			lignes += "nb_visite="+visite_solutions.get(i)+", "; 
			lignes += "nb_noeud_env="+noeud_envisage_solutions.get(i)+"\n";
		}
	}
	
	/**
	 * Rajoute le log � la fin du fichier dont le chemin est pass� en param�tre.
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
