/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *       Red Hat, Inc. - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.jst.js.bower.internal.wizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.jboss.tools.jst.js.bower.BowerJson;
import org.jboss.tools.jst.js.bower.BowerJson.Builder;
import org.jboss.tools.jst.js.bower.internal.BowerConstants;
import org.jboss.tools.jst.js.bower.internal.Messages;
import org.jboss.tools.jst.js.node.ui.PopUpPropertyDialog;
import org.jboss.tools.jst.js.node.util.WorkbenchResourceUtil;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
@SuppressWarnings("restriction")
public class BowerInitPage extends WizardPage {
	private final IStructuredSelection selection;
	private IContainer selectedContainer;
	private Text dirText;
	private Text nameText;
	private Text versionText;
	private Text licenseText;
	private Button useDefaultCheckBox;
	
	private Table authorsTable;
	private Button addAuthorButton;
	
	private Table ignoreTable;
	private Button addIgnoreButton;

	private String defaultName;
	private String defaultVersion;
	private String defaultLicense;
	private List<String> defaultAuthors;
	private List<String> defaultIgnore;
	private String defaultDirectory;
	
	public BowerInitPage(IStructuredSelection selection) {
		super(Messages.BowerInitWizard_pageName);
		setTitle(Messages.BowerInitWizard_pageTitle);
		setDescription(Messages.BowerInitWizard_pageDescription);
		this.selection = selection;
	}
	
	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		Composite mainComposite = SWTFactory.createComposite(parent, parent.getFont(), 1, 1, GridData.FILL_BOTH);
		((GridLayout) mainComposite.getLayout()).verticalSpacing = 4;
		
		createExecutionDirEditor(mainComposite);
		createUseDefaultsEditor(mainComposite);
		createBasePropertyEditor(mainComposite);
		createAuthorsEditor(mainComposite);
		createIgnoreEditor(mainComposite);
		
