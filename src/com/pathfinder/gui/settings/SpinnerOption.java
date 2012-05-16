package com.pathfinder.gui.settings;

import java.awt.GridLayout;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class SpinnerOption extends JPanel {

	public JSpinner mySpinner;
	public JLabel myLabel;

	public SpinnerOption(String label, double min, double max, double interval, double def, int width) {
		this.setLayout(new GridLayout(1, 2));
		myLabel = new JLabel(label);
		mySpinner = new JSpinner(new SpinnerNumberModel(def, min, max, interval));
		this.add(myLabel);
		this.add(mySpinner);
		JFormattedTextField tf = ((JSpinner.DefaultEditor) mySpinner.getEditor()).getTextField();
		tf.setEditable(false);
	}

}
