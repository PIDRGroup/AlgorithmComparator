package view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import model.Experience;
import model.algo.AStar;
import model.algo.Algorithm;
import model.algo.Dijkstra;
import model.algo.IDAStar;
import model.algo.Log;
import model.algo.RBFS;
import model.algo.SMAStar;
import model.algo.UniformCostSearch;
import model.env.Environment;
import model.env.GridEnvironment;
import model.env.Seed;
import model.env.UnknownPlaceException;
import sun.misc.UCDecoder;

public class BenchmarkingView {
	public static void main(String[] args) throws UnknownPlaceException, IOException {
		
		ArrayList<Algorithm> algos  = new ArrayList<Algorithm>();
		
		Calendar c = Calendar.getInstance();
		String date = c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR);
		
		Experience exp = new Experience("EXP du "+date, date);
		
		System.out.println("================== LANCEMENT DU BENCHMARKING ! ==================");
		
		File file = new File("logs.txt");
		if(file.exists()) file.delete();
		file.createNewFile();
		
		
		ArrayList<Seed> seeds = new ArrayList<Seed>();
		
		exp.addAlgos(algos);
		
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < seeds.size(); j++) {
				Environment env = new GridEnvironment(seeds.get(j));
				exp.setEnv(env);
				exp.launch();
				for(Algorithm a : algos){
					Log log = new Log(a);
					log.write("logs.txt");
					a.clearEval();
				}
			}
		}
		
		exp.clearAlgos();
		algos.clear();
		seeds.clear();
		
		algos.add(new Dijkstra());
		algos.add(new AStar());
		algos.add(new UniformCostSearch());
		algos.add(new IDAStar());
		exp.addAlgos(algos);
		
		seeds.add(new Seed(System.nanoTime(), new int[]{100, 10}, -600, 1200)); //1000
		seeds.add(new Seed(System.nanoTime(), new int[]{20, 10, 5}, -600, 1200)); //1000
		seeds.add(new Seed(System.nanoTime(), new int[]{4, 5, 2, 25}, -600, 1200)); //1000
		
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < seeds.size(); j++) {
				Environment env = new GridEnvironment(seeds.get(j));
				exp.setEnv(env);
				exp.launch();
				for(Algorithm a : algos){
					Log log = new Log(a);
					log.write("logs.txt");
					a.clearEval();
				}
			}
		}
	}
}
