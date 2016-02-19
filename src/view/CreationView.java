package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CreationView extends JPanel{
	
	private MainWindow parent;
	
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
		
		private JPanel panel_bounds;
			private JScrollPane pane_bounds;
			private ArrayList<BoundView> bounds;
		 
		private JPanel panel_dimensionW;
			private ButtonGroup group_bounds;
			private JRadioButton radio_min, radio_max, radio_avg, radio_fixed;
			private JPanel panel_fixed;
				private JLabel label_min, label_max;
				private JFormattedTextField field_min, field_max;
				
		public CreationView(MainWindow p){
			parent=p;
			
			label_name = new JLabel("Nom de l'expérience : ");
			field_name = new JTextField();
			field_name.setPreferredSize(new Dimension(400, 25));
			Calendar c = Calendar.getInstance();
			label_date = new JLabel("Date : " + c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.YEAR));
			
			panel_general = new JPanel();
			panel_general.add(label_name);
			panel_general.add(field_name);
			panel_general.add(label_date);
			
			check_algos = new ArrayList<JCheckBox>();
			check_algos.add(new JCheckBox("A*"));
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
			
			panel_nodes = new JPanel();
			label_nodes = new JLabel("Nombre de noeuds : ");
			NumberFormat integerFieldFormatter = NumberFormat.getIntegerInstance();
			integerFieldFormatter.setGroupingUsed(false);
			field_nodes = new JFormattedTextField(integerFieldFormatter);
			field_nodes.setPreferredSize(new Dimension(50, 25));
			label_dimensions = new JLabel("Nombre de dimensions : ");
			slider_dimensions = new JSlider(1, 50, 2);
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
				}
			});

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
			
			panel_env.setLayout(new BorderLayout());
			panel_env.add(panel_nodes, BorderLayout.NORTH);
			panel_env.add(pane_bounds, BorderLayout.CENTER);
			panel_env.add(panel_dimensionW, BorderLayout.SOUTH);
			
			this.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
			this.setLayout(new BorderLayout());
			this.add(panel_general, BorderLayout.NORTH);
			this.add(panel_algo, BorderLayout.CENTER);
			this.add(panel_env, BorderLayout.SOUTH);
		}
}
