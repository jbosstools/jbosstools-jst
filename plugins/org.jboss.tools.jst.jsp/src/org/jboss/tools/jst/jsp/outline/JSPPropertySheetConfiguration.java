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
package org.jboss.tools.jst.jsp.outline;

import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.xml.ui.views.properties.XMLPropertySheetConfiguration;

/**
 * @author Kabanovich
 * PropertySheetConfiguration implementation that overrides
 * creation of property source provider.
 */

public class JSPPropertySheetConfiguration extends XMLPropertySheetConfiguration {
	private AttributeSorter sorter = new AttributeSorter();
	private IPropertySheetPage fPropertySheetPage = null;
	private JSPPropertySourceProvider0 fPropertySourceProvider = null;

	public IPropertySourceProvider getPropertySourceProvider(IPropertySheetPage page) {
		if (fPropertySourceProvider == null) {
			super.getPropertySourceProvider(page);
			fPropertySheetPage = page;
			fPropertySourceProvider = new JSPPropertySourceProvider0();
			fPropertySourceProvider.setSorter(sorter);
		}
		return fPropertySourceProvider;
	}
	
	public AttributeSorter getSorter() {
		return sorter;
	}

	private class JSPPropertySourceProvider0 implements IPropertySourceProvider {
		AttributeSorter sorter = null;
		private IPropertySource fPropertySource = null;
		private INodeNotifier fSource = null;

		public IPropertySource getPropertySource(Object object) {
			if (fSource != null && object.equals(fSource)) {
				return fPropertySource;
			}

			if (object instanceof INodeNotifier) {
				fSource = (INodeNotifier) object;
				fPropertySource = new JSPPropertySourceAdapter((INodeNotifier) object);
			}
			else {
				fSource = null;
				fPropertySource = null;
			}
			return fPropertySource;
		}
		public void setSorter(AttributeSorter sorter) {
			this.sorter = sorter;
		}
	}

	public void unconfigure() {
		super.unconfigure();
		fPropertySheetPage = null;
		fPropertySourceProvider = null;
		
	}
}
