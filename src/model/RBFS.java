package model;

public class RBFS extends Algorithm{

	@Override
	public void grow(Environment world, Place source, Place destination) throws UnknownPlaceException {
		
		if(!world.isPlace(source))
			throw new UnknownPlaceException(source);
		
		if(!world.isPlace(destination))
			throw new UnknownPlaceException(destination);
		
		
		
	}

	int h(Place current){
		return 0;
	}
	
	@Override
	public String getName() {
		return "Recursive Best First Search";
	}

	
	

}
