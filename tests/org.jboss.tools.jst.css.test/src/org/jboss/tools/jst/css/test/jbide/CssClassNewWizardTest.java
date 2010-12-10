package org.jboss.tools.jst.css.test.jbide;

import java.lang.reflect.Field;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.test.util.ProjectImportTestSetup;
import org.jboss.tools.test.util.WorkbenchUtils;

public class CssClassNewWizardTest extends TestCase {

	private static final String PROJECT_NAME = "cssTest"; //$NON-NLS-1$
	private static final String PROJECT_BUNDLE_NAME = "org.jboss.tools.jst.css.test"; //$NON-NLS-1$
	private static final String PROJECT_PATH = "/resources/cssTest"; //$NON-NLS-1$
	private static final String WIZARD_ID = "org.jboss.tools.jst.web.ui.wizards.newfile.NewCSSClassWizard"; //$NON-NLS-1$
	private static final String CSS_FILE_PATH = "WebContent/pages/test.css"; //$NON-NLS-1$

	private IProject project;
	private IWizard wizard;
	private WizardDialog dialog;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		project = (IProject) ResourcesPlugin.getWorkspace().getRoot()
				.findMember(PROJECT_NAME);
		if (project == null) {
			ProjectImportTestSetup projectSetup = new ProjectImportTestSetup(
					null, PROJECT_BUNDLE_NAME, PROJECT_PATH, PROJECT_NAME);
			project = projectSetup.importProject();
		}
	}

	@Override
	protected void tearDown() throws Exception {
		close();
	}

	public void testCssClassNewWizardTestIsCreated() {

		wizard = WorkbenchUtils.findWizardByDefId(WIZARD_ID);

		ArrayList<IProject> list = new ArrayList<IProject>();
		StructuredSelection selection = new StructuredSelection(list);
		((IWorkbenchWizard) wizard).init(PlatformUI.getWorkbench(), selection);
		dialog = new WizardDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), wizard);
		dialog.setBlockOnOpen(false);
		dialog.open();

		try {
			IWizardPage startPage = wizard.getStartingPage();
			assertNotNull(startPage);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}

	public void testCssClassNewWizardValidation() {
		wizard = getWizard(false);
		boolean canFinish = wizard.canFinish();
		assertFalse("Finish button is enabled at first wizard page.", canFinish); //$NON-NLS-1$
	}

	public void testCssClassNewWizardValidation2() {

		wizard = getWizard(true);
		boolean canFinish = wizard.canFinish();

		// Assert Finish button is enabled by default if wizard is called on
		// Project
		assertFalse(
				"Finish button is disabled at first wizard page.", canFinish); //$NON-NLS-1$

		// Assert Finish button is disabled and error is present if
		// Folder field is empty
		// All other fields are correct

		// Assert Finish button is disabled and error is present if
		// Folder field points to folder that doesn't exist
		// All other fields are correct

		// Assert Finish button is disabled and error is present if
		// Folder field is correct
		// Name field is empty

		// Assert Finish button is disabled and error is present if
		// Folder field is correct
		// Name field contains forbidden characters

		// Assert Finish button is disabled and error is present if
		// Folder field is correct
		// Name field contains file name that already exists
	}

	public void testCssClassEditing() {
		ArrayList<IResource> list = new ArrayList<IResource>();
		IResource cssFile = project.findMember(CSS_FILE_PATH);
		assertNotNull(cssFile);
		list.add(cssFile);
		StructuredSelection selection = new StructuredSelection(list);
		wizard = WorkbenchUtils.findWizardByDefId(WIZARD_ID);

		((IWorkbenchWizard) wizard).init(PlatformUI.getWorkbench(), selection);

		dialog = new WizardDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), wizard);
		dialog.setBlockOnOpen(false);
		dialog.open();

		assertFalse("Finish button is not disabled.", wizard.canFinish()); //$NON-NLS-1$
		assertFalse(
				"Next button is not disabled.", wizard.getContainer().getCurrentPage().canFlipToNextPage()); //$NON-NLS-1$

		try {

			Field selectFileTextField = wizard.getContainer().getCurrentPage()
					.getClass().getDeclaredField("selectFileText"); //$NON-NLS-1$
			selectFileTextField.setAccessible(true);
			Text selectFileText = (Text) selectFileTextField.get(wizard
					.getContainer().getCurrentPage());
			selectFileText.setText(cssFile.getFullPath().toString());
			Field classNameTextField = wizard.getContainer().getCurrentPage()
					.getClass().getDeclaredField("classNameText"); //$NON-NLS-1$
			classNameTextField.setAccessible(true);
			Text classNameTextText = (Text) classNameTextField.get(wizard
					.getContainer().getCurrentPage());
			classNameTextText.setText("newCSS"); //$NON-NLS-1$

		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertTrue(
				"Next button is disabled.", wizard.getContainer().getCurrentPage().canFlipToNextPage()); //$NON-NLS-1$
		assertFalse("Finish button is not disabled.", wizard.canFinish()); //$NON-NLS-1$
		wizard.getContainer().showPage(
				wizard.getNextPage(wizard.getContainer().getCurrentPage()));
		assertTrue("Finish button is  disabled.", wizard.canFinish()); //$NON-NLS-1$
	}

	public void testCssClassWithEditor() {

		IResource cssFile = project.findMember(CSS_FILE_PATH);

		IEditorPart facesConfigEditor = WorkbenchUtils.openEditor(cssFile
				.getFullPath().toString());

		assertTrue(facesConfigEditor instanceof StructuredTextEditor);

//		ArrayList<IResource> list = new ArrayList<IResource>();

//		assertNotNull(cssFile);
//		list.add(cssFile);
//		StructuredSelection selection = new StructuredSelection(list);
		wizard = WorkbenchUtils.findWizardByDefId(WIZARD_ID);

//		((IWorkbenchWizard) wizard).init(PlatformUI.getWorkbench(), selection);

		dialog = new WizardDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), wizard);
		dialog.setBlockOnOpen(false);
		dialog.open();
	}

	private void close() {
		if (wizard != null) {
			wizard.performCancel();
			wizard = null;
		}
		if (dialog != null) {
			dialog.close();
			dialog = null;
		}
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.closeAllEditors(false);
	}

	private IWizard getWizard(boolean isProjectUsed) {

		ArrayList<IProject> list = new ArrayList<IProject>();
		if (isProjectUsed) {
			list.add(project);
		}
		StructuredSelection selection = new StructuredSelection(list);

		wizard = WorkbenchUtils.findWizardByDefId(WIZARD_ID);
		((IWorkbenchWizard) wizard).init(PlatformUI.getWorkbench(), selection);

		dialog = new WizardDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), wizard);
		dialog.setBlockOnOpen(false);
		dialog.open();

		return wizard;
	}
}
