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
		graph = new MyMap();
		seed=s;
		
		seed.setType(TypeSeed.GRID);
				
		/*
		 * Dans un hypercube, tous les points sont voisins des points qui ont exactement une coordonn�e diff�rente.
		 * Or ici, on connait la distance entre les points ainsi que les bornes de l'espace attribu�.
		 * On peut donc cr��r r�cursivement � partir du point initial. Chaque point va cr�er ses propres voisins et s'y lier.
		 * La condition d'arr�t correspond au d�passement de l'espace du probl�me.
		 */
		//On cr�e la place originelle
		int[] coord = new int[s.getNbDim()];
		for (int i = 0; i < coord.length; i++) coord[i] = s.getDimMin();
		
		/*
		 * On va g�n�rer le graphe par couche. 
		 * On va commencer par cr�er la premi�re ligne du graphe (graphe 1D).
		 * A partir de cette base, on va modifier ses coordonn�es et on va les associer afin de cr�er un graphe en 2D.
		 * Puis on va dupliquer le graphe 2D en modifiant les coordonn�es des points, et on va les associer pour cr�er le graphe 
		 * en 3D.
		 * Et on r�it�re jusqu'� N dimensions.
		 */
		
		graph = new MyMap();
		graph.addKey(new Place(0, coord));
		
		graph = grow(s, graph, 0);
	}
	
	/**
	 * 
	 * M�thode construisant le graphe par "couche". On cr�e la dimension N en combinant de multiples r�plique du graphe N-1.
	 * Pour chacune de ces r�pliques, on fera "glisser" la coordonn�e de la dimension que l'on construitpuis on les combinera ensemble.
	 * 
	 * Explications d�taill�es : 
	 * 
	 * L'algorithme commencera � la dimension 0 (un seul point de coordonn�es [inf, inf, ..., inf]
	 * 
	 * On commencera par construire la dimension 1 en r�pliquant ce point X fois (X �tant le nombre de points de la dimension 1),
	 * et on fera "glisser" sa premi�re coordonn�e (le point 2 aura [inf + dist1, inf, inf, ...], le point 3 [inf + 2 * dist1, inf, ...], ...)
	 * afin d'obtenir tous les points de cette dimension. Puis on liera ensemble tous les points qui sont voisins sur cette derni�re coordonn�e
	 * 
	 * Pour construire la dimension 2, on va r�pliquer le graphe Y fois (Y �tant le nombre de points de la dimension 2),
	 * et on va faire "glisser" la seconde coordonn�e de tous les points de toutes les lignes (le point 2 + X aura [inf + dist1, inf+dist2, inf, ...],
	 * le point 3 + X aura [inf + 2 * dist1, inf + 2 * dist2, inf, ...], ...) afin d'obtenir toutes les lignes dela dimension.
	 * Puis on liera ensemble toutes les lignes en reliant les points qui sont voisins sur cette derni�re coordonn�e.
	 * 
	 * Pour construire la dimension 3, on va r�pliquer les grilles Z fois (Z �tant le nombre de points de la dimension 3),
	 * et on va faire "glisser" la troisi�me coordonn�es de tous les points de toutes les grilles.
	 * (le point 2 + X + Y aura [inf + dist1, inf + dist2, inf + dist3, inf, ...], le point 3 + X + Y aura [inf + 2 * dist1, inf + 2 * dist2,
	 * inf + 2 * dist3, inf, ...], ...) afin d'botenir toutes grilles du cube.
	 * Puis on liera ensemble toutes les lignes en reliant tous les points de toutes les grilles qui sont voisins sur cette derni�re coordon�e.
	 * 
	 * Puis on fait de la r�currence sur ce principe jusqu'� la dimension N.
	 * 
	 * @param s Seed permettant de r�cup�rer les donn�es sur le nombre de dimensiosn et le nombre de points de chaque dimension.
	 * @param graphcet graph que l'on fait �voluer
	 * @param current_dim Dimension que l'on est en train de construire
	 */
	public static MyMap grow(Seed s, MyMap graphcet, int current_dim){
		
		//On s'arr�te si on est arriv� � la derni�re dimension
		if(current_dim == s.getNbDim())
			return graphcet;
		
		//Sinon, on cr�e la dimension courante
		
			//On commence par calculer la distance entre les points sur la dimension courante
			int dist = computeDistance(s, current_dim);
			int nb_places_dim = s.nbPlaces()[current_dim];
			
			//On cr�e autant de copies que n�cessaire du graphe
			ArrayList<MyMap> duplications = graphcet.duplicate(nb_places_dim);
			
			//On "glisse" les coordonn�es
			for (int i = 1; i < nb_places_dim; i++) {
				duplications.get(i).addToAllIndexes(i * duplications.get(i).getNbElements());
				slip(duplications.get(i), current_dim, i, dist);
			}

			//Maintenant, on fusionne tous les graphes que l'on a
			MyMap temp = blend(duplications);
			
//				for (int i = 0; i < nb_places_dim; i++) {
//					MyMap dup = duplications.get(i);
//					for (int j = 0; j < dup.keyList().size(); j++) {
//						System.out.print(dup.keyList().get(j).getIndex()+" : ");
//						for (int j2 = 0; j2 < dup.valueList(j).size(); j2++) {
//							System.out.print(dup.valueList(j).get(j2)+"  ");
//						}
//						System.out.println();
//					}
//				}
								
		//Puis on fait de la r�currence
		return grow(s, temp, current_dim + 1);
	}
	
	/**
	 * Fonction fusionnant plusieurs graphes r�guliers en un seul.
	 * Ex.: Si on lui envoie deux lignes de trois places, elle renverra une grille 2*3
	 * 		Si on lui envoie trois grilles de deux lignes de 5 points, elle renverra un cube 5*2*3
	 * 
	 * La fonction va aussi s'occuper de modifier les indices des places et mettra � jour les listes 
	 * d'adjacence avec ces nouveau indices. 
	 * 
	 * Enfin, elle cr�era des liens pour relier toutes les parties des graphes.
	 * 
	 * NOTE : Les coordonn�es doivent �tre d�plac�es au pr�alable par slip.
	 * NOTE : les indices dans les graphes pass�s en param�tre sont modifi�s
	 * 
	 * @param list_graphs Liste des graphes � fusionner
	 * @return
	 */
	public static MyMap blend(ArrayList<MyMap> list_graphs){
		
		/*
		 * On cr�e les liens : 
		 * 		- les graphes deux graphes extremums cr�ent un seul lien pour chaque point vers l'int�rieur du nouveau graphe.
		 * 		- les graphes internes cr�ent deux liens pour chaque point vers les graphes pr�c�dents et les graphes suivants
		 */
		for (int i = 0; i < list_graphs.size(); i++){
			
			MyMap current = list_graphs.get(i);
			
			//Si on est sur les graphes extremums
			if(i == 0 || i == list_graphs.size() - 1){
				
				//Graphe auquel on veut se connecter. Le deuxi�me si on se trouve sur le premier, et l'avant-dernier si on se trouve sur le dernier.
				MyMap to_connect;
				
				//Si on est sur le premier graphe 
				if(i == 0){
					to_connect = list_graphs.get(1);
				}else{ //Si on est sur le dernier graphe
					to_connect = list_graphs.get(list_graphs.size() - 2);
				}
				
				current.link(to_connect);
				
			}else{ //Autrement on est sur les graphes internes
				MyMap to_connect_previous = list_graphs.get(i - 1);
				MyMap to_connect_next = list_graphs.get(i + 1);
				
				current.link(to_connect_previous);
				current.link(to_connect_next);
			}
		}
		
		//Maintenant que toutes les places ont �t� r�index�es et tous les liens cr��s, on cr�e le nouvel objet final
		MyMap new_graph = new MyMap();
		
		for (int i = 0; i < list_graphs.size(); i++) {
			MyMap current = list_graphs.get(i);
			new_graph.keyList().addAll(current.keyList());
			new_graph.valueLists().addAll(current.valueLists());		
		}
		
		return new_graph;
	}
	
	/**
	 * Fonction permettant de d�caler un graphe sur une coordon�e sp�cifique.
	 * 
	 * NOTE : cette fonction ne s'occupe de changer les index dans la liste d'adjacence. Cela sera fait dans la fusion des graphes.
	 * 
	 * @param current_dim La dimension que l'on cr�e actuellement (et donc coordonn�es � modifier)
	 * @param current_dupplication N� de la copie (et donc nombre de d�calage qu'il faut r�aliser pour placer le graphe)
	 * @param distance_between_two_points Distance entre deux points (valeur unitaire d'un d�calage)
	 * 
	 */
	public static void slip(MyMap graph, int current_dim, int current_dupplication, int distance_between_two_points){
		//On parcourt toutes les places du graphe et on les d�cale
		ArrayList<Place> places = graph.keyList();
		
		for (int i = 0; i < places.size(); i++) {
			Place current = places.get(i);
			int current_coord = current.getCoordinate(current_dim);
			int new_value = current_coord + current_dupplication * distance_between_two_points;
			current.setCoordinate(current_dim, new_value);
		}
	}
	
	/**
	 * Calcule la distance entre deux points. Si jamais il y a un probl�me d'arrondi, 
	 * les bornes de la graine sont modifi�es.
	 * 
	 * @param s Graine
	 * @param current_dim Dimension courante dont on veut calculer la distance entre les points
	 * @return Distance entre les points
	 */
	public static int computeDistance(Seed s, int current_dim){
		int size = s.getDimMax() - s.getDimMin();
		int dist_between_2_points = (int) Math.ceil(size / (double) (s.nbPlaces()[current_dim]-2));
		
		return dist_between_2_points;
	}
	
	
