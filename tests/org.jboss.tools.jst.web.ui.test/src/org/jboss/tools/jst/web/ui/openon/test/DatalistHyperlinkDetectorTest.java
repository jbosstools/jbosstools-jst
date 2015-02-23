/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
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
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.common.text.ext.hyperlink.xml.XMLJumpToHyperlink;
import org.jboss.tools.jst.jsp.test.openon.HyperlinkTestUtil;
import org.jboss.tools.jst.jsp.test.openon.HyperlinkTestUtil.TestHyperlink;
import org.jboss.tools.jst.jsp.test.openon.HyperlinkTestUtil.TestRegion;
import org.jboss.tools.jst.web.ui.internal.text.ext.hyperlink.internal.DatalistHyperlinkDetector;

public class DatalistHyperlinkDetectorTest extends TestCase {
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
	
	public DatalistHyperlinkDetectorTest() {
		super("DatalistHyperlinkDetector Test"); //$NON-NLS-1$
	}
	
	public void testDetectorHTML() throws Exception{
		checkHyperlinkDetector("/src/test/datalist.html"); //$NON-NLS-1$
	}
	
	private void checkHyperlinkDetector(String pageName) throws Exception{
		List<TestRegion> regionList = getTestRegionList();
		
		HyperlinkTestUtil.checkRegionsInTextEditor(project, pageName, regionList, new DatalistHyperlinkDetector());
	}
	
	public void testHyperlinksHTML() throws Exception{
		checkHyperlinks("/src/test/datalist.html"); //$NON-NLS-1$
	}
	
	
	private void checkHyperlinks(String pageName) throws Exception{
		List<TestRegion> regionList = getTestRegionList();
		
		HyperlinkTestUtil.checkHyperlinksInTextEditor(project, pageName, regionList, new DatalistHyperlinkDetector());
	}
	
	private List<TestRegion> getTestRegionList(){
		ArrayList<TestRegion> regionList = new ArrayList<TestRegion>();
		
		regionList.add(new TestRegion("<input", new TestHyperlink[]{}));
		
		regionList.add(new TestRegion("browsers", new TestHyperlink[]{ //$NON-NLS-1$
			new TestHyperlink(XMLJumpToHyperlink.class, "Go to '<datalist id=\"browsers\">'") //$NON-NLS-1$
		}));

		regionList.add(new TestRegion("os", new TestHyperlink[]{ //$NON-NLS-1$
			new TestHyperlink(XMLJumpToHyperlink.class, "Go to '<datalist id=\"os\">'") //$NON-NLS-1$
		}));
		
		return regionList;
	}
}
