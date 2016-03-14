package model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Classe pour g�n�rer ou charger des environnements
 *
 */
public class EnvironmentFactory{
	
	/**
	 * G�n�rateur de nombres pseudo-al�atoires pour les fonctions de g�n�ration.
	 */
	private static Random rand = new Random();
	
	
	//// M�thodes de chargement d'environnements � partir de fichiers ////
	
	public Environment generateFromSeed(Seed s) throws UnknownPlaceException{
		Environment env = new Environment();
		
		Place current;
		for (int i = 0; i < s.nbPlaces(); i++) {
			current = s.nextPlace();
			env.addPlace(current);
			env.addLink(current, s.nextLink(env.getPlaces()));
		}
		
		return env;
	}
	
	
	/**
	 * Cette m�thode permet de charger un environnement depuis un fichier.
	 * 
	 * Le fichier sera format� de cette mani�re
	 * 
	 * x1,y1,z1 x2,y2,z2 ...
	 * 2 3
	 * 3 5
	 * 8 9 10
	 * ...
	 * 
	 * La premi�re ligne d�finira les place et leurs positions. Les noeuds seront s�par�s par des espaces,
	 * et chaque coordonn�es du noeud par une virgule.
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
		
		//On cr�e l'ensemble des places
		if((line=br.readLine())!=null && line != "" && !line.isEmpty()){
			String[] str_places = line.split(" ");
			
			//On cr�e chaque place � partir de ses coordonn�es
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
			
		//Num�ro de la place dont on d�finit les successeurs
		int index = 0;
		
		//tant qu'on tombe sur des lignes remplies, on cr�e des liens
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
	
	
	//// Fonctions de g�n�ration d'environnements pseudo-al�atoires ////
	
	
	/**
	 * 
	 * G�n�re une grille 2D selon une loi uniforme born�e.
	 * 
	 * @param nb_places La racine carr�e du nombre de place � g�n�rer (10 = une grille de 10 * 10 places)
	 * @param bound_x Bornes inf et sup de x
	 * @param bound_y Bornes inf et sup de y
	 * @return
	 * @throws MultiplePlaceException 
	 * @throws UnknownPlaceException 
	 */
	public static Environment generateUniformGrid2D(int nb_places, Bound bound_x, Bound bound_y) throws MultiplePlaceException, UnknownPlaceException{
		Environment env = new Environment(new ArrayList<Bound>(Arrays.asList(new Bound[]{bound_x, bound_y})));
		
		int total = (int) Math.pow(nb_places, 2);

		int width = bound_x.max() - bound_x.min();
		int height = bound_y.max() - bound_y.min();
		
		int width_interval = width / nb_places;
		int height_interval = height / nb_places;
		
		//On g�re les probl�mes d'arrondis
		if(width_interval * nb_places != width){
			
			//On n'aura pas de places sur le width. On augmente la taille des limites.
			width = width_interval * nb_places;
			bound_x = new Bound(bound_x.min(), width);
		
		}
		
		if(height_interval * nb_places != height){
			
			//On n'aura pas de places sur le width. On augmente la taille des limites.
			height = height_interval * nb_places;
			bound_y = new Bound(bound_y.min(), height);
			
		}
		
		//On commence par cr�er le lot de places
		for (int i = 0; i <= width; i+=width_interval) {
			for (int j = 0; j <= height; j+=height_interval) {
				env.addPlace(new Place(i, j));
			}
		}
		
		//Puis on g�n�re les liaisons
		ArrayList<Place> places = env.getPlaces();
		for (int i = 0; i < places.size(); i++) {
			Place current = places.get(i);
			
			if (current.getCoordinate(1) == 0){
				//On est sur la premi�re ligne
				
				if(current.getCoordinate(1) == 0){
					//On est sur le coin sup�rieur gauche
					
					env.addLink(current, env.getPlace(0, height_interval));
					env.addLink(current, env.getPlace(width_interval, 0));
					
				}else if(current.getCoordinate(1) == width){
					//On est sur le coin sup�rieur droit
					
					env.addLink(current, env.getPlace(width - width_interval, 0));
					env.addLink(current, env.getPlace(width, height_interval));
					
				}else{
					//On est � l'int�rieur de la ligne
					
					env.addLink(current, env.getPlace(current.getCoordinate(0) - width_interval, 0));
					env.addLink(current, env.getPlace(current.getCoordinate(0) + width_interval, 0));
					env.addLink(current, env.getPlace(current.getCoordinate(0), height_interval));
					
				}
				
			}else if(current.getCoordinate(1) == height){
				//On est sur la derni�re ligne
				
				if(current.getCoordinate(0) == 0){
					//On est sur le coin inf�rieur gauche
					
					env.addLink(current, env.getPlace(0, height - height_interval));
					env.addLink(current, env.getPlace(width_interval, height));
					
				}else if(current.getCoordinate(0) == width){
					//On est sur le coin inf�rieur droit
					
					env.addLink(current, env.getPlace(width - width_interval, height));
					env.addLink(current, env.getPlace(width, height - height_interval));
					
				}else{
					//On est � l'int�rieur de la ligne
					
					env.addLink(current, env.getPlace(current.getCoordinate(0) - width_interval, height));
					env.addLink(current, env.getPlace(current.getCoordinate(0) + width_interval, height));
					env.addLink(current, env.getPlace(current.getCoordinate(0), height - height_interval));
					
				}
				
			}else if(current.getCoordinate(0) == 0){
				//On est sur la premi�re colonne
				
				env.addLink(current, env.getPlace(0, current.getCoordinate(1) - height_interval));
				env.addLink(current, env.getPlace(0, current.getCoordinate(1) + height_interval));
				env.addLink(current, env.getPlace(width_interval, current.getCoordinate(1)));
				
			}else if(current.getCoordinate(0) == width){
				//On est sur la derni�re colonne
				
				env.addLink(current, env.getPlace(width, current.getCoordinate(1) - height_interval));
				env.addLink(current, env.getPlace(width, current.getCoordinate(1) + height_interval));
				env.addLink(current, env.getPlace(width - width_interval, current.getCoordinate(1)));
				
			}else{
				//On est � l'int�rieur de la grille
				
				env.addLink(current, env.getPlace(current.getCoordinate(0), current.getCoordinate(1) - height_interval));
				env.addLink(current, env.getPlace(current.getCoordinate(0), current.getCoordinate(1) + height_interval));
				env.addLink(current, env.getPlace(current.getCoordinate(0) - width_interval, current.getCoordinate(1)));
				env.addLink(current, env.getPlace(current.getCoordinate(0) + width_interval, current.getCoordinate(1)));
				
			}
		}
		
		return env;
	}
	
