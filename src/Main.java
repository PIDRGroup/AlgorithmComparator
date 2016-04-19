import java.io.IOException;

import model.env.GridEnvironment;
import model.env.UnknownPlaceException;
import view.*;

public class Main {
	public static void main(String[] args) {
		if(args.length != 1 && args.length != 2){
			usage();
		}
		
		switch (args[0]) {
			case "--console":
				try {
					ConsoleView.main(null);
				} catch (UnknownPlaceException | IOException e) {
					e.printStackTrace();
				}
				break;
				
			case "--view":
				MainWindow.main(null);
				break;
				
			case "--bench":
				BenchmarkingView.main(null);
				break;
				
			case "--test":
				if(args.length != 2){
					usage();
				}
				switch(args[1]){
					case "graphenv":
					case "consenv":
						GridEnvironment.main(args);
						break;
						
					default:
						usage();
				}
				break;
			default:
				usage();
				break;
			}
	}
	
	public static void usage(){
		System.err.println("Usage : java -jar AlgorithmComparator [--console | --view | --bench | --test [graphenv | consenv] ]");
		System.exit(1);
	}
}
