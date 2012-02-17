/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.wizards.newfile;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.tools.ant.types.FilterSet;
import org.apache.tools.ant.types.FilterSetCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.StringResource;
import org.apache.tools.ant.util.ResourceUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.datatools.connectivity.ConnectionProfileException;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.IProfileListener;
import org.eclipse.datatools.connectivity.ProfileManager;
import org.eclipse.datatools.connectivity.db.generic.ui.NewConnectionProfileWizard;
import org.eclipse.datatools.connectivity.drivers.DriverManager;
import org.eclipse.datatools.connectivity.internal.ui.wizards.NewCPWizard;
import org.eclipse.datatools.connectivity.internal.ui.wizards.NewCPWizardCategoryFilter;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.dialogs.DialogUtil;
import org.eclipse.ui.internal.dialogs.PropertyDialog;
import org.eclipse.ui.internal.wizards.newresource.ResourceMessages;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelProvider;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.jboss.tools.common.ui.IValidator;
import org.jboss.tools.common.ui.ValidatorFactory;
import org.jboss.tools.common.ui.widget.editor.ButtonFieldEditor;
import org.jboss.tools.common.ui.widget.editor.ComboFieldEditor;
import org.jboss.tools.common.ui.widget.editor.CompositeEditor;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.common.ui.widget.editor.IFieldEditorFactory;
import org.jboss.tools.common.ui.widget.editor.ITaggedFieldEditor;
import org.jboss.tools.common.zip.UnzipOperation;
import org.jboss.tools.jst.web.ui.Messages;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.osgi.framework.Bundle;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewDSXMLWizard extends BasicNewResourceWizard {
	public static final String WIZARD_ID = "org.jboss.tools.seam.ui.wizard.SeamDSXMLWizard"; //$NON-NLS-1$

	WizardNewDSXMLFileCreationPage mainPage;

	private boolean fOpenEditorOnFinish;

	public NewDSXMLWizard() {		
	}

	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		super.init(workbench, currentSelection);
		setWindowTitle(Messages.NewDSXMLWizard_TITLE);
	}

    public void addPages() {
        super.addPages();
        mainPage = new WizardNewDSXMLFileCreationPage("newFilePage1", getSelection()); //$NON-NLS-1$
        mainPage.setTitle(Messages.NewDSXMLWizard_TITLE);
        mainPage.setDescription(Messages.NewDSXMLWizard_DESCRIPTION);
        
        mainPage.setFileName("ds.xml");
        
        addPage(mainPage);
    }

	@Override
	public boolean performFinish() {
		IFile file = mainPage.createNewFile();
		if (file == null) {
			return false;
		}

		selectAndReveal(file);
		if (fOpenEditorOnFinish) {
			// Open editor on new file.
			IWorkbenchWindow dw = getWorkbench().getActiveWorkbenchWindow();
			try {
				if (dw != null) {
					IWorkbenchPage page = dw.getActivePage();
					if (page != null) {
						IDE.openEditor(page, file, true);
					}
				}
			} catch (PartInitException e) {
				DialogUtil.openError(dw.getShell(), ResourceMessages.FileResource_errorMessage, e.getMessage(), e);
			}
		}
		return true;
	}

	class WizardNewDSXMLFileCreationPage extends WizardNewFileCreationPage {
		private IFieldEditor connProfileSelEditor;
		private IFieldEditor templateSelEditor;

		public WizardNewDSXMLFileCreationPage(String pageName, IStructuredSelection selection) {
			super(pageName, selection);
		}
	
		protected InputStream getInitialContents() {
			String connection = connProfileSelEditor.getValueAsString();
			String templateName = templateSelEditor.getValueAsString();

			try {
				// 1. Find template. For Seam project it is done by its runtime.
				IPath containerPath = getContainerFullPath();
				IProject currentProject = ResourcesPlugin.getWorkspace().getRoot().getFolder(containerPath).getProject();

				File homePath = DSDataModelProvider.getTemplatesFolder();
				String templatePath = (NewDSXMLWizardFactory.TEMPLATE_LIST[1].equals(templateName))
					? "/Datasource/datasource-ds-as7.xml" //$NON-NLS-1$
					: "/Datasource/datasource-ds-default.xml"; //$NON-NLS-1$
				File dataSourceDsFile = new File(homePath, templatePath);

				//2. Create filter set for Ant.
				FilterSetCollection viewFilterSetCollection = new FilterSetCollection();

				// Do it by reusing data model provider.
				IDataModel model = DataModelFactory.createDataModel(new DSDataModelProvider());
				model.setProperty(IDSDataModelProperties.PROJECT_NAME, currentProject.getName());
				model.setProperty(IDSDataModelProperties.CONNECTION_PROFILE, connection);
				IConnectionProfile connProfile = ProfileManager.getInstance().getProfileByName(connection.toString());
				if(connProfile == null) {
					return null;
				}
				DSDataModelProvider.applyConnectionProfile(currentProject, model);
				FilterSet jdbcFilterSet = FilterSetFactory.createJdbcFilterSet(model);
				FilterSet projectFilterSet = FilterSetFactory.createProjectFilterSet(model);

				viewFilterSetCollection.addFilterSet(jdbcFilterSet);
				viewFilterSetCollection.addFilterSet(projectFilterSet);

				// 3. Run Ant - copy template with replaces to StringResource.				
				StringResource sr = new StringResource();

				ResourceUtils.copyResource(new FileResource(dataSourceDsFile), sr, viewFilterSetCollection,
						null, true, false, false, null, null, null, false);
				
				// 4. Return input stream for new ds file taken from StringResource.
				return sr.getInputStream();
			} catch (IOException e) {
				WebUiPlugin.getDefault().logError(e);
				return null;
			}
		}

		public void createControl(Composite parent) {
			super.createControl(parent);
			Composite topLevel = (Composite)getControl();
			connProfileSelEditor = NewDSXMLWizardFactory.createConnectionProfileSelectionFieldEditor(getConnectionProfileDefaultValue(), new IValidator() {
				public Map<String, IStatus> validate(Object value, Object context) {
					validatePage();
					return ValidatorFactory.NO_ERRORS;
				}
			}, false);
			templateSelEditor = NewDSXMLWizardFactory.createTemplateFieldEditor(NewDSXMLWizardFactory.TEMPLATE_LIST[0]);
			Composite q = new Composite(topLevel, 0);
			GridLayout l = new GridLayout(4, false);
			q.setLayout(l);
			connProfileSelEditor.doFillIntoGrid(q);
			templateSelEditor.doFillIntoGrid(q);
			connProfileSelEditor.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					validatePage();
				}
			});
			validatePage();
		}
    	
		protected boolean validatePage() {
			boolean valid = super.validatePage();
			if(valid && connProfileSelEditor != null) {
				String p = connProfileSelEditor.getValueAsString();
				if(p == null || p.length() == 0) {
					valid = false;
					setErrorMessage("Connenction profile must be set.");
				}
			}
			return valid;
		}

	}

	/**
	 * @return
	 */
	private Object getConnectionProfileDefaultValue() {
		List<String> values = getConnectionProfileNameList();
		String defaultDs = NewDSXMLWizardFactory.EMPTY_PROFILE; // Use preference, or dialog settings?
		return values.contains(defaultDs) ? defaultDs
				: !values.isEmpty() ? values.get(0) : NewDSXMLWizardFactory.EMPTY_PROFILE; //$NON-NLS-1$
	}

	private static List<String> getConnectionProfileNameList() {
		IConnectionProfile[] profiles = ProfileManager.getInstance()
				.getProfilesByCategory(
						"org.eclipse.datatools.connectivity.db.category"); //$NON-NLS-1$
		List<String> names = new ArrayList<String>();
		for (IConnectionProfile connectionProfile : profiles) {
			names.add(connectionProfile.getName());
		}
		return names;
	}

}

