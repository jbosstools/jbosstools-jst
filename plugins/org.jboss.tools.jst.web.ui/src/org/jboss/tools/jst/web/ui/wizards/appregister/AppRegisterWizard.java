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
package org.jboss.tools.jst.web.ui.wizards.appregister;

import java.util.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.wst.common.componentcore.internal.util.ComponentUtilities;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.*;
import org.jboss.tools.common.meta.action.SpecialWizard;
import org.jboss.tools.common.meta.key.WizardKeys;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.impl.FileSystemImpl;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.reporting.ProblemReportingHelper;
import org.jboss.tools.common.model.ui.*;
import org.jboss.tools.jst.web.context.RegisterTomcatContext;
import org.jboss.tools.jst.web.server.RegistrationHelper;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

public class AppRegisterWizard extends Wizard implements SpecialWizard {
	Properties p;
	XModelObject object;
	RegisterTomcatContext registry;	
	AppRegisterWizardPage page;
	
	public AppRegisterWizard() {
		setHelpAvailable(true);
	}
	
	public boolean performFinish() {
		try {
			IProject p = EclipseResourceUtil.getProject(object);
			String contextRoot = registry.getApplicationName();
			if(!contextRoot.equals(ComponentUtilities.getServerContextRoot(p))) {
				ComponentUtilities.setServerContextRoot(p, contextRoot);
			}
			IServer[] is = registry.getTargetServers();				
			for (int i = 0; i < is.length; i++) {
				RegistrationHelper.register(p, is[i]);
			}
			object.getModel().changeObjectAttribute(object, "application name", registry.getApplicationName());
		} catch (Exception ex) {
			ProblemReportingHelper.reportProblem(WebUiPlugin.PLUGIN_ID, "Exception caught in AppRegisterWizard.performFinish(): " + ex.getMessage(), ex);
			return false;
		}		
		return true;
	}

	public String getWebRootLocation() {
		XModelObject fs = object.getModel().getByPath("FileSystems/WEB-ROOT");
		///if(fs == null) fs = getRootFileSystemForModule(model, "");
		if(!(fs instanceof FileSystemImpl)) return null;
		return ((FileSystemImpl)fs).getAbsoluteLocation();
	}
    
	public void setObject(Object object) {
		p = (Properties)object;
		registry = new RegisterTomcatContext(RegisterTomcatContext.PROJECT_MODE_EXISTING);
		this.object = (XModelObject)p.get("object");
		registry.setProjectHandle(EclipseResourceUtil.getProject(this.object));
		registry.init();
		registry.setNatureIndex(p.getProperty("natureIndex"));
		XModelObject web = this.object.getModel().getByPath("Web");
		String servletVersion = web == null ? null : web.getAttributeValue("servlet version");
		registry.setServletVersion(servletVersion);
		registry.setApplicationName(this.object.getAttributeValue("application name"));
		page = new AppRegisterWizardPage(registry);
		String n = p.getProperty("natureIndex");
		if(n == null) n = "project"; else n += " Project";
		page.setTitle(WizardKeys.toDisplayName(n));
		addPage(page);
	}

	public int execute() {
		Shell shell = ModelUIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
		WizardDialog dialog = new WizardDialog(shell, this);
		dialog.create();
		dialog.getShell().setText("" + p.getProperty("title"));
		dialog.setTitleImage(ModelUIImages.getImageDescriptor(ModelUIImages.WIZARD_DEFAULT).createImage(null));
		return dialog.open();		
	}

}
