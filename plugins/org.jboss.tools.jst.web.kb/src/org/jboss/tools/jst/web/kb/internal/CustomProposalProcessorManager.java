/******************************************************************************* 
 * Copyright (c) 2015 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.IProposalProcessor;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.WebKbPlugin;

/**
 * @author Alexey Kazakov
 */
public class CustomProposalProcessorManager {

	private static final CustomProposalProcessorManager INSTANCE = new CustomProposalProcessorManager();

	private Set<IProposalProcessor> processors;

	private CustomProposalProcessorManager() {
	}

	public static CustomProposalProcessorManager getInstance() {
		return INSTANCE;
	}

	private void init() {
		if(processors==null) {
			processors = new HashSet<IProposalProcessor>();
	        IExtensionRegistry registry = Platform.getExtensionRegistry();
			IExtensionPoint extensionPoint = registry.getExtensionPoint("org.jboss.tools.jst.web.kb.proposalProcessor"); //$NON-NLS-1$
			if (extensionPoint != null) { 
				IExtension[] extensions = extensionPoint.getExtensions();
				for(IExtension extension : extensions) {
					IConfigurationElement[] elements = extension.getConfigurationElements();
					for (IConfigurationElement element : elements) {
						String className = element.getAttribute("class"); //$NON-NLS-1$
						if(className!=null) {
							try {
								Object obj = element.createExecutableExtension("class");
								if(obj instanceof IProposalProcessor) {
									processors.add((IProposalProcessor)obj);
								} else {
									WebKbPlugin.getDefault().logError("Custom Proposal Processor (class name: " + className + ", contributer: " + element.getContributor().getName() + ") must implement " + IProposalProcessor.class.getName());
								}
							} catch (CoreException e) {
								WebKbPlugin.getDefault().logError(e);
							}
						}
					}
				}
			}
		}
	}

	public List<TextProposal> getProposals(KbQuery query, IPageContext context) {
		init();
		List<TextProposal> proposals = new ArrayList<TextProposal>();
		for (IProposalProcessor processor : processors) {
			TextProposal[] prProposals = processor.getProposals(query, context);
			for (TextProposal proposal : prProposals) {
				proposals.add(proposal);
			}
		}
		return proposals;
	}
}