package model;

public abstract class Algorithm extends Thread{
	
	protected Environment world;
	
	/**
	 * Modify the environment depending on the current algorithm.
	 * Each algorithm has its own instance of the environment
	 */
	public abstract void grow(int source, int destination) throws UnknownPlace;
	public abstract void grow(String src, String dest) throws UnknownPlace;
}
