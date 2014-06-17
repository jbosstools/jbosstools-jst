/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.test;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.jboss.tools.common.base.test.validation.TestUtil;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.WebUtils;
import org.jboss.tools.jst.web.model.helpers.WebAppHelper;
import org.jboss.tools.test.util.ProjectImportTestSetup;
import org.jboss.tools.test.util.ResourcesUtils;

/**
 * @author Alexey Kazakov
 */
public class WebAppHelperTest extends TestCase {

	private IProject project;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		project = getTestProject();
	}

	public IProject getTestProject() {
		if(project==null) {
			try {
				project = findTestProject();
				if(project==null || !project.exists()) {
					project = ResourcesUtils.importProject(JstWebAllTests.PLUGIN_ID, JstWebAllTests.PROJECT_PATH);
					TestUtil._waitForValidation(project);
				}
			} catch (Exception e) {
				e.printStackTrace();
				fail("Can't import CDI test project: " + e.getMessage());
			}
		}
		return project;
	}

	public static IProject findTestProject() {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(JstWebAllTests.PROJECT_NAME);
	}

	/**
	 * See https://issues.jboss.org/browse/JBIDE-10579
	 * @throws Exception
	 */
	public void testFilters() throws Exception {
		XModelObject o = getWebXML30();

		assertEquals("3.0", WebAppHelper.getServletVersion(o));

		XModelObject[] fs = WebAppHelper.getFilters(o);
		assertEquals(1, fs.length);

		XModelObject[] ms = WebAppHelper.getFilterMappings(o);
		assertEquals(1, ms.length);		
	}

	public void testServlets() throws Exception {
		XModelObject o = getWebXML30();
		
		XModelObject s = WebAppHelper.findOrCreateServlet(o, "a.b.C", "C", 0);
		
		XModelObject[] ss = WebAppHelper.getServlets(o);
		assertEquals(1, ss.length);
		assertTrue(s == ss[0]);
		
	}

	private XModelObject getWebXML30() {
		IFile f = project.getFile("WebContent/WEB-INF/web30.xml");
		assertTrue(f.exists());
		XModelObject o = EclipseResourceUtil.createObjectForResource(f);
		assertNotNull(o);
		return o;
	}

	public void testAdministeredObjects() throws Exception {
		XModelObject webxml = getWebXML31();
		assertNotNull(webxml);
		
		XModelObject folder = webxml.getChildByPath("Administered Objects");
		assertNotNull(folder);
		
		XModelObject o = folder.getChildByPath("n1");
		assertNotNull(o);
		assertEquals("i1", o.getAttributeValue("interface-name"));
	}

	public void testConnectionFactories() throws Exception {
		XModelObject webxml = getWebXML31();
		assertNotNull(webxml);
		
		XModelObject folder = webxml.getChildByPath("Connection Factories");
		assertNotNull(folder);
		
		XModelObject o = folder.getChildByPath("n2");
		assertNotNull(o);
		assertEquals("i2", o.getAttributeValue("interface-name"));

		o = folder.getChildByPath("j1");
		assertNotNull(o);
	}

	public void testJMSDestinations() throws Exception {
		XModelObject webxml = getWebXML31();
		assertNotNull(webxml);
		
		XModelObject folder = webxml.getChildByPath("JMS Destinations");
		assertNotNull(folder);
		
		XModelObject o = folder.getChildByPath("n4");
		assertNotNull(o);
		assertEquals("i3", o.getAttributeValue("interface-name"));
	}

	public void testMailSessions() throws Exception {
		XModelObject webxml = getWebXML31();
		assertNotNull(webxml);
		
		XModelObject folder = webxml.getChildByPath("Mail Sessions");
		assertNotNull(folder);
		
		XModelObject o = folder.getChildByPath("n5");
		assertNotNull(o);
		assertEquals("u", o.getAttributeValue("user"));
	}

	private XModelObject getWebXML31() {
		IFile f = project.getFile("WebContent/WEB-INF/web31.xml");
		assertTrue(f.exists());
		XModelObject o = EclipseResourceUtil.createObjectForResource(f);
		assertNotNull(o);
		return o;
	}
}