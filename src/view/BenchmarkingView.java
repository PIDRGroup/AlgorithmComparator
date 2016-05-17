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

public class BenchmarkingView {
	public static void main(String[] args) throws UnknownPlaceException, IOException {
		
		ArrayList<Algorithm> algos  = new ArrayList<Algorithm>();
		
		if(args.length ==2 && args[1].equals("all")){
			algos.add(new Dijkstra());
			algos.add(new AStar());
			algos.add(new IDAStar());
			algos.add(new RBFS());
			algos.add(new SMAStar());
			algos.add(new UniformCostSearch());
		}else{
			ArrayList<Integer> selected_algos;
			selected_algos = ConsoleView.multipleMenu("Sélection des algorithmes", "Dijkstra", "A*", "IDA*", "RBFS", "SMA*", "UCS");
			
			for (int i = 0; i < selected_algos.size(); i++) {
				switch(selected_algos.get(i).intValue()){
					case 1:
						algos.add(new Dijkstra());
						break;
					case 2:
						algos.add(new AStar());
						break;
					case 3:
						algos.add(new IDAStar());
						break;
					case 4:
						algos.add(new RBFS());
						break;
					case 5:
						algos.add(new SMAStar());
						break;
					case 6:
						algos.add(new UniformCostSearch());
						break;
				}
			}
		}
		
		Calendar c = Calendar.getInstance();
		String date = c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR);
		
		Experience exp = new Experience("EXP du "+date, date);
		exp.addAlgos(algos);
		
		ArrayList<Seed> seeds = new ArrayList<Seed>();
		
		seeds.add(new Seed(System.nanoTime(), new int[]{10, 10}, -6000, 12000));
		seeds.add(new Seed(System.nanoTime(), new int[]{10, 10, 10}, -6000, 12000));
		seeds.add(new Seed(System.nanoTime(), new int[]{100, 10}, -6000, 12000));
		seeds.add(new Seed(System.nanoTime(), new int[]{100, 100}, -6000, 12000));
		seeds.add(new Seed(System.nanoTime(), new int[]{10, 100, 10}, -6000, 12000));
		seeds.add(new Seed(System.nanoTime(), new int[]{10, 10, 10, 10}, -6000, 12000));
		
		System.out.println("================== LANCEMENT DU BENCHMARKING ! ==================");
		
		File file = new File("logs.txt");
		if(file.exists()) file.delete();
		file.createNewFile();
		
		for (int i = 0; i < seeds.size(); i++) {
			Environment env = new GridEnvironment(seeds.get(i));
			exp.setEnv(env);
			exp.launch();
			for(Algorithm a : algos){
				Log log = new Log(a);
				log.write("logs.txt");
				a.clearEval();
			}
			System.out.println(env);
		}
	}
}