interface IDSDataModelProperties {

	String PROJECT_NAME = "project.name"; //$NON-NLS-1$

	/**
	 * Connection profile name
	 */
	String CONNECTION_PROFILE = "connection.profile"; //$NON-NLS-1$

	String TEMPLATE = "template"; //$NON-NLS-1$
	
	/**
	 * Connection user name token
	 */
	String DB_USER_NAME = "hibernate.connection.username"; //$NON-NLS-1$
	String DATATOOLS_DB_USER_NAME = "org.eclipse.datatools.connectivity.db.username"; //$NON-NLS-1$

	/**
	 * JDBC driver class name token
	 */
	String JDBC_DRIVER_CLASS_NAME = "hibernate.connection.driver_class"; //$NON-NLS-1$
	String DATATOOLS_JDBC_DRIVER_CLASS_NAME = "org.eclipse.datatools.connectivity.db.driverClass"; //$NON-NLS-1$

	/**
	 * Connection user name password
	 */
	String DB_USER_PASSWORD = "hibernate.connection.password"; //$NON-NLS-1$
	String DATATOOLS_DB_USER_PASSWORD = "org.eclipse.datatools.connectivity.db.password"; //$NON-NLS-1$

	/**
	 * JDBC Connection URL token
	 */
	String JDBC_URL_FOR_DB = "hibernate.connection.url"; //$NON-NLS-1$
	String DATATOOLS_JDBC_URL_FOR_DB = "org.eclipse.datatools.connectivity.db.URL"; //$NON-NLS-1$

