package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Algorithm;
import model.Bound;
import model.Environment;
import model.EnvironmentFactory;
import model.Experience;
import model.MultiplePlaceException;
import model.Place;
import model.UnknownPlaceException;

public class CreationView extends JPanel{
	
	private MainWindow parent;
	
	Place src, dest;
	
	private JButton btn_return;
	private JPanel panel_general;
		private JLabel label_name;
		private JTextField field_name;
		private JLabel label_date;
	
	private JPanel panel_algo;
		private ArrayList<JCheckBox> check_algos;
	
	private JPanel panel_env;
		private JPanel panel_nodes;
			private JLabel label_nodes, label_dimensions, label_slider;
			private JFormattedTextField field_nodes;
			private JSlider slider_dimensions;
			
		private JPanel panel_env_shape;
			private ButtonGroup group_shape;
			private JRadioButton radio_grid;
			private JRadioButton radio_alea;
		
		private JPanel panel_bounds;
			private JScrollPane pane_bounds;
			private ArrayList<BoundView> bounds;
			
		private JPanel panel_extremities;
			private JButton btn_set_src;
			private JButton btn_set_dest;
		 
		private JPanel panel_dimensionW;
			private ButtonGroup group_bounds;
			private JRadioButton radio_min, radio_max, radio_avg, radio_fixed;
			private JPanel panel_fixed;
				private JLabel label_min, label_max;
				private JFormattedTextField field_min, field_max;
				
		private JButton btn_launch;
				
