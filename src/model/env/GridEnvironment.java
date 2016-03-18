package model.env;

import java.util.ArrayList;

import javax.swing.JFrame;

import view.PointCloud;

public class GridEnvironment extends Environment{
		
	public GridEnvironment() throws UnknownPlaceException{
		this(new Seed());
	}
	
	public GridEnvironment(Seed s) throws UnknownPlaceException{
		graph = new MyMap<Place, Place>();
		seed=s;
		int size = s.getDimMax() - s.getDimMin();
		int dist_between_2_points = size / s.getNbPlaces();
		
		//S'il y a un problème d'arrondi, on replace la borne max afin de supprimer l'espace en trop
		if(dist_between_2_points * s.getNbPlaces() != size){
			s.setDimMax(dist_between_2_points * (s.getNbPlaces()-1) + s.getDimMin());
		}
		
		/*
		 * Dans un hypercube, tous les points sont voisins des points qui ont exactement une coordonnée différente.
		 * Or ici, on connait la distance entre les points ainsi que les bornes de l'espace attribué.
		 * On peut donc créér récursivement à partir du point initial. Chaque point va créer ses propres voisins et s'y lier.
		 * La condition d'arrêt correspond au dépassement de l'espace du problème.
		 */
		//On crée la place originelle
		ArrayList<Long> coordinates = new ArrayList<Long>();
		for (int i = 0; i < s.getNbDim(); i++) coordinates.add((long) s.getDimMin());
		Place origin = new Place(coordinates);
		
		graph.addKey(origin);
		grow(origin, s, dist_between_2_points);
	}
	
	public void grow(Place p, Seed s, int distance) throws UnknownPlaceException{
		
		Place current;
		
		/* Pour chaque dimensions, on crée le voisin si celui-ci est dans les bornes.
		Pour chaque dimensions, on a au plus deux voisins (un avant et un après)
		Etant donné qu'on part du coin avec les coordonnées minorantes, on ne construit que le 
		voisin d'après, celui d'avant est déjà créé. Par contre on crée */
		ArrayList<Place> new_neighbors;
		
		for 
			new_neighbors = new ArrayList<Place>();
			//On commence par créer l'ensemble des voisins du point courant et les lier
			for (int i = 0; i < p.getCoordinates().length; i++) {
						
				//On vérifie que la coordonnée que l'on va créer fait partie de l'environnement.
				int previous = p.getCoordinate(i) - distance;
				int next = p.getCoordinate(i) + distance;
				
				if(previous >= s.getDimMin()){
					Place neigh = new Place(p.getCoordinates());
					neigh.setCoordinate(i, previous);
					
					//On vérifie si la place existe déjà dans le graph
					if(graph.indexOf(neigh) == -1){
						graph.addKey(neigh);
						new_neighbors.add(neigh);
					}
					
					//On crée le lien p --> previous (le lien previous --> p est créé par previous)
					this.addLink(p, neigh);
				}
				
				if(next <= s.getDimMax()){
					Place neigh = new Place(p.getCoordinates());
					neigh.setCoordinate(i, next);
					
					//On vérifie si la place existe déjà dans le graph
					if(graph.indexOf(neigh) == -1){
						graph.addKey(neigh);
						new_neighbors.add(neigh);
					}
					
					//On crée le lien p --> next (le lien next --> p est créé par next)
					this.addLink(p, neigh);
				}
			}
	}
	
	public static void main(String[] args) {
		Seed seed = new Seed(TypeSeed.GRID, System.nanoTime(), 12, 2, 0, 600);
		try {
			GridEnvironment ge = new GridEnvironment(seed);
			PointCloud pc = new PointCloud(ge, 600, 600);
			JFrame jf = new JFrame("Test grille");
			jf.setContentPane(pc);
			jf.pack();
			jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jf.setVisible(true);
			System.out.println(ge);
		} catch (UnknownPlaceException e) {
			e.printStackTrace();
		}
	}
}
