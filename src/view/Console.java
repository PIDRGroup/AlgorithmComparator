package view;

import model.*;

public class Console {
	public static void main(String[] args) {
		try {
			
			Environment env =  EnvironmentFactory.generateUniformGrid(100);

			AStar astar = new AStar(env);
			
			astar.setSrc(env.alea());
			astar.setDest(env.alea());
			astar.grow();
			
			System.out.println(astar.getPathLabels());
			
		} catch (MultiplePlaceException | UnknownPlaceException e) {
			e.printStackTrace();
		}
	}
}
