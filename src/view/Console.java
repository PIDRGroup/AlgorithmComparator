package view;

import java.io.IOException;

import model.*;

public class Console {
	public static void main(String[] args) {
		try {
			Environment<Integer> env =  EnvLoader.loadInteger("./graph/test.g");
			env.getMatrix().addPlace("J'aime les trains");
			env.getMatrix().addLink("I Like Trains", "J'aime les trains", 12000);
			//System.out.println(env);
			
			//env.getMatrix().delete("J'aime les trains");
			
			//System.out.println(env);
			
			//env.getMatrix().delete("Unknown");
			
			Dijkstra<Integer> dijkstra = new Dijkstra<Integer>(env);
			dijkstra.grow("Kebab", "J'aime les trains");
			
		} catch (IOException | MultiplePlace | UnknownPlace e) {
			e.printStackTrace();
		}
	}
}
