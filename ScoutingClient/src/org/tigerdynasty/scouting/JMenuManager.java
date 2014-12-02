package org.tigerdynasty.scouting;



import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JMenuItem;

public class JMenuManager extends JMenuItem {
	Color dynastyOrange = new Color(241, 101, 33);
	String teamNum;
	int i;
	public JMenuManager(String s)
	{
		super(s);
	}

	public void setNumber(int i, String teamNum) {
		this.i = i;
		this.teamNum = teamNum;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
//		if (ui != null) {
//			Graphics scratchGraphics = (g == null) ? null : g.create();
//			try {
//				ui.update(scratchGraphics, this);
//			}
//			finally {
//				scratchGraphics.dispose();
//			}
//		}
		if (ClientDatabase.selected == i) {
			this.setBackground(dynastyOrange);
		} else {
			this.setBackground(Color.WHITE);
		}
		this.setOpaque(true);
		super.paintComponent(g2d);
		// g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);

	}

}