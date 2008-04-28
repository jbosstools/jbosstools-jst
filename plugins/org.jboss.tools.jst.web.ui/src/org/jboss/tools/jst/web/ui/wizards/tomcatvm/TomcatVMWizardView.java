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
package org.jboss.tools.jst.web.ui.wizards.tomcatvm;

import java.util.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;
import org.jboss.tools.common.model.ui.attribute.XAttributeSupport;
import org.jboss.tools.common.model.ui.wizards.query.AbstractQueryWizardView;
import org.jboss.tools.common.meta.action.*;
import org.jboss.tools.common.meta.action.impl.XEntityDataImpl;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.tomcat.TomcatVMHelper;

public class TomcatVMWizardView extends AbstractQueryWizardView {
	XAttributeSupport support = new XAttributeSupport();
	Properties context;
	
	public TomcatVMWizardView() {
		XEntityData data = XEntityDataImpl.create(new String[][]{{"TomcatVMHelper", "yes"}, {"vm", "yes"}, /*{"ignore", "no"}*/}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		data.setValue("vm", TomcatVMHelper.getJVMInstall().getInstallLocation().getAbsolutePath()); //$NON-NLS-1$
//		data.setValue("ignore", "no");
		support.init(PreferenceModelUtilities.getPreferenceModel().getRoot(), data); 
	}
	 
	public void dispose() {
		super.dispose();
		if (support!=null) support.dispose();
		support = null;
	}
	public Control createControl(Composite parent) {
		Control c = support.createControl(parent);
		support.getPropertyEditorAdapterByName("vm").addValueChangeListener(new InputChangeListener()); //$NON-NLS-1$
		validate();
		return c;
	}
	
	private void validate() {
		String location = getLocation();
		String message = null;
		String errorMessage = null;
		if(location.length() == 0) {
			errorMessage = WebUIMessages.PATH_TO_JVM_IS_EMPTY;
		} else if(!new java.io.File(location).isDirectory()) {
			errorMessage = WebUIMessages.SPECIFIED_FOLDER_DOESNOT_EXIST;
		} else if(!new java.io.File(location + "/bin/java.exe").isFile()) { //$NON-NLS-1$
			errorMessage = WebUIMessages.SPECIFIED_FOLDER_ISNOT_JVMHOME;
		} else if(TomcatVMHelper.findToolsJarInVM(location) == null) {
			message = WebUIMessages.CANNOT_FIND_TOOLSJAR;
		}
		setErrorMessage(errorMessage);
		commandBar.setEnabled(OK, errorMessage == null);
		setMessage(message);		
	}

	public void setObject(Object object) {
		context = (Properties)object;
	}
	
	class InputChangeListener implements java.beans.PropertyChangeListener {
		public void propertyChange(java.beans.PropertyChangeEvent evt) {
			validate();
		}
	}
	
	public void stopEditing() {
		support.save();
		context.setProperty("vm", getLocation()); //$NON-NLS-1$
	}
	
	private String getLocation() {
		return support.getValues().getProperty("vm"); //$NON-NLS-1$
	}
	
	public Point getPreferredSize() {
		return new Point(500, SWT.DEFAULT);
	}

}
