package model.env;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import view.PointCloud;

public class GridEnvironment extends Environment{
		
	public GridEnvironment() throws UnknownPlaceException{
		this(new Seed());
	}
	
	public GridEnvironment(Seed s) throws UnknownPlaceException{
		graph = new MyMap<Place, Place>();
		seed=s;
		
		seed.setType(TypeSeed.GRID);
		
		int size = s.getDimMax() - s.getDimMin();
		int dist_between_2_points = (int) Math.ceil(size / (double) (s.getNbPlaces()-2));
		
		
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
		ArrayList<Place> to_visit_queue = new ArrayList<Place>();
		
		to_visit_queue.add(0, p);
		
		while(!to_visit_queue.isEmpty()){
			
			current = to_visit_queue.get(to_visit_queue.size()-1);
			
			//On commence par créer l'ensemble des voisins du point courant et les lier
			for (int i = 0; i < current.getCoordinates().length; i++) {
						
				//Pour la coordonnée courante, on va créer le voisin suivant et le précédent
				int previous = current.getCoordinate(i) - distance;
				int next = current.getCoordinate(i) + distance;
				
				//On vérifie que les coordonnées que l'on va créer font partie de l'environnement.
				if(previous >= s.getDimMin()){
					Place neigh = new Place(current.getCoordinates());
					
					//On change la seule coordonnée variable de la place
					neigh.setCoordinate(i, previous);
					
					//Si la place n'est pas dans le graphe, on l'y ajoute et on la visitera (on ne l'a pas vu s'il n'est pas dans le graphe)
					if(graph.indexOf(neigh) == -1){
						graph.addKey(neigh);
						if(degre(neigh) < seed.getNbDim() * 2)
							to_visit_queue.add(0, neigh);
					}
					
					//On crée le lien p --> previous (le lien previous --> p est créé par previous)
					this.addLink(current, neigh);
				}
				
				if(next <= s.getDimMax()){
					Place neigh = new Place(current.getCoordinates());
					neigh.setCoordinate(i, next);
					
					//Si la place n'est pas dans le graphe, on l'y ajoute et on la visitera (on ne l'a pas vu s'il n'est pas dans le graphe)
					if(graph.indexOf(neigh) == -1){
						graph.addKey(neigh);
						if(degre(neigh) < seed.getNbDim() * 2)
							to_visit_queue.add(0, neigh);
					}
					
					//On crée le lien p --> next (le lien next --> p est créé par next)
					this.addLink(current, neigh);
				}
			}
			
			//On le supprime des noeuds à visiter
			to_visit_queue.remove(to_visit_queue.size()-1);
		}
	}
	
	public static void testGraphic() throws UnknownPlaceException{
		
		System.out.println("===== Test graphique de génération de grille =====");
		
		int size = 600;
		Seed seed = new Seed(System.nanoTime(), 100, 2, 0, size);
		GridEnvironment ge = new GridEnvironment(seed);
		System.out.println(ge.size());
		PointCloud pc = new PointCloud(ge, size, size);
		JFrame jf = new JFrame("Test grille");
		JScrollPane jsp = new JScrollPane(pc);
		jsp.setPreferredSize(new Dimension(600, 600));
		jf.setContentPane(jsp);
		jf.pack();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
		System.out.println(ge);
		System.out.println(ge.size());
		System.out.println(seed.getDimMax());
	}
	
	public static void testConsole() throws UnknownPlaceException{
		System.out.println("===== Test console de génération de grille =====");
		
		ArrayList<Seed> seeds = new ArrayList<Seed>();
		
		//Graines d'environnement en 2D
		seeds.add(new Seed(System.nanoTime(), 5, 2, 0, 6000));
		seeds.add(new Seed(System.nanoTime(), 10, 2, 0, 6000));
		seeds.add(new Seed(System.nanoTime(), 100, 2, 0, 605043021));
		//seeds.add(new Seed(System.nanoTime(), 1000, 2, 0, 100000));
		
		//Graines d'environnement en 3D
		seeds.add(new Seed(System.nanoTime(), 5, 3, 0, 6000));
		seeds.add(new Seed(System.nanoTime(), 10, 3, 0, 6000));
		//seeds.add(new Seed(System.nanoTime(), 100, 3, 0, 6000));
		
		//Graines d'environnement en 10D
		seeds.add(new Seed(System.nanoTime(), 5, 5, 0, 6000));
		
		long time;
		GridEnvironment ge;
		
		for(int i = 0; i < seeds.size(); i++){
			Seed s = seeds.get(i);
			
			time = System.currentTimeMillis();
			ge = new GridEnvironment(s);
			
			System.out.print("TEST "+(i+1)+" : " +ge.size()+" places en "+s.getNbDim()+" dimensions = ");
			System.out.print(((System.currentTimeMillis() - time))+" ms\n");
		}
	}
	
	public static void main(String[] args) {
		
		if(args.length == 0){
			try {
				testConsole();
				//testGraphic();
			} catch (UnknownPlaceException e) {
				e.printStackTrace();
			}
			System.exit(0);
		}
		
		switch (args[1]) {
			case "graphenv":
			try {
				testGraphic();
			} catch (UnknownPlaceException e1) {
				e1.printStackTrace();
			}
				break;
				
			case "consenv":
				try {
					testConsole();
				} catch (UnknownPlaceException e) {
					e.printStackTrace();
				}
				break;
	
			default:
				break;
		}
	}
}
