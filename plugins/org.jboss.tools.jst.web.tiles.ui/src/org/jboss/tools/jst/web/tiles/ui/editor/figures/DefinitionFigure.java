/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.tiles.ui.editor.figures;

import java.util.List;
import java.util.Vector;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.jface.resource.ImageDescriptor;

import org.jboss.tools.common.gef.GEFGraphicalViewer;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.jst.web.tiles.ui.TilesUIPlugin;
import org.jboss.tools.jst.web.tiles.ui.editor.TilesEditor;
import org.jboss.tools.jst.web.tiles.ui.editor.edit.DefinitionEditPart;
import org.jboss.tools.jst.web.tiles.ui.editor.figures.xpl.FixedConnectionAnchor;
import org.jboss.tools.jst.web.tiles.ui.editor.model.IDefinition;
import org.jboss.tools.jst.web.tiles.ui.editor.print.PrintIconHelper;

public class DefinitionFigure extends NodeFigure implements HandleBounds,
		FigureListener, MouseListener, MouseMotionListener, KeyListener {
	private static final Dimension SIZE = new Dimension(114, 21);

	protected Color bgColor = new Color(null, 0xe2, 0xef, 0xdb);

	public static final Image expandIcon = ImageDescriptor.createFromFile(
			TilesEditor.class, "icons/tiles_minus.gif").createImage(); //$NON-NLS-1$

	public static final Image collapseIcon = ImageDescriptor.createFromFile(
			TilesEditor.class, "icons/tiles_plus.gif").createImage(); //$NON-NLS-1$

	private Image icon = null;

	public IDefinition definition;

	private Label label = null;

	String path;

	DefinitionEditPart editPart;

	public void setGroupEditPart(DefinitionEditPart part) {
		editPart = part;
	}

	public void setConstraint(IFigure child, Object constraint) {
		super.setConstraint(child, constraint);
	}

	public void setPath(String path) {
		this.path = path;
		if (label != null) {
			label.setText(path);
			label.setSize(label.getPreferredSize());
		}
	}

	public void refreshFont() {
		if (label != null) {
			label.setText(definition.getName());
			label.setFont(definition.getTilesModel().getOptions()
					.getDefinitionNameFont());
			label.setSize(label.getPreferredSize());
			label.setLocation(new Point(getLocation().x + 24, getLocation().y
					- (7 + definition.getTilesModel().getOptions()
							.getDefinitionNameFont().getFontData()[0]
							.getHeight())));
		}
	}

	public void setIcon(Image i) {
		icon = PrintIconHelper.getPrintImage(i);
	}

	public void addNotify() {
		if (definition == null)
			return;
		label = new Label(path);
		label.setFont(definition.getTilesModel().getOptions()
				.getDefinitionNameFont());
		getParent().add(label);
		label.setForegroundColor(ColorConstants.black);
		label.setOpaque(false);
		label.setText(path);
		label.setVisible(true);
		label.setSize(label.getPreferredSize());
		label
				.setLocation(new Point(getLocation().x + 24, getLocation().y
						- (7 + definition.getTilesModel().getOptions()
								.getDefinitionNameFont().getFontData()[0]
								.getHeight())));
		label.addMouseListener(this);
		addMouseListener(this);
	}

	public void removeNotify() {
		if (definition == null)
			return;
		label.removeMouseListener(this);
		removeMouseListener(this);
		getParent().remove(label);
	}

	public void figureMoved(IFigure source) {
		if (definition != null && label != null)
			label.setLocation(new Point(getLocation().x + 24,
					getLocation().y - 14));
	}

	public void init(int number) {
		FixedConnectionAnchor c;
		if (number == 0)
			number = 1;
		for (int i = 0; i < number; i++) {
			c = new FixedConnectionAnchor(this);
			c.offsetV = 32 + LINK_HEIGHT * i;
			c.leftToRight = false;
			connectionAnchors.put((i + 1) + "_OUT", c); //$NON-NLS-1$
			outputConnectionAnchors.addElement(c);
		}
	}

	public DefinitionFigure(IDefinition definition) {
		this.definition = definition;

		if (definition != null) {
			setIcon(definition.getImage());
			setPath(definition.getName());
		}

		setOpaque(false);
		addFigureListener(this);

		addKeyListener(this);

		setBorder(new GroupBorder(blackColor));

		FixedConnectionAnchor c;
		c = new FixedConnectionAnchor(this);
		c.offsetV = 10;
		c.leftToRight = false;
		connectionAnchors.put("1_IN", c); //$NON-NLS-1$
		inputConnectionAnchors.addElement(c);

		c = new FixedConnectionAnchor(this);
		c.offsetV = 10;
		c.offsetH = -1;
		connectionAnchors.put("1_OUT", c); //$NON-NLS-1$
		outputConnectionAnchors.addElement(c);
	}

	/**
	 * @see org.eclipse.gef.handles.HandleBounds#getHandleBounds()
	 */
	public Rectangle getHandleBounds() {
		return getBounds().getCropped(new Insets(0, 0, 0, 0));
	}

	/**
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		return SIZE;
	}

	int width, height;

	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
	 */
	protected void paintFigure(Graphics g) {
		Rectangle r = getBounds().getCopy();
		g.translate(r.getLocation());
		int width = r.width - 1;

		if (definition != null && definition.isAnotherTiles()) {
			if (!definition.isConfirmed())
				g.setBackgroundColor(lightGrayColor);
			else
				g.setBackgroundColor(whiteColor);
		} else {
			g.setBackgroundColor(whiteColor);
		}

		g.fillRectangle(1, 1, 22, 19);

		if (icon != null)
			g.drawImage(icon, 4, 2);
		if (definition != null && definition.hasErrors())
			g.drawImage(errorIcon, 4, 10);

		if (definition != null && definition.isAnotherTiles()) {
			if (!definition.isConfirmed())
				g.setBackgroundColor(lightGrayColor);
			else
				g.setBackgroundColor(anotherTilesColor);
		} else {
			g.setBackgroundColor(bgColor);
		}

		g.fillRectangle(24, 1, width - 25, 19);

		if (definition.isExpanded())
			g.drawImage(expandIcon, 98, 6);
		else if (definition.isCollapsed())
			g.drawImage(collapseIcon, 98, 6);
	}

	class GroupBorder extends LineBorder {
		public GroupBorder(Color color) {
			super(color);
		}

		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			Rectangle r = getPaintRectangle(figure, insets).getCopy();
			graphics.translate(r.getLocation());
			int width = r.width - 1;

			if (definition != null && definition.isAnotherTiles()) {
				graphics.setForegroundColor(darkGrayColor);
				if (definition.isConfirmed())
					graphics.setLineStyle(Graphics.LINE_DOT);
			} else {
				graphics.setForegroundColor(blackColor);
			}

			graphics.drawLine(1, 0, width - 2, 0);
			graphics.drawLine(width - 1, 1, width - 1, 19);
			graphics.drawLine(23, 0, 23, 20);
			graphics.drawLine(0, 1, 0, 19);
			graphics.drawLine(1, 20, width - 2, 20);
		}
	}

	public void mouseDoubleClicked(MouseEvent me) {
	}

	public void mousePressed(MouseEvent me) {
		if (me.button == 3 && me.getSource() == label) {
			((GEFGraphicalViewer) editPart.getViewer()).setNoDeselect();
			editPart.getViewer().select(editPart);
		}
		if (me.button == 1
				&& me.x > 93 + getLocation().x
				&& me.x < 114 + getLocation().x
				&& me.getSource() == this
				&& editPart.getViewer().getEditDomain().getActiveTool() instanceof SelectionTool) {

			if (definition.isExpanded())
				definition.collapse();
			else if (definition.isCollapsed())
				definition.expand();
			repaint();
		}
	}

	public void mouseReleased(MouseEvent me) {
	}

	public void mouseDragged(MouseEvent me) {
	}

	public void mouseEntered(MouseEvent me) {
	}

	public void mouseExited(MouseEvent me) {
	}

	public void closeNavigator() {
	}

	public void mouseHover(MouseEvent me) {
	}

	public void mouseMoved(MouseEvent me) {
	}

	public void keyPressed(KeyEvent ke) {
	}

	public void keyReleased(KeyEvent ke) {
	}

	private Point oldLocation = null, newLocation = null,
			curentLocation = null;

	private int curent = 0;

	private int step = 20;

	private Dimension size = null;

	private boolean animated = false;

	public void setBounds(Rectangle rect) {
		oldLocation = getLocation();
		if (oldLocation.x == rect.x && oldLocation.y == rect.y) {
			if (animated) {
				animator.removeAnimateListener(this);
				animated = false;
			}
			return;
		}
		if ((oldLocation.x == 0 && oldLocation.y == 0)
				|| !definition.getTilesModel().getOptions().isAnimateLayout())
			super.setBounds(rect);
		else {
			curent = 0;
			newLocation = new Point(rect.x, rect.y);
			curentLocation = oldLocation.getCopy();
			size = new Dimension(rect.width, rect.height);
			if (!animated)
				animator.addAnimateListener(this);
			animated = true;
		}
	}

	public void animate() {
		curent++;
		if (curent > 1000) {
			animator.removeAnimateListener(this);
			animated = false;
			return;
		}

		double distance = oldLocation.getDistance(newLocation);

		int steps = (int) distance / step;

		if (steps == 0) {
			super.setBounds(new Rectangle(newLocation.x, newLocation.y,
					size.width, size.height));
			animator.removeAnimateListener(this);
			animated = false;
			return;
		}

		Dimension difference = newLocation.getDifference(oldLocation);

		difference.width = difference.width / steps;
		difference.height = difference.height / steps;
		curentLocation.x += difference.width;
		curentLocation.y += difference.height;

		if (oldLocation.x < newLocation.x) {
			if (curentLocation.x > newLocation.x)
				curentLocation.x = newLocation.x;
		} else if (curentLocation.x < newLocation.x)
			curentLocation.x = newLocation.x;
		if (oldLocation.y < newLocation.y) {
			if (curentLocation.y > newLocation.y)
				curentLocation.y = newLocation.y;
		} else if (curentLocation.y < newLocation.y)
			curentLocation.y = newLocation.y;

		super.setBounds(new Rectangle(curentLocation.x, curentLocation.y,
				size.width, size.height));
		if (curentLocation.x == newLocation.x
				&& curentLocation.y == newLocation.y) {
			animator.removeAnimateListener(this);
			animated = false;
		}
	}

	public static Animator animator = new Animator();

	public static class Animator extends Thread {
		private static List<DefinitionFigure> listeners = new Vector<DefinitionFigure>();

		public Animator() {
			super(""); //$NON-NLS-1$
			start();
		}

		public void run() {
			try {
				while (true) {
					if (listeners.size() == 0) {
						synchronized (this) {
							this.wait();
						}
					}
					sleep(10);
					fireAnimate();

				}
			} catch (Exception ex) {
				TilesUIPlugin.getPluginLog().logError(ex);
			}
		}

		public void addAnimateListener(DefinitionFigure figure) {
			listeners.add(figure);
			if (listeners.size() == 1) {
				synchronized (this) {
					this.notifyAll();
				}
			}
		}

		public void removeAnimateListener(DefinitionFigure figure) {
			listeners.remove(figure);
		}

		public void fireAnimate() {
			if (listeners.size() == 0 || Display.getDefault() == null)
				return;
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					for (int i = 0; i < listeners.size(); i++) {
						(listeners.get(i)).animate();
					}
				}
			});

		}

	}

}