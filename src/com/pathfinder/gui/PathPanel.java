package com.pathfinder.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.pathfinder.function.Function;

public class PathPanel extends JPanel implements MouseMotionListener, MouseListener {

	private int width = 0, height = 0;
	private int spacing = 0;
	private ArrayList<Point> myPoints;
	private double[][][] arrows;
	private double[][] indicators;
	public double[][] gridTemp1;
	public double[][] gridTemp2;
	public int fps = 0;

	// Configurable Settings
	public int desiredSpeed = 50;
	public int particleSize = 10;
	public int particleOpacity = 20;
	public boolean drawAxes = true;
	public boolean drawVectors = true;
	public boolean drawGrid = true;
	public boolean drawParticles = true;
	public int spawnType = 0;
	public double goalInfluence = 30.0;
	public double fieldInfluence = 0.75;
	public double deadZone = 3.0;
	public boolean drawPoints = false;

	// vectorField specifies the equation.
	// 0: F(x,y) = (x-y)i + (x+y)j
	// 1: F(x,y) = xi + yj
	// 2: F(x,y) = -xi - yj
	// 3: F(x,y) = xi + (y-x)j
	private int vectorField = 0;

	public void setVectorField(int vf) {
		vectorField = vf;
		maxVelocity = 0.0;
	}

	public int point1X = 100;
	public int point1Y = 100;
	public int point2X = 500;
	public int point2Y = 500;
	private int pointRadius = 8;

	public PathPanel(int ws, int hs, int spa) {
		spacing = spa;
		width = ws * spacing;
		height = hs * spacing;
		myPoints = new ArrayList<Point>();
		arrows = new double[ws + 10][hs + 10][2];
		indicators = new double[1000][4];
		this.setPreferredSize(new Dimension(width, height));
		this.addMouseMotionListener(this);
		this.addMouseListener(this);

		gridTemp1 = new double[100][2];
		gridTemp2 = new double[100][2];
		for (int i = 0; i < gridTemp1.length; i++) {
			gridTemp1[i][0] = Math.cos(Math.PI * 2 * ((double) i / (double) gridTemp1.length)) * 10;
			gridTemp1[i][1] = Math.sin(Math.PI * 2 * ((double) i / (double) gridTemp1.length)) * 10;
		}

		for (int i = 0; i < indicators.length; i++) {
			spawnNew(i);
		}
	}

	public void changeIndicatorSize(int newSize) {
		indicators = new double[newSize][4];
		for (int i = 0; i < indicators.length; i++) {
			spawnNew(i);
		}
	}

	public void update() {
		for (int i = 0; i < arrows.length; i++) {
			for (int j = 0; j < arrows[i].length; j++) {
				double x = (i * spacing - (width / 2));
				double y = (j * spacing - (height / 2));

				double xN = x;
				double yN = y;

				// Formula here !!!!!11!!
				x = x(xN, yN);
				y = y(xN, yN);

				x = x / 6.0;
				y = y / 6.0;
				arrows[i][j][0] = x;
				arrows[i][j][1] = y;
			}
		}

		for (int i = 0; i < indicators.length; i++) {
			indicators[i][2] = indicators[i][0];
			indicators[i][3] = indicators[i][1];

			double xN = indicators[i][0] - width / 2;
			double yN = indicators[i][1] - height / 2;
			double tempX = x(xN, yN);
			double tempY = y(xN, yN);
			double mag = Math.sqrt(xN * xN + yN * yN);

			indicators[i][0] += (tempX * (((double) desiredSpeed / 1000.0)));
			indicators[i][1] += (tempY * (((double) desiredSpeed / 1000.0)));
			if (indicators[i][0] < 0
					|| mag >= (Math.sqrt(2) * width / 2)
					|| (vectorField == 2 && (indicators[i][0] < width / 2 + 6.0 && indicators[i][0] > width / 2 - 6.0 && indicators[i][1] < height / 2 + 6.0 && indicators[i][1] > height / 2 - 6.0))) {
				spawnNew(i);
			}

		}

	}

