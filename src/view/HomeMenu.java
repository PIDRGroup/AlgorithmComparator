package view;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class HomeMenu extends JPanel{
	
	//Affichage de la liste des expériences disponibles dans le dossier par défaut
	private JPanel panel_container;
	private JTextArea textarea_experiences;
	private JScrollPane pane_experiences;
	
	//Sélection du fichier de test avec drag and drop
	private JPanel panel_browse;
	private JTextField field_selected_file;
	private JButton btn_browse;
	
	//Création - chargement d'une expérience
	private JButton btn_new, btn_load;
	
	public HomeMenu(){
		
		//La liste des expériences dans un text area
		textarea_experiences = new JTextArea();
		textarea_experiences.setEditable(false);
		pane_experiences = new JScrollPane(textarea_experiences);
		
		//La zone de sélection de fichier
		btn_browse = new JButton("Parcourir");
		field_selected_file = new JTextField();
		
		panel_browse = new JPanel();
		panel_browse.add(field_selected_file);
		panel_browse.add(btn_browse);
		
		//On crée la zonebox
		panel_container = new JPanel();
		panel_container.setBorder(new TitledBorder("Expériences sauvegardées"));
		
		panel_container.setLayout(new BorderLayout());
		panel_container.add(pane_experiences, BorderLayout.NORTH);
		panel_container.add(panel_browse, BorderLayout.SOUTH);
		
		//On crée les boutons principaux.
		btn_new = new JButton("Créer une nouvelle expérience");
		btn_load = new JButton("Charger l'expérience sélectionée");
		
		this.setLayout(new BorderLayout());
		this.add(panel_container, BorderLayout.NORTH);
	}
}
