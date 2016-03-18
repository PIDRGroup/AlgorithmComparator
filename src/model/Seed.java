package model;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Seed {
	private long seed;
	private Random generator;
	private int nb_places;
	private int nb_dim;
	private int dim_min, dim_max;
	
	public Seed(long s, int nbPlaces, int nb_dim, int dim_max, int dim_min){
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
	
	public static Seed load(String path) throws IOException{
		
		Seed seed = null;
		BufferedReader br = new BufferedReader(new FileReader(new File(path)));
		String line;
		
		line = br.readLine();
		long s = Long.parseLong(line);
		line = br.readLine();
		int np = Integer.parseInt(line);
		line = br.readLine();
		int nd = Integer.parseInt(line);
		line = br.readLine();
		int nmin = Integer.parseInt(line);
		line = br.readLine();
		int nmax = Integer.parseInt(line);
		br.close();
		
		return new Seed(s, np, nd, nmin, nmax);
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
		return seed+"\n"+nb_places+"\n"+nb_dim+"\n"+dim_min+"\n"+dim_max+"\n";
	}
}
