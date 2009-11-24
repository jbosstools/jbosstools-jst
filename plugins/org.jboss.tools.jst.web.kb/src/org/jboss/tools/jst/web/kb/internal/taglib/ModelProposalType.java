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
package org.jboss.tools.jst.web.kb.internal.taglib;

import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;

/**
 * @author Alexey Kazakov
 */
public abstract class ModelProposalType extends CustomProposalType {

	protected WebPromptingProvider provider;
	protected XModel xModel;

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.CustomProposalType#init(org.jboss.tools.jst.web.kb.IPageContext)
	 */
	@Override
	protected void init(ELContext context) {
		provider = WebPromptingProvider.getInstance();
		IModelNature nature = EclipseResourceUtil.getModelNature(context.getResource().getProject());
		if(nature!=null) {
			xModel = nature.getModel();
		}
	}

	protected boolean isReadyToUse() {
		return (provider != null && xModel != null);
	}
}