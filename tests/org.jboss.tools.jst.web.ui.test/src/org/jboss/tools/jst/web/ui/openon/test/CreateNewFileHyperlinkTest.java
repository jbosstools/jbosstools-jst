/*******************************************************************************
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.openon.test;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.wizards.NewWizardRegistry;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.jboss.tools.jst.jsp.test.openon.HyperlinkTestUtil;
import org.jboss.tools.jst.jsp.test.openon.HyperlinkTestUtil.TestHyperlink;
import org.jboss.tools.jst.jsp.test.openon.HyperlinkTestUtil.TestRegion;
import org.jboss.tools.jst.web.ui.internal.text.ext.hyperlink.internal.CreateNewFileHyperlink;
import org.jboss.tools.jst.web.ui.internal.text.ext.hyperlink.internal.CreateNewFileHyperlinkDetector;

import junit.framework.TestCase;

public class CreateNewFileHyperlinkTest extends TestCase{
	private static final String PROJECT_NAME = "Test"; //$NON-NLS-1$
	
	public IProject project = null;

	protected void setUp() {
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(
				PROJECT_NAME);
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(false);
	}
	
	protected void tearDown() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(false);
	}
	
	public CreateNewFileHyperlinkTest() {
		super("Offer to create missing file Open On test"); //$NON-NLS-1$
	}
	
	public void testDetectorForXML() throws Exception{
		checkHyperlinkDetector("/src/test/test.xml"); //$NON-NLS-1$
	}

	public void testDetectorJSP() throws Exception{
		checkHyperlinkDetector("/src/test/test.jsp"); //$NON-NLS-1$
	}
	
	public void testDetectorXHTML() throws Exception{
		checkHyperlinkDetector("/src/test/test.xhtml"); //$NON-NLS-1$
	}

	public void testDetectorHTML() throws Exception{
		checkHyperlinkDetector("/src/test/test.html"); //$NON-NLS-1$
	}
	
	public void testDetectorHTM() throws Exception{
		checkHyperlinkDetector("/src/test/test.htm"); //$NON-NLS-1$
	}
	
	private void checkHyperlinkDetector(String pageName) throws Exception{
		ArrayList<TestRegion> regionList = new ArrayList<TestRegion>();
		
		regionList.add(new TestRegion("anyfile.css", new TestHyperlink[]{ //$NON-NLS-1$
			new TestHyperlink(CreateNewFileHyperlink.class, "File 'anyfile.css' does not exist. Click here to create.") //$NON-NLS-1$
		}));
		regionList.add(new TestRegion("anyfile.js", new TestHyperlink[]{ //$NON-NLS-1$
			new TestHyperlink(CreateNewFileHyperlink.class, "File 'anyfile.js' does not exist. Click here to create.") //$NON-NLS-1$
		}));

		regionList.add(new TestRegion("anyfolder/anyfile.jsp", new TestHyperlink[]{ //$NON-NLS-1$
			new TestHyperlink(CreateNewFileHyperlink.class, "File 'anyfolder/anyfile.jsp' does not exist. Click here to create.") //$NON-NLS-1$
		}));
		regionList.add(new TestRegion("./anyfile.htm", new TestHyperlink[]{ //$NON-NLS-1$
			new TestHyperlink(CreateNewFileHyperlink.class, "File './anyfile.htm' does not exist. Click here to create.") //$NON-NLS-1$
		}));

		regionList.add(new TestRegion("/anyfolder/anyfile.xhtml", new TestHyperlink[]{ //$NON-NLS-1$
			new TestHyperlink(CreateNewFileHyperlink.class, "File '/anyfolder/anyfile.xhtml' does not exist. Click here to create.") //$NON-NLS-1$
		}));
		regionList.add(new TestRegion("/anyfolder/anyfile.html", new TestHyperlink[]{ //$NON-NLS-1$
			new TestHyperlink(CreateNewFileHyperlink.class, "File '/anyfolder/anyfile.html' does not exist. Click here to create.") //$NON-NLS-1$
		}));

		
		HyperlinkTestUtil.checkRegionsInTextEditor(project, pageName, regionList, new CreateNewFileHyperlinkDetector());
	}
	
	public void testRemoteFiles() throws Exception{
		checkHyperlinkDetectorEmptyList("/src/test/test_remote.jsp"); //$NON-NLS-1$
	}
	
	private void checkHyperlinkDetectorEmptyList(String pageName) throws Exception{
		ArrayList<TestRegion> regionList = new ArrayList<TestRegion>();
		
		HyperlinkTestUtil.checkRegionsInTextEditor(project, pageName, regionList, new CreateNewFileHyperlinkDetector());
	}

	public void testHyperlinksForXML() throws Exception{
		checkHyperlinks("/src/test/test.xml"); //$NON-NLS-1$
	}

	public void testHyperlinksJSP() throws Exception{
		checkHyperlinks("/src/test/test.jsp"); //$NON-NLS-1$
	}
	
	public void testHyperlinksXHTML() throws Exception{
		checkHyperlinks("/src/test/test.xhtml"); //$NON-NLS-1$
	}

	public void testHyperlinksHTML() throws Exception{
		checkHyperlinks("/src/test/test.html"); //$NON-NLS-1$
	}
	
	public void testHyperlinksHTM() throws Exception{
		checkHyperlinks("/src/test/test.htm"); //$NON-NLS-1$
	}
	
	private void checkHyperlinks(String pageName) throws Exception{
		ArrayList<TestRegion> regionList = new ArrayList<TestRegion>();
		
		regionList.add(new TestRegion("anyfolder/anyfile.jsp", new TestHyperlink[]{ //$NON-NLS-1$
			new TestHyperlink(CreateNewFileHyperlink.class, "File 'anyfolder/anyfile.jsp' does not exist. Click here to create.") //$NON-NLS-1$
		}));
		regionList.add(new TestRegion("./anyfile.htm", new TestHyperlink[]{ //$NON-NLS-1$
			new TestHyperlink(CreateNewFileHyperlink.class, "File './anyfile.htm' does not exist. Click here to create.") //$NON-NLS-1$
		}));

		regionList.add(new TestRegion("/anyfolder/anyfile.xhtml", new TestHyperlink[]{ //$NON-NLS-1$
			new TestHyperlink(CreateNewFileHyperlink.class, "File '/anyfolder/anyfile.xhtml' does not exist. Click here to create.") //$NON-NLS-1$
		}));
		regionList.add(new TestRegion("/anyfolder/anyfile.html", new TestHyperlink[]{ //$NON-NLS-1$
			new TestHyperlink(CreateNewFileHyperlink.class, "File '/anyfolder/anyfile.html' does not exist. Click here to create.") //$NON-NLS-1$
		}));

		
		HyperlinkTestUtil.checkHyperlinksInTextEditor(project, pageName, regionList, new CreateNewFileHyperlinkDetector());
	}
	
	public void testCSSWizard(){
		checkWizard("css"); //$NON-NLS-1$
	}

	public void testJSWizard(){
		checkWizard("js"); //$NON-NLS-1$
	}

	public void testJSPWizard(){
		checkWizard("jsp"); //$NON-NLS-1$
	}
	
	public void testHTMWizard(){
		checkWizard("htm"); //$NON-NLS-1$
	}

	public void testHTMLWizard(){
		checkWizard("html"); //$NON-NLS-1$
	}

	public void testXHTMLWizard(){
		checkWizard("xhtml"); //$NON-NLS-1$
	}
	
	private void checkWizard(String extension){
		String wizardId = CreateNewFileHyperlink.getWizardID(extension);
		
		assertNotNull("Wizard id for extension - '"+extension+"' not found",wizardId); //$NON-NLS-1$ //$NON-NLS-2$
		
		NewWizardRegistry registry = NewWizardRegistry.getInstance();
		IWizardDescriptor descriptor = registry.findWizard(wizardId);
		
		assertNotNull("Wizard with id - '"+wizardId+"; not found",descriptor); //$NON-NLS-1$ //$NON-NLS-2$
			
	}
	
}
