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
		int conversion;
		
		while(!stop){
			Environment env = null;
			conversion = -1;
			
			while(conversion == -1){
				menu("Environnement", "Créer un nouvel environnement", "Charger un enrironnement");
				read = sc.nextLine();
				conversion = convert(read, 2);
				if(conversion == -1) System.out.println("Entrez un entier entre 1 et 2");
			}
			
			if(conversion == 2){
				//Chargement d'un environnement
				conversion = -1;
				System.out.println("================== Chargement d'un environnement ==================");
				System.out.print("Chemin du fichier de la graine : ");
				String path = sc.nextLine();
				
				while(env == null){
					try{
						System.out.print("Chemin du fichier de la graine : ");
						path = sc.nextLine();
						Seed s = Seed.load(path);
						if(s.getType() == TypeSeed.GRID)
							env = new GridEnvironment(s);
						else
							env = new RandomEnvironment(s);
						
					}catch(Exception e){
						e.printStackTrace();
						env = null;
					}
				}
				
				System.out.println("L'environnement a bien été généré !\n"+env.getSeed());
				
			}else{
				//Création d'un environnement
				conversion = -1;
				System.out.println("================== Création d'un environnement ==================");
				int nb_places = getField("Nombre de places", 2);
				int nb_dim = getField("Nombre de dimensions", 2);
				int dim_inf = getField("Borne inf des dimensions", -Integer.MAX_VALUE);
				int dim_sup = getField("Borne sup des dimensions", dim_inf);
				boolean grid = getDual("En grille ou en aléatoire ? [1 - 2]");
				
				Seed s = new Seed((grid)?TypeSeed.GRID : TypeSeed.RAND, System.nanoTime(), nb_places, nb_dim, dim_inf, dim_sup);
				
				if(grid){
					env = new GridEnvironment(s);
				}else{
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
