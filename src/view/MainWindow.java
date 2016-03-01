package view;

import javax.swing.JFrame;

import model.Experience;

public class MainWindow extends JFrame{
	
	private HomeView home;
	private CreationView creation;
	private EvaluationView results;
	
	public MainWindow(){
		home = new HomeView(this);
		creation = new CreationView(this);
		
		this.setTitle("AlgoComparator - Accueil");
		this.setContentPane(home);
		this.pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void switchCreation(){
		this.setTitle("AlgoComparator - Création d'expérience");
		this.setContentPane(creation);
		this.pack();
		this.validate();
	}
	
	public void switchHome(){
		this.setTitle("AlgoComparator - Accueil");
		this.setContentPane(home);
		this.pack();
		this.validate();
	}
	
	public void switchRes(Experience exp){
		this.setTitle("AlgoComparator - Résultats");
		results = new EvaluationView(this, exp);
		this.setContentPane(results);
		this.pack();
		this.validate();
	}
	
	public static void main(String[] args) {
		new MainWindow();
	}
}
