package view;
import java.util.ArrayList;

import javax.swing.JPanel;

import model.Evaluation;

public class ResultView extends JPanel{
	private MainWindow parent;
	private ArrayList<Evaluation> evals;
	
	public ResultView(MainWindow parent){
		this.parent = parent;
		evals = new ArrayList<Evaluation>();
	}
	
	public void setEvals(ArrayList<Evaluation> ev){
		evals = ev;
	}
}
