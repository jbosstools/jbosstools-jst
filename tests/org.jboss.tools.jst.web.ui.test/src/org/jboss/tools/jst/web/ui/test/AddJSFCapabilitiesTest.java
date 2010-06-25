package org.jboss.tools.jst.web.ui.test;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.common.meta.key.WizardKeys;
import org.jboss.tools.common.model.ui.ModelUIPlugin;
import org.jboss.tools.common.model.ui.action.AddNatureActionDelegate;
import org.jboss.tools.common.model.ui.util.ExtensionPointUtils;
import org.jboss.tools.jst.web.ui.wizards.project.ImportWebProjectWizard;
import org.jboss.tools.test.util.JobUtils;
import org.jboss.tools.test.util.ProjectImportTestSetup;


public class AddJSFCapabilitiesTest extends TestCase {
	IProject project = null;

	public AddJSFCapabilitiesTest() {
		super("Add JSF Capabilities Test");
	}

	public AddJSFCapabilitiesTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		JobUtils.waitForIdle(3000);
		IResource project = ResourcesPlugin.getWorkspace().getRoot().findMember("test_add_jsf_capabilities");
		if(project == null) {
			ProjectImportTestSetup setup = new ProjectImportTestSetup(
					this,
					"org.jboss.tools.jst.web.ui.test",
					"projects/test_add_jsf_capabilities",
					"test_add_jsf_capabilities");
			project = setup.importProject();
		}
		this.project = project.getProject();
		JobUtils.waitForIdle();
	}

	public void testAddJSFCapabilities() {
		ImportWebProjectWizard wizard = (ImportWebProjectWizard)new Act().getWizard(project);
		WizardDialog dialog = new WizardDialog(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				wizard);
		dialog.setBlockOnOpen(false);
		dialog.open();

		//TODO implement finish of wizard and check of results.
		//TODO it is necessary to provide server runtime; without it, wizard cannot finish
		System.out.println(wizard.canFinish());
	}

	private void refreshProject(IProject project){
		try {
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
			JobUtils.waitForIdle();
			JobUtils.delay(2000);
		} catch (CoreException e) {
			// ignore
		}
	}
	
	class Act extends AddNatureActionDelegate {

		protected IWizard getWizard(IProject project) {
			ImportWebProjectWizard wizard = (ImportWebProjectWizard)ExtensionPointUtils.findImportWizardsItem(
					"org.jboss.tools.jsf",
					"org.jboss.tools.jsf.ui.wizard.project.ImportProjectWizard" //$NON-NLS-1$
			);
			if (wizard == null) throw new IllegalArgumentException("Wizard org.jboss.tools.common.model.ui.wizards.ImportProjectWizard is not found.");	 //$NON-NLS-1$
			wizard.setInitialName(project.getName());
			wizard.setInitialLocation(findWebXML(project.getLocation().toString()));
			wizard.init(ModelUIPlugin.getDefault().getWorkbench(), null);
			wizard.setWindowTitle(WizardKeys.getString("ADD_JSF_NATURE")); //$NON-NLS-1$
			return wizard;
		}

		protected String getNatureID() {
			return null;
		}
		
	}
}
