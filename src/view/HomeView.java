package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.Experience;

public class HomeView extends JPanel{
	
	//Affichage de la liste des expï¿½riences disponibles dans le dossier par dï¿½faut
	private JPanel panel_container;
	private JList<String> list_experiences;
	private JScrollPane pane_experiences;
	
	//Sï¿½lection du fichier de test avec drag and drop
	private JPanel panel_browse;
	private JTextField field_selected_file;
	private JButton btn_browse;
	private JFileChooser file_chooser;
	
	//Crï¿½ation - chargement d'une expï¿½rience
	private JPanel panel_main;
	private JButton btn_new, btn_load;
	
	private MainWindow parent;
	
	public HomeView(final MainWindow parent){
		this.parent = parent;
		
		//La liste des expï¿½riences dans un text area
		list_experiences = new JList<String>();
		list_experiences.setPreferredSize(new Dimension(600, 250));
		
		pane_experiences = new JScrollPane(list_experiences);
		
		pane_experiences.setMinimumSize(new Dimension(600, 250));
		pane_experiences.setMaximumSize(new Dimension(600, 250));
		
		//La zone de sï¿½lection de fichier
		btn_browse = new JButton("Parcourir");
		field_selected_file = new JTextField();
		field_selected_file.setPreferredSize(new Dimension(400, 25));
		field_selected_file.setDropMode(DropMode.INSERT);
		field_selected_file.setDropTarget(new DropTarget(field_selected_file, new DropTargetAdapter() {
			@Override
			public void drop(DropTargetDropEvent dtde) {
				dtde.acceptDrop(DnDConstants.ACTION_COPY);
				try {
					Transferable t = dtde.getTransferable();
					List l = null;
					if(t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
						l = (List) t.getTransferData(DataFlavor.javaFileListFlavor);
						File f = (File) l.get(0);
						field_selected_file.setText(f.getAbsolutePath());
					}
					
					dtde.dropComplete(true);
				} catch (UnsupportedFlavorException | IOException e) {
					dtde.dropComplete(false);
					e.printStackTrace();
				}
			}
		}));
		file_chooser = new JFileChooser();
		file_chooser.setFileFilter(new FileNameExtensionFilter("Fichiers de résultats d'expérience (.xp, .exp)", "xp", "exp"));
		
		btn_browse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				file_chooser.showOpenDialog(btn_browse);
				File file = file_chooser.getSelectedFile();
				field_selected_file.setText(file.getAbsolutePath());
			}
		});
		
		panel_browse = new JPanel();
		panel_browse.add(field_selected_file);
		panel_browse.add(btn_browse);
		
		//On crï¿½e la zonebox
		panel_container = new JPanel();
		TitledBorder border = new TitledBorder("Expï¿½riences sauvegardées");
		panel_container.setBorder(border);
		
		panel_container.setLayout(new BorderLayout());
		panel_container.add(pane_experiences, BorderLayout.NORTH);
		panel_container.add(panel_browse, BorderLayout.SOUTH);
		
		//On crï¿½e les boutons principaux.
		btn_new = new JButton("Créer une nouvelle expérience");
		btn_load = new JButton("Charger l'expérience sélectionée");
		
		btn_new.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				parent.switchCreation();
			}
		});
		
		btn_load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					parent.switchRes(Experience.load(field_selected_file.getText()));
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		panel_main = new JPanel();
		panel_main.add(btn_new);
		panel_main.add(btn_load);
		
		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		this.setLayout(new BorderLayout());
		this.add(panel_container, BorderLayout.NORTH);
		this.add(panel_main, BorderLayout.SOUTH);
	}
}
