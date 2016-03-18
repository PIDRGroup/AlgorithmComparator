import java.io.IOException;

import model.UnknownPlaceException;
import view.*;

public class Main {
	public static void main(String[] args) {
		if(args.length != 1){
			System.err.println("Usage : java -jar AlgorithmComparator [--console | --view | --server]");
			System.exit(1);
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
			
		case "--server":
			System.out.println("Option non-implémentée");
			break;
			
		default:
			System.err.println("Usage : java -jar AlgorithmComparator [--console | --view | --server]");
			System.exit(1);
			break;
		}
	}
}
