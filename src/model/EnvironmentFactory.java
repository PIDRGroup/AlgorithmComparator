package model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * Classe pour générer ou charger des environnements
 *
 */
public class EnvironmentFactory{
	
	/**
	 * Générateur de nombres pseudo-aléatoires pour les fonctions de génération.
	 */
	private static Random rand = new Random();
	
	
	//// Méthodes de chargement d'environnements à partir de fichiers ////
	
	
	/**
	 * Cette méthode permet de charger un environnement depuis un fichier.
	 * 
	 * Le fichier sera formaté de cette manière
	 * 
	 * x1,y1,z1 x2,y2,z2 ...
	 * 2 3
	 * 3 5
	 * 8 9 10
	 * ...
	 * 
	 * La première ligne définira les place et leurs positions. Les noeuds seront séparés par des espaces,
	 * et chaque coordonnées du noeud par une virgule.
	 * 
	 * La ligne suivante indique les indices des successeurs du premier noeuds, la suivante ceux du second, ...
	 * Si une place n'a pas de successeurs, alors la ligne correspondante sera vide.
	 * 
	 * @return
	 * @throws IOException 
	 * @throws MultiplePlaceException 
	 * @throws UnknownPlaceException 
	 */
	public Environment load(String path) throws IOException, MultiplePlaceException, UnknownPlaceException{
		Environment env = new Environment();
		
		InputStream ips=new FileInputStream(path); 
		InputStreamReader ipsr=new InputStreamReader(ips);
		BufferedReader br=new BufferedReader(ipsr);

		String line = null;
		ArrayList<Place> places = new ArrayList<Place>();
		
		//On crée l'ensemble des places
		if((line=br.readLine())!=null && line != "" && !line.isEmpty()){
			String[] str_places = line.split(" ");
			
			//On crée chaque place à partir de ses coordonnées
			for (int i = 0; i < str_places.length; i++) {
				String[] str_coordinates = str_places[i].split(",");
				int[] coordinates = new int[str_coordinates.length];
				
				for (int j = 0; j < str_coordinates.length; j++) {
					coordinates[i] = Integer.parseInt(str_coordinates[i]);
				}
				
				Place p = new Place(coordinates);
				env.addPlace(p);
			}
		}else{
			return null;
		}
			
		//Numéro de la place dont on définit les successeurs
		int index = 0;
		
		//tant qu'on tombe sur des lignes remplies, on crée des liens
		while ((line=br.readLine())!=null && line != "" && !line.isEmpty()){
			
			//Tableau des index des successeurs du noeud courant
			String[] str_links = line.split(" ");
			
			for (int i = 0; i < str_links.length; i++){
				//Index du successeur courant
				int succ = Integer.parseInt(str_links[i]);
				
				env.addLink(places.get(index), places.get(succ));
			}
			
			index++;
		}
		
		return env;
	}
	
	
	//// Fonctions de génération d'environnements pseudo-aléatoires ////
	
	
	/**
	 * 
	 * Génère une grille 2D selon une loi uniforme bornée.
	 * 
	 * @param bound_x Bornes inf et sup de x
	 * @param bound_y Bornes inf et sup de y
	 * @return
	 * @throws MultiplePlaceException 
	 * @throws UnknownPlaceException 
	 */
	public static Environment generateUniformGrid(int nb_places, Bound bound_x, Bound bound_y) throws MultiplePlaceException, UnknownPlaceException{
		Environment env = new Environment();

		//int width = bound_x.max() - bound_x.min();
		//int height = bound_y.max() - bound_y.min();
		
		return env;
	}
	
	/**
	 * Génère une grille carrée
	 * @param nb_places Nombre de places de la grille
	 * @param bounds_x_y Bornes inf et sup de la grille
	 * @return
	 * @throws MultiplePlaceException
	 * @throws UnknownPlaceException
	 */
	public static Environment generateUniformGrid(int nb_places, Bound bounds_x_y) throws MultiplePlaceException, UnknownPlaceException{
		return generateUniformGrid(nb_places, bounds_x_y, bounds_x_y);
	}
	
	/**
	 * Génère une grille carrée variant entre 0 et 600
	 * @param nb_places Nombre de places de la grille
	 * @return
	 * @throws MultiplePlaceException
	 * @throws UnknownPlaceException
	 */
	public static Environment generateUniformGrid(int nb_places) throws MultiplePlaceException, UnknownPlaceException{
		return generateUniformGrid(nb_places, new Bound(0, 600));
	}
	
	public static Environment generateAlea(int nb_places, Bound... bounds) throws MultiplePlaceException, UnknownPlaceException{
		Environment env = new Environment();
		int nb_dim = bounds.length;
		
		//On crée tout d'abord les places
		for (int i = 0; i < nb_places; i++) {
			int[] coord = new int[nb_dim];
			for(int j=0; j<nb_dim; j++){
				coord[j] = rand.nextInt(bounds[i].max() - bounds[i].min()) + bounds[i].min(); 
			}
			env.addPlace(new Place(coord));
		}
		
		//Puis on crée les liens : entre 1 et 10
		for (int i = 0; i < nb_places; i++){
			
			Place current = env.getPlace(i);
			int nb_links = rand.nextInt(9) + 1;
			
			for(int j=0; j<nb_links; j++){
				env.addLink(current, env.alea());
			}
		}
		
		return env;
	}
}
