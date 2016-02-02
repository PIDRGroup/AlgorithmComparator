package view;

import model.*;

public class Console {
	public static void main(String[] args) {
		try {
			
			long t1 = System.currentTimeMillis() / 1000;
			Environment<Integer> env =  EnvGenerator.generateUniformGrid(100);

			long t2 = System.currentTimeMillis() / 1000;
			
			System.out.println("TIME ###  " + (t2 - t1) + " sec. ###\n");
			//System.out.println(env+"\n");
			AStar<Integer> astar = new AStar<Integer>(env);
			long t3 = System.currentTimeMillis() / 1000;
			astar.grow("9", "94");
			long t4 = System.currentTimeMillis() / 1000;
			
			System.out.println("TIME ###  " + (t4 - t3) + " sec. ###\n");
			System.out.println(astar.getPathLabels());
			
		} catch (MultiplePlaceException | UnknownPlaceException e) {
			e.printStackTrace();
		}
	}
}
