package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PopupExtremity extends JDialog{
	private ArrayList<JFormattedTextField> fields;
	private JButton btn_validate;
	private boolean validate;
	
	private PopupExtremity(String title, int nb_dim, int def){
		this.setModal(true);
		
		validate = false;
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());
		container.add(new JLabel("Indiquer les coordonn�es de votre place : "), BorderLayout.NORTH);
		
		JPanel sub_container = new JPanel();
		sub_container.setLayout(new GridLayout(0, 5));
		
		this.setTitle(title);
		NumberFormat integerFieldFormatter = NumberFormat.getIntegerInstance();
		integerFieldFormatter.setGroupingUsed(false);
		
		fields = new ArrayList<JFormattedTextField>(nb_dim);
		for (int i = 0; i < nb_dim; i++) {
			JFormattedTextField jftf = new JFormattedTextField(integerFieldFormatter);
			jftf.setValue(new Long(def));
			jftf.setPreferredSize(new Dimension(50, 20));
			fields.add(jftf);
			
			JLabel label = new JLabel("D"+(i+1));
			JPanel sub_sub_cont = new JPanel();
			sub_sub_cont.add(label);
			sub_sub_cont.add(jftf);
			
			sub_container.add(sub_sub_cont);
		}
		
		
		
		btn_validate = new JButton("OK !");
		final PopupExtremity access = this;
		
		btn_validate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				validate = true;
				access.dispose();
			}
			
		});
		
		container.add(sub_container, BorderLayout.CENTER);
		container.add(btn_validate, BorderLayout.SOUTH);
		this.setContentPane(container);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		this.pack();
		this.setVisible(true);
	}
	
	public static ArrayList<Long> showPopup(String title,int nb_dim, int def){
		ArrayList<Long> res = new ArrayList<Long>();
		PopupExtremity pe = new PopupExtremity(title, nb_dim, def);
		
		while(!pe.validate){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		
		for (int i = 0; i < pe.fields.size(); i++) {
			res.add((Long) pe.fields.get(i).getValue());
		}
		
		return res;
	}
}
