package model;

public class RBFS<E extends Number> extends Algorithm{

	@Override
	public void grow(Place source, Place destination) throws UnknownPlaceException {
		
		if(!world.isPlace(source))
			throw new UnknownPlaceException(source);
		
		if(!world.isPlace(destination))
			throw new UnknownPlaceException(destination);
		
		estimated_time = 0;
		nb_visited_nodes = 0;
	}

	@Override
	public String getName() {
		return "Recursive Best First Search";
	}

	

}
