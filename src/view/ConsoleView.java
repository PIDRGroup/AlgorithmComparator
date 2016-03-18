package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;

import model.*;

public class ConsoleView {
	private static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) throws UnknownPlaceException, IOException {
		boolean stop = false;
		String read;
		int conversion;
		
		while(!stop){
			conversion = -1;
			while(conversion == -1){
				menu("Environnement", "Charger un enrironnement", "Créer un nouvel environnement");
				read = sc.nextLine();
				conversion = convert(read, 2);
				if(conversion == -1) System.out.println("Entrez un entier entre 1 et 2");
			}
			
			if(conversion == 1){
				//Chargement d'un environnement
				
			}else{
				//Création d'un environnement
				conversion = -1;
				System.out.println("================== Création d'un environnement ==================");
				int nb_dim = getField("Nombre de dimensions");
				int dim_inf = getField("Borne inf des dimensions");
				int dim_sup = getField("Borne sup des dimensions");
				int nb_places = getField("Nombre de places");
				boolean grid = getDual("En grille ou en aléatoire ? [1 - 2]");
				//boolean oriented = getDual("Le graphe est-il orienté ? [1 = oui, 2 = non]");
				
				Environment env = null;
				Seed s = new Seed(System.nanoTime(), nb_places, nb_places, dim_inf, dim_sup);
				if(grid){
					System.out.println("Option non-implémentée");
				}else{
					env = EnvironmentManager.generateAleaFromSeed(s);
				}
				
				read="";
				while(!read.equals("n") && !read.equals("o")){
					System.out.print("Désirez-vous sauvegarder la graine de l'environnement ? [o-n] : ");
					read = sc.nextLine();
					if(!read.equals("n") && !read.equals("o")) System.out.println("Mauvaise entrée");
				}
				
				if(read.equals("o")){
					System.out.print("Entrez le chemin du fichier : ");
					s.save(sc.nextLine());
				}
			}
			
			System.out.print("Voulez-vous recommencer une expérience ? [o/n] : ");
			stop = sc.nextLine().equals("n");
			
		}
	}
		
	public static int getField(String field){
		int i = -1;
		
		while(i == -1){
			System.out.print("   - "+field+" : ");
			try{
				i = Integer.parseInt(sc.nextLine());
			}catch(Exception e){
				i=-1;
				System.err.println("Le champ a été mal renseigné.");
			}
		}
		System.out.println();
		
		if(i < 1) i = -1;
		
		return i;
	}
	
	public static boolean getDual(String field){
		int choice = -1;
		
		while(choice != 1 && choice != 2){
			System.out.print("   - "+field+" : ");
			try{
				choice = Integer.parseInt(sc.nextLine());
			}catch(Exception e){
				choice=-1;
				System.err.println("\nLe champ a été mal renseigné.");
			}
		}
		
		System.out.println();
		
		return choice == 1;
	}

	public static void menu(String title, String... options){
		System.out.println("================== "+ title +" ==================");
		int i;
		for (i = 0; i < options.length; i++) {
			System.out.println("   - "+(i+1)+" : "+options[i]);
		}
		System.out.print("\nVotre choix [de 1 à "+i+"] : ");
	}
	
	public static int convert(String s, int limit){
		int i;
		try{
			i = Integer.parseInt(s);
		}catch(Exception e){i = -1;}
		
		if(limit != -1 && (i < 1 || i > limit)) i =-1;
		
		return i;
	}
}
