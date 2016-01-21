package model;

public abstract class Algorithm<E extends Number> extends Thread{
	
	protected Environment<E> world;
	
	/**
	 * Modify the environment depending on the current algorithm.
	 * Each algorithm has its own instance of the environment
	 */
	public abstract void grow(int source, int destination) throws UnknownPlace;
	public abstract void grow(String src, String dest) throws UnknownPlace;
}
