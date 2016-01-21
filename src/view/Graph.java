package view;

import java.util.Observable;
import java.util.Observer;

import edu.uci.ics.jung.graph.*;

import model.*;

public class Graph implements Observer{
	
	public Graph(){
		Environment<Integer> env_astar = new Environment<Integer>();
		env_astar.addObserver(this);
		AStar a_star = new AStar(env_astar);
		
		Environment<Integer> env_dijkstra = env_astar.duplicate();
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
