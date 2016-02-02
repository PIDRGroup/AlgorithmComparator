package view;

import model.*;

public class Console {
	public static void main(String[] args) {
		try {
			
			long t1 = System.currentTimeMillis() / 1000;
			Environment<Integer> env =  EnvGenerator.generateUniformGrid(100);
			long t2 = System.currentTimeMillis() / 1000;
			
			System.out.println("TIME ###  " + (t2 - t1) + " sec. ###\n");
			System.out.println(env+"\n");
			
			Dijkstra<Integer> dijkstra = new Dijkstra<Integer>(env);
			dijkstra.grow("3", "15");
			long t3 = System.currentTimeMillis() / 1000;
			
			System.out.println("TIME ###  " + (t3 - t1) + " sec. ###\n");
			System.out.println(dijkstra.getPathLabels());
			
		} catch (MultiplePlaceException | UnknownPlaceException e) {
			e.printStackTrace();
		}
	}
}
