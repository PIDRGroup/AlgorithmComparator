package view;

import java.io.IOException;
import java.util.Scanner;

import model.*;
import model.env.*;

public class ConsoleView {
	private static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) throws UnknownPlaceException, IOException {
		boolean stop = false;
		String read;
		int retour;
		
		while(!stop){
			Environment env = null;
			
			retour = menu("Environnement", "Créer un nouvel environnement", "Charger un enrironnement");
							
			if(retour == 2){
				//Chargement d'un environnement
				retour = -1;
				System.out.println("================== Chargement d'un environnement ==================");
				String path;
				
				while(env == null){
					try{
						System.out.print("Chemin du fichier de la graine : ");
						path = sc.nextLine();
						System.out.println();
						
						Seed s = Seed.load(path);
						if(s.getType() == TypeSeed.GRID)
							env = new GridEnvironment(s);
						else if(s.getType() == TypeSeed.RAND)
							env = new RandomEnvironment(s);
						
					}catch(Exception e){
						e.printStackTrace();
						env = null;
					}
					
					if(env == null){
						System.err.println("Une erreur est survenue dans le chargement du fichier.");
					}
				}
				
				System.out.println("L'environnement a bien été généré !\n"+env.getSeed());
				
			}else{
				//Création d'un environnement
				retour = -1;
				System.out.println("================== Création d'un environnement ==================");
				int type = menu("Quel type de graphe désirez-vous créer ?", "Une grille", "Un graphe aléatoire");
				int nb_places = getField("Nombre de places", 2);
				int nb_dim = getField("Nombre de dimensions", 2);
				int dim_inf = getField("Borne inf des dimensions", -Integer.MAX_VALUE);
				int dim_sup = getField("Borne sup des dimensions", dim_inf);
				
				Seed s = new Seed(System.nanoTime(), nb_places, nb_dim, dim_inf, dim_sup);
				
				if(type == 1){
					env = new GridEnvironment(s);
				}else if(type == 2){
					env = new RandomEnvironment(s);
				}
				
				read="";
				while(!read.equals("n") && !read.equals("o")){
					System.out.print("Désirez-vous sauvegarder la graine de l'environnement ? [o-n] : ");
					read = sc.nextLine();
					if(!read.equals("n") && !read.equals("o")) System.out.println("Mauvaise entrée");
				}
				
				if(read.equals("o")){
					System.out.print("Entrez le chemin du fichier à créer : ");
					String path = sc.nextLine();
					if(!path.contains(".seed")) path+=".seed";
					s.save(path);
				}
			}
			
			System.out.print("Voulez-vous recommencer une expérience ? [o/n] : ");
			stop = sc.nextLine().equals("n");
		}
	}
		
	public static int getField(String field, int min){
		int ret = 0;
		boolean loop = true;
		
		while(loop){
			System.out.print("   - "+field+" : ");
			try{
				ret = Integer.parseInt(sc.nextLine());
				loop = false;
			}catch(Exception e){
				loop = true;
				System.err.println("Le champ a été mal renseigné.");
			}
			if(ret < min){
				loop = true;
				System.err.println("\nLe valeur entrée doit être supérieure à "+min);
			}
		}
		System.out.println();
		
		return ret;
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

	public static int menu(String title, String... options){
		int read = -1;
		System.out.println("================== "+ title +" ==================");
		while(read < 1 || read > options.length){
			int i;
			for (i = 0; i < options.length; i++) {
				System.out.println("   - "+(i+1)+" : "+options[i]);
			}
			System.out.print("\nVotre choix [de 1 à "+i+"] : ");
			try{
				read = Integer.parseInt(sc.nextLine());
			}catch(Exception e){
				System.err.println("Il faut entrer un chiffre !");
				read=-1;
			}
			
			if(read < 1 || read > options.length){
				System.err.println("Entrez un entier entre 1 et "+options.length+" !");
			}		
		}
		
		return read;
	}
	
	public static int submenu(String title, String... options){
		int read = -1;
		System.out.println("	*** "+ title +" ***");
		while(read < 1 || read > options.length){
			int i;
			for (i = 0; i < options.length; i++) {
				System.out.println("   - "+(i+1)+" : "+options[i]);
			}
			System.out.print("\n	Votre choix [de 1 à "+i+"] : ");
			try{
				read = Integer.parseInt(sc.nextLine());
			}catch(Exception e){
				System.err.println("	Il faut entrer un chiffre !");
				read=-1;
			}
			
			if(read < 1 || read > options.length){
				System.err.println("	Entrez un entier entre 1 et "+options.length+" !");
			}		
		}
		
		return read;
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
