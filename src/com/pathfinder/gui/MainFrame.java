package com.pathfinder.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainFrame extends JFrame {

	public MainFrame(PathPanel p, SettingsPanel s) {
		super("Pathfinder");

		JPanel main = new JPanel(new BorderLayout());
		main.add(p);
		main.add(s, BorderLayout.EAST);
		this.add(main);
		this.setResizable(false);

		this.pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		final PathPanel p = new PathPanel(25, 25, 25);
		final SettingsPanel s = new SettingsPanel(p);
		final MainFrame mf = new MainFrame(p, s);
		final long waitSpeed = 33L;

		new Thread(new Runnable() {
			public void run() {
				int framesRendered = 0;
				long oldTime = System.currentTimeMillis();
				while (true) {
					long old = System.currentTimeMillis();
					p.update();
					p.repaint();
					long delta = System.currentTimeMillis() - old;
					try {
						Thread.sleep(Math.max(0, waitSpeed - delta));
					} catch (InterruptedException e) {
					}
					framesRendered++;
					if (framesRendered >= 60) {
						long dt = System.currentTimeMillis() - oldTime;
						p.fps = (int) ((double) framesRendered / (double) (dt) * 1000);
						framesRendered = 0;
						oldTime = System.currentTimeMillis();
					}
				}
			}
		}).start();
		mf.setVisible(true);
	}
}