//	public int grow(Seed s, int dist_between_two_points, int current_dim, int[] coords, int linked){
//		
//		int nb_places_in_current_dim = s.nbPlaces()[current_dim];
//		
//		if(current_dim < s.getNbDim() - 1){
//			for (int i = 0; i < nb_places_in_current_dim; i++) {
//				linked = grow(s, dist_between_two_points, current_dim+1, coords, linked);
//				coords[current_dim] += dist_between_two_points;
//			}
//			coords[current_dim] = s.getDimMin(); //on r�initialise la dimension courante
//			return linked;
//		}else{
//			
//			//on cr�e tous les points de la dimension actuelle
//			for (int i = 0; i < nb_places_in_current_dim; i++) {
//				Place p = new Place(i, coords);
//				graph.addKey(p);
//				linked++;
//				
//				int ecart = 0, j=0;
//				while((linked - ecart - 1) >= 0){
//					this.addLink(p.getIndex(), graph.keyList().get(linked - ecart - 1).getIndex());
//					this.addLink(graph.keyList().get(linked - ecart - 1).getIndex(), p.getIndex());
//					ecart = (ecart == 0) ? s.nbPlaces()[s.getNbDim()- j - 1] : ecart * s.nbPlaces()[s.getNbDim()- j - 1];
//					j++; 
//				}
//				coords[current_dim] += dist_between_two_points;
//			}
//			coords[current_dim] = s.getDimMin(); //on r�initialise la dimension courante
//			return linked;
//		}
//		
//	}
	
