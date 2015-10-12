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
package org.jboss.tools.jst.js.npm.internal.wizard;

import java.util.Map;
import java.util.TreeMap;

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
import org.jboss.tools.jst.js.node.ui.PopUpKeyValueDialog;
import org.jboss.tools.jst.js.node.util.WorkbenchResourceUtil;
import org.jboss.tools.jst.js.npm.PackageJson;
import org.jboss.tools.jst.js.npm.PackageJson.Builder;
import org.jboss.tools.jst.js.npm.internal.Messages;
import org.jboss.tools.jst.js.npm.internal.NpmConstants;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
@SuppressWarnings("restriction")
public class NpmInitPage extends WizardPage {
	private final IStructuredSelection selection;
	private IContainer selectedContainer;
	private Text dirText;
	private Text nameText;
	private Text versionText;
	private Text descriptionText;
	private Text mainText;
	private Text authorText;
	private Text licenseText;
	
	private Button useDefaultCheckBox;
	
	private Table scriptsTable;
	private Button addScriptButton;

	private String defaultName;
	private String defaultVersion;
	private String defaultDescription;
	private String defaultMain;
	private String defaultAuthor;	
	private String defaultLicense;
	
	private String defaultDirectory;
	private Map<String, String> defaultScripts;
	
	public NpmInitPage(IStructuredSelection selection) {
		super(Messages.NpmInitWizard_PageName);
		setTitle(Messages.NpmInitWizard_PageTitle);
		setDescription(Messages.NpmInitWizard_PageDescription);
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
		createScriptsEditor(mainComposite);

		Dialog.applyDialogFont(mainComposite);
		initiDefaultsValues();
		setControl(mainComposite);
	}

	public String getExecutionDir() {
		return dirText.getText();
	}
	
	public PackageJson getModel() {
		String name = nameText.getText();
		String version = versionText.getText();
		String description = descriptionText.getText();
		String main = mainText.getText();
		String author = authorText.getText();
		String license = licenseText.getText();
		Map<String, String> scripts = getKeyValueItems(scriptsTable);

		Builder builder = new PackageJson.Builder();
	
		if (name != null && !name.isEmpty()) {
			builder.name(name);
		}
		
		if (version != null && !version.isEmpty()) {
			builder.version(version);
		}
		
		if (description != null) { // empty by default
			builder.description(description);
		}
		
		if (main != null && !main.isEmpty()) {
			builder.main(main);
		}
		
		if (scripts != null && !scripts.isEmpty()) {
			builder.scripts(scripts);
		}
		
		if (author != null) { // empty by default
			builder.author(author);
		}
		
		if (license != null && !license.isEmpty()) {
			builder.license(license);
		}
			
		PackageJson model = builder.build();

		return model;
	}
	

	private void initiDefaultsValues() {
		this.selectedContainer = WorkbenchResourceUtil.getContainerFromSelection(this.selection);
		
		String location = WorkbenchResourceUtil.getAbsolutePath(selectedContainer);
		
		this.defaultDirectory = (location != null) ? location : "";  //$NON-NLS-1$
		this.defaultName = (selectedContainer != null) ? selectedContainer.getProject().getName().replaceAll("\\s","") : NpmConstants.DEFAULT_NAME; //$NON-NLS-1$ //$NON-NLS-2$
		
		this.defaultVersion = NpmConstants.DEFAULT_VERSION; 
		this.defaultLicense =  NpmConstants.DEFAULT_LICENSE;
		this.defaultDescription = NpmConstants.DEFAULT_DESCRIPTION;
		this.defaultMain = NpmConstants.DEFAULT_MAIN;
		this.defaultAuthor = NpmConstants.DEFAULT_AUTHOR;		
		this.defaultScripts = NpmConstants.DEFAULT_SCRIPTS;
				
		this.dirText.setText(defaultDirectory);
		this.nameText.setText(defaultName);
		this.versionText.setText(defaultVersion);
		this.descriptionText.setText(defaultDescription);
		this.mainText.setText(defaultMain);
		this.authorText.setText(defaultAuthor);
		this.licenseText.setText(defaultLicense);
		
		setDefaults(scriptsTable, defaultScripts); 
				
		boolean useDefault = useDefaultCheckBox.getSelection();
		
		this.nameText.setEnabled(!useDefault);
		this.versionText.setEnabled(!useDefault);
		this.descriptionText.setEnabled(!useDefault);
		this.mainText.setEnabled(!useDefault);
		this.authorText.setEnabled(!useDefault);
		this.licenseText.setEnabled(!useDefault);
		
		this.scriptsTable.setEnabled(!useDefault);
		this.addScriptButton.setEnabled(!useDefault);
	}
	
