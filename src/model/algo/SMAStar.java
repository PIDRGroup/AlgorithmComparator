package model.algo;

import java.util.ArrayList;

import model.env.Environment;
import model.env.Place;
import model.env.UnknownPlaceException;

public class SMAStar extends Algorithm{

	@Override
	public void grow(Environment world, Place src, Place dest)
			throws UnknownPlaceException {
		// TODO Auto-generated method stub
		if(!world.isPlace(src))
			throw new UnknownPlaceException(src);
		
		if(!world.isPlace(dest))
			throw new UnknownPlaceException(dest);
		
		ArrayList<Place> queue = new ArrayList<Place>();
		queue.add(src);
		
		while(true){
			if (queue.isEmpty()){
				System.out.println("Chemin non trouvé! La queue est vide");
				break;
			}
		}
		
	}
	
	public double h(Place current){
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "SMA*";
	}

}