//	public void links() throws UnknownPlaceException{
//		ArrayList<Place> places = graph.keyList();
//		int[] size_dims = seed.nbPlaces();
//		
//		//On parcourt toutes les places
//		for (int i = 0; i < places.size(); i++) {
//			//On r�cup�re la place courante
//			Place current = places.get(i);
//			int[] coordinates = current.getCoordinates();
//			
//			//Pour chaque dimension, on prend le pr�d�cesseur et le successeur
//			for (int j = 0; j < coordinates.length; j++) {
//				int prev = i - size_dims[j];
//				int succ = i + size_dims[j];
//				
//				if()
//					this.addLink(i, prev);
//
//				if()
//					this.addLink(i, succ);
//			}
//		}
//		
//		//Pour chaque place
////		for (int i = 0; i < places.size(); i++) {
////			//Pour chaque coordonn�e de la place, on chercher son successeur
////			Place current = places.get(i);
////			int ecart = 1;
////			for (int j = 0; j < size_dims.length; j++) {
////				if(i+ecart>=places.size())
////					break;
////				if((i + ecart) % size_dims[j] != 0 || ecart == size_dims[j]){
////					Place neighbor = places.get(i + ecart);
////					this.addLink(current, neighbor);
////					this.addLink(neighbor, current);
////				}
////				ecart = ecart * size_dims[nb_dim- j - 1];
////			}
////		}
//	}
	
