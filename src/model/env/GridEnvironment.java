package model.env;

import java.awt.Dimension;
import java.util.ArrayList;

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
		int dist_between_2_points = (int) Math.ceil(size / (double) (s.nbPlaces()[0]-2));
		
		
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
		int[] coord = new int[s.getNbDim()];
		for (int i = 0; i < coord.length; i++) coord[i] = s.getDimMin();
		
		//On ajoute les places
		grow(s, dist_between_2_points, 0, coord, 0);
		
		//On ajoute les liens
		links(s.getNbDim());
	}
	
	public void links(int nb_dim) throws UnknownPlaceException{
		ArrayList<Place> places = graph.keyList();
		int[] size_dims = seed.nbPlaces();
		
		//Pour chaque place
		for (int i = 0; i < places.size(); i++) {
			//Pour chaque coordonn�e de la place, on chercher son successeur
			Place current = places.get(i);
			int ecart = 1;
			for (int j = 0; j < size_dims.length; j++) {
				if(i+ecart>=places.size())
					break;
				if((i + ecart) % size_dims[j] != 0 || ecart == size_dims[j]){
					Place neighbor = places.get(i + ecart);
					this.addLink(current, neighbor);
					this.addLink(neighbor, current);
				}
				ecart = ecart * size_dims[nb_dim- j - 1];
			}
		}
	}
	
	public int grow(Seed s, int distance, int current_dim, int[] coords, int linked) throws UnknownPlaceException{
		
		int n = s.nbPlaces()[current_dim];
		
		if(current_dim < s.getNbDim() - 1){
			for (int i = 0; i < n; i++) {
				linked=grow(s, distance, current_dim+1, coords, linked);
				coords[current_dim] += distance;
			}
			coords[current_dim] = s.getDimMin(); //on r�initialise la dimension courante
			return linked;
		}else{
			
			for (int i = 0; i < n; i++) {
				Place p = new Place(i, coords);
				graph.addKey(p);
//				linked++;
//				
//				int ecart = 0, j=0;
//				while((linked - ecart - 1) >= 0){
//					this.addLink(p, graph.keyList().get(linked - ecart - 1));
//					this.addLink(graph.keyList().get(linked - ecart - 1), p);
//					ecart = (ecart == 0) ? size_dims[s.getNbDim()- j - 1] : ecart * size_dims[s.getNbDim()- j - 1];
//					j++;
//				}
				
				coords[current_dim] += distance;
			}
			coords[current_dim] = s.getDimMin(); //on r�initialise la dimension courante
			
			return linked;
		}
		
//		Place current;
//		
//		/* Pour chaque dimensions, on cr�e le voisin si celui-ci est dans les bornes.
//		Pour chaque dimensions, on a au plus deux voisins (un avant et un apr�s)
//		Etant donn� qu'on part du coin avec les coordonn�es minorantes, on ne construit que le 
//		voisin d'apr�s, celui d'avant est d�j� cr��. Par contre on cr�e */
//		ArrayList<Place> to_visit_queue = new ArrayList<Place>();
//		
//		to_visit_queue.add(0, p);
//		
//		while(!to_visit_queue.isEmpty()){
//			
//			current = to_visit_queue.get(to_visit_queue.size()-1);
//			
//			//On commence par cr�er l'ensemble des voisins du point courant et les lier
//			for (int i = 0; i < current.getCoordinates().length; i++) {
//						
//				//Pour la coordonn�e courante, on va cr�er le voisin suivant et le pr�c�dent
//				int previous = current.getCoordinate(i) - distance;
//				int next = current.getCoordinate(i) + distance;
//				
//				//On v�rifie que les coordonn�es que l'on va cr�er font partie de l'environnement.
//				if(previous >= s.getDimMin()){
//					Place neigh = new Place(current.getCoordinates());
//					
//					//On change la seule coordonn�e variable de la place
//					neigh.setCoordinate(i, previous);
//					
//					//Si la place n'est pas dans le graphe, on l'y ajoute et on la visitera (on ne l'a pas vu s'il n'est pas dans le graphe)
//					if(graph.indexOf(neigh) == -1){
//						graph.addKey(neigh);
//						if(degre(neigh) < seed.getNbDim() * 2)
//							to_visit_queue.add(0, neigh);
//					}
//					
//					//On cr�e le lien p --> previous (le lien previous --> p est cr�� par previous)
//					this.addLink(current, neigh);
//				}
//				
//				if(next <= s.getDimMax()){
//					Place neigh = new Place(current.getCoordinates());
//					neigh.setCoordinate(i, next);
//					
//					//Si la place n'est pas dans le graphe, on l'y ajoute et on la visitera (on ne l'a pas vu s'il n'est pas dans le graphe)
//					if(graph.indexOf(neigh) == -1){
//						graph.addKey(neigh);
//						if(degre(neigh) < seed.getNbDim() * 2)
//							to_visit_queue.add(0, neigh);
//					}
//					
//					//On cr�e le lien p --> next (le lien next --> p est cr�� par next)
//					this.addLink(current, neigh);
//				}
//			}
//			
//			//On le supprime des noeuds � visiter
//			to_visit_queue.remove(to_visit_queue.size()-1);
//		}
	}
	
	public static void testGraphic() throws UnknownPlaceException{
		
		System.out.println("===== Test graphique de g�n�ration de grille =====");
		
		int size = 600;
		Seed seed = new Seed(System.nanoTime(), new int[]{10, 10}, 2, 0, size);
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
		
		//On fait varier le nombre de dimensions
		ArrayList<Integer> dims = new ArrayList<Integer>();
		for(int dim = 2; dim <= 3; dim++){
			//On fait varier le nombre de points
			while(dims.size() < dim) dims.add(0);
			
			for(int points = 10; points <= 100; points*=10){
				//On cr�e des grilles r�guli�res
				for (int i = 0; i < dim; i++)dims.set(i, points);
				
				Seed s = new Seed(System.nanoTime(), dims, dim, -6000, 12000);
				seeds.add(s);
			}
		}
		
		seeds.remove(seeds.size()-1);
		seeds.add(new Seed(System.nanoTime(), new int[]{100, 1000}, 2, -6000, 12000));
		
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
