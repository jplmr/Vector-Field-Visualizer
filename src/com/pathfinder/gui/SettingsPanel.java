package com.pathfinder.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.pathfinder.gui.settings.BooleanOption;
import com.pathfinder.gui.settings.ComboBoxOption;
import com.pathfinder.gui.settings.SliderOption;
import com.pathfinder.gui.settings.SpinnerOption;

public class SettingsPanel extends JPanel {

	private PathPanel myPathPanel;

	public SettingsPanel(final PathPanel p) {
		this.setPreferredSize(new Dimension(250, 600));
		myPathPanel = p;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		JLabel headerLabel = new JLabel("2D Vector Field Pathfinding");
		headerLabel.setFont(new Font("Arial", 0, 17));
		headerLabel.setHorizontalTextPosition(JLabel.CENTER);

		JLabel subHeading = new JLabel("Settings");
		subHeading.setFont(new Font("Arial", 1, 12));

		JPanel headerPanel = new JPanel(new GridLayout(2, 1));
		headerPanel.add(headerLabel);
		headerPanel.add(subHeading);
		headerPanel.setBorder(new EmptyBorder(5, 5, 1, 5));
		headerPanel.setSize(new Dimension(250, 40));
		headerPanel.setMaximumSize(new Dimension(250, 40));

		this.add(headerPanel);

		final SliderOption desiredSpeed = new SliderOption("Delta Time value between frames", 0, 100, 250, p.desiredSpeed);
		desiredSpeed.mySlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				p.desiredSpeed = desiredSpeed.mySlider.getValue();
				desiredSpeed.updateLabel();
			}
		});
		this.add(desiredSpeed);

		final SliderOption particleSize = new SliderOption("Particle Size", 1, 20, 250, p.particleSize);
		particleSize.mySlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				p.particleSize = particleSize.mySlider.getValue();
				particleSize.updateLabel();
			}
		});
		this.add(particleSize);

		final SliderOption particleOpacity = new SliderOption("Particle Opacity", 1, 100, 250, p.particleOpacity);
		particleOpacity.mySlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				p.particleOpacity = particleOpacity.mySlider.getValue();
				particleOpacity.updateLabel();
			}
		});
		this.add(particleOpacity);

		JPanel options2 = new JPanel(new GridLayout(4, 2));
		options2.setMaximumSize(new Dimension(250, 80));

		final BooleanOption drawAxes = new BooleanOption("Axes", 250, p.drawAxes);
		drawAxes.myCheckBox.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				p.drawAxes = drawAxes.myCheckBox.isSelected();
			}
		});
		options2.add(drawAxes);

		final SpinnerOption goalInf = new SpinnerOption("Goal Inf.", 25.0, 75.0, 1.0, 30.0, 125);
		goalInf.mySpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				p.goalInfluence = (Double) goalInf.mySpinner.getValue();
			}
		});
		options2.add(goalInf);

		final BooleanOption drawVectors = new BooleanOption("Vectors", 250, p.drawVectors);
		drawVectors.myCheckBox.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				p.drawVectors = drawVectors.myCheckBox.isSelected();
			}
		});
		options2.add(drawVectors);

		final SpinnerOption fieldInf = new SpinnerOption("Field Inf.", 0.5, 2.0, 0.01, 0.75, 125);
		fieldInf.mySpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				p.fieldInfluence = (double) fieldInf.mySpinner.getValue();
			}
		});
		options2.add(fieldInf);

		final BooleanOption drawGrid = new BooleanOption("Grid", 250, p.drawGrid);
		drawGrid.myCheckBox.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				p.drawGrid = drawGrid.myCheckBox.isSelected();
			}
		});
		options2.add(drawGrid);

		final SpinnerOption deadZone = new SpinnerOption("Deadzone", 0.5, 6.0, 0.5, 3.0, 125);
		deadZone.mySpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				p.deadZone = (double) deadZone.mySpinner.getValue();
			}
		});
		options2.add(deadZone);

		final BooleanOption drawParticles = new BooleanOption("Particles", 250, p.drawParticles);
		drawParticles.myCheckBox.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				p.drawParticles = drawParticles.myCheckBox.isSelected();
			}
		});
		options2.add(drawParticles);

		final BooleanOption drawPoints = new BooleanOption("Draw Pts", 250, p.drawPoints);
		drawPoints.myCheckBox.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				boolean b = drawPoints.myCheckBox.isSelected();
				if (b && !p.drawPoints) {
					p.point1X = 100;
					p.point1Y = 100;
					p.point2X = 500;
					p.point2Y = 500;
				}
				p.drawPoints = b;
			}
		});

		options2.add(drawPoints);
		options2.setBorder(new EmptyBorder(0, 5, 0, 5));
		this.add(options2);

		final ComboBoxOption numberOfParticles = new ComboBoxOption("Number of Particles", new String[] { "100", "500", "1,000", "2,500", "5,000" }, 250, 2);
		numberOfParticles.myJComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int i = numberOfParticles.myJComboBox.getSelectedIndex();
				switch (i) {
				case 0:
					p.changeIndicatorSize(100);
					break;
				case 1:
					p.changeIndicatorSize(500);
					break;
				case 2:
					p.changeIndicatorSize(1000);
					break;
				case 3:
					p.changeIndicatorSize(2500);
					break;
				case 4:
					p.changeIndicatorSize(5000);
					break;
				}
			}
		});
		this.add(numberOfParticles);

		final ComboBoxOption equationToUse = new ComboBoxOption("Equation", new String[] { "F = <x-y, x+y>", "F = <x, y>", "F = <-x, -y>", "F = <x, y-x>", "<html>F = rsin&#952;, rcos&#952;</html>" }, 250, 0);
		equationToUse.myJComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				p.setVectorField(equationToUse.myJComboBox.getSelectedIndex());
			}
		});
		this.add(equationToUse);

		final ComboBoxOption spawnType = new ComboBoxOption("Spawn Type", new String[] { "Random Anywhere", "Centered Circle", "Random Center" }, 250, 0);
		spawnType.myJComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				p.spawnType = spawnType.myJComboBox.getSelectedIndex();
			}
		});
		this.add(spawnType);

		JButton reset = new JButton("Reset All Particles");
		JPanel resetPanel = new JPanel(new GridLayout(1, 1));
		resetPanel.add(reset);
		resetPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		resetPanel.setSize(new Dimension(250, 40));
		resetPanel.setMaximumSize(new Dimension(250, 35));
		reset.setFocusable(false);
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				p.resetAll();
			}
		});
		this.add(resetPanel);

		JPanel explainPanel = new JPanel(new GridLayout(1, 1));
		explainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		explainPanel.setSize(new Dimension(250, 40));
		explainPanel.setMaximumSize(new Dimension(250, 350));
		JEditorPane jt = new JEditorPane();
		jt.setBorder(null);
		jt.setEditable(false);
		jt.setContentType("text/html");

		jt.setText("<html><p style=\"font-family:Arial\"><b>About</b> "
				+ "<br /><br /> This is a visual representation of a 2D vector field. The above options change the settings for the graph on the left."
				+ "<br /><br /> A line is drawn between points 1 and 2 representing roughly the best path that should be followed in order to do the least amount of work when travelling through the field."
				+ " You can click and drag the points to view different optimal paths." + "<br /><br /></p></html>");

		jt.setBackground(this.getBackground());

		explainPanel.add(jt);
		this.add(explainPanel);

	}
}
