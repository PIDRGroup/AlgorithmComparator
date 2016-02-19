package view;

import javax.swing.JFrame;

public class MainWindow extends JFrame{
	
	public MainWindow(){
		this.setTitle("AlgoComparator");
		this.setContentPane(new HomeMenu());
		this.pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new MainWindow();
	}
}
