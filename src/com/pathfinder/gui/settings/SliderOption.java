package com.pathfinder.gui.settings;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;

public class SliderOption extends JPanel {

	public JLabel myLabel;
	public JSlider mySlider;
	private String baseLabel;

	public SliderOption(String label, int min, int max, int width, int def) {

		this.setLayout(new GridLayout(2, 1));

		baseLabel = label;
		myLabel = new JLabel(label + ": " + def);
		myLabel.setPreferredSize(new Dimension(width, 15));
		mySlider = new JSlider(min, max);
		mySlider.setPreferredSize(new Dimension(width, 20));
		mySlider.setValue(def);
		mySlider.setFocusable(false);
		mySlider.setPaintTicks(true);
		mySlider.setSnapToTicks(true);
		this.setBorder(new EmptyBorder(1, 5, 1, 5));
		this.add(myLabel);
		this.add(mySlider);
		this.setMaximumSize(new Dimension(width, 65));

	}

	public void updateLabel() {
		myLabel.setText(baseLabel + ": " + mySlider.getValue());
	}
}