	/**
	 * Driver file name 
	 */
	String JDBC_DRIVER_JAR_PATH = "driver.file"; //$NON-NLS-1$
	String DATATOOLS_JDBC_DRIVER_JAR_PATH = "org.eclipse.datatools.connectivity.driverDefinitionID"; //$NON-NLS-1$
}

class DSDataModelProvider extends AbstractDataModelProvider implements IDSDataModelProperties {

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	/**
	* Returns set of facet properties for facet wizard page
	* 
	* @return set of property names
	*/
	public Set getPropertyNames() {
		Set<String> names = super.getPropertyNames();
		
		names.add(PROJECT_NAME);
		names.add(CONNECTION_PROFILE);
		
	// Database group
//		names.add(DB_TYPE);
//		names.add(HIBERNATE_DIALECT);
		names.add(JDBC_DRIVER_CLASS_NAME);
		names.add(JDBC_URL_FOR_DB);

		names.add(DB_USER_NAME);
		names.add(DB_USER_PASSWORD);
//		names.add(DB_SCHEMA_NAME);
//		names.add(DB_DEFAULT_SCHEMA_NAME);
//		names.add(DB_CATALOG_NAME);
//		names.add(DB_DEFAULT_CATALOG_NAME);
//		names.add(DB_ALREADY_EXISTS);
//		names.add(RECREATE_TABLES_AND_DATA_ON_DEPLOY);

		names.add(JDBC_DRIVER_JAR_PATH);

		return names;
	}

	/**
	* Returns default value for a given property
	* 
	* @param propertyName name of property which default value requested
	* @return default value 
	*/
	public Object getDefaultProperty(String propertyName) {
		return super.getDefaultProperty(propertyName);
	}

	public static File TEMPLATE_FOLDER = null;

	/**
	 * Calculate path to templates folder
	 *  
	 * @return path to templates
	 * @throws IOException if templates folder not found
	 */
	public static File getTemplatesFolder() throws IOException {
		if(TEMPLATE_FOLDER == null) {
			Bundle bundle = WebUiPlugin.getDefault().getBundle();
			String version = bundle.getVersion().toString();
			IPath stateLocation = Platform.getStateLocation(bundle);
			File templatesDir = FileLocator.getBundleFile(bundle);
			if(templatesDir.isFile()) {
				File toCopy = new File(stateLocation.toFile(),version);
				if(!toCopy.exists()) {
					toCopy.mkdirs();
					UnzipOperation unZip = new UnzipOperation(templatesDir.getAbsolutePath());
					unZip.execute(toCopy,"templates.*"); //$NON-NLS-1$
				}
				templatesDir = toCopy;
			}
			TEMPLATE_FOLDER = new File(templatesDir,"templates"); //$NON-NLS-1$
		}
		return TEMPLATE_FOLDER;
	}


	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelProvider#init()
	 */
	@Override
	public void init() {
		super.init();
	}

