package com.pathfinder.gui.settings;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class BooleanOption extends JPanel {

	public JLabel myLabel;
	public JCheckBox myCheckBox;

	public BooleanOption(String label, int width, boolean def) {
		myLabel = new JLabel(label + ": ");
		myCheckBox = new JCheckBox();
		myCheckBox.setSelected(def);
		myCheckBox.setFocusable(false);

		this.setLayout(new GridLayout(1, 2));
		//this.setBorder(new EmptyBorder(5, 5, 5, 5));

		this.setMaximumSize(new Dimension(width, 25));
		this.add(myLabel);
		this.add(myCheckBox);
	}

}
