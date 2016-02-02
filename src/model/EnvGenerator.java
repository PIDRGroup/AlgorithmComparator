package model;

import java.util.Random;

/**
 * 
 * G�n�re des environnements pseudo-al�toirement en fonction des contraintes fournies.
 *
 */
public class EnvGenerator<E extends Number>{
	private static Random rand = new Random();
	
	/**
	 * G�n�re un environnement d'Integer pseudo-al�atoirement selon une loi normale
	 * 
	 * @param nb_places
	 * @return
	 * @throws MultiplePlaceException 
	 * @throws UnknownPlaceException 
	 */
	public static Environment<Integer> generateUniformGrid(int height, int width, int bound) throws MultiplePlaceException, UnknownPlaceException{
		Environment<Integer> env = new Environment<Integer>();
		
		int nb_places = height * width;
		
		for(int i=0; i<nb_places; i++){
			env.addPlace(i+"");
		}
		
		int val;
		int node;
		for(int i=0; i<nb_places; i++){

			if(i < width){
				//On est sur la premi�re ligne
				
				if(i == 0){
					//On est sur le premier coin
					env.addLink(i, i+1, rand.nextInt(bound));
					env.addLink(i, i+width, rand.nextInt(bound));
				}else if(i == width-1){
					//On est sur le second coin
					env.addLink(i, i-1, rand.nextInt(bound));
					env.addLink(i, i+width, rand.nextInt(bound));
				}else{
					//On est est entre les deux coins
					env.addLink(i, i+1, rand.nextInt(bound));
					env.addLink(i, i+width, rand.nextInt(bound));
					env.addLink(i, i-1, rand.nextInt(bound));
				}
				
			}else if(i>=nb_places-width-1){
				//On est sur la derni�re ligne
				
				if(i == 0){
					//On est sur le 3�me coin
					env.addLink(i, i+1, rand.nextInt(bound));
					env.addLink(i, i-width, rand.nextInt(bound));
				}else if(i == nb_places-1){
					//On est sur le quatri�me coin
					env.addLink(i, i-1, rand.nextInt(bound));
					env.addLink(i, i-width, rand.nextInt(bound));
				}else{
					//On est entre les deux coins
					env.addLink(i, i-1, rand.nextInt(bound));
					env.addLink(i, i+1, rand.nextInt(bound));
					env.addLink(i, i-width, rand.nextInt(bound));
				}
				
			}else if(i%(width-1) == 0){
				//On est sur la premi�re colonne
				
				env.addLink(i, i+1, rand.nextInt(bound));
				env.addLink(i, i+width, rand.nextInt(bound));
				env.addLink(i, i-width, rand.nextInt(bound));
				
			}else if(i%(width-1) == width-1){
				//On est sur la derni�re colonne
				
				env.addLink(i, i-1, rand.nextInt(bound));
				env.addLink(i, i+width, rand.nextInt(bound));
				env.addLink(i, i-width, rand.nextInt(bound));
				
			}else{
				//On se trouve dans tout le coeur
				
				env.addLink(i, i-1, rand.nextInt(bound));
				env.addLink(i, i+1, rand.nextInt(bound));
				env.addLink(i, i+width, rand.nextInt(bound));
				env.addLink(i, i-width, rand.nextInt(bound));
			}
			
		}
		return env;
	}
	
	public static Environment<Integer> generateUniformGrid(int edge, int bound) throws MultiplePlaceException, UnknownPlaceException{
		return generateUniformGrid(edge, edge, bound);
	}
	
	public static Environment<Integer> generateUniformGrid(int edge) throws MultiplePlaceException, UnknownPlaceException{
		return generateUniformGrid(edge, 100);
	}

}
