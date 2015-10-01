/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.js.node.preference;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.tools.jst.js.node.Messages;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class JavaScriptToolsPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	public static final String PAGE_ID = "org.jboss.tools.jst.js.node.preference.JavaScriptTools"; //$NON-NLS-1$
	
	public JavaScriptToolsPreferencePage() {
		super(GRID);
	}
	
	@Override
	public void init(IWorkbench wb) {
		setDescription(Messages.PreferenceDescription_JavaScriptTools);
	}
	
	@Override
	protected void createFieldEditors() {
	}

}