	private int numOff = 0;

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.white);
		g2.fillRect(0, 0, width, height);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (drawGrid) {
			g2.setColor(Color.red);
			g2.setStroke(new BasicStroke(0.3f));

			for (int uniqueI = 0; uniqueI < gridTemp1.length; uniqueI++) {
				gridTemp1[uniqueI][0] = Math.cos(Math.PI * 2 * ((double) uniqueI / (double) gridTemp1.length)) * 10.0;
				gridTemp1[uniqueI][1] = Math.sin(Math.PI * 2 * ((double) uniqueI / (double) gridTemp1.length)) * 10.0;
			}

			for (int ja = 0; ja < 10; ja++) {
				for (int io = 0; io < gridTemp1.length; io++) {
					try {
						g2.drawLine(width / 2 + (int) gridTemp1[io][0], height / 2 + (int) gridTemp1[io][1], width / 2 + (int) gridTemp1[io + 1][0], height / 2
								+ (int) gridTemp1[io + 1][1]);
					} catch (IndexOutOfBoundsException e) {
						g2.drawLine(width / 2 + (int) gridTemp1[0][0], height / 2 + (int) gridTemp1[0][1], width / 2 + (int) gridTemp1[io][0], height / 2 + (int) gridTemp1[io][1]);
					}

					gridTemp2[io][0] = gridTemp1[io][0] + x(gridTemp1[io][0], gridTemp1[io][1]);
					gridTemp2[io][1] = gridTemp1[io][1] + y(gridTemp1[io][0], gridTemp1[io][1]);

					try {
						g2.drawLine(width / 2 + (int) gridTemp1[io][0], height / 2 + (int) gridTemp1[io][1], width / 2 + (int) gridTemp2[io][0], height / 2
								+ (int) gridTemp2[io][1]);
					} catch (IndexOutOfBoundsException e) {
						g2.drawLine(width / 2 + (int) gridTemp1[0][0], height / 2 + (int) gridTemp1[0][1], width / 2 + (int) gridTemp2[io][0], height / 2 + (int) gridTemp2[io][1]);
					}

				}
				for (int ma = 0; ma < gridTemp1.length; ma++) {
					gridTemp1[ma][0] = gridTemp2[ma][0];
					gridTemp1[ma][1] = gridTemp2[ma][1];
				}
			}

		}
		if (drawAxes) {
			g2.setColor(Color.black);
			g2.drawLine(width / 2, 0, width / 2, height);
			g2.drawLine(0, height / 2, width, height / 2);
		}

		if (drawGrid) {
			g2.setColor(Color.red);
			g2.setStroke(new BasicStroke(0.3f));

			for (int uniqueI = 0; uniqueI < gridTemp1.length; uniqueI++) {
				gridTemp1[uniqueI][0] = Math.cos(Math.PI * 2 * ((double) uniqueI / (double) gridTemp1.length)) * 10.0;
				gridTemp1[uniqueI][1] = Math.sin(Math.PI * 2 * ((double) uniqueI / (double) gridTemp1.length)) * 10.0;
			}

			for (int ja = 0; ja < 10; ja++) {
				for (int io = 0; io < gridTemp1.length; io++) {
					try {
						g2.drawLine(width / 2 + (int) gridTemp1[io][0], height / 2 + (int) gridTemp1[io][1], width / 2 + (int) gridTemp1[io + 1][0], height / 2
								+ (int) gridTemp1[io + 1][1]);
					} catch (IndexOutOfBoundsException e) {
						g2.drawLine(width / 2 + (int) gridTemp1[0][0], height / 2 + (int) gridTemp1[0][1], width / 2 + (int) gridTemp1[io][0], height / 2 + (int) gridTemp1[io][1]);
					}

					gridTemp2[io][0] = gridTemp1[io][0] + x(gridTemp1[io][0], gridTemp1[io][1]);
					gridTemp2[io][1] = gridTemp1[io][1] + y(gridTemp1[io][0], gridTemp1[io][1]);

					try {
						g2.drawLine(width / 2 + (int) gridTemp1[io][0], height / 2 + (int) gridTemp1[io][1], width / 2 + (int) gridTemp2[io][0], height / 2
								+ (int) gridTemp2[io][1]);
					} catch (IndexOutOfBoundsException e) {
						g2.drawLine(width / 2 + (int) gridTemp1[0][0], height / 2 + (int) gridTemp1[0][1], width / 2 + (int) gridTemp2[io][0], height / 2 + (int) gridTemp2[io][1]);
					}

				}
				for (int ma = 0; ma < gridTemp1.length; ma++) {
					gridTemp1[ma][0] = gridTemp2[ma][0];
					gridTemp1[ma][1] = gridTemp2[ma][1];
				}
			}

		}

		if (drawVectors) {
			for (int i = 0; i < arrows.length; i++) {
				for (int j = 0; j < arrows[0].length; j++) {
					if (drawVectors) {
						g2.setStroke(new BasicStroke(0.6f));
						g2.setColor(Color.black);
						g2.drawLine((int) (spacing * i), (int) (spacing * j), (int) (spacing * i + arrows[i][j][0]), (int) (spacing * j + arrows[i][j][1]));
						g2.drawLine((int) ((spacing * i + arrows[i][j][0])), (int) ((spacing * j + arrows[i][j][1])),
								(int) (spacing * i + 7 * arrows[i][j][0] / 8 + 3 * arrows[i][j][1] / 30), (int) (spacing * j + 7 * arrows[i][j][1] / 8 - 3 * arrows[i][j][0] / 30));
						g2.drawLine((int) ((spacing * i + arrows[i][j][0])), (int) ((spacing * j + arrows[i][j][1])),
								(int) (spacing * i + 7 * arrows[i][j][0] / 8 - 3 * arrows[i][j][1] / 30), (int) (spacing * j + 7 * arrows[i][j][1] / 8 + 3 * arrows[i][j][0] / 30));
					}
				}
			}
		}

		if (drawParticles)
			for (int i = 0; i < indicators.length; i++) {
				double dx = indicators[i][2] - indicators[i][0];
				double dy = indicators[i][3] - indicators[i][1];
				double magnitude = (Math.sqrt(dx * dx + dy * dy));

				if (indicators[i][2] != -1337.0) {
					g2.setColor(new Color(Color.HSBtoRGB(Math.min((float) (1.0 - magnitude / 60.0 - 0.3f), 0.5f), .9f, 0.8f)));
					g2.setColor(new Color((float) g2.getColor().getRed() / 255.0f, (float) g2.getColor().getGreen() / 255.0f, (float) g2.getColor().getBlue() / 255.0f,
							(float) ((float) particleOpacity / 100.0f)));
					g2.fillOval((int) indicators[i][0] - particleSize, (int) indicators[i][1] - particleSize, 2 * particleSize, 2 * particleSize);
				}
			}

		// Optimal Path Drawing

		if (drawPoints) {

			double cx = point1X - width / 2;
			double cy = height / 2 - point1Y;
			double destx = point2X - width / 2;
			double desty = height / 2 - point2Y;
			double ox = cx;
			double oy = cy;
			double work = 0;

			g2.setStroke(new BasicStroke(2.0f));
			g2.setColor(Color.black);

			while (Math.abs(cx - destx) > pointRadius * deadZone || Math.abs(cy - desty) > pointRadius * deadZone) {
				double length = Math.sqrt(Math.pow((destx - cx), 2) + Math.pow((desty - cy), 2));
				double xCompTowards = (destx - cx) / length * goalInfluence;
				double yCompTowards = (desty - cy) / length * goalInfluence;
				double xVector = y(cx, cy) / (15 - (double) desiredSpeed / 70.0) * fieldInfluence;
				double yVector = -x(cx, cy) / (15 - (double) desiredSpeed / 70.0) * fieldInfluence;

				cx = xCompTowards + xVector + cx;
				cy = yCompTowards + yVector + cy;
				g2.drawLine((int) ox + width / 2, height / 2 - (int) oy, (int) cx + width / 2, height / 2 - (int) cy);
				work += (cx - ox) * xVector + (cy - oy) * yVector;

				ox = cx;
				oy = cy;

			}

			if (!Double.isInfinite(ox) && !Double.isInfinite(oy) && !Double.isNaN(ox) && !Double.isNaN(oy))
				g2.drawLine((int) ox + width / 2, height / 2 - (int) oy, (int) destx + width / 2, height / 2 - (int) desty);

			g2.setColor(Color.black);
			g2.fillOval(point1X - pointRadius, point1Y - pointRadius, pointRadius * 2, pointRadius * 2);
			g2.fillOval(point2X - pointRadius, point2Y - pointRadius, pointRadius * 2, pointRadius * 2);

			g2.setColor(Color.white);
			g2.drawString("1", point1X - 2, point1Y + pointRadius / 2 - 1);
			g2.drawString("2", point2X - 2, point2Y + pointRadius / 2 - 1);

			g2.setColor(Color.white);
			g2.fillRect(width - 72, 0, 72, 11);
			g2.setColor(Color.black);
			g2.drawString("Work: " + work, width - 70, 10);

		}

		g2.setColor(Color.white);

		g2.fillRect(0, 0, 38, 11);
		g2.setColor(Color.black);
		g2.drawString("FPS: " + fps, 0, 10);

		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke(2.0f));

		g2.drawLine(width, 0, width, height);

	}

	private double maxVelocity = 0.0;

	public void resetAll() {
		for (int i = 0; i < indicators.length; i++) {
			spawnNew(i);
		}
	}

	public void spawnNew(int i) {
		switch (spawnType) {
		case 0:
			indicators[i][0] = Math.random() * width;
			indicators[i][1] = Math.random() * height;
			break;
		case 1:
			indicators[i][0] = width / 2 + Math.cos(i * 2 * Math.PI / indicators.length) * 5;
			indicators[i][1] = height / 2 - Math.sin(i * 2 * Math.PI / indicators.length) * 5;
			break;
		case 2:
			indicators[i][0] = (width / 2) + 20.0 * (Math.random() - 0.5);
			indicators[i][1] = (height / 2) + 20.0 * (Math.random() - 0.5);
			break;
		default:
			indicators[i][0] = Math.random() * width;
			indicators[i][1] = Math.random() * height;
			break;
		}
		indicators[i][2] = -1337.0;
		indicators[i][3] = -1337.0;
	}

	public double x(double x, double y) {
		switch (vectorField) {
		case 0:
			return x - y;
		case 1:
			return x;
		case 2:
			return -x;
		case 3:
			return x;
		case 4:
			double theta = Math.atan2(y, x);
			double mag = Math.sqrt(x * x + y * y);
			return Math.sin(theta) * mag;
		}
		return x;
	}

	public double y(double x, double y) {
		switch (vectorField) {
		case 0:
			return x + y;
		case 1:
			return y;
		case 2:
			return -y;
		case 3:
			return y - x;
		case 4:
			double theta = Math.atan2(y, x);
			double mag = Math.sqrt(x * x + y * y);
			return -Math.cos(theta) * mag;
		}
		return y;
	}

	public boolean mouseDownOnPt1 = false;
	public boolean mouseDownOnPt2 = false;

	@Override
	public void mouseDragged(MouseEvent e) {
		if (e.getX() > point1X - pointRadius && e.getX() < point1X + pointRadius && e.getY() > point1Y - pointRadius && e.getY() < point1Y + pointRadius && !mouseDownOnPt2
				|| mouseDownOnPt1) {
			point1X = e.getX();
			point1Y = e.getY();
			mouseDownOnPt1 = true;
			point1X = Math.min(width, point1X);
			point1X = Math.max(0, point1X);
			point1Y = Math.min(height, point1Y);
			point1Y = Math.max(0, point1Y);
		}
		if (e.getX() > point2X - pointRadius && e.getX() < point2X + pointRadius && e.getY() > point2Y - pointRadius && e.getY() < point2Y + pointRadius && !mouseDownOnPt1
				|| mouseDownOnPt2) {
			point2X = e.getX();
			point2Y = e.getY();
			mouseDownOnPt2 = true;
			point2X = Math.min(width, point2X);
			point2X = Math.max(0, point2X);
			point2Y = Math.min(height, point2Y);
			point2Y = Math.max(0, point2Y);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getX() > point1X - pointRadius && e.getX() < point1X + pointRadius && e.getY() > point1Y - pointRadius && e.getY() < point1Y + pointRadius || mouseDownOnPt1) {
			point1X = e.getX();
			point1Y = e.getY();
			mouseDownOnPt1 = true;
		}
		if (e.getX() > point2X - pointRadius && e.getX() < point2X + pointRadius && e.getY() > point2Y - pointRadius && e.getY() < point2Y + pointRadius || mouseDownOnPt2) {
			point2X = e.getX();
			point2Y = e.getY();
			mouseDownOnPt2 = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (mouseDownOnPt1) {
			mouseDownOnPt1 = false;
		}
		if (mouseDownOnPt2) {
			mouseDownOnPt2 = false;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