	public static void applyConnectionProfile(IProject project, IDataModel model) {
		if (model.getProperty(CONNECTION_PROFILE) != null) {
			IConnectionProfile connProfile = ProfileManager.getInstance().getProfileByName(model.getProperty(CONNECTION_PROFILE).toString());
			if (connProfile != null) {
				Properties props = connProfile.getBaseProperties(); // Properties("org.eclipse.datatools.connectivity.db.generic.connectionProfile");

				// Collect properties name from DTP Connection Profile
				model.setProperty(DB_USER_NAME,
						props.get(DATATOOLS_DB_USER_NAME) == null
						? EMPTY_STRING	: props.get(DATATOOLS_DB_USER_NAME).toString());

				model.setProperty(
						JDBC_DRIVER_CLASS_NAME,
						props.get(DATATOOLS_JDBC_DRIVER_CLASS_NAME) == null
						? EMPTY_STRING	: props.get(DATATOOLS_JDBC_DRIVER_CLASS_NAME).toString());

				model.setProperty(
						DB_USER_PASSWORD,
						props.get(DATATOOLS_DB_USER_PASSWORD) == null
						? EMPTY_STRING	: props.get(DATATOOLS_DB_USER_PASSWORD).toString());

				model.setProperty(
						JDBC_URL_FOR_DB,
						props.get(DATATOOLS_JDBC_URL_FOR_DB) == null
						? EMPTY_STRING	: props.get(DATATOOLS_JDBC_URL_FOR_DB).toString());

				if(props.get(DATATOOLS_JDBC_DRIVER_JAR_PATH) != null) {
					model.setProperty(
							JDBC_DRIVER_JAR_PATH,
							DriverManager
									.getInstance()
									.getDriverInstanceByID(
											props.get(DATATOOLS_JDBC_DRIVER_JAR_PATH).toString()).getJarListAsArray());
				}
			}
		}

//This wizard has no input field for DB_DEFAULT_SCHEMA_NAME and DB_DEFAULT_CATALOG_NAME
//		String defaultSchema = (String) model.getProperty(DB_DEFAULT_SCHEMA_NAME);
//		if (!EMPTY_STRING.equals(defaultSchema)) {
//			model.setStringProperty(DB_SCHEMA_NAME,
//					PROP_INDENT	+ NLS.bind(PROP_DECL, new String[] {DB_DEFAULT_SCHEMA_NAME, defaultSchema }));
//		}
//
//		String defaultCatalog = (String) model.getProperty(DB_DEFAULT_CATALOG_NAME);
//		if (!EMPTY_STRING.equals(defaultCatalog)) {
//			model.setStringProperty(DB_CATALOG_NAME,
//					PROP_INDENT + NLS.bind(PROP_DECL, new String[] {DB_DEFAULT_CATALOG_NAME, defaultCatalog}));
//		}
	}

}

class NewDSXMLWizardFactory {
	static String EMPTY_PROFILE = "                            "; //$NON-NLS-1$
	
	public static String[] TEMPLATE_LIST = {
		"Format: JBoss AS 5", "Format: JBoss AS 7" //$NON-NLS-1$ //$NON-NLS-2$
	};

