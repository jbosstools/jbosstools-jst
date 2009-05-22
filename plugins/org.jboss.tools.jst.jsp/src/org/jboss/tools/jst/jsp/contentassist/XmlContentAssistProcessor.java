/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.jsp.contentassist;

import org.eclipse.core.resources.IFile;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.el.core.resolver.ELContextImpl;
import org.jboss.tools.common.el.core.resolver.ELResolver;

public class XmlContentAssistProcessor extends AbstractXMLContentAssistProcessor {

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.jsp.contentassist.AbstractXMLContentAssistProcessor#createContext()
	 */
	@Override
	protected ELContext createContext() {
		IFile file = getResource();
		ELResolver[] elResolvers = getELResolvers(file);

		ELContextImpl context = new ELContextImpl();
		context.setResource(getResource());
		context.setElResolvers(elResolvers);
		setVars(context);

		return context;
	}

	protected void setVars(ELContext context) {
		// TODO
	}
}