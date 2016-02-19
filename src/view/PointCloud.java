package view;

import java.awt.*;
import java.util.*;
import javax.swing.JPanel;
import model.*;

/**
 * 
 * Cette classe permet d'afficher graphiquement un nuage de points repr�sentant un environnement � une ou deux
 * dimensions.
 *
 */
public class PointCloud extends JPanel implements Observer{
	
	private Algorithm algo;
	private ArrayList<Point> points;
	
	//On d�finit la couleur des noeuds du chemin, des noeuds qui ont �t� d�velopp�s et des noeuds qui n'ont pas �t� touch�s.
	private final static Color COLOR_POINT = Color.BLACK, COLOR_PATH = Color.GREEN, COLOR_EXPANDED = Color.LIGHT_GRAY;
	
	public PointCloud(Algorithm alg, int width, int height){
		algo = alg;
		this.setSize(new Dimension(width, height));
		
		//On cr�e l'ensemble des points initiaux.
		Environment env = algo.getEnv();
		if(env.nbDim() == 1){
			for(Place p : env.getPlaces()){
				//Si c'est en 1D, on place tous les points sur une ligne
				points.add(new Point(p.getCoordinate(0), p.getCoordinate(this.getHeight() / 2)));
			}
		}else{
			//On consid�re que c'est forc�ment un environnement 2D. On ne traite pas les cas o� D > 2
			for(Place p : env.getPlaces()){
				points.add(new Point(p.getCoordinate(0), p.getCoordinate(1)));
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g){
		
		Environment env = algo.getEnv();
		
		//On affiche tous les points de l'environnement
		g.setColor(COLOR_POINT);
		
		if(algo.getEnv().nbDim() == 1){
			int middle = (int) (this.getSize().getHeight() / 2);
			for(Place p : env.getPlaces()){
				//Si c'est en 1D, on place tous les points sur une ligne
				g.drawRect(p.getCoordinate(0), middle, 1, 1);
			}
		}else{
			//On consid�re que c'est forc�ment un environnement 2D. On ne traite pas les cas o� D > 2
			for(Place p : env.getPlaces()){
				g.drawRect(p.getCoordinate(0), p.getCoordinate(1), 1, 1);
			}
		}
		
		//On change la couleur des points du chemin
		g.setColor(COLOR_PATH);
		if(algo.getEnv().nbDim() == 1){
			int middle = (int) (this.getSize().getHeight() / 2);
			for(Place p : algo.getPath()){
				//Si c'est en 1D, on place tous les points sur une ligne
				g.drawRect(p.getCoordinate(0), middle, 1, 1);
			}
		}else{
			//On consid�re que c'est forc�ment un environnement 2D. On ne traite pas les cas o� D > 2
			for(Place p : algo.getPath()){
				g.drawRect(p.getCoordinate(0), p.getCoordinate(1), 1, 1);
			}
		}
	}
}
