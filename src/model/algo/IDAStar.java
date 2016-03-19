package model.algo;

import java.util.ArrayList;

import model.env.Environment;
import model.env.Place;
import model.env.UnknownPlaceException;

public class IDAStar extends Algorithm{
	
	private Environment world;
	private Place destination;
	final int found = -1; 
	
	public IDAStar(){
		path = new ArrayList<Place>();
		eval = new Evaluation(); 
	}

	@Override
	public void grow(Environment world, Place src, Place dest)
			throws UnknownPlaceException {
		
		this.world = world;
		this.destination = dest;
		
		if(!world.isPlace(src))
			throw new UnknownPlaceException(src);
		
		if(!world.isPlace(dest))
			throw new UnknownPlaceException(dest);
		
		eval.start();
		
		double t;
		double bound = h(src);
		
		while(true){
			t = search(src, 0, bound);
			if (t == found){
				path.add(src);
				break;
			}
			if (t == Integer.MAX_VALUE) {
				System.out.println("Aucun chemin trouvé!"); 
				break;
			}
			bound = t;
		}
		
	}
	
	private double search(Place node, double g, double bound) throws UnknownPlaceException{
		
		eval.newWhile();
		
		double f = g + h(node);
		
		if (f > bound){
			path.add(node);
			return f;
		}
		
		if (node.equals(destination)){
			path.add(node);
			eval.gotASolution(bound);
			return found;
		}
		
		double min = Integer.MAX_VALUE;
		double t;
		double dist;
		
		for (int i = 0; i < world.size(); i++){
			if ((dist = world.get(node, world.getByIndex(i))) < Integer.MAX_VALUE){
				t = search(world.getByIndex(i), g + dist, bound);
				if (t == found) {
					path.add(world.getByIndex(i));
					return found;
				}
				if (t < min) min = t;
			}
		}
		
		return min;
	}

	private double h(Place current){
		return 0;
	}
	
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "IDA*";
	}

}
