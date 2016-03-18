package view;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import model.Experience;
import model.algo.Algorithm;
import model.algo.Evaluation;
import model.env.Environment;

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
		
		btn_save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				int res = jfc.showOpenDialog(btn_save);
				if(res == JFileChooser.APPROVE_OPTION){
					File f=jfc.getSelectedFile();
					try {
						exp.save(f.getAbsolutePath());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
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
	private JCheckBox box_show;
	
	public ResultView(Environment env, Algorithm alg, Evaluation ev){
		this.setLayout(new BorderLayout());
		
		label_execution_time = new JLabel(ev.getNbWhile()+" exécutions au total");
		label_execution_to_best = new JLabel(ev.getTimeSolutions().get(ev.getTimeSolutions().size()-1)+" exécutions jusqu'au meilleur");
		label_execution_to_first = new JLabel(ev.getTimeSolutions().get(0)+"exécutions jusqu'au premier");
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
		area_path.setPreferredSize(new Dimension(300, 100));
		area_path.setLineWrap(true);
		area_path.setWrapStyleWord(true);
		JScrollPane pane = new JScrollPane(area_path);
		
		panel_data.add(panel_general, BorderLayout.WEST);
		panel_data.add(pane, BorderLayout.EAST);
		
		this.add(panel_data, BorderLayout.NORTH);
		JPanel container = new JPanel();
		int size = env.getSeed().getDimMax() - env.getSeed().getDimMin();
		cloud = new PointCloud(env, alg, size, size);
		box_show = new JCheckBox("Montrer les liens");
		box_show.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				cloud.showLinks(box_show.isSelected());
			}
		});
		container.add(cloud);
		container.add(box_show);
		container.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.add(container, BorderLayout.SOUTH);
	}
}
