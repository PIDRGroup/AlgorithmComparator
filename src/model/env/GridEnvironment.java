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
		
		
		//S'il y a un probl�me d'arrondi, on replace la borne max afin de supprimer l'espace en trop
		if(dist_between_2_points * s.getNbPlaces() != size){
			s.setDimMax(dist_between_2_points * (s.getNbPlaces()-1) + s.getDimMin());
		}
				
		/*
		 * Dans un hypercube, tous les points sont voisins des points qui ont exactement une coordonn�e diff�rente.
		 * Or ici, on connait la distance entre les points ainsi que les bornes de l'espace attribu�.
		 * On peut donc cr��r r�cursivement � partir du point initial. Chaque point va cr�er ses propres voisins et s'y lier.
		 * La condition d'arr�t correspond au d�passement de l'espace du probl�me.
		 */
		//On cr�e la place originelle
		ArrayList<Long> coordinates = new ArrayList<Long>();
		for (int i = 0; i < s.getNbDim(); i++) coordinates.add((long) s.getDimMin());
		Place origin = new Place(coordinates);
		
		graph.addKey(origin);
		grow(origin, s, dist_between_2_points);
	}
	
	public void grow(Place p, Seed s, int distance) throws UnknownPlaceException{
		
		Place current;
		
		/* Pour chaque dimensions, on cr�e le voisin si celui-ci est dans les bornes.
		Pour chaque dimensions, on a au plus deux voisins (un avant et un apr�s)
		Etant donn� qu'on part du coin avec les coordonn�es minorantes, on ne construit que le 
		voisin d'apr�s, celui d'avant est d�j� cr��. Par contre on cr�e */
		ArrayList<Place> to_visit_queue = new ArrayList<Place>();
		
		to_visit_queue.add(0, p);
		
		while(!to_visit_queue.isEmpty()){
			
			current = to_visit_queue.get(to_visit_queue.size()-1);
			
			//On commence par cr�er l'ensemble des voisins du point courant et les lier
			for (int i = 0; i < current.getCoordinates().length; i++) {
						
				//Pour la coordonn�e courante, on va cr�er le voisin suivant et le pr�c�dent
				int previous = current.getCoordinate(i) - distance;
				int next = current.getCoordinate(i) + distance;
				
				//On v�rifie que les coordonn�es que l'on va cr�er font partie de l'environnement.
				if(previous >= s.getDimMin()){
					Place neigh = new Place(current.getCoordinates());
					
					//On change la seule coordonn�e variable de la place
					neigh.setCoordinate(i, previous);
					
					//Si la place n'est pas dans le graphe, on l'y ajoute et on la visitera (on ne l'a pas vu s'il n'est pas dans le graphe)
					if(graph.indexOf(neigh) == -1){
						graph.addKey(neigh);
						if(degre(neigh) < seed.getNbDim() * 2)
							to_visit_queue.add(0, neigh);
					}
					
					//On cr�e le lien p --> previous (le lien previous --> p est cr�� par previous)
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
					
					//On cr�e le lien p --> next (le lien next --> p est cr�� par next)
					this.addLink(current, neigh);
				}
			}
			
			//On le supprime des noeuds � visiter
			to_visit_queue.remove(to_visit_queue.size()-1);
		}
	}
	
	public static void testGraphic() throws UnknownPlaceException{
		
		System.out.println("===== Test graphique de g�n�ration de grille =====");
		
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
		System.out.println("===== Test console de g�n�ration de grille =====");
		
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
