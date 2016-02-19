package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Bound;

public class BoundView extends JPanel{
	private JLabel desc, l_min, l_max;
	private JFormattedTextField min, max;
	
	public BoundView(String d){
		desc = new JLabel(d);
		l_min = new JLabel("Borne inf.");
		l_max = new JLabel("Borne sup.");
		
		NumberFormat integerFieldFormatter = NumberFormat.getIntegerInstance();
		integerFieldFormatter.setGroupingUsed(false);
		min = new JFormattedTextField(integerFieldFormatter);
		max = new JFormattedTextField(integerFieldFormatter);
		
		min.setValue(new Integer(0));
		max.setValue(new Integer(600));
		
		this.setLayout(new GridLayout(1, 5));
		this.add(desc);
		this.add(l_min);
		this.add(min);
		this.add(l_max);
		this.add(max);
		this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
	}
	
	public int max(){
		return (int) max.getValue();
	}
	
	public int min(){
		return (int) min.getValue();
	}
	
	public Bound bound(){
		return new Bound(min(), max());
	}
}
