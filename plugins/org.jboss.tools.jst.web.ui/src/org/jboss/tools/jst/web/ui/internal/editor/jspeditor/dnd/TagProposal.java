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
package org.jboss.tools.jst.web.ui.internal.editor.jspeditor.dnd;

import org.jboss.tools.common.model.ui.editors.dnd.IAttributeValueLoader;
import org.jboss.tools.common.model.ui.editors.dnd.IDropWizardModel;
import org.jboss.tools.common.model.ui.editors.dnd.ITagProposal;

public class TagProposal implements ITagProposal {
	
	public static final String EMPTY_URI = ""; //$NON-NLS-1$
	
	String uri;
	String libraryVersion = ""; //$NON-NLS-1$
	String prefix;
	String name;
	IAttributeValueLoader attributeValueLoader =  EMPTY_ATTRIBUTE_VALUE_LOADER;
	
	/**
	 * TagProposal with empty attribute value loader  
	 *
	 */
	public TagProposal(
		String uri, 
		String prefix, 
		String name
	) {
		this(uri, "", prefix, name); //$NON-NLS-1$
	}

	public TagProposal(
			String uri,
			String libraryVersion,
			String prefix, 
			String name
		) {
			this.uri = uri;
			this.libraryVersion = (libraryVersion == null) ? "" : libraryVersion; //$NON-NLS-1$
			this.prefix = prefix;
			this.name = name;
		}

	/**
	 * 
	 *
	 */
	public TagProposal(
		String uri, 
		String prefix, 
		String name,
		IAttributeValueLoader loader
	) {
		this.uri = uri;
		this.prefix = prefix;
		this.name = name;
		this.attributeValueLoader = loader;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * 
	 * @return
	 */
	public String getUri() {
		return uri;
	}
	
	public String getLibraryVersion() {
		return libraryVersion;
	}
	
	public IAttributeValueLoader getAttributesValueLoader() {
		return attributeValueLoader;
	}

	public String getDisplayString() {
		return getPrefix() == ITagProposal.EMPTY_PREFIX ? getName() : getPrefix() + ":" + getName(); //$NON-NLS-1$
	}

	public String getDetails() {
		return getUri();
	}

}
