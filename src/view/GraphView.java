package view;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

import javax.swing.*;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import model.*;
import sun.management.snmp.jvminstr.JvmThreadInstanceEntryImpl.ThreadStateMap;

public class GraphView extends JPanel{
	
	private final static int SIZE = 600;
	private final static Color[] colors = {Color.CYAN, Color.GREEN, Color.RED, Color.PINK, Color.BLACK, Color.WHITE};
	
	private ArrayList<Algorithm> algorithms;
	private JPanel legend;
	
	public GraphView(){
		
		this.setLayout(new BorderLayout());
		algorithms = new ArrayList<Algorithm>();
		
		//On crée le panneau de contrôle
		JPanel pan_control = new JPanel();
		pan_control.setLayout(new BorderLayout());
		JButton btn_run = new JButton("Run !");
		pan_control.add(btn_run, BorderLayout.NORTH);
		
		btn_run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Thread> threads = new ArrayList<Thread>();
				for(Algorithm alg : algorithms){
					/*Thread t = new Thread(alg);
					threads.add(t);
					t.start();*/
				}
				
				//On attend que tous les threads soient finis
				for (int i = 0; i < threads.size(); i++) {
					try {
						threads.get(i).join();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				
				String estimations = "";
				
				for (int i = 0; i < algorithms.size(); i++) {
					Algorithm current = algorithms.get(i);
					//estimations += current.getName() + " : " + current.getDuration() + " ns (time), " + current.getNbNodes() + " nb visited nodes.\n";
				}
				
				JOptionPane.showMessageDialog(btn_run, estimations, "Estimations", JOptionPane.NO_OPTION);
			}
		});
		
		//On crée la légende des couleurs
		
		legend = new JPanel();
		
		for (int i = 0; i < algorithms.size(); i++) {
			JPanel col = new JPanel();
			col.setPreferredSize(new Dimension(20, 20));
			col.setBackground(GraphView.colors[i]);
			
			legend.add(col);
			legend.add(new JLabel(algorithms.get(i).getName()));
		}
		
		pan_control.add(legend, BorderLayout.SOUTH);
		this.add(pan_control);
       
	}
	
	public void addAlgo(Algorithm algo){
		algorithms.add(algo);
		
		JPanel col = new JPanel();
		col.setPreferredSize(new Dimension(20, 20));
		col.setBackground(GraphView.colors[algorithms.size()-1]);
		
		legend.add(col);
		legend.add(new JLabel(algorithms.get(algorithms.size()-1).getName()));
		
		repaint();
	}
	
	public static void main(String[] args) {
		
		Environment env = null;
		try {
			env = EnvironmentManager.generateUniformGrid2D(100);
		
			GraphView gv = new GraphView();
			
			/*AStar a_star = new AStar(env);
			a_star.setSrc(env.alea());
			a_star.setDest(env.alea());
			gv.addAlgo(a_star);
			
			Environment copy = env.duplicate();
			Dijkstra dijkstra = new Dijkstra(copy);
			dijkstra.setSrc(env.alea());
			dijkstra.setDest(env.alea());*/
	             
	        // Set up a new stroke Transformer for the edges
	        JFrame frame = new JFrame("AlgoComparator");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.getContentPane().add(gv);
	        frame.pack();
	        frame.setVisible(true);  
		} catch (MultiplePlaceException | UnknownPlaceException e) {
			e.printStackTrace();
		}
	}

}
