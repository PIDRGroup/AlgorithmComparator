package view;

import model.*;

public class Console {
	public static void main(String[] args) {
		try {
			
			Bound b1 = new Bound(0, 500), b2 = new Bound(0, 700);
			
			System.out.println("G�n�ration de l'environnement");
			Environment env =  EnvironmentFactory.generateAlea(10000, b1, b2);

			AStar astar = new AStar(env);
			
			System.out.println("S�lection des points de d�part et d'arriv�e");
			astar.setSrc(env.alea());
			astar.setDest(env.alea());
			
			System.out.println("Calcul du chemin");
			astar.grow();
			
			System.out.println(astar.getPathLabels());
			
		} catch (MultiplePlaceException | UnknownPlaceException e) {
			e.printStackTrace();
		}
	}
}
