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
package org.jboss.tools.jst.js.npm.internal.preference.editor;

import java.io.File;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.jst.js.npm.internal.Messages;
import org.jboss.tools.jst.js.npm.util.NpmUtil;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class NpmHomeFieldEditor extends DirectoryFieldEditor {

	public NpmHomeFieldEditor(String name, String label, Composite composite) {
		super(name, label, composite);
		setEmptyStringAllowed(true);
	}

	@Override
	protected boolean doCheckState() {
		String filename = getTextControl().getText();
		filename = filename.trim();
		if (filename.isEmpty()) {
			this.getPage().setMessage(Messages.NpmPreferencePage_NotSpecifiedNpmWarning, IStatus.WARNING);
			return true;
		} else {
			// clear the warning message
			this.getPage().setMessage(null);
		}

		if (!filename.endsWith(File.separator)) {
			filename = filename + File.separator;
		}

		File selectedFile = new File(filename);
		File npmExecutable = new File(selectedFile, NpmUtil.getNpmExecutableName());
		if (npmExecutable == null || !npmExecutable.exists()) {
			setErrorMessage(Messages.NpmPreferencePage_NotValidNpmError);
			return false;
		}
		return true;
	}

	@Override
	public void setValidateStrategy(int value) {
		super.setValidateStrategy(VALIDATE_ON_KEY_STROKE);
	}

}
