package model;

public class IDAStar extends Algorithm{

	@Override
	public void grow(Environment world, Place src, Place dest)
			throws UnknownPlaceException {
		
		if(!world.isPlace(src))
			throw new UnknownPlaceException(src);
		
		if(!world.isPlace(dest))
			throw new UnknownPlaceException(dest);
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "IDAStar";
	}

}
