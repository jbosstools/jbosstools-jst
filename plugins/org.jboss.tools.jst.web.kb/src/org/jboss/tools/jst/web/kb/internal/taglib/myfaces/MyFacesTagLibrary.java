/******************************************************************************* 
 * Copyright (c) 2010 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/

package org.jboss.tools.jst.web.kb.internal.taglib.myfaces;

import org.jboss.tools.jst.web.kb.internal.KbXMLStoreConstants;
import org.jboss.tools.jst.web.kb.internal.taglib.AbstractTagLib;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class MyFacesTagLibrary extends AbstractTagLib {

	public MyFacesTagLibrary clone() throws CloneNotSupportedException {
		return (MyFacesTagLibrary)super.clone();
	}

	public String getXMLClass() {
		return KbXMLStoreConstants.CLS_MYFACES_LIBRARY;
	}

}
