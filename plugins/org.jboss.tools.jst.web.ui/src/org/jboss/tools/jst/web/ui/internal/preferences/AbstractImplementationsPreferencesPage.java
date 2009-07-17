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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.tools.jst.web.project.helpers.AbstractWebProjectTemplate;
import org.jboss.tools.jst.web.project.version.ProjectVersion;

public class AbstractImplementationsPreferencesPage extends PreferencePage implements
		IWorkbenchPreferencePage {

	/**
	 * @deprecated use bundle via Messages.getString()
	 */
	public static final String BUNDLE_NAME = "preferences";  //$NON-NLS-1$
	/**
	 * @deprecated use bundle via Messages.getString()
	 */
	public static final ResourceBundle BUNDLE = ResourceBundle.getBundle(AbstractImplementationsPreferencesPage.class.getPackage().getName() + "." + BUNDLE_NAME);	 //$NON-NLS-1$
	
	private AbstractWebProjectTemplate helper;
	private String[] implementations;	
	private String[] templates;	
	private String[] libraries;
	private String errorMessage;
	
	private Label errorMessageLabel;
	private Button addButtonImpl;	
	private Button removeButtonImpl;
	private Button addButtonLibraries;
	private Button removeButtonLibraries;
	private Button addButtonProject;
	private Button editButtonProject;
	private Button upButtonProject;
	private Button downButtonProject;
	private Button removeButtonProject;
	
    private int widthHint;
    private int heightHint;
    
    private List listImplementations;
    private List listProject;
	
	public AbstractImplementationsPreferencesPage() {
		super();
	}

	public AbstractImplementationsPreferencesPage(String title) {
		super(title);
	}

	/**
	 * @param title
	 * @param image
	 */
	public AbstractImplementationsPreferencesPage(String title, ImageDescriptor image) {
		super(title, image);
	}
	
	protected AbstractWebProjectTemplate createHelper() {
		return null;
	}

	protected Control createContents(Composite parent) {
		noDefaultAndApplyButton();

	    widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
	    heightHint = convertVerticalDLUsToPixels(14 /*IDialogConstants.BUTTON_HEIGHT*/);
		
		
		Composite root = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		root.setLayout(layout);
		
		//Add in a dummy(or errorMessage) label for spacing
		errorMessageLabel = new Label(root,SWT.NONE);
		if (errorMessage != null) {
			errorMessageLabel.setText(errorMessage);
			errorMessageLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		}
		
		Composite impl = new Composite(root, SWT.NULL);
		GridLayout layoutImpl = new GridLayout();
		layoutImpl.numColumns = 2;
		impl.setLayout(layoutImpl);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.grabExcessHorizontalSpace = true;
		impl.setLayoutData(data);		
		
		// listImplementations
		listImplementations = new List(impl, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
		if (errorMessage == null) listImplementations.setItems(implementations);
		GridData gridDataList = new GridData(GridData.FILL_BOTH);
		gridDataList.widthHint = 270;//Minimum width for the column.
		gridDataList.horizontalSpan = 1;
		gridDataList.verticalSpan = 2;
		listImplementations.setLayoutData(gridDataList);

		heightHint = convertVerticalDLUsToPixels(14 /*IDialogConstants.BUTTON_HEIGHT*/);
		widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);		
		
		addButtonImpl = createButton(impl, Messages.ImplementationsPreferencesPage_Add); 
		if (errorMessage != null) addButtonImpl.setEnabled(false);

		removeButtonImpl = createButton(impl, Messages.ImplementationsPreferencesPage_Remove); 
		removeButtonImpl.setEnabled(false);		

		// TabFolder
		TabFolder tabbedComposite = new TabFolder(root,SWT.NULL);
		GridData gridDataTab = new GridData(GridData.FILL_BOTH);
		tabbedComposite.setLayoutData(gridDataTab);
		
		TabItem librariesTab = new TabItem(tabbedComposite,SWT.NULL);
		librariesTab.setText(Messages.ImplementationsPreferencesPage_Library_Sets); 
		
		TabItem projectTab = new TabItem(tabbedComposite,SWT.NULL);
		projectTab.setText(Messages.ImplementationsPreferencesPage_Project_Templates); 

		// listLibraries		
		Composite entryLibraries = new Composite(tabbedComposite, SWT.NULL);
		GridLayout layoutEntryLibraries = new GridLayout();
		layoutEntryLibraries.numColumns = 2;
		entryLibraries.setLayout(layoutEntryLibraries);

		final List listLibraries = new List(entryLibraries, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
		GridData gridDataListLibraries= new GridData(GridData.FILL_BOTH);
		gridDataListLibraries.verticalSpan = 2;
		listLibraries.setLayoutData(gridDataListLibraries);
			

		addButtonLibraries = createButton(entryLibraries, Messages.ImplementationsPreferencesPage_Add); 
		addButtonLibraries.setEnabled(false);		

		removeButtonLibraries = createButton(entryLibraries, Messages.ImplementationsPreferencesPage_Remove); 
		removeButtonLibraries.setEnabled(false);		

		librariesTab.setControl(entryLibraries);
		
		// listProject		
		Composite entryProject = new Composite(tabbedComposite, SWT.NULL);
		GridLayout layoutEntryProject = new GridLayout();
		layoutEntryProject.numColumns = 2;
		entryProject.setLayout(layoutEntryProject);

		listProject = new List(entryProject, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
		GridData gridDataListProject= new GridData(GridData.FILL_BOTH);
		gridDataListProject.verticalSpan = 5;
		listProject.setLayoutData(gridDataListProject);
			

		addButtonProject = createButton(entryProject, Messages.ImplementationsPreferencesPage_Add); 
		addButtonProject.setEnabled(false);
		
		editButtonProject = createButton(entryProject, Messages.ImplementationsPreferencesPage_Edit); 
		editButtonProject.setEnabled(false);
		
		upButtonProject = createButton(entryProject, Messages.ImplementationsPreferencesPage_Up); 
		upButtonProject.setEnabled(false);
		GridData d = (GridData)upButtonProject.getLayoutData();
		d.verticalIndent = 5;

		downButtonProject = createButton(entryProject, Messages.ImplementationsPreferencesPage_Down); 
		downButtonProject.setEnabled(false);

		removeButtonProject = createButton(entryProject, Messages.ImplementationsPreferencesPage_Remove); 
		removeButtonProject.setEnabled(false);
		d = (GridData)removeButtonProject.getLayoutData();
		d.verticalIndent = 5;

		projectTab.setControl(entryProject);		
		
		// Listeners for List
		listImplementations.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int index = listImplementations.getSelectionIndex();
				String nameImplementation = listImplementations.getItem(index);
				
				templates = helper.getTemplateList(nameImplementation);
				listProject.setItems(templates);

				ProjectVersion version = helper.getProjectVersions().getVersion(nameImplementation);
				if (version != null) {
					libraries = version.getLibraryNames();
					listLibraries.setItems(libraries);
				}
				
				removeButtonImpl.setEnabled(true);
				addButtonLibraries.setEnabled(true);
				removeButtonLibraries.setEnabled(false);				
				addButtonProject.setEnabled(true);
				editButtonProject.setEnabled(false);
				upButtonProject.setEnabled(false);
				downButtonProject.setEnabled(false);
				removeButtonProject.setEnabled(false);
			}

		});

		listLibraries.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				removeButtonLibraries.setEnabled(true);
			}

		});
		
		listProject.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				editButtonProject.setEnabled(true);
				removeButtonProject.setEnabled(true);
				updateUpDownProjectEnablement();
			}

		});
		
		// Listeners for Buttons
		addButtonImpl.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String newVersionNameImplementation = helper.getProjectVersions().addVersion();
				implementations = helper.getVersionList();
				listImplementations.setItems(implementations);
				if (newVersionNameImplementation != null){
					listImplementations.setSelection( new String [] {newVersionNameImplementation});

					templates = helper.getTemplateList(newVersionNameImplementation);
					listProject.setItems(templates);

					ProjectVersion version = helper.getProjectVersions().getVersion(newVersionNameImplementation);
					if (version != null) {
						libraries = version.getLibraryNames();
						listLibraries.setItems(libraries);
					}
					
				}
				else removeButtonImpl.setEnabled(false);				
			}
		});

		removeButtonImpl.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int index = listImplementations.getSelectionIndex();
				if (index > -1) {
					String nameImplementation = listImplementations.getItem(index);
					if (nameImplementation != null){
						helper.getProjectVersions().removeVersion(nameImplementation);
						implementations = helper.getVersionList();
						listImplementations.setItems(implementations);
						removeButtonImpl.setEnabled(false);
						addButtonLibraries.setEnabled(false);
						addButtonProject.setEnabled(false);
						
						listLibraries.setItems(new String [0]);						
						listProject.setItems(new String [0]);						
					}					
				}
			}
		});		
		
		addButtonLibraries.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int index = listImplementations.getSelectionIndex();
				if (index > -1) {
					String nameImplementation = listImplementations.getItem(index);
					
					ProjectVersion version = helper.getProjectVersions().getVersion(nameImplementation);
					if (version != null) {
						String nameLib = version.addLibrary();
						libraries = version.getLibraryNames();
						listLibraries.setItems(libraries);
						if (nameLib != null) listLibraries.setSelection( new String [] {nameLib});
						else removeButtonLibraries.setEnabled(false);						
					}					
					
				}
			}
		});

		removeButtonLibraries.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int index = listImplementations.getSelectionIndex();
				if (index > -1) {
					String nameImplementation = listImplementations.getItem(index);

					int indexLib = listLibraries.getSelectionIndex();
					if (indexLib > -1) {

						String nameLib = listLibraries.getItem(indexLib);
						ProjectVersion version = helper.getProjectVersions().getVersion(nameImplementation);
						if (version != null) {
							version.removeLibrary(nameLib);
							libraries = version.getLibraryNames();
							listLibraries.setItems(libraries);
							removeButtonLibraries.setEnabled(false);							
						}					
					}
				}
			}
		});		
		
		addButtonProject.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				addProject();
			}
		});
		upButtonProject.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int from = listProject.getSelectionIndex();
				moveProject(from, from - 1);
			}
		});
		downButtonProject.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int from = listProject.getSelectionIndex();
				moveProject(from, from + 1);
			}
		});
		removeButtonProject.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				removeProject();
			}
		});
		editButtonProject.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				editProject();
			}
		});		
		
		return root;		
	}
	
	private void updateUpDownProjectEnablement() {
		int s = listProject.getSelectionIndex();
		upButtonProject.setEnabled(s > 0);
		boolean downEnabled = s >= 0 && s < (listProject.getItemCount() - 1);
		downButtonProject.setEnabled(downEnabled);
	}
	
	private String getSelectedImplementation() {
		int index = listImplementations.getSelectionIndex();
		return (index < 0) ? null : listImplementations.getItem(index);
	}
	
	private String getSelectedProject() {
		int indexProject = listProject.getSelectionIndex();
		return (indexProject < 0) ? null : listProject.getItem(indexProject);						
	}
	
	private void addProject() {
		String nameImplementation = getSelectedImplementation();
		if(nameImplementation == null) return;
		int s = listProject.getSelectionIndex();
		String addNameProject = helper.addProjectTemplate(nameImplementation);
		templates = helper.getTemplateList(nameImplementation);
		listProject.setItems(templates);
		if (addNameProject != null) {
			listProject.setSelection( new String [] {addNameProject});
			int t = listProject.getSelectionIndex();
			if(t != s) {
				moveProject(t, s);
			}
		} else {
			editButtonProject.setEnabled(false);
			removeButtonProject.setEnabled(false);						
		}
	}

	private void removeProject() {
		String nameImplementation = getSelectedImplementation();
		String nameProject = getSelectedProject();
		int s = listProject.getSelectionIndex();
		if(nameImplementation == null ||nameProject == null) return;
		helper.removeProjectTemplate(nameImplementation, nameProject);
		templates = helper.getTemplateList(nameImplementation);
		listProject.setItems(templates);
		if(s >= listProject.getItemCount()) s = listProject.getItemCount() - 1;
		if(s >= 0) {
			listProject.setSelection(s);
			removeButtonProject.setEnabled(true);
			editButtonProject.setEnabled(true);
		} else {
			removeButtonProject.setEnabled(false);
			editButtonProject.setEnabled(false);
		}
		updateUpDownProjectEnablement();
	}

	private void moveProject(int from, int to) {
		if(from == to) return;
		if(from < 0 || to < 0) return;
		if(from >= listProject.getItemCount() || to >= listProject.getItemCount()) return;
		String nameImplementation = getSelectedImplementation();
		if(nameImplementation == null) return;
		ProjectVersion version = helper.getProjectVersions().getVersion(nameImplementation);
		String[] order = (String[])listProject.getItems().clone();
		String v = order[from];
		int di = (from < to) ? 1 : -1;
		int k = from;
		while(k != to) {
			order[k] = order[k + di];
			k += di;
		}
		order[to] = v;
		version.setOrder(order);
		listProject.setItems(order);
		listProject.setSelection(to);
		updateUpDownProjectEnablement();
	}
	
	private void editProject() {
		String nameImplementation = getSelectedImplementation();
		String nameProject = getSelectedProject();
		if(nameImplementation == null ||nameProject == null) return;
		String newName = helper.editProjectTemplate(nameImplementation, nameProject);
		if(newName == null || newName.equals(nameProject)) return;
		templates = helper.getTemplateList(nameImplementation);
		listProject.setItems(templates);
		listProject.setSelection(new String[]{newName});
	}

	public void init(IWorkbench workbench) {
		helper = createHelper();
		errorMessage = helper.getProjectVersions().getErrorMessage();
		if (errorMessage == null) {
			implementations = helper.getVersionList();			
		}
	}
	
	private Button createButton(Composite parent, String labelText){
	    Button button = new Button(parent, SWT.PUSH);
	    GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gridData.verticalAlignment = GridData.BEGINNING;	    
		gridData.heightHint = heightHint;
		gridData.widthHint = Math.max(widthHint,button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		button.setLayoutData(gridData);		
		if(labelText != null) button.setText(labelText);
		return button;		
	}
	
}