		Dialog.applyDialogFont(mainComposite);
		initiDefaultsValues();
		setControl(mainComposite);
	}
	
	public String getExecutionDir() {
		return dirText.getText();
	}
	
	public BowerJson getModel() {
		String name = nameText.getText();
		String version = versionText.getText();
		String license = licenseText.getText();
		List<String> authors = getItems(authorsTable);
		List<String> ignore = getItems(ignoreTable);
        
		Builder builder = new BowerJson.Builder();
	
		if (name != null && !name.isEmpty()) {
			builder.name(name);
		}
		
		if (version != null && !version.isEmpty()) {
			builder.version(version);
		}
		
		if (license != null && !license.isEmpty()) {
			builder.license(license);
		}
		
		if (authors != null && !authors.isEmpty()) {
			builder.authrors(authors);
		}
		
		if (ignore != null && !ignore.isEmpty()) {
			builder.ignore(ignore);
		}
		
		BowerJson model = builder.build();

		return model;
	}
	

	private void initiDefaultsValues() {
		this.selectedContainer = WorkbenchResourceUtil.getContainerFromSelection(this.selection);
		
		String location = WorkbenchResourceUtil.getAbsolutePath(selectedContainer);
		
		this.defaultDirectory = (location != null) ? location : "";  //$NON-NLS-1$
		this.defaultName = (selectedContainer != null) ? selectedContainer.getProject().getName() : BowerConstants.DEFAULT_NAME;
		
		this.defaultVersion = BowerConstants.DEFAULT_VERSION; 
		this.defaultLicense =  BowerConstants.DEFAULT_LICENSE;
		
		this.defaultIgnore =  Arrays.asList(BowerConstants.DEFAULT_IGNORE);
				
		this.dirText.setText(defaultDirectory);
		this.nameText.setText(defaultName);
		this.versionText.setText(defaultVersion);
		this.licenseText.setText(defaultLicense);
		
		// Authors
		setDefaults(authorsTable, defaultAuthors);
		// Ignore
		setDefaults(ignoreTable, defaultIgnore);
		
		boolean useDefault = useDefaultCheckBox.getSelection();
		
		this.nameText.setEnabled(!useDefault);
		this.versionText.setEnabled(!useDefault);
		this.licenseText.setEnabled(!useDefault);
		
		this.authorsTable.setEnabled(!useDefault);
		this.addAuthorButton.setEnabled(!useDefault);
		
		this.ignoreTable.setEnabled(!useDefault);
		this.addIgnoreButton.setEnabled(!useDefault);
	}
	
	private void createExecutionDirEditor(Composite mainComposite) {
		Label label = new Label(mainComposite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		label.setText(Messages.BowerLaunchConfigurationTab_baseDirectory);

		this.dirText = new Text(mainComposite, SWT.BORDER);
		this.dirText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 1));
		this.dirText.addModifyListener(new EntriesChangedListener());

		final Composite buttonComposite = new Composite(mainComposite, SWT.NONE);
		buttonComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 5, 1));
		final GridLayout buttonGridLayout = new GridLayout();
		buttonGridLayout.marginWidth = 0;
		buttonGridLayout.marginHeight = 0;
		buttonGridLayout.numColumns = 1;
		buttonComposite.setLayout(buttonGridLayout);

		final Button browseWorkspaceButton = new Button(buttonComposite, SWT.NONE);
		browseWorkspaceButton.setText(Messages.BowerLaunchConfigurationTab_browseWorkspace);
		browseWorkspaceButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ContainerSelectionDialog dialog = new ContainerSelectionDialog(getShell(), 
						ResourcesPlugin.getWorkspace().getRoot(), false, Messages.BowerLaunchConfigurationTab_rootFolderSelection);
				dialog.showClosedProjects(false);

				int buttonId = dialog.open();
				if (buttonId == IDialogConstants.OK_ID) {
					Object[] resource = dialog.getResult();
					if (resource != null && resource.length > 0) {
						IPath path = ((IPath) resource[0]);
						IResource selectedResource = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
						String location = WorkbenchResourceUtil.getAbsolutePath(selectedResource);
						if (selectedResource.exists()) {
							dirText.setText(location);
						}
					}
				}
			}
		});

	}
	
	private void createUseDefaultsEditor(Composite mainComposite) {
		Composite group = SWTFactory.createComposite(mainComposite, 2, 1, GridData.FILL_HORIZONTAL);
		useDefaultCheckBox = SWTFactory.createCheckButton(group, Messages.BowerLaunchConfigurationTab_useDefaulConfiguration, null, true, 2);

		useDefaultCheckBox.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean useDefault = ((Button) e.widget).getSelection();
				nameText.setEnabled(!useDefault);
				versionText.setEnabled(!useDefault);
				licenseText.setEnabled(!useDefault);
				authorsTable.setEnabled(!useDefault);
				addAuthorButton.setEnabled(!useDefault);
				ignoreTable.setEnabled(!useDefault);
				addIgnoreButton.setEnabled(!useDefault);
				if (useDefault) {
					setDefaults();
				} else {
					nameText.setEnabled(true);
					versionText.setEnabled(true);
					licenseText.setEnabled(true);
					authorsTable.setEnabled(true);
					addAuthorButton.setEnabled(true);
					ignoreTable.setEnabled(true);
					addIgnoreButton.setEnabled(true);
				}
			}
		});
	}
	
	private void createBasePropertyEditor(Composite mainComposite) {
		Group group = SWTFactory.createGroup(mainComposite, Messages.BowerLaunchConfigurationTab_Properties, 2, 1, GridData.FILL_HORIZONTAL);
		
		Label nameLabel = new Label(group, SWT.NONE);
		nameLabel.setText(Messages.BowerLaunchConfigurationTab_Name);
		nameText = new Text(group, SWT.BORDER);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		nameText.addModifyListener(new EntriesChangedListener());

		Label versionLabel = new Label(group, SWT.NONE);
		versionLabel.setText(Messages.BowerLaunchConfigurationTab_Version);
		versionText = new Text(group, SWT.BORDER);
		versionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		versionText.addModifyListener(new EntriesChangedListener());

		Label licenseLabel = new Label(group, SWT.NONE);
		licenseLabel.setText(Messages.BowerLaunchConfigurationTab_License);
		licenseText = new Text(group, SWT.BORDER);
		licenseText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		licenseText.addModifyListener(new EntriesChangedListener());
	}
	
	private void createAuthorsEditor(Composite mainComposite) {
		TableGroupComposite authorsComposite = new TableGroupComposite(Messages.BowerLaunchConfigurationTab_Authors,
				Messages.BowerLaunchConfigurationTab_Author, mainComposite, Messages.BowerInitWizard_addAuthor, Messages.BowerInitWizard_editAuthor);
		authorsComposite.createControls();
		this.authorsTable = authorsComposite.getTable();
		this.addAuthorButton = authorsComposite.getAddButton();
	}
	
	private void createIgnoreEditor(Composite mainComposite) {
		TableGroupComposite ignoreComposite = new TableGroupComposite(Messages.BowerLaunchConfigurationTab_Ignore,
				Messages.BowerLaunchConfigurationTab_Ignore, mainComposite, Messages.BowerInitWizard_addIgnore, Messages.BowerInitWizard_editIgnore); 
		ignoreComposite.createControls();
		this.ignoreTable = ignoreComposite.getTable();
		this.addIgnoreButton = ignoreComposite.getAddButton();
	}
	

	@Override
	public String getName() {
		return Messages.BowerLaunchConfigurationTab_launchMainTabName;
	}
	
	private List<String> getItems(Table table) {
		List<String> authors = new ArrayList<>();
		TableItem[] items = table.getItems();
		if (items != null && items.length > 0) {
			for (TableItem item : items) {
				authors.add(item.getText());
			}
		}
		return authors;
	}
	
	private void setDefaults(Table table, List<String> defaultItems) {
		if (table != null && defaultItems != null && !defaultItems.isEmpty()) {
			table.removeAll();
			for (String item : defaultItems) {
				new TableItem(table, SWT.NONE).setText(item);
			}
		}
	}
	

	private void setDefaults() {
		nameText.setText(defaultName);
		versionText.setText(defaultVersion);
		licenseText.setText(defaultLicense);
		
		setDefaults(authorsTable, defaultAuthors);
		setDefaults(ignoreTable, defaultIgnore);	
	}
	
	private class EntriesChangedListener implements ModifyListener, SelectionListener {
		public void modifyText(ModifyEvent e) {
			entriesChanged();
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			entriesChanged();
		}

		public void widgetSelected(SelectionEvent e) {
			entriesChanged();
		}
	}
	
	private class TableGroupComposite {
		private String groupLabel;
		private String propertyLabel;
		private Composite parent;
		private Table table;
		private Button addButton;
		private Button editButton;
		private Button removeButton;
		private String addDialogTitle;
		private String editDialogTitle;
		
		public TableGroupComposite(String groupLabel, String propertyLabel, Composite parent, String addDialogTitle, String editDialogTitle) {
			this.groupLabel = groupLabel;
			this.propertyLabel = propertyLabel;
			this.parent = parent;
			this.addDialogTitle = addDialogTitle;
			this.editDialogTitle = editDialogTitle;
		}
		
		public Table getTable() {
			return this.table;
		}
		
		public Button getAddButton() {
			return this.addButton;
		}
		

		public void createControls() {
			Composite tableGroup = SWTFactory.createGroup(parent, groupLabel, 2, 1, GridData.FILL_HORIZONTAL);
			TableViewer tableViewer = new TableViewer(tableGroup, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		    tableViewer.addDoubleClickListener(new IDoubleClickListener() {
		      public void doubleClick(DoubleClickEvent event) {
		        TableItem[] selection = table.getSelection();
		        if(selection.length == 1) {
		          editProperty(editDialogTitle, selection[0].getText(0), selection[0].getText(1));
		        }
		      }
		    });
		    tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

		      public void selectionChanged(SelectionChangedEvent event) {
		        TableItem[] items = table.getSelection();
		        if(items == null || items.length == 0) {
		          editButton.setEnabled(false);
		          removeButton.setEnabled(false);
		        } else if(items.length == 1) {
		          editButton.setEnabled(true);
		          removeButton.setEnabled(true);
		        } else {
		          editButton.setEnabled(false);
		          removeButton.setEnabled(true);
		        }
		      }

		    });

		    table = tableViewer.getTable();
			GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
			data.heightHint = 50;
			table.setLayoutData(data);
		    table.setLinesVisible(true);
		    table.setHeaderVisible(true);

		    final TableColumn propColumn = new TableColumn(this.table, SWT.NONE, 0);
		    propColumn.setWidth(300);

			Composite buttonComposite = new Composite(tableGroup, SWT.NONE);
			FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
			fillLayout.spacing = 2;
			buttonComposite.setLayout(fillLayout);
		    
		    addButton = new Button(buttonComposite, SWT.NONE);
		    addButton.setText(Messages.BowerLaunchConfigurationTab_buttonAdd);
		    addButton.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent e) {
		        addProperty(addDialogTitle);
		      }
		    });
		    editButton = new Button(buttonComposite, SWT.NONE);
		    editButton.setText(Messages.BowerLaunchConfigurationTab_buttonEdit);
		    editButton.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent e) {
		        if(table.getSelectionCount() > 0) {
		          TableItem[] selection = table.getSelection();
		          if(selection.length == 1) {
		            editProperty(editDialogTitle, selection[0].getText(0), selection[0].getText(1));
		          }
		        }
		      }
		    });
		    editButton.setEnabled(false);
		    removeButton = new Button(buttonComposite, SWT.NONE);
		    removeButton.setText(Messages.BowerLaunchConfigurationTab_buttonRemove);
		    removeButton.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent e) {
		        if(table.getSelectionCount() > 0) {
		          table.remove(table.getSelectionIndices());
		        }
		      }
		    });
		    removeButton.setEnabled(false);
		}
		
		private void addProperty(String title) {
			PopUpPropertyDialog dialog = new PopUpPropertyDialog(getShell(), title, propertyLabel, "", null); //$NON-NLS-1$
			if (dialog.open() == IDialogConstants.OK_ID) {
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, dialog.getName());
			}
		}

		private void editProperty(String title, String name, String value) {
			PopUpPropertyDialog dialog = new PopUpPropertyDialog(getShell(), title, propertyLabel, name, null);
			if (dialog.open() == IDialogConstants.OK_ID) {
				TableItem[] item = table.getSelection();
				item[0].setText(0, dialog.getName());
			}
		}
	}

	private void entriesChanged() {
		String dir = dirText.getText();
		if (dir == null || dir.isEmpty()) {
			setPageComplete(false);
			setErrorMessage(Messages.BowerInitWizard_errorDirNotDefiened);
		} else {
			IContainer container = ResourcesPlugin.getWorkspace().getRoot().getContainerForLocation(new Path(dir));
			if (container != null && container.exists()) {
				IFile bowerJson = container.getFile(new Path(BowerConstants.BOWER_JSON));
				if (bowerJson.exists()) {
					setError(Messages.BowerInitWizard_errorBowerJsonAlreadyExist);
				} else {
					
					setComplete();
				}
			} else {
				setError(Messages.BowerInitWizard_errorDirNotExist);
			}
		}
	}

	private void setError(String message) {
		setPageComplete(false);
		setErrorMessage(message);
	}

	private void setComplete() {
		setPageComplete(true);
		setErrorMessage(null);
	}

}
