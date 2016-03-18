package model.env;

public class RandomEnvironment extends Environment{

	public RandomEnvironment() throws UnknownPlaceException{
		this(new Seed());
	}
	
	public RandomEnvironment(Seed s) throws UnknownPlaceException{
		graph = new MyMap<Place, Place>();
		Place current;
		seed=s;
		
		seed.setType(TypeSeed.RAND);
	}

}
