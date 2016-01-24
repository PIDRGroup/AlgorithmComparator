package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import model.*;

public class Graph implements Observer{
	
	public Graph(){
		Environment<Integer> env_astar = new Environment<Integer>();
		env_astar.addObserver(this);
		AStar a_star = new AStar(env_astar);
		
		Environment<Integer> env_dijkstra = env_astar.duplicate();
		env_dijkstra.addObserver(this);
		Dijkstra dijkstra = new Dijkstra(env_dijkstra);
		
		edu.uci.ics.jung.graph.Graph<String, Integer> g = new SparseMultigraph<String, Integer>();
	}

	@Override
	public void update(Observable o, Object arg) {
		
	}
	
	public static void main(String[] args) {
		
		edu.uci.ics.jung.graph.Graph<String, Integer> g = new DirectedSparseMultigraph<String, Integer>();
		g.addVertex("Coucou");
		g.addVertex("Kebab");
		g.addEdge(50, "Coucou", "Kebab");
		g.addEdge(30, "Kebab", "Coucou");
		
        Layout<String, Integer> layout = new CircleLayout(g);
        layout.setSize(new Dimension(300,300));
        BasicVisualizationServer<String, Integer> vv = 
              new BasicVisualizationServer<String, Integer>(layout);
        vv.setPreferredSize(new Dimension(350,350));       
        // Setup up a new vertex to paint transformer...
        Transformer<String,Paint> vertexPaint = new Transformer<String,Paint>() {
            public Paint transform(String i) {
                return Color.GREEN;
            }
        };  
        // Set up a new stroke Transformer for the edges
        float dash[] = {10.0f};
        final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
             BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        Transformer<Integer, Stroke> edgeStrokeTransformer = 
              new Transformer<Integer, Stroke>() {
            public Stroke transform(Integer s) {
                return edgeStroke;
            }
        };
        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
        JFrame frame = new JFrame("Simple Graph View 2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);        
	}

}
