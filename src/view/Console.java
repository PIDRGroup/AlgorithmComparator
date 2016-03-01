package view;

import javax.swing.JFrame;

import model.*;

public class Console {
	public static void main(String[] args) {
		try {
			
			Bound b1 = new Bound(0, 500), b2 = new Bound(0, 700);
			
			System.out.println("Génération de l'environnement");
			Environment env =  EnvironmentFactory.generateUniformGrid2D(10, b1, b2);

			AStar astar = new AStar();
			
			System.out.println("Sélection des points de départ et d'arrivée");
			
			Experience exp = new Experience("Exp1", "Today", env);
			System.out.println("Point de départ : " + exp.getSrc());
			System.out.println("Point d'arrivée : " + exp.getDest());
			
			JFrame frame = new JFrame("test");
			frame.setContentPane(new PointCloud(env, null, 500, 700));
			frame.setVisible(true);
			frame.pack();
			
			System.out.println("Calcul du chemin");
			exp.addAlgo(astar);
			long a = System.currentTimeMillis();
			exp.launch();
			long b = System.currentTimeMillis();
			System.out.println("TIME : " + ((b-a)/1000));
			
			System.out.println(astar.getPath());
			
		} catch (MultiplePlaceException | UnknownPlaceException e) {
			e.printStackTrace();
		}
	}
}
