package model.algo;

import java.util.ArrayList;

import model.env.Environment;
import model.env.Place;
import model.env.UnknownPlaceException;

public class IDAStar extends Algorithm{
	
	private Environment world;
	private Place destination;
	private ArrayList<Place> noeudenvisage;
	final int found = -1; 
	
	public IDAStar(){
		path = new ArrayList<Place>();
		eval = new Evaluation(); 
		noeudenvisage = new ArrayList<Place>();
	}

	@Override
	public void grow(Environment world, Place src, Place dest)
			throws UnknownPlaceException {
		
		this.world = world;
		this.destination = dest;
		
		this.eval.start();
		this.eval.newVisite(src);
		
		double t;
		double bound = h(src);
		
		while(true){
			t = search(src, 0, bound, 0);
			if (t == found){
				this.path.add(src);
				this.eval.gotASolution(bound, path.size());
				break;
			}
			if (t == Integer.MAX_VALUE) {
				System.out.println("Aucun chemin trouvé!"); 
				break;
			}
			bound = t;
		}
		
	}
	
	private double search(Place node, double g, double bound, int depth) throws UnknownPlaceException{
		
		depth++;
		
		if (!this.noeudenvisage.contains(node)){
			this.noeudenvisage.add(node);
			this.eval.newNoeudEnvisage();
		}
		
		double f = g + h(node);
		
		if (f > bound){
			return f;
		}
		
		if (node.equals(destination)){
			return found;
		}
		
		if (world.get(node.getIndex(), destination.getIndex()) < Double.MAX_VALUE){
			this.eval.gotASolution(bound, depth + 1);
		}
		
		double min = Double.MAX_VALUE;
		double t;
		double dist;
		
		for (int i = 0; i < world.size(); i++){
			if ((dist = world.get(node.getIndex(), world.getByIndex(i).getIndex())) < Double.MAX_VALUE){
				
				this.eval.newVisite(world.getByIndex(i));
				
				t = search(world.getByIndex(i), g + dist, bound, depth);
				
				if (t == found) {
					this.path.add(world.getByIndex(i));
					return found;
				}
				if (t < min) min = t;
			}
		}
		
		return min;
	}

	private double h(Place current){
		return current.distanceEuclidienne(this.destination);
	}
	
	@Override
	public String getName() {
		return "IDA*";
	}

}
