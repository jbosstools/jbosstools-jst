/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal;

import org.jboss.tools.common.model.project.ext.store.XMLStoreConstants;

/**
 * @author Viacheslav Kabanovich
 */
public interface KbXMLStoreConstants extends XMLStoreConstants {

	public String TAG_LIBRARY = "library"; //$NON-NLS-1$
	public String TAG_COMPONENT = "component"; //$NON-NLS-1$
	public String TAG_ATTRIBUTE = "attribute"; //$NON-NLS-1$
	public String TAG_FACET = "facet"; //$NON-NLS-1$
	public String TAG_FUNCTION = "function"; //$NON-NLS-1$

	public String CLS_TLD_LIBRARY = "tld"; //$NON-NLS-1$
	public String CLS_FACELET_LIBRARY = "facelet"; //$NON-NLS-1$
	public String CLS_FACESCONFIG_LIBRARY = "faces-config"; //$NON-NLS-1$
	public String CLS_COMPOSITE_LIBRARY = "composite"; //$NON-NLS-1$

	public String ATTR_URI = "uri"; //$NON-NLS-1$
	public String ATTR_SHORT_NAME = "short-name"; //$NON-NLS-1$
}
