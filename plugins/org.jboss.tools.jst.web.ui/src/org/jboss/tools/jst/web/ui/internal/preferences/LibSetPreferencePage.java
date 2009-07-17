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
package org.jboss.tools.jst.web.ui.internal.preferences;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jboss.tools.common.model.ui.attribute.adapter.IModelPropertyEditorAdapter;
import org.jboss.tools.common.model.ui.attribute.adapter.StructuredListAdapter;
import org.jboss.tools.common.model.ui.preferences.TabbedPreferencesPage;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

public class LibSetPreferencePage extends TabbedPreferencesPage {
	private static final String ATTR_LIBRARIES = "Libraries"; //$NON-NLS-1$

	public LibSetPreferencePage(String[] paths)	{ 
		super(paths);		
		for (int i = 0; i < paths.length; i++) {
			IModelPropertyEditorAdapter adapter = getXMOTabPage(paths[i]).getAttributeSupport().getPropertyEditorAdapterByName(getStructuredListAttrName(paths[i]));
			if (adapter instanceof StructuredListAdapter) 
				((StructuredListAdapter)adapter).setNewValueProvider(getNewValueProvider(paths[i]));
		}
	}

	protected String getStructuredListAttrName(String path) {
		return ATTR_LIBRARIES;
	}
	
	protected StructuredListAdapter.INewValueProvider getNewValueProvider(String path) {
		StructuredListAdapter.INewValueProvider result = new AddJarProvider();		
		return result;
	}
	
	protected String[] getValueList() {
		return new String[0];
	}
	
	boolean isNewValueValid(String value) {
		boolean result = (value != null);
		
		if (result) {
			IPath valuePath = new Path(value); 
			String valueList[] = getValueList();
			for (int i = 0; i < valueList.length && result; i++) {
				IPath path = new Path(valueList[i]);
				result = !(valuePath.equals(path) || valuePath.equals(JavaCore.getResolvedVariablePath(new Path(valueList[i]))));
			}
		}		
		return result;
	}
	
//		return path with java variable if exists 
	private String getAdaptedPath(String absolutePath) {
		String result = null;				
		if (absolutePath != null) {
			IPath path = new Path(absolutePath);
			
			Map prefixes = new HashMap();
			String varNames[] = JavaCore.getClasspathVariableNames();
			IPath varPath;
			for (int i = 0; i < varNames.length; i++)
			{
				varPath = JavaCore.getClasspathVariable(varNames[i]);
				if (varPath != null && varPath.isPrefixOf(path))
					prefixes.put(varNames[i], varPath);
			}
			
			String varName = null;
			Iterator keys = prefixes.keySet().iterator();
			int pathSize = -1;
			while (keys.hasNext())
			{
				String tmpVarName = (String)keys.next();
				varPath = (IPath)prefixes.get(tmpVarName);
				if (varPath.segmentCount() > pathSize)
				{
					pathSize = varPath.segmentCount();
					varName = tmpVarName;
				}
			}
			
			if (varName != null)
			{
				IPath newPath = (new Path(varName)).append(path.removeFirstSegments(pathSize));
				result = newPath.toString();
			}
			else
				result = absolutePath;
		}				
		return result;
	}

	class AddJarProvider implements StructuredListAdapter.INewValueProvider	{
		public Object getValue() {
			String result = null;			
			FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
			dialog.setFilterExtensions(new String[]{"*.jar;*.zip"}); //$NON-NLS-1$
			result = dialog.open();			
			return isNewValueValid(result) ? getAdaptedPath(result) : null;
		}
	}
}
