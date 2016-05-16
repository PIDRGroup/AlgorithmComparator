package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import model.*;
import model.env.*;
import model.algo.*;
import model.algo.Algorithm;

public class ConsoleView {
	private static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) throws UnknownPlaceException, IOException {
		boolean stop = false;
		String read;
		int retour;
		
		while(!stop){
			
			/* ********* On commence par créer l'environnement ********* */
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
												
					}catch(Exception e){
						e.printStackTrace();
						env = null;
					}
					
					if(env == null){
						System.err.println("Une erreur est survenue dans le chargement du fichier.");
					}
				}
				
				System.out.println("L'environnement a bien été chargé !\n");
				
			}else{
				//Création d'un environnement
				retour = -1;
				System.out.println("================== Création d'un environnement ==================");
				int nb_dim = getField("Nombre de dimensions", 2);
				System.out.print("  - Les dimensions sont-elles régulières ? [o-n] : ");
				String r = sc.nextLine();
				while (!r.equals("o") && !r.equals("n")){
					System.out.println();
					System.out.print("Mauvaise entrée. Entrez o (oui) ou n (non) : ");
					r = sc.nextLine();
				}
				
				int dimensions[] = new int[nb_dim];
				
				boolean regulier = r.equals("o");
				int nb_places;
				if(regulier){
					nb_places = getField("Nombre de places", 2);
					
					for(int i=0; i<nb_dim; i++){
						dimensions[i] = nb_places;
					}
				}else{
					
					for(int i=0; i<nb_dim; i++){
						nb_places = getField("Nombre de places de la dimension "+i, 2);
						dimensions[i] = nb_places;
					}
					
				}
				
				int dim_inf = getField("Borne inf des dimensions", -Integer.MAX_VALUE);
				int dim_sup = getField("Borne sup des dimensions", dim_inf);
				
				Seed s = new Seed(System.nanoTime(), dimensions, dim_inf, dim_sup);		
				env = new GridEnvironment(s);
								
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
						
			/* ********* Puis on crée les algorithmes ********* */
			ArrayList<Integer> selected_algos = multipleMenu("Sélection des algorithmes", "Dijkstra", "A*", "IDA*", "RBFS", "SMA*", "UCS");
			ArrayList<Algorithm> algos = new ArrayList<Algorithm>();
			
			for (int i = 0; i < selected_algos.size(); i++) {
				switch(selected_algos.get(i).intValue()){
					case 1:
						algos.add(new Dijkstra());
						break;
					case 2:
						algos.add(new AStar());
						break;
					case 3:
						algos.add(new IDAStar());
						break;
					case 4:
						algos.add(new RBFS());
						break;
					case 5:
						algos.add(new SMAStar());
						break;
					case 6:
						algos.add(new UniformCostSearch());
						break;
				}
			}
			
			/* ********* Maintenant, on lance l'expérience ********* */
			Calendar c = Calendar.getInstance();
			String date = c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR);
			Experience exp = new Experience("EXP du "+date, date, env);
			exp.addAlgos(algos);
			
			System.out.println("================== LANCEMENT DE L'EXPERIENCE ! ==================");
			exp.launch();
			
			System.out.print("Voulez-vous sauvegarder les logs ? [o-n] : ");
			read = "";
			while(!read.equals("o") && !read.equals("n")){
				read = sc.nextLine();
				if(!read.equals("o") && !read.equals("n"))
					System.err.println("Mauvaise entrée. Recommencez.");
			}
			
			if(read.equals("o")){
				System.out.print("Entrez le chemin du fichier à écrire : ");
				read = sc.nextLine();
				
				Log log;
				for(Algorithm algo : exp.getAlgos()){
					log = new Log(algo);
					log.write(read);
				}
			}
			
			/* ********* Enfin on demande à l'utilisateur s'il veut recommencer ********* */
			
			read = "";
			while(!read.equals("n") && !read.equals("o")){
				System.out.print("Voulez-vous recommencer une expérience ? [o/n] : ");
				read = sc.nextLine();
				
				if(!read.equals("n") && !read.equals("o")){
					System.err.println("Entrez o ou n !");
				}
			}
			stop = read.equals("n");
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
	
	public static ArrayList<Integer> multipleMenu(String title, String... options){
		boolean stop = false;
		ArrayList<Integer> vals = null;
		
		System.out.println("================== "+ title +" ==================");
		
		System.out.println("Ce menu est à choix multiple. Vous pouvez renseigner plusieurs choix séparés par des espaces.");
		
		while(!stop){
			int i;
			for (i = 0; i < options.length; i++) {
				System.out.println("   - "+(i+1)+" : "+options[i]);
			}
			System.out.print("\nVos choix [entre 1 à "+i+"] : ");
			
			String read = sc.nextLine();
			String[] parts = read.split(" ");
			vals = new ArrayList<Integer>();
			boolean err = false;
			
			for (int j = 0; j < parts.length; j++) {
				try{
					int val = Integer.parseInt(parts[j]);
					
					if(val < 1 || val > options.length){
						System.err.println("Entrez un entier entre 1 et "+i+" !");
						err = true;
						break;
					}else{
						vals.add(val);
					}
				}catch(Exception e){
					System.err.println("Il faut entrer un chiffre !");
					err = true;
					break; //On sort de la boucle
				}
			}
			
			stop = !err;
		}
		
		return vals;
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
