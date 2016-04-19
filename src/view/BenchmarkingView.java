package view;

import java.util.ArrayList;
import java.util.Calendar;

import model.Experience;
import model.algo.AStar;
import model.algo.Algorithm;
import model.algo.Dijkstra;
import model.algo.IDAStar;
import model.algo.RBFS;
import model.algo.SMAStar;
import model.algo.UniformCostSearch;

public class BenchmarkingView {
	public static void main(String[] args) {
		ArrayList<Integer> selected_algos = ConsoleView.multipleMenu("Sélection des algorithmes", "Dijkstra", "A*", "IDA*", "RBFS", "SMA*", "UCS");
		ArrayList<Algorithm> algos = new ArrayList<Algorithm>();
		
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
		
		System.out.println("================== LANCEMENT DU BENCHMARKING ! ==================");
		Calendar c = Calendar.getInstance();
		String date = c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR);
//		Experience exp = new Experience("EXP du "+date, date, env);
//		exp.addAlgos(algos);
//		
//		exp.launch();
	}
}
