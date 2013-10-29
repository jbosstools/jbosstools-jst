/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.internal.properties.advanced;

import org.jboss.tools.common.ui.widget.editor.IFieldEditor;


/**
 * Lightweight object created with viewer for creating field editor as needed.
 * 
 * @author Viacheslav Kabanovich
 *
 */
public interface IFieldEditorProvider {
	public IFieldEditor createEditor();
}
