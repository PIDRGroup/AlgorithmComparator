package view;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import model.Algorithm;
import model.Environment;
import model.Evaluation;
import model.Experience;

public class EvaluationView extends JPanel{
	
	private MainWindow parent;
	private ArrayList<Evaluation> evals;
	private ArrayList<Algorithm> algos;
	
	private JTabbedPane tabbed_pane;
	private JButton btn_return, btn_save, btn_home;
	private ArrayList<ResultView> eva_views;
	
	public EvaluationView(MainWindow parent, Experience exp){
		this.parent = parent;
		evals = exp.getEvals();
		algos = exp.getAlgos();
		
		btn_return = new JButton("Nouvelle expérience");
		btn_save = new JButton("Sauvegarder");
		btn_home = new JButton("Accueil");
		
		eva_views = new ArrayList<ResultView>(evals.size());
		tabbed_pane = new JTabbedPane();
		
		for (int i = 0; i < evals.size(); i++) {
			ResultView rv = new ResultView(exp.getEnv(), algos.get(i), evals.get(i));
			eva_views.add(rv);
			tabbed_pane.addTab(algos.get(i).getName(), rv);
		}
		
		btn_return.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.switchCreation();
			}
		});
		
		btn_home.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.switchHome();
			}
		});
		
		tabbed_pane.addTab("", null);
		tabbed_pane.addTab("", null);
		tabbed_pane.addTab("", null);
		tabbed_pane.setTabComponentAt(tabbed_pane.getTabCount()-3, btn_save);
		tabbed_pane.setTabComponentAt(tabbed_pane.getTabCount()-2, btn_return);
		tabbed_pane.setTabComponentAt(tabbed_pane.getTabCount()-1, btn_home);
		
		this.add(tabbed_pane);
	}
	
	public void setEvals(ArrayList<Evaluation> ev){
		evals = ev;
	}
}

class ResultView extends JPanel{
	private JPanel panel_data, panel_general;
	private PointCloud cloud;
	private JLabel label_execution_time, label_execution_to_best, label_execution_to_first, label_nb_nodes;
	private JTextArea area_path;
	
	public ResultView(Environment env, Algorithm alg, Evaluation ev){
		this.setLayout(new BorderLayout());
		
		label_execution_time = new JLabel(ev.getnb_while()+" exécutions au total");
		label_execution_to_best = new JLabel(ev.getbest_solution()+" exécutions jusqu'au meilleur");
		label_execution_to_first = new JLabel(ev.getfirst_solution()+"exécutions jusqu'au premier");
		label_nb_nodes = new JLabel(alg.getPath().size()+" noeuds dans le chemin");
		
		panel_data = new JPanel();
		panel_data.setLayout(new BorderLayout());
		
		panel_general = new JPanel();
		panel_general.setLayout(new GridLayout(0, 1));
		panel_general.add(label_execution_time);
		panel_general.add(label_execution_to_best);
		panel_general.add(label_execution_to_first);
		panel_general.add(label_nb_nodes);
		
		area_path = new JTextArea(alg.getPath().toString());
		area_path.setEditable(false);
		JScrollPane pane = new JScrollPane(area_path);
		
		panel_data.add(panel_general, BorderLayout.WEST);
		panel_data.add(pane, BorderLayout.EAST);
		
		this.add(panel_data, BorderLayout.NORTH);
		cloud = new PointCloud(env, alg, env.getBound(0).size(), env.getBound(1).size());
		this.add(cloud, BorderLayout.SOUTH);
	}
}