	public static IFieldEditor createTemplateFieldEditor(Object defaultValue) {
		IFieldEditor result = IFieldEditorFactory.INSTANCE.createComboEditor(
				IDSDataModelProperties.TEMPLATE, 
				Messages.NewDSXMLWizard_TEMPLATE_FIELD, 
				Arrays.asList(TEMPLATE_LIST), 
				TEMPLATE_LIST[0]);
		return result;
	}
	/**
	 * Creates Selection Field of Connection Profiles
	 * @param defaultValue
	 * @param canBeEmpty
	 * @return
	 */
	public static IFieldEditor createConnectionProfileSelectionFieldEditor(Object defaultValue, IValidator validator, final boolean canBeEmpty) {
		EditConnectionProfileAction editAction = new EditConnectionProfileAction(validator);
		NewConnectionProfileAction newAction = new NewConnectionProfileAction(validator);
		List<String> profiles = getConnectionProfileNameList();
		if(canBeEmpty || profiles.isEmpty()) {
			profiles.add(0, EMPTY_PROFILE);
		}
		IFieldEditor connProfileSelEditor = IFieldEditorFactory.INSTANCE.createComboWithTwoButtons(
				IDSDataModelProperties.CONNECTION_PROFILE,
				Messages.NewDSXMLWizard_CONNECTION_PROFILE_FIELD,
				profiles,
				defaultValue,
				false, editAction,
				newAction,
				ValidatorFactory.NO_ERRORS_VALIDATOR);
		editAction.setEditor(connProfileSelEditor);
		newAction.setEditor(connProfileSelEditor);
		final ButtonFieldEditor editButton = (ButtonFieldEditor)((CompositeEditor)connProfileSelEditor).getEditors().get(2);
		editButton.setEnabled(!"".equals(defaultValue) && !EMPTY_PROFILE.equals(defaultValue)); //$NON-NLS-1$
		if(canBeEmpty || profiles.isEmpty()) {
			connProfileSelEditor.addPropertyChangeListener(new PropertyChangeListener(){
				public void propertyChange(PropertyChangeEvent evt) {
					boolean ediatble = !"".equals(evt.getNewValue()) && !EMPTY_PROFILE.equals(evt.getNewValue()); //$NON-NLS-1$
					editButton.setEnabled(ediatble);
				}
			});
		}
		final ComboFieldEditor comboEditor = ((ComboFieldEditor)((CompositeEditor)connProfileSelEditor).getEditors().get(1));
		final IProfileListener profileListener = new IProfileListener() {
			private void update() {
				final List<String> profiles = getConnectionProfileNameList();
				if(canBeEmpty || profiles.isEmpty()) {
					profiles.add(0, EMPTY_PROFILE);
				}
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						comboEditor.setTags((profiles.toArray(new String[0])));
					}
				});
			}

			public void profileAdded(IConnectionProfile profile) {
				update();
			}

			public void profileChanged(IConnectionProfile profile) {
				update();
			}

			public void profileDeleted(IConnectionProfile profile) {
				update();
			}
		};
		ProfileManager.getInstance().addProfileListener(profileListener);
		comboEditor.addDisposeListener(new DisposeListener(){
			public void widgetDisposed(DisposeEvent e) {
				ProfileManager.getInstance().removeProfileListener(profileListener);
			}
		});
		return connProfileSelEditor;
	}

	private static class EditConnectionProfileAction extends ButtonFieldEditor.ButtonPressedAction {

		private IValidator validator;
		private IFieldEditor connProfileSelEditor;

		/**
		 * @param validator
		 */
		public EditConnectionProfileAction(IValidator validator) {
			super(Messages.NewDSXMLWizard_EDIT_BUTTON);
			this.validator = validator;
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.jface.action.Action#run()
		 */
		@Override
		public void run() {
			IConnectionProfile selectedProfile = ProfileManager.getInstance()
					.getProfileByName(getFieldEditor().getValue().toString());
			String oldName = getFieldEditor().getValue().toString();

			if (selectedProfile == null) {
				return;
			}
			PropertyDialog.createDialogOn(Display.getCurrent().getActiveShell(),
							"org.eclipse.datatools.connectivity.db.generic.profileProperties", //$NON-NLS-1$
							selectedProfile).open();
			if (!oldName.equals(selectedProfile.getName())) {
				getFieldEditor().setValue(selectedProfile.getName());
				((ITaggedFieldEditor) ((CompositeEditor) connProfileSelEditor)
						.getEditors().get(1)).setTags(getConnectionProfileNameList()
						.toArray(new String[0]));
				oldName = selectedProfile.getName();
			}
			validator.validate(selectedProfile.getName(), null);
		}

		public void setEditor(IFieldEditor connProfileSelEditor) {
			this.connProfileSelEditor = connProfileSelEditor; 
		}
	};

	/**
	 * Handler for ButtonFieldEditor that shows Property Editor dialog for
	 * selected ConnectionProfile
	 * 
	 * @author eskimo
	 */
	private static class NewConnectionProfileAction extends	ButtonFieldEditor.ButtonPressedAction {

		private IValidator validator;
		private IFieldEditor connProfileSelEditor;

		public NewConnectionProfileAction(IValidator validator) {
			super(Messages.NewDSXMLWizard_NEW_BUTTON);
			this.validator = validator;
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.jface.action.Action#run()
		 */
		@Override
		public void run() {
			IProfileListener listener = new ConnectionProfileChangeListener(validator, connProfileSelEditor);

			ProfileManager.getInstance().addProfileListener(listener);
			NewCPWizardCategoryFilter filter = new NewCPWizardCategoryFilter("org.eclipse.datatools.connectivity.db.category"); //$NON-NLS-1$
			NewCPWizard wizard = new NewCPWizard(filter, null);
			new NewConnectionProfileWizard() {
				public boolean performFinish() {
					// create profile only
					try {
						ProfileManager.getInstance().createProfile(
								getProfileName() == null ? "" //$NON-NLS-1$
										: getProfileName(),
								getProfileDescription() == null ? "" //$NON-NLS-1$
										: getProfileDescription(),
								mProviderID,
								getProfileProperties(),
								mProfilePage.getRepository() == null ? "" //$NON-NLS-1$
										: mProfilePage.getRepository()
												.getName(), false);
					} catch (ConnectionProfileException e) {
						WebUiPlugin.getPluginLog().logError(e);
					}

					return true;
				}
			};
			WizardDialog wizardDialog = new WizardDialog(Display.getCurrent()
					.getActiveShell(), wizard);
			wizardDialog.open();
			ProfileManager.getInstance().removeProfileListener(listener);
		}

		public void setEditor(IFieldEditor connProfileSelEditor) {
			this.connProfileSelEditor = connProfileSelEditor; 
		}
	}

	private static class ConnectionProfileChangeListener implements IProfileListener {

		private IFieldEditor connProfileSelEditor;
		private IValidator validator;

		/**
		 * @param validator
		 */
		public ConnectionProfileChangeListener(IValidator validator, IFieldEditor connProfileSelEditor) {
			this.validator = validator;
			this.connProfileSelEditor = connProfileSelEditor;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.datatools.connectivity.IProfileListener#profileAdded(org.eclipse.datatools.connectivity.IConnectionProfile)
		 */
		public void profileAdded(final IConnectionProfile profile) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					connProfileSelEditor.setValue(profile.getName());
					((ITaggedFieldEditor) ((CompositeEditor) connProfileSelEditor)
							.getEditors().get(1)).setTags(getConnectionProfileNameList()
							.toArray(new String[0]));
					Control c = (Control)connProfileSelEditor.getEditorControls()[0];
					c.getParent().layout();
					c.getParent().getParent().layout();					
					validator.validate(profile.getName(), null);
				}
			});
		}

		/* (non-Javadoc)
		 * @see org.eclipse.datatools.connectivity.IProfileListener#profileChanged(org.eclipse.datatools.connectivity.IConnectionProfile)
		 */
		public void profileChanged(IConnectionProfile profile) {
			profileAdded(profile);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.datatools.connectivity.IProfileListener#profileDeleted(org.eclipse.datatools.connectivity.IConnectionProfile)
		 */
		public void profileDeleted(IConnectionProfile profile) {
			// this event never happens
		}
	}

	private static List<String> getConnectionProfileNameList() {
		IConnectionProfile[] profiles = ProfileManager.getInstance()
				.getProfilesByCategory("org.eclipse.datatools.connectivity.db.category"); //$NON-NLS-1$
		List<String> names = new ArrayList<String>();
		for (IConnectionProfile connectionProfile : profiles) {
			names.add(connectionProfile.getName());
		}
		return names;
	}

}

