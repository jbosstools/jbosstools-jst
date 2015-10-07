/******************************************************************************* 
 * Copyright (c) 2015 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.js.bower.test;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.jboss.tools.jst.js.bower.BowerJson;
import org.jboss.tools.jst.js.bower.Bowerrc;
import org.jboss.tools.jst.js.bower.util.BowerUtil;

import com.google.gson.Gson;

import junit.framework.TestCase;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class BowerCoreTestCase extends TestCase {

	private IProject testProject;
	private IFile bowerJson;
	private IFile bowerrc;
	
	@Override
	protected void setUp() {
		this.testProject = ResourcesPlugin.getWorkspace().getRoot().getProject("TestBowerProject"); //$NON-NLS-1$
		this.bowerJson = this.testProject.getFile(new Path("bower.json"));  //$NON-NLS-1$
		this.bowerrc = this.testProject.getFile(new Path(".bowerrc"));  //$NON-NLS-1$
	}

	public void testProject() {
		assertNotNull("Can't load TestBowerProject", this.testProject); //$NON-NLS-1$
		assertTrue(this.testProject.exists());
	}
	
	public void testBowerJson() {
		assertNotNull("Can't load bower.json", this.bowerJson); //$NON-NLS-1$
		assertTrue(this.bowerJson.exists());
	}
	
	public void testBowerrc() {
		assertNotNull("Can't load .bowerrc", this.bowerrc); //$NON-NLS-1$
		assertTrue(this.bowerrc.exists());
	}
	
	public void testIndexHtml() {
		IFile file = testProject.getFile(new Path("WebContent/pages/index.html"));  //$NON-NLS-1$
		assertTrue(file.exists());
	}
	
	public void testBowerJsonModel() throws UnsupportedEncodingException, CoreException {
		Reader reader = new InputStreamReader(this.bowerJson.getContents(), "UTF-8"); //$NON-NLS-1$
		BowerJson model = new Gson().fromJson(reader, BowerJson.class);
		assertNotNull(model);
		assertEquals("TestBowerProject", model.getName()); //$NON-NLS-1$
		assertEquals("0.0.0", model.getVersion()); //$NON-NLS-1$
		assertEquals("MIT", model.getLicense()); //$NON-NLS-1$
	}
	
	public void testBowerrcModel() throws UnsupportedEncodingException, CoreException {
		Reader reader = new InputStreamReader(this.bowerrc.getContents(), "UTF-8"); //$NON-NLS-1$
		Bowerrc model = new Gson().fromJson(reader, Bowerrc.class);
		assertNotNull(model);
		assertEquals("app/components/", model.getDirectory()); //$NON-NLS-1$
		assertEquals(120000, model.getTimeout());
		assertFalse(model.isAnalytics());
	}
	
	public void testBowerrcDirectoryName() throws UnsupportedEncodingException, CoreException {
		String directoryName = BowerUtil.getDirectoryName(bowerrc);
		assertNotNull(directoryName);
		assertEquals(directoryName, "components"); //$NON-NLS-1$
	}
	
}