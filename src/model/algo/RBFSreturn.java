package model.algo;

public class RBFSreturn {
	
	private boolean failure;
	private double bestf;
	
	public RBFSreturn(boolean failure, double bestf) {
		this.failure = failure;
		this.bestf = bestf;
	}
	
	public boolean isFailure(){
		return this.failure;
	}
	
	public double getBestF(){
		return this.bestf;
	}
}