class FilterSetFactory {
	
	public static FilterSet JDBC_TEMPLATE;
	public static FilterSet PROJECT_TEMPLATE;
	public static FilterSet FILTERS_TEMPLATE;
	public static FilterSet HIBERNATE_DIALECT_TEMPLATE;
	
	static {
		JDBC_TEMPLATE = new FilterSet();
		JDBC_TEMPLATE.addFilter("jdbcUrl","${hibernate.connection.url}"); //$NON-NLS-1$ //$NON-NLS-2$
		JDBC_TEMPLATE.addFilter("driverClass","${hibernate.connection.driver_class}"); //$NON-NLS-1$ //$NON-NLS-2$
		JDBC_TEMPLATE.addFilter("username","${hibernate.connection.username}"); //$NON-NLS-1$ //$NON-NLS-2$
		JDBC_TEMPLATE.addFilter("password","${hibernate.connection.password}"); //$NON-NLS-1$ //$NON-NLS-2$
		JDBC_TEMPLATE.addFilter("catalogProperty","${catalog.property}"); //$NON-NLS-1$ //$NON-NLS-2$
		JDBC_TEMPLATE.addFilter("schemaProperty","${schema.property}"); //$NON-NLS-1$ //$NON-NLS-2$
		
		PROJECT_TEMPLATE = new FilterSet();
		PROJECT_TEMPLATE.addFilter("projectName","${project.name}"); //$NON-NLS-1$ //$NON-NLS-2$
		PROJECT_TEMPLATE.addFilter("ejbProjectName","${seam.ejb.project}"); //$NON-NLS-1$ //$NON-NLS-2$
		PROJECT_TEMPLATE.addFilter("jbossHome","${jboss.home}"); //$NON-NLS-1$ //$NON-NLS-2$
		PROJECT_TEMPLATE.addFilter("hbm2ddl","${hibernate.hbm2ddl.auto}"); //$NON-NLS-1$ //$NON-NLS-2$
		PROJECT_TEMPLATE.addFilter("driverJar","${driver.file}"); //$NON-NLS-1$ //$NON-NLS-2$
		PROJECT_TEMPLATE.addFilter("debug","true");		 //$NON-NLS-1$ //$NON-NLS-2$
		//todo: keep this local for seam2 ?
		PROJECT_TEMPLATE.addFilter("skin","blueSky"); //$NON-NLS-1$ //$NON-NLS-2$
		PROJECT_TEMPLATE.addFilter("connectionProfile","${connection.profile}"); //$NON-NLS-1$ //$NON-NLS-2$
		
		FILTERS_TEMPLATE = new FilterSet();
		FILTERS_TEMPLATE.addFilter("interfaceName","${interface.name}"); //$NON-NLS-1$ //$NON-NLS-2$
		FILTERS_TEMPLATE.addFilter("beanName","${bean.name}"); //$NON-NLS-1$ //$NON-NLS-2$
		FILTERS_TEMPLATE.addFilter("entityName","${entity.name}"); //$NON-NLS-1$ //$NON-NLS-2$
		FILTERS_TEMPLATE.addFilter("methodName","${method.name}"); //$NON-NLS-1$ //$NON-NLS-2$
		FILTERS_TEMPLATE.addFilter("componentName","${component.name}"); //$NON-NLS-1$ //$NON-NLS-2$
		FILTERS_TEMPLATE.addFilter("pageName","${page.name}"); //$NON-NLS-1$ //$NON-NLS-2$
		FILTERS_TEMPLATE.addFilter("masterPageName","${masterPage.name}"); //$NON-NLS-1$ //$NON-NLS-2$
		FILTERS_TEMPLATE.addFilter("actionPackage","${action.package}"); //$NON-NLS-1$ //$NON-NLS-2$
		FILTERS_TEMPLATE.addFilter("modelPackage","${model.package}"); //$NON-NLS-1$ //$NON-NLS-2$
		FILTERS_TEMPLATE.addFilter("testPackage","${test.package}"); //$NON-NLS-1$ //$NON-NLS-2$
		FILTERS_TEMPLATE.addFilter("listName","${component.name}List"); //$NON-NLS-1$ //$NON-NLS-2$
		FILTERS_TEMPLATE.addFilter("homeName","${component.name}Home"); //$NON-NLS-1$ //$NON-NLS-2$
		FILTERS_TEMPLATE.addFilter("query","${query.text}"); //$NON-NLS-1$ //$NON-NLS-2$
		FILTERS_TEMPLATE.addFilter("seamTestProject","${seam.test.project}"); //$NON-NLS-1$ //$NON-NLS-2$

		HIBERNATE_DIALECT_TEMPLATE = new FilterSet();
		HIBERNATE_DIALECT_TEMPLATE.addFilter("hibernate.dialect","${hibernate.dialect}"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static FilterSet createJdbcFilterSet(IDataModel values, boolean encodeValuesForPropertyFiles) {
		return applyProperties((FilterSet)JDBC_TEMPLATE.clone(), values, encodeValuesForPropertyFiles);
	}

	public static FilterSet createProjectFilterSet(IDataModel values, boolean encodeValuesForPropertyFiles){
		return applyProperties((FilterSet)PROJECT_TEMPLATE.clone(), values, encodeValuesForPropertyFiles);
	}

	public static FilterSet createFiltersFilterSet(IDataModel values, boolean encodeValuesForPropertyFiles) {
		return applyProperties((FilterSet)FILTERS_TEMPLATE.clone(), values, encodeValuesForPropertyFiles);
	}

	public static FilterSet createHibernateDialectFilterSet(IDataModel values, boolean encodeValuesForPropertyFiles) {
		return applyProperties((FilterSet)HIBERNATE_DIALECT_TEMPLATE.clone(), values, encodeValuesForPropertyFiles);
	}

	public static FilterSet createFiltersFilterSet(Map<?,?> values, boolean encodeValuesForPropertyFiles) {
		return applyProperties((FilterSet)FILTERS_TEMPLATE.clone(), values, false);
	}

	public static FilterSet createJdbcFilterSet(IDataModel values) {
		return createJdbcFilterSet(values, false);
	}

	public static FilterSet createProjectFilterSet(IDataModel values){
		return createProjectFilterSet(values, false);
	}

	public static FilterSet createFiltersFilterSet(IDataModel values) {
		return createFiltersFilterSet(values, false);
	}

	public static FilterSet createHibernateDialectFilterSet(IDataModel values) {
		return createHibernateDialectFilterSet(values, false);
	}

	public static FilterSet createFiltersFilterSet(Map<?,?> values) {
		return createFiltersFilterSet(values, false);
	}

	private static FilterSet applyProperties(FilterSet template, IDataModel values, boolean encodeValuesForPropertyFiles) {
		FilterSet result = new FilterSet();
		for (Object filter : template.getFilterHash().keySet()) {
			String value = template.getFilterHash().get(filter).toString();
			for (Object property : values.getAllProperties()) {
				if(value.contains("${"+property.toString()+"}")) { //$NON-NLS-1$ //$NON-NLS-2$
					Object propertyValue = values.getProperty(property.toString());
					if(encodeValuesForPropertyFiles && propertyValue!=null) {
						propertyValue = propertyValue.toString().replace("\\", "\\\\"); //$NON-NLS-1$ //$NON-NLS-2$
					}
					value = value.replace("${"+property.toString()+"}",propertyValue==null?"":propertyValue.toString()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
			}
			result.addFilter(filter.toString(), value);
		}
		return result;
	}

	private static FilterSet applyProperties(FilterSet template, Map<?,?> values, boolean encodeValuesForPropertyFiles) {
		FilterSet result = new FilterSet();
		for (Object filter : template.getFilterHash().keySet()) {
			String value = template.getFilterHash().get(filter).toString();
			for (Object property : values.keySet()){
				if(value.contains("${"+property.toString()+"}")) { //$NON-NLS-1$ //$NON-NLS-2$
					Object propertyValue = values.get(property.toString());
					if(encodeValuesForPropertyFiles && propertyValue!=null) {
						propertyValue = propertyValue.toString().replace("\\", "\\\\");//$NON-NLS-1$ //$NON-NLS-2$
					}
					value = value.replace("${"+property.toString()+"}",propertyValue==null?"":propertyValue.toString()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
			}
			result.addFilter(filter.toString(), value);
		}
		return result;
	}
}
