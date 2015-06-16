package com.elvensmite;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class DrawGraph {
	JFrame frame = new JFrame("Auto-Updating Maps");
	JPanel panel = new JPanel();
	
	public DrawGraph() {
		panel.setLayout(new GridLayout(9,9));
		frame.add(panel);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new DrawGraph();
			}
		});
	}
	
}