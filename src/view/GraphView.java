package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import model.*;

public class GraphView extends JPanel implements Observer{
	private final static int SIZE = 600;
	
	public GraphView(Graph g){
		Layout<String, Integer> layout = new CircleLayout(g);
		layout.setSize(new Dimension(SIZE,SIZE));
		BasicVisualizationServer<String, Integer> bvs = new BasicVisualizationServer<String, Integer>(layout);
		bvs.setPreferredSize(new Dimension(SIZE,SIZE));
		
		//On indique la couleur des places
		Transformer<String,Paint> vertexPaint = new Transformer<String,Paint>() {
            public Paint transform(String i) {
                return Color.CYAN;
            }
        }; 
       
        bvs.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        
        //On indique qu'on veut afficher les labels
        bvs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        bvs.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        bvs.getRenderer().getVertexLabelRenderer().setPosition(Position.AUTO);
        bvs.getRenderContext().getEdgeLabelRenderer().setRotateEdgeLabels(false);
        
        this.add(bvs);
	}

	@Override
	public void update(Observable o, Object arg) {
		
	}
	
	public static void main(String[] args) {
		
		Environment<Integer> env_astar = null;
		try {
			env_astar = EnvLoader.loadInteger("./graph/test.g");
		} catch (IOException | MultiplePlace | UnknownPlace e) {
			e.printStackTrace();
		}
		Environment<Integer> env_dijkstra = env_astar.duplicate();
		
		GraphView gv = new GraphView(env_astar.toGraph());
		
		env_astar.addObserver(gv);
		AStar a_star = new AStar(env_astar);
		
		env_dijkstra.addObserver(gv);
		Dijkstra dijkstra = new Dijkstra(env_dijkstra);
             
        // Set up a new stroke Transformer for the edges
        JFrame frame = new JFrame("Simple Graph View 2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(gv);
        frame.pack();
        frame.setVisible(true);   
	}

}
