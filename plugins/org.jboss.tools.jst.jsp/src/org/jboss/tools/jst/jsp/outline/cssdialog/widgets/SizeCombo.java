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


import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Util;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class SizeCombo extends Composite implements Listener, CSSWidget {

	private Combo sizeCombo;

	private Combo unitCombo;

	/**
	 * @param parent
	 * @param style
	 */
	public SizeCombo(Composite parent, List<String> values) {

		super(parent, SWT.None);

		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;

		setLayout(layout);
		setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false,
				2, 1));

		sizeCombo = new Combo(this, SWT.BORDER | SWT.SINGLE);
		sizeCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
				true, false));
		sizeCombo.addListener(SWT.Modify, this);

		for (String value : values) {
			sizeCombo.add(value);
		}

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

		sizeCombo.setText(parsedValue[0]);

		unitCombo.select(unitCombo.indexOf(parsedValue[1]));

	}

	public String getText() {
		return sizeCombo.getText() + unitCombo.getText();
	}

	public void handleEvent(Event event) {

		if (event.widget == sizeCombo) {
			if (sizeCombo.indexOf(sizeCombo.getText()) != -1) {
				unitCombo.setEnabled(false);
				unitCombo.select(0);
			} else {
				unitCombo.setEnabled(true);
			}
		}

		notifyListeners(event.type, event);
	}

}
