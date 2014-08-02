/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.jsdt.configuration;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper.ICommand;
import org.jboss.tools.jst.jsdt.JstJsdtPlugin;
import org.jboss.tools.jst.web.kb.IFacilityChecker;
import org.jboss.tools.jst.web.kb.internal.AngularJSRecognizer;
import org.jboss.tools.jst.web.kb.internal.JSRecognizer;
import org.w3c.dom.NodeList;

import tern.eclipse.ide.core.ITernNatureCapability;

public class AngularJSProjectCapability implements ITernNatureCapability, IFacilityChecker {
	public static final QualifiedName USE_ANGULAR_MODULE = new QualifiedName("org.jboss.tools.jst.jsdt", "UseAngularModule");
	public static final QualifiedName DO_NOT_SHOW_ANGULAR_MODULE_WARNING = new QualifiedName("org.jboss.tools.jst.jsdt", "DoNotShowAngularModuleWarning");
	
	@Override
	public boolean hasTernNature(IProject project) throws CoreException {
		String value = project == null ? null : project.getPersistentProperty(USE_ANGULAR_MODULE);
		return value == null ? false : value.equals("yes");
	}

	
	public static void checkAngularCapability(IFile file) {
		IProject project = file == null ? null : file.getProject();
		if (project == null || !project.isAccessible()) return;
		
		try {
			String useAngularModule = project.getPersistentProperty(USE_ANGULAR_MODULE);
			if (useAngularModule == null) 
				useAngularModule = "no";
			if ("yes".equals(useAngularModule))
				return; // Flag is already set on the given project
			
			if(!detectAngularUsage(file))
				return;

			String doNotShowAngularModuleWarning = project.getPersistentProperty(DO_NOT_SHOW_ANGULAR_MODULE_WARNING);
			if (doNotShowAngularModuleWarning == null)
				doNotShowAngularModuleWarning = "yes";
			
			if (!"yes".equals(doNotShowAngularModuleWarning)) {
				// Show Angular Module Warning
				AngularModuleInfoDialog dialog = new AngularModuleInfoDialog(project);
				dialog.open();
			}
		} catch (CoreException e) {
			JstJsdtPlugin.getDefault().logError(e);
		} 
	}
	
	public static boolean detectAngularUsage(IFile file) {
		final Boolean[] result = new Boolean[] {JSRecognizer.getJSReferenceVersion(file, AngularJSRecognizer.ANGULAR_JS_LIB_NAME)!=null};
		if(!result[0]) {
			StructuredModelWrapper.execute(file, new ICommand() {
				public void execute(IDOMDocument xmlDocument) {
					try {
						NodeList list = (NodeList) XPathFactory.newInstance().newXPath().compile(AngularJSRecognizer.ANGULAR_NG_ATTRIBUTE_PATTERN).evaluate(xmlDocument,XPathConstants.NODESET);
						for (int i = 0; i < list.getLength(); i++) {
							IDOMAttr attr = ((IDOMAttr)  list.item(i));
						}
						result[0] = list.getLength()>0;
					} catch (XPathExpressionException e) {
						JstJsdtPlugin.getDefault().logError(e);
					}
				}
			});
		}
		return result[0];
	}


	@Override
	public void checkFacilities(IFile file) {
		checkAngularCapability(file);
	}
}
