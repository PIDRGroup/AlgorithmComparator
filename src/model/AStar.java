package model;

public class AStar<E extends Number> extends Algorithm<E>{
	
	public AStar(Environment<E> env){
		this.world = env;
	}

	@Override
	public void grow(int source, int destination) throws UnknownPlace{
		
	}

	@Override
	public void grow(String src, String dest) throws UnknownPlace {
		
	}

}
