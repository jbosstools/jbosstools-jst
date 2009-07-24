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

package org.jboss.tools.jst.css.properties;

import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.BaseTabControl;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class MessageCSSSection extends AbstractCSSSection {

	private AggregateValidationStatus aggregateStatus;

	private Text messageLabel;

	private Label messageImageLabel;

	private IStatus status = Status.OK_STATUS;

	private Composite messageComposite;

	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);

		aggregateStatus = new AggregateValidationStatus(getBindingContext()
				.getValidationStatusProviders(),
				AggregateValidationStatus.MAX_SEVERITY);
		aggregateStatus.addValueChangeListener(new IValueChangeListener() {
			public void handleValueChange(ValueChangeEvent event) {

				handleStatusChanged((IStatus) event.diff.getNewValue());
			}
		});

		messageComposite = new Composite(parent, SWT.None);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 0;
		gridLayout.verticalSpacing = 0;
		messageComposite.setLayout(gridLayout);
		messageImageLabel = new Label(messageComposite, SWT.CENTER);
		messageImageLabel.setImage(JFaceResources
				.getImage(Dialog.DLG_IMG_MESSAGE_ERROR));
		messageLabel = new Text(messageComposite, SWT.WRAP | SWT.READ_ONLY);
		messageComposite.setVisible(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.jst.css.properties.AbstractCssSection#createTabControl
	 * (org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public BaseTabControl createSectionControl(Composite parent) {
		return null;
	}

	protected void handleStatusChanged(IStatus newStatus) {
		if (newStatus.getSeverity() != status.getSeverity()) {
			messageLabel.setText(newStatus.getMessage());
			messageComposite.setVisible(!newStatus.isOK());
			messageComposite.layout(true);
			status = newStatus;
		}

	}

}
