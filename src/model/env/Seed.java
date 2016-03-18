package model.env;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Seed {
	private long seed;
	private Random generator;
	private int nb_places;
	private int nb_dim;
	private int dim_min, dim_max;
	private TypeSeed type;
	
	public Seed(){
		seed = System.nanoTime();
		generator = new Random(seed);
		nb_places =  1000;
		nb_dim = 2;
		dim_min = 0;
		dim_max = 600;
		type = TypeSeed.GRID;
	}
	
	public Seed(TypeSeed ts, long s, int nbPlaces, int nb_dim, int dim_min, int dim_max){
		type = ts;
		seed = s;
		this.nb_dim = nb_dim;
		this.dim_min = dim_min;
		this.dim_max = dim_max;
		generator = new Random(seed);
		nb_places = nbPlaces;
	}

	public void save(String path) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path)));
		bw.write(this+"");
		bw.close();
	}
	
	public static Seed load(String path) throws IOException, UnknownParameterException, InvalidNumberParametersException, IllegalParametersException{
		
		Seed s = new Seed();
		BufferedReader br = new BufferedReader(new FileReader(new File(path)));
		String line;
		
		while((line = br.readLine()) != null){
			String[] els = line.split("=");
			if(els.length != 2) throw new UnknownParameterException(els[0]);
			
			switch (els[0]) {
				case "seed":
					s.setSeed(Long.parseLong(els[1]));
					break;
					
				case "places":
					s.setNbPlaces(Integer.parseInt(els[1]));
					break;
					
				case "dimensions":
					s.setNbDim(Integer.parseInt(els[1]));
					break;
					
				case "min":
					s.setDimMin(Integer.parseInt(els[1]));
					break;
					
				case "max":
					s.setDimMax(Integer.parseInt(els[1]));
					break;
	
				default:
					throw new UnknownParameterException(els[0]);
			}
		}
		
		//On vérifie que la seed est bien construite et on régénère le RNG.
		s.isComplete();
		
		return s;
	}
	
	public void isComplete() throws UnknownParameterException, InvalidNumberParametersException, IllegalParametersException{
		if(seed == 0 || nb_places == 0 || nb_dim == 0 || dim_max == 0 || dim_min == 0){
			throw new InvalidNumberParametersException();
		}
		
		if(seed < 0 || nb_places < 0 || nb_dim < 0 || dim_min < 0 || dim_max < 0){
			throw new IllegalParametersException();
		}
		
		generator = new Random(seed);
	}
	
	public Place nextPlace(){
		ArrayList<Long> list = new ArrayList<Long>();
		
		for (int i = 0; i < nb_dim; i++) {
			list.add(new Long(generator.nextInt(dim_max-dim_min) + dim_min));
		}
		
		return new Place(list);
	}
	
	public Place nextLink(ArrayList<Place> places){
		int i = -1;
		
		i = generator.nextInt(places.size());
		
		return places.get(i);
	}
	
	public int nbPlaces(){
		return nb_places;
	}
	
	public String toString(){
		return "seed="+seed+"\nplaces="+nb_places+"\ndimensions="+nb_dim+"\nmin="+dim_min+"\nmax="+dim_max+"\n";
	}
	
	
	
	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}

	public int getNbPlaces() {
		return nb_places;
	}

	public void setNbPlaces(int nb_places) {
		this.nb_places = nb_places;
	}

	public int getNbDim() {
		return nb_dim;
	}

	public void setNbDim(int nb_dim) {
		this.nb_dim = nb_dim;
	}

	public int getDimMin() {
		return dim_min;
	}

	public void setDimMin(int dim_min) {
		this.dim_min = dim_min;
	}

	public int getDimMax() {
		return dim_max;
	}

	public void setDimMax(int dim_max) {
		this.dim_max = dim_max;
	}

	public Random getGenerator() {
		return generator;
	}
	
	public void setType(TypeSeed ts){
		type=ts;
	}
	
	public TypeSeed getType(){
		return type;
	}
}

class UnknownParameterException extends Exception{
	public UnknownParameterException(String param){
		super("Paramètre inconnu dans le fichier de graine : "+param);
	}
}

class InvalidNumberParametersException extends Exception{
	public InvalidNumberParametersException(){
		super("Le nombre de paramètre du fichier de graine n'est pas bon : graine, nb places, nb dimensions, dimension min, dimension max");
	}
}

class IllegalParametersException extends Exception{
	public IllegalParametersException(){
		super("L'un des paramètres a une mauvaise valeur");
	}
}
