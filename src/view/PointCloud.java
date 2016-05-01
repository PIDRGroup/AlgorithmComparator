package view;

import java.awt.*;
import java.util.*;
import javax.swing.JPanel;
import model.*;
import model.algo.Algorithm;
import model.env.Environment;
import model.env.Place;
import model.env.UnknownPlaceException;

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
	private boolean show_links;
	
	//On définit la couleur des noeuds du chemin, des noeuds qui ont été développés et des noeuds qui n'ont pas été touchés.
	public final static Color COLOR_POINT = Color.BLACK, COLOR_PATH = Color.GREEN, COLOR_EXPANDED = Color.LIGHT_GRAY, COLOR_LINE = Color.RED;
	
	//Constructeur pour vérifier les environnements
	public PointCloud(Environment env, int width, int height){
		this.setBackground(Color.WHITE);
		show_links = true;
		points = new ArrayList<Point>();
		this.env = env;
		
		//On considère que c'est forcément un environnement 2D. On ne traite pas les cas où D > 2
		for(Place p : env.getPlaces()){
			points.add(new Point(p.getCoordinate(0), p.getCoordinate(1)));
		}
		
		this.setPreferredSize(new Dimension(width, height));
		this.repaint();
	}
	
	public PointCloud(Environment e, Algorithm alg, int width, int height){
		
		this.setBackground(Color.WHITE);
		show_links = false;
		
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
	
	public void showLinks(boolean sl){
		show_links=sl;
		repaint();
	}

	@Override
	public void update(Observable o, Object arg) {
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g){
		this.setBackground(Color.WHITE);
		g.setColor(Color.WHITE);
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		//On affiche tous les points de l'environnement
		g.setColor(COLOR_POINT);
		
		if(env.nbDim() == 1){
			int middle = (int) (this.getSize().getHeight() / 2);
			for(Place p : env.getPlaces()){
				//Si c'est en 1D, on place tous les points sur une ligne
				g.fillRect(p.getCoordinate(0)-2, middle-2, 4, 4);
			}
		}else{
			//On considère que c'est forcément un environnement 2D. On ne traite pas les cas où D > 2
			for(Place p : env.getPlaces()){
				g.fillRect(p.getCoordinate(0) - 2, p.getCoordinate(1) - 2, 4, 4);
			}
		}
		
		//On change la couleur des points du chemin
		g.setColor(COLOR_PATH);
		if(algo != null && env.nbDim() == 1){
			int middle = (int) (this.getSize().getHeight() / 2);
			for(Place p : algo.getPath()){
				//Si c'est en 1D, on place tous les points sur une ligne
				g.fillRect(p.getCoordinate(0) - 4, middle - 4, 8, 8);
			}
		}else if(algo != null){
			//On considère que c'est forcément un environnement 2D. On ne traite pas les cas où D > 2
			for(Place p : algo.getPath()){
				g.fillRect(p.getCoordinate(0) - 3, p.getCoordinate(1) - 3, 6, 6);
			}
		}
		
		g.setColor(COLOR_LINE);
		//Si l'utilisateur le souhaite, on affiche les liens
		if(show_links){
			for (Place p : env.getPlaces()) {
				try {
					ArrayList<Place> links = env.getLinks(p.getIndex());
					for(Place l : links){
						g.drawLine(p.getCoordinate(0), p.getCoordinate(1), l.getCoordinate(0), l.getCoordinate(1));
					}
				} catch (UnknownPlaceException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