	/**
	 * G�n�re une grille carr�e
	 * @param nb_places Nombre de places de la grille
	 * @param bounds_x_y Bornes inf et sup de la grille
	 * @return
	 * @throws MultiplePlaceException
	 * @throws UnknownPlaceException
	 */
	public static Environment generateUniformGrid2D(int nb_places, Bound bounds_x_y) throws MultiplePlaceException, UnknownPlaceException{
		return generateUniformGrid2D(nb_places, bounds_x_y, bounds_x_y);
	}
	
	/**
	 * G�n�re une grille carr�e variant entre 0 et 600
	 * @param nb_places Nombre de places de la grille
	 * @return
	 * @throws MultiplePlaceException
	 * @throws UnknownPlaceException
	 */
	public static Environment generateUniformGrid2D(int nb_places) throws MultiplePlaceException, UnknownPlaceException{
		return generateUniformGrid2D(nb_places, new Bound(0, 600));
	}
	
	public static Environment generateAlea(int nb_places, Bound... bounds) throws MultiplePlaceException, UnknownPlaceException{
		Environment env = new Environment(new ArrayList<Bound>(Arrays.asList(bounds)));
		int nb_dim = bounds.length;
				
		//On cr�e tout d'abord les places
		for (int i = 0; i < nb_places; i++) {
			int[] coord = new int[nb_dim];
			for(int j=0; j<nb_dim; j++){
				coord[j] = rand.nextInt(bounds[j].max() - bounds[j].min()) + bounds[j].min(); 
			}
			env.addPlace(new Place(coord));
		}
		
		//Puis on cr�e les liens : entre 1 et 10
		for (int i = 0; i < nb_places; i++){
			
			Place current = env.getPlace(i);
			int nb_links = rand.nextInt(4) + 1;
			
			for(int j=0; j<nb_links; j++){
				env.addLink(current, env.alea());
			}
		}
		
		return env;
	}
	
	public static Environment generateAlea(int nb_places, ArrayList<Bound> bounds) throws MultiplePlaceException, UnknownPlaceException{
		return generateAlea(nb_places, bounds.toArray(new Bound[1]));
	}
}
