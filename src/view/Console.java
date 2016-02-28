package view;

import model.*;

public class Console {
	public static void main(String[] args) {
		try {
			
			Bound b1 = new Bound(0, 500), b2 = new Bound(0, 700);
			
			System.out.println("G�n�ration de l'environnement");
			Environment env =  EnvironmentFactory.generateUniformGrid2D(50, b1, b2);

			AStar astar = new AStar();
			
			System.out.println("S�lection des points de d�part et d'arriv�e");
			
			Experience exp = new Experience("Exp1", "Today", env);
			System.out.println("Point de d�part : " + exp.getSrc());
			System.out.println("Point d'arriv�e : " + exp.getDest());
			
			System.out.println("Calcul du chemin");
			exp.addAlgo(astar);
			exp.launch();
			
			System.out.println(astar.getPath());
			
		} catch (MultiplePlaceException | UnknownPlaceException e) {
			e.printStackTrace();
		}
	}
}
