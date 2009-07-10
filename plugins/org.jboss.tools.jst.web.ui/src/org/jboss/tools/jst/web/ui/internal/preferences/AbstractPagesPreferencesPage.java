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
package org.jboss.tools.jst.web.ui.internal.preferences;

import java.util.ResourceBundle;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


import org.jboss.tools.jst.web.project.helpers.AbstractWebProjectTemplate;

/**
 * @author Gavrs
 */
public abstract class AbstractPagesPreferencesPage  extends PreferencePage implements IWorkbenchPreferencePage {
	/**
	 * @deprecated use bundle via Messages.getString()
	 */
	public static final String BUNDLE_NAME = "preferences";  //$NON-NLS-1$
	/**
	 * @deprecated use bundle via Messages.getString()
	 */
	public static  ResourceBundle BUNDLE = ResourceBundle.getBundle(AbstractPagesPreferencesPage.class.getPackage().getName() + "." + BUNDLE_NAME);  //$NON-NLS-1$
	protected AbstractWebProjectTemplate helper;
	protected String[] pages;
	protected String[] pageLabels;
	protected String defaultPage;
	protected Button removeButton,addButtonPages,setDefaultButton;
	private List listPages;

	protected AbstractPagesPreferencesPage() {
		super(); 
	}
	/**
	 * Creates a new preference page with the given title and no image.
	 *
	 * @param title the title of this preference page
	 */
	protected AbstractPagesPreferencesPage(String title) {
		super(title);
	}
	/**
	 * Creates a new abstract preference page with the given title and image.
	 *
	 * @param title the title of this preference page
	 * @param image the image for this preference page,
	 *  or <code>null</code> if none
	 */
	protected AbstractPagesPreferencesPage(String title, ImageDescriptor image) {
		super(title, image);
	}
  
 	public void init(IWorkbench workbench) {
		helper = createHelper();
		pages = helper.getPageTemplateList();
		defaultPage = helper.getDefaultPageTemplate();

		pageLabels = initLabels(pages, defaultPage);
	}
 	
 	private String[] initLabels(String[] pages, String defaultPage) {
		String[] pageLabels = new String[pages == null ? 0 : pages.length];
		for (int i = 0; pageLabels != null && i < pageLabels.length; i++) {
			pageLabels[i] = pages[i];
			if (pages[i].equals(defaultPage)) pageLabels[i] += " " + Messages.AbstractPagesPreferencePage_3; //$NON-NLS-1$
		}
		return pageLabels;
 	}
 	
	protected AbstractWebProjectTemplate createHelper() {
		return null;
	}
 	
	protected Control createContents(Composite parent) {
		noDefaultAndApplyButton();
		
		Composite entryPage = new Composite(parent, SWT.NULL);
		GridData data = new GridData(GridData.FILL_BOTH);//HORIZONTAL_ALIGN_FILL
		
		entryPage.setLayoutData(data);
		int heightHint =convertVerticalDLUsToPixels(14/*DialogConstants.BUTTON_HEIGHT*/);
		int widthHint =convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		entryPage.setLayout(layout);

		listPages = new List(entryPage, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
		listPages.setItems(pageLabels);
		
		
		GridData gridDataList = new GridData(GridData.FILL_BOTH);//FILL_HORIZONTAL FILL_BOTH
		gridDataList.horizontalSpan = 1;
		gridDataList.verticalSpan = isSetDefaultAllowed() ? 3 : 2;
		listPages.setLayoutData(gridDataList);
		
	    addButtonPages = new Button(entryPage, SWT.PUSH);
		addButtonPages.setText(Messages.AbstractPagesPreferencePage_0);
		GridData gridDataAddButton = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gridDataAddButton.widthHint = widthHint;
		gridDataAddButton.heightHint=heightHint;
		addButtonPages.setLayoutData(gridDataAddButton);

		removeButton = new Button(entryPage, SWT.PUSH);
		removeButton.setText(Messages.AbstractPagesPreferencePage_1);
		GridData gridDataRemoveButton = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gridDataRemoveButton.widthHint = widthHint;
		gridDataRemoveButton.heightHint=heightHint;
		gridDataRemoveButton.verticalAlignment = GridData.BEGINNING;
		removeButton.setLayoutData(gridDataRemoveButton);

		removeButton.setEnabled(false);
		
		if (isSetDefaultAllowed()) {
			setDefaultButton = new Button(entryPage, SWT.PUSH);
			setDefaultButton.setText(Messages.AbstractPagesPreferencePage_2);
			GridData gridDataSetDefaultButton = new GridData(GridData.HORIZONTAL_ALIGN_END);
			gridDataSetDefaultButton.widthHint = widthHint;
			gridDataSetDefaultButton.heightHint=heightHint;
			gridDataSetDefaultButton.verticalAlignment = GridData.END;
			setDefaultButton.setLayoutData(gridDataSetDefaultButton);
	
			setDefaultButton.setEnabled(false);

		}

		// Listeners
		listPages.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if(!removeButton.getEnabled())//Returns true if the receiver is enabled,
				removeButton.setEnabled(true);
								
			}
		});
		
		addButtonPages.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String nameNewItem = helper.addPageTemplate();
				boolean cancel = false;
				pages = helper.getPageTemplateList();
				listPages.setItems(pages);
				if (nameNewItem != null) {
					int ind = listPages.indexOf(nameNewItem);
					listPages.select(ind);
					cancel = false;
				} else {
					if (!removeButton.getEnabled())// Returns true if the
													// receiver is enabled,
					{
						cancel = true;
						removeButton.setEnabled(false);
					}
				}
				if (!cancel && (removeButton.getEnabled()))// Returns true if
															// the receiver is
															// enabled,
					removeButton.setEnabled(false);

			}
		});
		
		removeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int indexDel = listPages.getSelectionIndex();
				// -1 if no item is selected.
				if (indexDel > -1) {
					String namePage = listPages.getItem(indexDel);
					helper.removePageTemplate(namePage);
					pages = helper.getPageTemplateList();
					listPages.setItems(pages);
				}
				removeButton.setEnabled(false);

			}
		});

		if (isSetDefaultAllowed()){
			listPages.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					boolean bCanSetDefault = false;
					
					int index = listPages.getSelectionIndex();
					//-1 if no item is selected. 
					if (index > -1) {
						String namePage = listPages.getItem(index);
						int srcIndex = -1;
						for (int i = 0; i < pageLabels.length && srcIndex == -1; i++) {
							if (pageLabels[i].equals(namePage))
								srcIndex = i;
						}
						if (srcIndex > -1) {
							bCanSetDefault = !pages[srcIndex].equals(defaultPage);
						}
					}
					if(bCanSetDefault != setDefaultButton.getEnabled())
						setDefaultButton.setEnabled(bCanSetDefault);
				}
			});
			
			setDefaultButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					int index = listPages.getSelectionIndex();
					//-1 if no item is selected. 
					if (index > -1) {
						String namePage = listPages.getItem(index);
						int srcIndex = -1;
						for (int i = 0; i < pageLabels.length && srcIndex == -1; i++) {
							if (pageLabels[i].equals(namePage))
								srcIndex = i;
						}
						if (srcIndex > -1) {
							defaultPage = pages[srcIndex];
							pageLabels = initLabels(pages, defaultPage);
							helper.setDefaultPageTemplate(defaultPage);
							listPages.setItems(pageLabels);
							listPages.select(index);
						}
					}
					
			       setDefaultButton.setEnabled(false);
				}
			});

		}

		return entryPage;
	}

	protected boolean isSetDefaultAllowed() {
		return false;
	}
	
}
