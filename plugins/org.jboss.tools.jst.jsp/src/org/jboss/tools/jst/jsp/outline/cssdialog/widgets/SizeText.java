/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jst.jsp.outline.cssdialog.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Util;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class SizeText extends Composite implements Listener, CSSWidget {

	private Text sizeText;

	private Combo unitCombo;

	/**
	 * @param parent
	 * @param style
	 */
	public SizeText(Composite parent) {

		super(parent,SWT.None);

		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;

		setLayout(layout);
		setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false,
				2, 1));

		sizeText = new Text(this, SWT.BORDER | SWT.SINGLE);
		sizeText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
				true, false));
		sizeText.addListener(SWT.Modify, this);

		unitCombo = new Combo(this, SWT.BORDER | SWT.READ_ONLY);
		unitCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
				false, false));

		for (String unit : Constants.extSizes) {

			unitCombo.add(unit);
		}

		unitCombo.addListener(SWT.Modify, this);

	}

	public void setText(String text) {

		String[] parsedValue = Util.convertExtString(text);

		sizeText.setText(parsedValue[0]);

		unitCombo.select(unitCombo.indexOf(parsedValue[1]));

	}

	public String getText() {
		return sizeText.getText() + unitCombo.getText();
	}
	
	public void handleEvent(Event event) {
		notifyListeners(event.type, event);
	}

}
