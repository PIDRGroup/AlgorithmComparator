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
		 * Dans un hypercube, tous les points sont voisins des points qui ont exactement une coordonnée différente.
		 * Or ici, on connait la distance entre les points ainsi que les bornes de l'espace attribué.
		 * On peut donc créér récursivement à partir du point initial. Chaque point va créer ses propres voisins et s'y lier.
		 * La condition d'arrêt correspond au dépassement de l'espace du problème.
		 */
		//On crée la place originelle
		int[] coord = new int[s.getNbDim()];
		for (int i = 0; i < coord.length; i++) coord[i] = s.getDimMin();
		
		/*
		 * On va générer le graphe par couche. 
		 * On va commencer par créer la première ligne du graphe (graphe 1D).
		 * A partir de cette base, on va modifier ses coordonnées et on va les associer afin de créer un graphe en 2D.
		 * Puis on va dupliquer le graphe 2D en modifiant les coordonnées des points, et on va les associer pour créer le graphe 
		 * en 3D.
		 * Et on réitère jusqu'à N dimensions.
		 */
		
		graph = new MyMap();
		graph.addKey(new Place(0, coord));
		
		graph = grow(s, graph, 0);
	}
	
	/**
	 * 
	 * Méthode construisant le graphe par "couche". On crée la dimension N en combinant de multiples réplique du graphe N-1.
	 * Pour chacune de ces répliques, on fera "glisser" la coordonnée de la dimension que l'on construitpuis on les combinera ensemble.
	 * 
	 * Explications détaillées : 
	 * 
	 * L'algorithme commencera à la dimension 0 (un seul point de coordonnées [inf, inf, ..., inf]
	 * 
	 * On commencera par construire la dimension 1 en répliquant ce point X fois (X étant le nombre de points de la dimension 1),
	 * et on fera "glisser" sa première coordonnée (le point 2 aura [inf + dist1, inf, inf, ...], le point 3 [inf + 2 * dist1, inf, ...], ...)
	 * afin d'obtenir tous les points de cette dimension. Puis on liera ensemble tous les points qui sont voisins sur cette dernière coordonnée
	 * 
	 * Pour construire la dimension 2, on va répliquer le graphe Y fois (Y étant le nombre de points de la dimension 2),
	 * et on va faire "glisser" la seconde coordonnée de tous les points de toutes les lignes (le point 2 + X aura [inf + dist1, inf+dist2, inf, ...],
	 * le point 3 + X aura [inf + 2 * dist1, inf + 2 * dist2, inf, ...], ...) afin d'obtenir toutes les lignes dela dimension.
	 * Puis on liera ensemble toutes les lignes en reliant les points qui sont voisins sur cette dernière coordonnée.
	 * 
	 * Pour construire la dimension 3, on va répliquer les grilles Z fois (Z étant le nombre de points de la dimension 3),
	 * et on va faire "glisser" la troisième coordonnées de tous les points de toutes les grilles.
	 * (le point 2 + X + Y aura [inf + dist1, inf + dist2, inf + dist3, inf, ...], le point 3 + X + Y aura [inf + 2 * dist1, inf + 2 * dist2,
	 * inf + 2 * dist3, inf, ...], ...) afin d'botenir toutes grilles du cube.
	 * Puis on liera ensemble toutes les lignes en reliant tous les points de toutes les grilles qui sont voisins sur cette dernière coordonée.
	 * 
	 * Puis on fait de la récurrence sur ce principe jusqu'à la dimension N.
	 * 
	 * @param s Seed permettant de récupérer les données sur le nombre de dimensiosn et le nombre de points de chaque dimension.
	 * @param graphcet graph que l'on fait évoluer
	 * @param current_dim Dimension que l'on est en train de construire
	 */
	public static MyMap grow(Seed s, MyMap graphcet, int current_dim){
		
		//On s'arrête si on est arrivé à la dernière dimension
		if(current_dim == s.getNbDim())
			return graphcet;
		
		//Sinon, on crée la dimension courante
		
			//On commence par calculer la distance entre les points sur la dimension courante
			int dist = computeDistance(s, current_dim);
			int nb_places_dim = s.nbPlaces()[current_dim];
			
			//On crée autant de copies que nécessaire du graphe
			ArrayList<MyMap> duplications = graphcet.duplicate(nb_places_dim);
			
			//On "glisse" les coordonnées
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
								
		//Puis on fait de la récurrence
		return grow(s, temp, current_dim + 1);
	}
	
	/**
	 * Fonction fusionnant plusieurs graphes réguliers en un seul.
	 * Ex.: Si on lui envoie deux lignes de trois places, elle renverra une grille 2*3
	 * 		Si on lui envoie trois grilles de deux lignes de 5 points, elle renverra un cube 5*2*3
	 * 
	 * La fonction va aussi s'occuper de modifier les indices des places et mettra à jour les listes 
	 * d'adjacence avec ces nouveau indices. 
	 * 
	 * Enfin, elle créera des liens pour relier toutes les parties des graphes.
	 * 
	 * NOTE : Les coordonnées doivent être déplacées au préalable par slip.
	 * NOTE : les indices dans les graphes passés en paramètre sont modifiés
	 * 
	 * @param list_graphs Liste des graphes à fusionner
	 * @return
	 */
	public static MyMap blend(ArrayList<MyMap> list_graphs){
		
		/*
		 * On crée les liens : 
		 * 		- les graphes deux graphes extremums créent un seul lien pour chaque point vers l'intérieur du nouveau graphe.
		 * 		- les graphes internes créent deux liens pour chaque point vers les graphes précédents et les graphes suivants
		 */
		for (int i = 0; i < list_graphs.size(); i++){
			
			MyMap current = list_graphs.get(i);
			
			//Si on est sur les graphes extremums
			if(i == 0 || i == list_graphs.size() - 1){
				
				//Graphe auquel on veut se connecter. Le deuxième si on se trouve sur le premier, et l'avant-dernier si on se trouve sur le dernier.
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
		
		//Maintenant que toutes les places ont été réindexées et tous les liens créés, on crée le nouvel objet final
		MyMap new_graph = new MyMap();
		
		for (int i = 0; i < list_graphs.size(); i++) {
			MyMap current = list_graphs.get(i);
			new_graph.keyList().addAll(current.keyList());
			new_graph.valueLists().addAll(current.valueLists());		
		}
		
		return new_graph;
	}
	
	/**
	 * Fonction permettant de décaler un graphe sur une coordonée spécifique.
	 * 
	 * NOTE : cette fonction ne s'occupe de changer les index dans la liste d'adjacence. Cela sera fait dans la fusion des graphes.
	 * 
	 * @param current_dim La dimension que l'on crée actuellement (et donc coordonnées à modifier)
	 * @param current_dupplication N° de la copie (et donc nombre de décalage qu'il faut réaliser pour placer le graphe)
	 * @param distance_between_two_points Distance entre deux points (valeur unitaire d'un décalage)
	 * 
	 */
	public static void slip(MyMap graph, int current_dim, int current_dupplication, int distance_between_two_points){
		//On parcourt toutes les places du graphe et on les décale
		ArrayList<Place> places = graph.keyList();
		
		for (int i = 0; i < places.size(); i++) {
			Place current = places.get(i);
			int current_coord = current.getCoordinate(current_dim);
			int new_value = current_coord + current_dupplication * distance_between_two_points;
			current.setCoordinate(current_dim, new_value);
		}
	}
	
	/**
	 * Calcule la distance entre deux points. Si jamais il y a un problème d'arrondi, 
	 * les bornes de la graine sont modifiées.
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
//			coords[current_dim] = s.getDimMin(); //on réinitialise la dimension courante
//			return linked;
//		}else{
//			
//			//on crée tous les points de la dimension actuelle
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
//			coords[current_dim] = s.getDimMin(); //on réinitialise la dimension courante
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
//			//On récupère la place courante
//			Place current = places.get(i);
//			int[] coordinates = current.getCoordinates();
//			
//			//Pour chaque dimension, on prend le prédécesseur et le successeur
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
////			//Pour chaque coordonnée de la place, on chercher son successeur
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
//			coords[current_dim] = s.getDimMin(); //on réinitialise la dimension courante
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
//			coords[current_dim] = s.getDimMin(); //on réinitialise la dimension courante
//			
//			return linked;
//		}
		
//		Place current;
//		
//		/* Pour chaque dimensions, on crée le voisin si celui-ci est dans les bornes.
//		Pour chaque dimensions, on a au plus deux voisins (un avant et un après)
//		Etant donné qu'on part du coin avec les coordonnées minorantes, on ne construit que le 
//		voisin d'après, celui d'avant est déjà créé. Par contre on crée */
//		ArrayList<Place> to_visit_queue = new ArrayList<Place>();
//		
//		to_visit_queue.add(0, p);
//		
//		while(!to_visit_queue.isEmpty()){
//			
//			current = to_visit_queue.get(to_visit_queue.size()-1);
//			
//			//On commence par créer l'ensemble des voisins du point courant et les lier
//			for (int i = 0; i < current.getCoordinates().length; i++) {
//						
//				//Pour la coordonnée courante, on va créer le voisin suivant et le précédent
//				int previous = current.getCoordinate(i) - distance;
//				int next = current.getCoordinate(i) + distance;
//				
//				//On vérifie que les coordonnées que l'on va créer font partie de l'environnement.
//				if(previous >= s.getDimMin()){
//					Place neigh = new Place(current.getCoordinates());
//					
//					//On change la seule coordonnée variable de la place
//					neigh.setCoordinate(i, previous);
//					
//					//Si la place n'est pas dans le graphe, on l'y ajoute et on la visitera (on ne l'a pas vu s'il n'est pas dans le graphe)
//					if(graph.indexOf(neigh) == -1){
//						graph.addKey(neigh);
//						if(degre(neigh) < seed.getNbDim() * 2)
//							to_visit_queue.add(0, neigh);
//					}
//					
//					//On crée le lien p --> previous (le lien previous --> p est créé par previous)
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
//					//On crée le lien p --> next (le lien next --> p est créé par next)
//					this.addLink(current, neigh);
//				}
//			}
//			
//			//On le supprime des noeuds à visiter
//			to_visit_queue.remove(to_visit_queue.size()-1);
//		}
//	}
	
	public static void testGraphic() throws UnknownPlaceException{
		
		System.out.println("===== Test graphique de génération de grille =====");
		
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
		System.out.println("===== Test console de génération de grille =====");
		
		ArrayList<Seed> seeds = new ArrayList<Seed>();
		
		//On crée des environnements 2D
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