//	public int grow(Seed s, int distance, int current_dim, int[] coords, int linked) throws UnknownPlaceException{
//		
//		int n = s.nbPlaces()[current_dim];
//		
//		if(current_dim < s.getNbDim() - 1){
//			for (int i = 0; i < n; i++) {
//				linked = grow(s, distance, current_dim+1, coords, linked);
//				coords[current_dim] += distance;
//			}
//			coords[current_dim] = s.getDimMin(); //on r�initialise la dimension courante
//			return linked;
//		}else{
//			
//			for (int i = 0; i < n; i++) {
//				Place p = new Place(i, coords);
//				graph.addKey(p);
//				linked++;
//				
//				int ecart = 0, j=0;
//				while((linked - ecart - 1) >= 0){
//					this.addLink(p.getIndex(), graph.keyList().get(linked - ecart - 1).getIndex());
//					this.addLink(graph.keyList().get(linked - ecart - 1).getIndex(), p.getIndex());
//					ecart = (ecart == 0) ? s.nbPlaces()[s.getNbDim()- j - 1] : ecart * s.nbPlaces()[s.getNbDim()- j - 1];
//					j++;
//				}
//				coords[current_dim] += distance;
//			}
//			coords[current_dim] = s.getDimMin(); //on r�initialise la dimension courante
//			
//			return linked;
//		}
		
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
//	}
	
	public static void testGraphic() throws UnknownPlaceException{
		
		System.out.println("===== Test graphique de g�n�ration de grille =====");
		
		int size = 600;
		Seed seed = new Seed(System.nanoTime(), new int[]{10, 10}, 0, size);
		GridEnvironment ge = new GridEnvironment(seed);
		PointCloud pc = new PointCloud(ge, size, size);
		JFrame jf = new JFrame("Test grille");
		JScrollPane jsp = new JScrollPane(pc);
		jsp.setPreferredSize(new Dimension(600, 600));
		jf.setContentPane(jsp);
		jf.pack();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
		
		for (int i = 0; i < ge.graph.valueLists().size(); i++) {
			ArrayList<Integer> list = ge.graph.valueList(i);
			System.out.print(ge.graph.keyList().get(i).getIndex()+" : ");
			for (int j = 0; j < list.size(); j++) {
				System.out.print(list.get(j)+", ");
			}
			System.out.println();
		}
		
	}
	
	public static void testConsole() throws UnknownPlaceException{
		System.out.println("===== Test console de g�n�ration de grille =====");
		
		ArrayList<Seed> seeds = new ArrayList<Seed>();
		
		//On cr�e des environnements 2D
		seeds.add(new Seed(System.nanoTime(), new int[]{10, 10}, -6000, 12000)); //100
		seeds.add(new Seed(System.nanoTime(), new int[]{10, 100}, -6000, 12000)); //1000
		seeds.add(new Seed(System.nanoTime(), new int[]{100, 100}, -6000, 12000)); //10 000
		seeds.add(new Seed(System.nanoTime(), new int[]{100, 1000}, -6000, 12000)); //100 000
		seeds.add(new Seed(System.nanoTime(), new int[]{1000, 1000}, -6000, 12000)); //1 000 000
		seeds.add(new Seed(System.nanoTime(), new int[]{1000, 10000}, -6000, 12000)); //10 000 000
		
		//Puis des environnements 3D
		seeds.add(new Seed(System.nanoTime(), new int[]{10, 10, 10}, -6000, 12000)); //1000
		seeds.add(new Seed(System.nanoTime(), new int[]{10, 10, 100}, -6000, 12000)); //10 000
		seeds.add(new Seed(System.nanoTime(), new int[]{10, 100, 100}, -6000, 12000)); //100 000
		seeds.add(new Seed(System.nanoTime(), new int[]{100, 100, 100}, -6000, 12000)); //1 000 000
		seeds.add(new Seed(System.nanoTime(), new int[]{100, 1000, 100}, -6000, 12000)); //10 000 000
		
		//Et enfin quelques 4D
		seeds.add(new Seed(System.nanoTime(), new int[]{10, 10, 10, 10}, -6000, 12000)); //10 000
		seeds.add(new Seed(System.nanoTime(), new int[]{100, 10, 10, 10}, -6000, 12000)); //100 000
		seeds.add(new Seed(System.nanoTime(), new int[]{10, 100, 10, 100}, -6000, 12000)); //1 000 000
		seeds.add(new Seed(System.nanoTime(), new int[]{10, 100, 100, 100}, -6000, 12000)); //10 000 000
		seeds.add(new Seed(System.nanoTime(), new int[]{100, 100, 100, 100}, -6000, 12000)); //100 000 000
		seeds.add(new Seed(System.nanoTime(), new int[]{100, 1000, 100, 100}, -6000, 12000)); //1 000 000 000
		
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
