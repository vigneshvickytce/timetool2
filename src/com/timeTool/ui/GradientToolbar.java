package com.timeTool.ui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

import javax.swing.JToolBar;

/**
 * This is a toolbar with a gradient background color. Rollover buttons are set by default.
 *
 */

public final class GradientToolbar extends JToolBar {
	private final Color	topColor;
	private final Color	bottomColor;


	/**
	 * Creates a gradient tollbar using the specified colors.
	 *
	 * @param topColor
	 *            The starting color for the gradient background.
	 * @param bottomColor
	 *            The ending color for the gradient background.
	 */
	public GradientToolbar(Color topColor, Color bottomColor) {
		super();

		this.topColor = topColor;
		this.bottomColor = bottomColor;

		setFloatable(false);
		setRollover(true);
		setOpaque(false);
	}


	// Overrides parent to paint the gradient background
	@Override
	public void paintComponent(Graphics g1) {
		final Graphics2D g2 = (Graphics2D) g1;
		final Paint oldPaint = g2.getPaint();

		super.paintComponent(g2);

		g2.setPaint(new GradientPaint(0, 0, topColor, 0, getHeight(), bottomColor));
		g2.fillRect(0, 0, getWidth(), getHeight());
		g2.setPaint(oldPaint);
	}
}