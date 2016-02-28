package view;

import java.awt.*;
import java.util.*;
import javax.swing.JPanel;
import model.*;

/**
 * 
 * Cette classe permet d'afficher graphiquement un nuage de points représentant un environnement à une ou deux
 * dimensions.
 *
 */
public class PointCloud extends JPanel implements Observer{
	
	private Environment env;
	private Algorithm algo;
	private ArrayList<Point> points;
	
	//On définit la couleur des noeuds du chemin, des noeuds qui ont été développés et des noeuds qui n'ont pas été touchés.
	public final static Color COLOR_POINT = Color.BLACK, COLOR_PATH = Color.GREEN, COLOR_EXPANDED = Color.LIGHT_GRAY;
	
	public PointCloud(Environment e, Algorithm alg, int width, int height){
		
		this.setBackground(Color.WHITE);
		
		points = new ArrayList<Point>();
		algo = alg;
		this.setSize(new Dimension(width, height));
		
		//On crée l'ensemble des points initiaux.
		env=e;
		if(env.nbDim() == 1){
			for(Place p : env.getPlaces()){
				//Si c'est en 1D, on place tous les points sur une ligne
				points.add(new Point(p.getCoordinate(0), p.getCoordinate(this.getHeight() / 2)));
			}
		}else{
			//On considère que c'est forcément un environnement 2D. On ne traite pas les cas où D > 2
			for(Place p : env.getPlaces()){
				points.add(new Point(p.getCoordinate(0), p.getCoordinate(1)));
			}
		}
		
		this.setPreferredSize(new Dimension(width, height));
	}

	@Override
	public void update(Observable o, Object arg) {
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g){
		
		//On affiche tous les points de l'environnement
		g.setColor(COLOR_POINT);
		
		if(env.nbDim() == 1){
			int middle = (int) (this.getSize().getHeight() / 2);
			for(Place p : env.getPlaces()){
				//Si c'est en 1D, on place tous les points sur une ligne
				g.drawRect(p.getCoordinate(0), middle, 1, 1);
			}
		}else{
			//On considère que c'est forcément un environnement 2D. On ne traite pas les cas où D > 2
			for(Place p : env.getPlaces()){
				g.drawRect(p.getCoordinate(0), p.getCoordinate(1), 1, 1);
			}
		}
		
		//On change la couleur des points du chemin
		/*g.setColor(COLOR_PATH);
		if(env.nbDim() == 1){
			int middle = (int) (this.getSize().getHeight() / 2);
			for(Place p : algo.getPath()){
				//Si c'est en 1D, on place tous les points sur une ligne
				g.drawRect(p.getCoordinate(0), middle, 1, 1);
			}
		}else{
			//On considère que c'est forcément un environnement 2D. On ne traite pas les cas où D > 2
			for(Place p : algo.getPath()){
				g.drawRect(p.getCoordinate(0), p.getCoordinate(1), 1, 1);
			}
		}*/
	}
}
