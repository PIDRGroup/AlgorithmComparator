package model;

import java.util.ArrayList;

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
		eval.new_while();
		
		double f = g + h(node);
		
		if (f > bound){
			path.add(node);
			return f;
		}
		
		if (node.equals(destination)){
			path.add(node);
			eval.gotasolution();
			return found;
		}
		
		double min = Integer.MAX_VALUE;
		double t;
		double dist;
		
		for (int i = 0; i < world.size(); i++){
			if ((dist = world.get(node, world.getPlace(i))) < Integer.MAX_VALUE){
				t = search(world.getPlace(i), g + dist, bound);
				if (t == found) {
					path.add(world.getPlace(i));
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
