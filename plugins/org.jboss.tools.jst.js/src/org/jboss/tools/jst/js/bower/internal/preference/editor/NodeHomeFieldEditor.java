/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *       Red Hat, Inc. - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.jst.js.bower.internal.preference.editor;

import java.io.File;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.jst.js.bower.internal.Messages;
import org.jboss.tools.jst.js.bower.internal.util.ExternalToolUtil;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class NodeHomeFieldEditor extends DirectoryFieldEditor {

	public NodeHomeFieldEditor(String name, String label, Composite composite) {
		super(name, label, composite);
		setEmptyStringAllowed(true);
	}

	@Override
	protected boolean doCheckState() {
		String filename = getTextControl().getText();
		filename = filename.trim();
		if (filename.isEmpty()) {
			this.getPage().setMessage(Messages.BowerPreferencePage_NotSpecifiedNodeWarning, IStatus.WARNING);
			return true;
		} else {
			// clear the warning message
			this.getPage().setMessage(null);
		}

		if (!filename.endsWith(File.separator)) {
			filename = filename + File.separator;
		}

		File selectedFile = new File(filename);
		String nodeExecutableName = ExternalToolUtil.getNodeExecutableName();
		File nodeExecutable = new File(selectedFile, nodeExecutableName);
		if (nodeExecutable == null || !nodeExecutable.exists()) {
			setErrorMessage(Messages.BowerPreferencePage_NotValidNodeError);
			return false;
		}

		return true;
	}

	@Override
	public void setValidateStrategy(int value) {
		super.setValidateStrategy(VALIDATE_ON_KEY_STROKE);
	}

}
