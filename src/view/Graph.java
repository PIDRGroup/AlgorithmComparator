package view;

import java.util.Observable;
import java.util.Observer;

import model.*;

public class Graph implements Observer{
	
	public Graph(){
		Environment env_astar = new Environment();
		env_astar.addObserver(this);
		AStar a_star = new AStar(env_astar);
		
		Environment env_dijkstra = env_astar.duplicate();
		env_dijkstra.addObserver(this);
		Dijkstra dijkstra = new Dijkstra(env_dijkstra);
	}

	@Override
	public void update(Observable o, Object arg) {
		
	}
	
	public static void main(String[] args) {
		Graph g = new Graph();
	}

}