		public CreationView(MainWindow p){
			parent=p;
			src = new Place(0, 0);
			dest = new Place(600, 600);
			
			label_name = new JLabel("Nom de l'expérience : ");
			field_name = new JTextField();
			field_name.setPreferredSize(new Dimension(400, 25));
			Calendar c = Calendar.getInstance();
			String date = c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR);
			label_date = new JLabel("Date : " + date);
			field_name.setText("exp-"+c.get(Calendar.DAY_OF_MONTH)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.YEAR)+"-"+c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND));
			
			panel_general = new JPanel();
			panel_general.add(label_name);
			panel_general.add(field_name);
			panel_general.add(label_date);
			
			check_algos = new ArrayList<JCheckBox>();
			check_algos.add(new JCheckBox("AStar"));
			check_algos.add(new JCheckBox("Dijkstra"));
			check_algos.add(new JCheckBox("GBFS"));
			check_algos.add(new JCheckBox("RBFS"));
			
			panel_algo = new JPanel();
			panel_algo.setBorder(new TitledBorder("Algorithmes"));
			panel_algo.setLayout(new GridLayout(0, 3));
			
			for (JCheckBox box : check_algos) {
				box.setSelected(true);
				panel_algo.add(box);
			}
			
			panel_env = new JPanel();
			panel_env.setBorder(new TitledBorder("Environnement"));
			
			panel_env_shape = new JPanel();
			group_shape = new ButtonGroup();
			radio_grid = new JRadioButton("Grille 2D");
			radio_grid.setSelected(true);
			radio_alea = new JRadioButton("Semi-aléatoire");
			group_shape.add(radio_alea);
			group_shape.add(radio_grid);
			panel_env_shape.add(radio_grid);
			panel_env_shape.add(radio_alea);
			
			radio_grid.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					slider_dimensions.setMaximum(2);
					for (int i = 0; i < bounds.size()-2; i++) {
						bounds.remove(bounds.size()-1);
					}
				}
			});
			
			radio_alea.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					slider_dimensions.setMaximum(50);
				}
			});
			
			panel_nodes = new JPanel();
			label_nodes = new JLabel("Nombre de noeuds : ");
			NumberFormat integerFieldFormatter = NumberFormat.getIntegerInstance();
			integerFieldFormatter.setGroupingUsed(false);
			field_nodes = new JFormattedTextField(integerFieldFormatter);
			field_nodes.setValue(new Long(5000));
			field_nodes.setPreferredSize(new Dimension(50, 25));
			label_dimensions = new JLabel("Nombre de dimensions : ");
			slider_dimensions = new JSlider(1, 2, 2);
			label_slider = new JLabel("2");
			
			panel_nodes.add(label_nodes);
			panel_nodes.add(field_nodes);
			panel_nodes.add(label_dimensions);
			panel_nodes.add(slider_dimensions);
			panel_nodes.add(label_slider);
			
			panel_bounds = new JPanel();
			pane_bounds = new JScrollPane(panel_bounds);
			pane_bounds.setPreferredSize(new Dimension(600, 250));
			bounds = new ArrayList<BoundView>();
			panel_bounds.setLayout(new GridLayout(0, 1));
			
			bounds.add(new BoundView("D1"));
			bounds.add(new BoundView("D2"));
			panel_bounds.add(bounds.get(0));
			panel_bounds.add(bounds.get(1));
			
			slider_dimensions.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					int nb_dim = slider_dimensions.getValue();
					label_slider.setText(nb_dim+"");
					int size = bounds.size();
					
					if(nb_dim > size){
						for(int i=1; i<=nb_dim - size; i++){
							BoundView bv = new BoundView("D"+(size+i)+"");
							bounds.add(bv);
							panel_bounds.add(bv);
						}
					}else if(nb_dim < size){
						for(int i=1; i<=size - nb_dim; i++){
							panel_bounds.remove(bounds.get(size-i));
							bounds.remove(size-i);
						}
					}
					
					//On crée une nouvelle place par défaut d'arrivée et de départ
					ArrayList<Long> l_src = new ArrayList<Long>(), l_dest = new ArrayList<Long>();
					for (int i = 0; i < nb_dim; i++) {
						l_src.add(new Long(0));
						l_dest.add(new Long(600));
					}
					
					src = new Place(l_src);
					dest = new Place(l_dest);
				}
			});
			
			panel_extremities = new JPanel();
			btn_set_src = new JButton("Départ");
			btn_set_dest= new JButton("Arrivée");
			
			btn_set_src.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ArrayList<Long> l = PopupExtremity.showPopup("Place de départ", slider_dimensions.getValue(), 0);
					src = new Place(l);
				}
			});
			
			btn_set_dest.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ArrayList<Long> l = PopupExtremity.showPopup("Place d'arrivée", slider_dimensions.getValue(), 600);	
					dest = new Place(l);
				}
			});
			
			panel_extremities.add(btn_set_src);
			panel_extremities.add(btn_set_dest);

			panel_dimensionW = new JPanel();
			panel_dimensionW.setBorder(new TitledBorder("Dimension W (dimension des contraintes)"));
			panel_dimensionW.setLayout(new GridLayout(4, 1));
			
			radio_min = new JRadioButton("Min. des bornes des autres coordonnées");
			radio_max = new JRadioButton("Max. des bornes des autres coordonnées");
			radio_avg= new JRadioButton("Moyenne des bornes des autres coordonnées");
			radio_fixed = new JRadioButton("Bornes manuelles");
			
			group_bounds = new ButtonGroup();
			group_bounds.add(radio_min);
			group_bounds.add(radio_max);
			group_bounds.add(radio_avg);
			group_bounds.add(radio_fixed);
			group_bounds.setSelected(radio_min.getModel(), true);
			
			label_min = new JLabel("Min : ");
			label_max = new JLabel("Max : ");
			field_min = new JFormattedTextField(integerFieldFormatter);
			field_max = new JFormattedTextField(integerFieldFormatter);
			field_min.setValue(new Long(50));
			field_max.setValue(new Long(1000));
			
			panel_fixed = new JPanel();
			panel_fixed.setLayout(new BoxLayout(panel_fixed, BoxLayout.X_AXIS));
			panel_fixed.add(radio_fixed);
			panel_fixed.add(label_min);
			panel_fixed.add(field_min);
			panel_fixed.add(label_max);
			panel_fixed.add(field_max);
			field_min.setEnabled(false);
			field_max.setEnabled(false);
			
			radio_fixed.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					field_min.setEnabled(true);
					field_max.setEnabled(true);
				}
			});
			
			ActionListener deactivate_listener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					field_min.setEnabled(false);
					field_max.setEnabled(false);
				}
			};
			
			radio_avg.addActionListener(deactivate_listener);
			radio_max.addActionListener(deactivate_listener);
			radio_min.addActionListener(deactivate_listener);
			
			panel_dimensionW.add(radio_min);
			panel_dimensionW.add(radio_max);
			panel_dimensionW.add(radio_avg);
			panel_dimensionW.add(panel_fixed);
			
			JPanel north = new JPanel();
			north.setLayout(new BorderLayout());
			north.add(panel_env_shape, BorderLayout.NORTH);
			north.add(panel_nodes, BorderLayout.SOUTH);
			
			JPanel center = new JPanel();
			center.setLayout(new BorderLayout());
			center.add(pane_bounds, BorderLayout.NORTH);
			center.add(panel_extremities, BorderLayout.SOUTH);
			
			panel_env.setLayout(new BorderLayout());
			panel_env.add(north, BorderLayout.NORTH);
			panel_env.add(center, BorderLayout.CENTER);
			panel_env.add(panel_dimensionW, BorderLayout.SOUTH);
			
			JPanel container = new JPanel();
			
			container.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
			container.setLayout(new BorderLayout());
			container.add(panel_general, BorderLayout.NORTH);
			container.add(panel_algo, BorderLayout.CENTER);
			container.add(panel_env, BorderLayout.SOUTH);
			
			btn_launch = new JButton("Lancer l'expérience !");
			btn_launch.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String err_mess = check();
					if(err_mess.equals("")){
						//On lance l'expérience
						ArrayList<Bound> b = new ArrayList<Bound>();
						for(BoundView bv : bounds) b.add(bv.bound());
						Environment env = null;
						try {
							if(radio_alea.isSelected())
								env = EnvironmentFactory.generateAlea((int) (long) field_nodes.getValue(), b);
							else if(radio_grid.isSelected())
								env = EnvironmentFactory.generateUniformGrid2D((int) (long) field_nodes.getValue(), b.get(0), b.get(1));
						} catch (MultiplePlaceException | UnknownPlaceException e) {
							e.printStackTrace();
						}
						Experience exp = new Experience(field_name.getText(), label_date.getText(), env, env.alea(), env.alea());
						for(JCheckBox box : check_algos){
							if(box.isSelected()){
								Algorithm a;
								try {
									Class alg = Class.forName("model."+box.getText());
									a = (Algorithm) alg.getConstructor().newInstance();
									exp.addAlgo(a);
								} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
										| InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
									e.printStackTrace();
								}
								
							}
						}
						
						try {
							exp.launch();
						} catch (UnknownPlaceException e) {
							e.printStackTrace();
						}
						
					}else{
						JOptionPane.showMessageDialog(btn_launch, err_mess, "Une erreur est survenue !", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			
			btn_return = new JButton("Retour");
			btn_return.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					parent.switchHome();
				}
			});
			
			JPanel resizer = new JPanel(); //Un JPanel pour contrôler la taille du bouton de retour (histoire qu'il ne prenne pas toute la largeur)
			resizer.setLayout(new BoxLayout(resizer, BoxLayout.X_AXIS));
			resizer.add(btn_return);
			
			this.setLayout(new BorderLayout());
			this.add(resizer, BorderLayout.NORTH);
			this.add(container,BorderLayout.CENTER);
			this.add(btn_launch, BorderLayout.SOUTH);
		}
		
		/**
		 * Vérifie les entrées utilisateurs.
		 * 
		 * @return Renvoie le message d'erreur. Si aucune n'a été détectée, renvoie une chaine vide.
		 */
		public String check(){
			String err_mess = "";
			
			String name = field_name.getText();
			if(name == null || name.isEmpty() || name.equals("") || name.trim().equals("")){
				err_mess += " - Le nom de l'expérience doit être renseigné !\n";
			}
			
			int nb_algos = 0;
			for(JCheckBox box : check_algos){
				nb_algos+= (box.isSelected()) ? 1 : 0;
			}
			
			if(nb_algos == 0){
				err_mess += " - Au moins un algo doit-être sélectionné !\n";
			}
			
			long nb_noeuds = (long) field_nodes.getValue();
			if(nb_noeuds == 0){
				err_mess+=" - Vous devez indiquer le nombre de noeuds de l'expérience !\n";
			}
			
			String bounds_error = " - Bornes dans les dimensions : ";
			int errors=0;
			for(BoundView bv : bounds){
				if(bv.min()>bv.max() || bv.min()<0 || bv.max()<0){
					errors++;
					bounds_error+=bv.desc()+", ";
				}
			}
			
			if(errors > 0) err_mess+=bounds_error+" !\n";
			
			if(radio_fixed.isSelected()){
				long min = (long) field_min.getValue();
				long max = (long) field_max.getValue();
				
				if(min == 0){
					err_mess+=" - Vous avez choisi de fixer les bornes, vous devez donc indiquer la borne min !\n";
				}
				
				if(max == 0){
					err_mess+=" - Vous avez choisi de fixer les bornes, vous devez donc indiquer la borne max !\n";
				}
				
				if(max < min){
					err_mess+=" - La borne inf. doit être plus petite que la borne sup. dans la dimension W !\n";
				}
				
				if(min<0 || max<0){
					err_mess+=" - Les bornes ne peuvent pas être négatives !\n";
				}
			}
			
			int nb_dim = slider_dimensions.getValue();
			
			if(src.getCoordinates().length != nb_dim){
				err_mess+="Le nombre de dimensions des places aux extrémités n'est pas en accord avec le nombre de l'env !\n";
			}
			
			return err_mess;
		}
}