	private void createExecutionDirEditor(Composite mainComposite) {
		Label label = new Label(mainComposite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		label.setText(Messages.NpmInitWizard_BaseDirectory);

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
		browseWorkspaceButton.setText(Messages.NpmInitWizard_BrowseWorkspace);
		browseWorkspaceButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ContainerSelectionDialog dialog = new ContainerSelectionDialog(getShell(), 
						ResourcesPlugin.getWorkspace().getRoot(), false, Messages.NpmInitWizard_RootFolderSelection);
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
		useDefaultCheckBox = SWTFactory.createCheckButton(group, Messages.NpmInitWizard_UseDefaulConfiguration, null, true, 2);
		useDefaultCheckBox.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean useDefault = ((Button) e.widget).getSelection();
				nameText.setEnabled(!useDefault);
				versionText.setEnabled(!useDefault);
				descriptionText.setEnabled(!useDefault);
				mainText.setEnabled(!useDefault);
				authorText.setEnabled(!useDefault);
				licenseText.setEnabled(!useDefault);
				scriptsTable.setEnabled(!useDefault);
				addScriptButton.setEnabled(!useDefault);
				
				if (useDefault) {
					setDefaults();
				} else {
					nameText.setEnabled(true);
					versionText.setEnabled(true);
					descriptionText.setEnabled(true);
					mainText.setEnabled(true);
					authorText.setEnabled(true);
					licenseText.setEnabled(true);
					scriptsTable.setEnabled(true);
					addScriptButton.setEnabled(true);
				}
			}
		});
	}
	
	private void createBasePropertyEditor(Composite mainComposite) {
		Group group = SWTFactory.createGroup(mainComposite, Messages.NpmInitWizard_Properties, 2, 1, GridData.FILL_HORIZONTAL);
		
		Label nameLabel = new Label(group, SWT.NONE);
		nameLabel.setText(Messages.NpmInitWizard_Name);
		nameText = new Text(group, SWT.BORDER);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		nameText.addModifyListener(new EntriesChangedListener());

		Label versionLabel = new Label(group, SWT.NONE);
		versionLabel.setText(Messages.NpmInitWizard_Version);
		versionText = new Text(group, SWT.BORDER);
		versionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		versionText.addModifyListener(new EntriesChangedListener());
		
		Label descriptionLabel = new Label(group, SWT.NONE);
		descriptionLabel.setText(Messages.NpmInitWizard_Description);
		descriptionText = new Text(group, SWT.BORDER);
		descriptionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		descriptionText.addModifyListener(new EntriesChangedListener());
		
		Label mainLabel = new Label(group, SWT.NONE);
		mainLabel.setText(Messages.NpmInitWizard_Main);
		mainText = new Text(group, SWT.BORDER);
		mainText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		mainText.addModifyListener(new EntriesChangedListener());
		
		Label authorLabel = new Label(group, SWT.NONE);
		authorLabel.setText(Messages.NpmInitWizard_Author);
		authorText = new Text(group, SWT.BORDER);
		authorText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		authorText.addModifyListener(new EntriesChangedListener());
		
		Label licenseLabel = new Label(group, SWT.NONE);
		licenseLabel.setText(Messages.NpmInitWizard_License);
		licenseText = new Text(group, SWT.BORDER);
		licenseText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		licenseText.addModifyListener(new EntriesChangedListener());
	}
	
	private void createScriptsEditor(Composite mainComposite) {
		TableGroupComposite scriptsComposite = new TableGroupComposite(Messages.NpmInitWizard_Scripts,
				Messages.NpmInitWizard_ScriptPopUpName, Messages.NpmInitWizard_ScriptPopUpValue, mainComposite,
				Messages.NpmInitWizard_AddScript, Messages.NpmInitWizard_EditScript);
		scriptsComposite.createControls();
		this.scriptsTable = scriptsComposite.getTable();
		this.addScriptButton = scriptsComposite.getAddButton();
	}
	
	
	@Override
	public String getName() {
		return Messages.NpmInitWizard_LaunchMainTabName;
	}
	
	private Map<String, String> getKeyValueItems(Table table) {
		Map<String, String> map = new TreeMap<>();
		TableItem[] items = table.getItems();
		if (items != null && items.length > 0) {
			for (TableItem item : items) {
				map.put(item.getText(0), item.getText(1));
			}
		}
		return map;
	}
	
	
	private void setDefaults(Table table, Map<String, String> defaultItems) {
		if (table != null && defaultItems != null && !defaultItems.isEmpty()) {
			table.removeAll();
			for (Map.Entry<String, String> entry : defaultItems.entrySet()) {
			    TableItem item = new TableItem(table, SWT.NONE);
			    item.setText(0, entry.getKey());
			    item.setText(1, entry.getValue());
			}
		}
	}
	

	private void setDefaults() {
		nameText.setText(defaultName);
		versionText.setText(defaultVersion);
		descriptionText.setText(defaultDescription);
		mainText.setText(defaultMain);
		authorText.setText(defaultAuthor);
		licenseText.setText(defaultLicense);
		setDefaults(scriptsTable, defaultScripts);
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
		private String keyLabel;
		private String valueLabel;
		private Composite parent;
		private Table table;
		private Button addButton;
		private Button editButton;
		private Button removeButton;
		private String addDialogTitle;
		private String editDialogTitle;
		
		public TableGroupComposite(String groupLabel, String keyLabel, String valueLabel, Composite parent, String addDialogTitle, String editDialogTitle) {
			this.groupLabel = groupLabel;
			this.keyLabel = keyLabel;
			this.valueLabel = valueLabel;
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
		    propColumn.setWidth(120);
		    propColumn.setText(Messages.NpmInitWizard_ScriptName); 
		    
		    final TableColumn valueColumn = new TableColumn(this.table, SWT.NONE, 1);
		    valueColumn.setWidth(250);
		    valueColumn.setText(Messages.NpmInitWizard_ScriptValue); 

			Composite buttonComposite = new Composite(tableGroup, SWT.NONE);
			FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
			fillLayout.spacing = 2;
			buttonComposite.setLayout(fillLayout);
		    
		    addButton = new Button(buttonComposite, SWT.NONE);
		    addButton.setText(Messages.NpmInitWizard_ButtonAdd);
		    addButton.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent e) {
		        addProperty(addDialogTitle);
		      }
		    });
		    editButton = new Button(buttonComposite, SWT.NONE);
		    editButton.setText(Messages.NpmInitWizard_ButtonEdit);
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
		    removeButton.setText(Messages.NpmInitWizard_ButtonRemove);
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
			PopUpKeyValueDialog dialog = new PopUpKeyValueDialog(getShell(), title, "", "", keyLabel, valueLabel,  null); //$NON-NLS-1$ //$NON-NLS-2$
			if (dialog.open() == IDialogConstants.OK_ID) {
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, dialog.getKey());
				item.setText(1, dialog.getValue());
				entriesChanged();
			}
		}

		private void editProperty(String title, String name, String value) {
			PopUpKeyValueDialog dialog = new PopUpKeyValueDialog(getShell(), title, name, value, keyLabel, valueLabel,  null);
			if (dialog.open() == IDialogConstants.OK_ID) {
				TableItem[] item = table.getSelection();
				item[0].setText(0, dialog.getKey());
				item[0].setText(1, dialog.getValue());
				entriesChanged();
			}
		}
	}

	private void entriesChanged() {
		String dir = dirText.getText();
		if (dir == null || dir.isEmpty()) {
			setPageComplete(false);
			setErrorMessage(Messages.NpmInitWizard_ErrorDirNotDefiened);
		} else {
			IContainer container = ResourcesPlugin.getWorkspace().getRoot().getContainerForLocation(new Path(dir));
			if (container != null && container.exists()) {
				IFile packageJson = container.getFile(new Path(NpmConstants.PACKAGE_JSON));
				if (packageJson.exists()) {
					setError(Messages.NpmInitWizard_ErrorPackageJsonAlreadyExist);
				} else {
					setComplete();
				}
			} else {
				setError(Messages.NpmInitWizard_ErrorDirNotExist);
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
