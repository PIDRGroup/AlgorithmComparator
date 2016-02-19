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

public class HomeMenu extends JPanel{
	
	//Affichage de la liste des expériences disponibles dans le dossier par défaut
	private JPanel panel_container;
	private JList<String> list_experiences;
	private JScrollPane pane_experiences;
	
	//Sélection du fichier de test avec drag and drop
	private JPanel panel_browse;
	private JTextField field_selected_file;
	private JButton btn_browse;
	private JFileChooser file_chooser;
	
	//Création - chargement d'une expérience
	private JPanel panel_main;
	private JButton btn_new, btn_load;
	
	public HomeMenu(){
		
		//La liste des expériences dans un text area
		list_experiences = new JList<String>();
		list_experiences.setPreferredSize(new Dimension(600, 250));
		
		pane_experiences = new JScrollPane(list_experiences);
		
		pane_experiences.setMinimumSize(new Dimension(600, 250));
		pane_experiences.setMaximumSize(new Dimension(600, 250));
		
		//La zone de sélection de fichier
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
		
		//On crée la zonebox
		panel_container = new JPanel();
		TitledBorder border = new TitledBorder("Expériences sauvegardées");
		panel_container.setBorder(border);
		
		panel_container.setLayout(new BorderLayout());
		panel_container.add(pane_experiences, BorderLayout.NORTH);
		panel_container.add(panel_browse, BorderLayout.SOUTH);
		
		//On crée les boutons principaux.
		btn_new = new JButton("Créer une nouvelle expérience");
		btn_load = new JButton("Charger l'expérience sélectionée");
		
		panel_main = new JPanel();
		panel_main.add(btn_new);
		panel_main.add(btn_load);
		
		this.setLayout(new BorderLayout());
		this.add(panel_container, BorderLayout.NORTH);
		this.add(panel_main, BorderLayout.SOUTH);
	}
}
