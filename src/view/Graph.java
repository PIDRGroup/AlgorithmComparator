package view;

import java.util.Observable;
import java.util.Observer;

import model.*;

public class Graph implements Observer{
	
	public Graph(){
		Environment env_astar = new Environment();
		env_astar.addObserver(this);
		
		Environment env_dijkstra = new Environment(env_astar);
		
		AStar a_star = new AStar(env_astar);
		Dijkstra dijkstra = new Dijkstra(env_dijkstra);
	}

	@Override
	public void update(Observable o, Object arg) {
		
	}

}
