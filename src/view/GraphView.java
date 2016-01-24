package view;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

import javax.swing.*;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import model.*;

public class GraphView<E extends Number> extends JPanel implements Observer{
	
	private final static int SIZE = 600;
	private final static Color[] colors = {Color.CYAN, Color.GREEN, Color.RED, Color.PINK, Color.BLACK, Color.WHITE};
	
	private ArrayList<Algorithm<E>> algorithms;
	private Graph<String, E> graph;
	private BasicVisualizationServer<String, E> visual;
	private JPanel legend;
	
	public GraphView(Graph<String, E> g){
		graph = g;
		
		this.setLayout(new BorderLayout());
		algorithms = new ArrayList<Algorithm<E>>();
		
		//On crée le panneau de contrôle
		JPanel pan_control = new JPanel();
		pan_control.setLayout(new BorderLayout());
		JButton btn_run = new JButton("Run !");
		pan_control.add(btn_run, BorderLayout.NORTH);
		
		btn_run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(Algorithm<? extends Number> alg : algorithms){
					Thread t = new Thread(alg);
					t.start();
				}
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
		
		//On crée l'interface du graphe 
		
		Layout<String, E> layout = new CircleLayout<String, E>(g);
		layout.setSize(new Dimension(SIZE,SIZE));
		visual = new BasicVisualizationServer<String, E>(layout);
		visual.setPreferredSize(new Dimension(SIZE,SIZE));
		
		//On indique la couleur des places
		Transformer<String,Paint> vertexPaint = new Transformer<String,Paint>() {
			
            public Paint transform(String str) {
            	for(int i=0; i<algorithms.size(); i++){
            		Algorithm<E> alg = algorithms.get(i);            		
            		if(alg.getPathLabels().contains(str))
            			return GraphView.colors[i];
            	}
            	return Color.LIGHT_GRAY;
            }
            
        }; 
       
        visual.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        
        //On indique qu'on veut afficher les labels
        visual.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<String>());
        visual.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<E>());
        visual.getRenderer().getVertexLabelRenderer().setPosition(Position.AUTO);
        visual.getRenderContext().getEdgeLabelRenderer().setRotateEdgeLabels(false);
        
        visual.setBackground(Color.WHITE);
        this.add(visual, BorderLayout.SOUTH);
	}
	
	public void addAlgo(Algorithm<E> algo){
		algorithms.add(algo);
		
		JPanel col = new JPanel();
		col.setPreferredSize(new Dimension(20, 20));
		col.setBackground(GraphView.colors[algorithms.size()-1]);
		
		legend.add(col);
		legend.add(new JLabel(algorithms.get(algorithms.size()-1).getName()));
		
		repaint();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		visual.repaint();
	}
	
	public static void main(String[] args) {
		
		Environment<Integer> env = null;
		try {
			env = EnvLoader.loadInteger("./graph/test.g");
		} catch (IOException | MultiplePlace | UnknownPlace e) {
			e.printStackTrace();
		}
		
		GraphView<Integer> gv = new GraphView<Integer>(env.toGraph());
		
		AStar<Integer> a_star = new AStar<Integer>(env);
		a_star.setSrc("Kebab");
		a_star.setDest("I Like Trains");
		a_star.addObserver(gv);
		env.addObserver(gv);
		gv.addAlgo(a_star);
		
		Environment<Integer> copy = env.duplicate();
		copy.addObserver(gv);
		Dijkstra<Integer> dijkstra = new Dijkstra<Integer>(copy);
		dijkstra.setSrc("Kebab");
		dijkstra.setDest("I Like Trains");;
		dijkstra.addObserver(gv);
		//gv.addAlgo(dijkstra);
             
        // Set up a new stroke Transformer for the edges
        JFrame frame = new JFrame("Simple Graph View 2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(gv);
        frame.pack();
        frame.setVisible(true);   
	}

}
