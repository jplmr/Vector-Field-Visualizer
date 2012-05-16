package com.pathfinder.gui.settings;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ComboBoxOption extends JPanel {

	public JLabel myLabel;
	public JComboBox<String> myJComboBox;

	public ComboBoxOption(String text, String[] options, int width, int def) {
		myJComboBox = new JComboBox<String>(options);
		myJComboBox.setSelectedIndex(def);
		myJComboBox.setFocusable(false);
		myLabel = new JLabel(text + ": ");

		this.setLayout(new GridLayout(1, 2));
		this.add(myLabel);
		this.add(myJComboBox);
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setMaximumSize(new Dimension(width, 30));
	}

}
