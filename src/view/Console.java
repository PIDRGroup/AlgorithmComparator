package view;

import java.io.IOException;

import model.*;

public class Console {
	public static void main(String[] args) {
		try {
			Environment<Integer> env =  EnvironmentFactory.loadInteger("./graph/test.g");
			env.addPlace("J'aime les trains");
			env.addLink("I Like Trains", "J'aime les trains", 12000);
			//System.out.println(env);
			
			//env.getMatrix().delete("J'aime les trains");
			
			System.out.println(env);
			
			//env.getMatrix().delete("Unknown");
			
			Dijkstra<Integer> dijkstra = new Dijkstra<Integer>(env);
			dijkstra.grow("Kebab", "J'aime les trains");
			System.out.println(dijkstra.getPathLabels());
			
		} catch (IOException | MultiplePlaceException | UnknownPlaceException e) {
			e.printStackTrace();
		}
	}
}
