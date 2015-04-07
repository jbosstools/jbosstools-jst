/******************************************************************************* 
 * Copyright (c) 2015 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil.TwoColumns;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewSpinnerWizardPage extends NewIonicWidgetWizardPage implements IonicConstants {
	static String PREFIX = "wizards/spinner/";
	
	static Map<String, AnimatedImage> IMAGES = new HashMap<String, AnimatedImage>();
	
	static {
		int nornalDelay = 80;
		int smallDelay = nornalDelay / 2;
		addImage(SPINNER_ICON_ANDROID, 61, nornalDelay);
		addImage(SPINNER_ICON_BUBBLES, 8, nornalDelay);
		addImage(SPINNER_ICON_CIRCLES, 8, nornalDelay);
		addImage(SPINNER_ICON_CRESCENT, 15, smallDelay);
		addImage(SPINNER_ICON_DOTS, 10, nornalDelay);
		addImage(SPINNER_ICON_IOS, 12, nornalDelay);
		addImage(SPINNER_ICON_IOS_SMALL, 12, nornalDelay);
		addImage(SPINNER_ICON_LINES, 11, nornalDelay);
		addImage(SPINNER_ICON_RIPPLE, 13, nornalDelay);
		addImage(SPINNER_ICON_SPIRAL, 15, smallDelay);
	}

	private static void addImage(String name, int imageCount, int delay) {
		IMAGES.put(name, AnimatedImage.create(PREFIX + name + "/" + name, imageCount, delay));
	}

	public NewSpinnerWizardPage() {
		super("newSpinner", IonicWizardMessages.newSpinnerTitle);
		setDescription(IonicWizardMessages.newSpinnerWizardDescription);
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		String[] icons = IonicFieldEditorFactory.spinnerIcons;
		
		AnimatedImageThread thread = new AnimatedImageThread();
		
		Group g = parent == null ? null : LayoutUtil.createGroup(parent, "Icon");
		Composite p = createGrid(g);
		for (int i = 0; i < icons.length; i++) {
			IFieldEditor icon = IonicFieldEditorFactory.createSpinnerIconEditor(icons[i], IonicFieldEditorFactory.SPINNER_ICON_DEFAULT);
			addEditor(icon, p);
			if(p != null) {
				Button b = findButton(icon);
				if(b != null) {
					Composite bp = b.getParent();
					GridData d = (GridData)bp.getLayoutData();
					if(d.horizontalSpan > 2) {
						d.horizontalSpan = 2;
						bp.setLayoutData(d);
					}
					AnimatedImage image = IMAGES.get(icons[i]);
					if(image != null) {
						b.setImage(image.getImage());
						thread.addBinding(new AnimatedImageBinding(image, b));
					}
				}
			}
		}

		IFieldEditor color = IonicFieldEditorFactory.createSpinnerColorEditor();
		addEditor(color, parent);
		
		thread.start();
	}

	private Composite createGrid(Composite g) {
		if(g != null) {
			Composite c = new Composite(g, SWT.NONE);
			c.setLayout(new GridLayout(6, false));
			c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			return c;
		}
		return null;
	}

	private Button findButton(IFieldEditor icon) {
		for (Object c: icon.getEditorControls()) {
			if(c instanceof Composite) {
				for (Object c1: ((Composite)c).getChildren()) {
					if(c1 instanceof Button) {
						return (Button)c1;
					}
				}
			}
		}
		return null;
	}

	boolean isAdjustingIcon = false;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(isAdjustingIcon) {
			return;
		}
		String name = evt.getPropertyName();
		if(name.startsWith(IonicConstants.ATTR_ICON + "-")) {
			isAdjustingIcon = true;
			String value = evt.getNewValue().toString();
			String[] icons = IonicFieldEditorFactory.spinnerIcons;
			for (int i = 0; i < icons.length; i++) {
				String nm = IonicConstants.ATTR_ICON + "-" + icons[i];
				if(nm.equals(name)) continue;
				setEditorValue(nm, value);
			}
			isAdjustingIcon = false;
		}
		super.propertyChange(evt);
	}

	@Override
	protected int getPreferredBrowser() {
		return SWT.WEBKIT;
	}

}
