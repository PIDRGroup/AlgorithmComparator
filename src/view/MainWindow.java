package view;

import javax.swing.JFrame;

public class MainWindow extends JFrame{
	
	private HomeView home;
	private CreationView creation;
	private ResultView results;
	
	public MainWindow(){
		home = new HomeView(this);
		creation = new CreationView(this);
		results = new ResultView(this);
		
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
	
	public void switchRes(){
		this.setTitle("AlgoComparator - Résultats");
		this.setContentPane(results);
		this.pack();
		this.validate();
	}
	
	public static void main(String[] args) {
		new MainWindow();
	}
}
