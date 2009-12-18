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
package org.jboss.tools.jst.jsp.jspeditor.info;

import java.util.List;
import java.util.Map;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.ui.internal.taginfo.XMLTagInfoHoverProcessor;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.jsp.contentassist.Utils;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageContextFactory;
import org.jboss.tools.jst.web.kb.PageProcessor;
import org.jboss.tools.jst.web.kb.KbQuery.Type;
import org.jboss.tools.jst.web.kb.taglib.INameSpace;

/**
 * 
 * @author Victor Rubezhny
 *
 */
@SuppressWarnings("restriction")
public class FaceletTagInfoHoverProcessor extends XMLTagInfoHoverProcessor {
	private ELContext fContext;
	private int fDocumentPosition;

	public FaceletTagInfoHoverProcessor() {
		super();
	}

	@Override
	protected String computeHoverHelp(ITextViewer textViewer,
			int documentPosition) {
		this.fDocumentPosition = documentPosition;
		this.fContext = null;
		
		IFile file = getResource(textViewer.getDocument());
		if (file == null)
			return null;
		
		fContext = PageContextFactory.createPageContext(file);
		if (fContext == null)
			return null;
		
		return super.computeHoverHelp(textViewer, documentPosition);
	}

	@Override
	protected String computeTagAttNameHelp(IDOMNode xmlnode,
			IDOMNode parentNode, IStructuredDocumentRegion flatNode,
			ITextRegion region) {
		if (fContext == null)
			return null;

		String tagName = Utils.getTagName(xmlnode, true);
		String query = flatNode.getText(region);
		String prefix = getPrefix(tagName);
		String uri = getUri(prefix);
		String[] parentTags = Utils.getParentTags(xmlnode, true, true);
		String parent = Utils.getParent(xmlnode, true, true, true);
		
		KbQuery kbQuery = Utils.createKbQuery(Type.ATTRIBUTE_NAME, fDocumentPosition, query, query,  //$NON-NLS-1$
				prefix, uri, parentTags, parent, false);

		TextProposal[] proposals = PageProcessor.getInstance().getProposals(kbQuery, fContext);
		if (proposals == null)
			return null;
		
		for(TextProposal proposal : proposals) {
			if (proposal != null && proposal.getContextInfo() != null &&
				proposal.getContextInfo().trim().length() > 0) {
				return proposal.getContextInfo();
			}
		}

		return null;
	}

	@Override
	protected String computeTagAttValueHelp(IDOMNode xmlnode,
			IDOMNode parentNode, IStructuredDocumentRegion flatNode,
			ITextRegion region) {
		return null;
	}

	@Override
	protected String computeTagNameHelp(IDOMNode xmlnode, IDOMNode parentNode,
			IStructuredDocumentRegion flatNode, ITextRegion region) {
		if (fContext == null)
			return null;

		String query = Utils.getTagName(xmlnode, true);
		String prefix = getPrefix(query);
		String uri = getUri(prefix);
		String[] parentTags = Utils.getParentTags(xmlnode, false, true);
		String parent = Utils.getParent(xmlnode, false, false, true);
		
		KbQuery kbQuery = Utils.createKbQuery(Type.TAG_NAME, fDocumentPosition, query, "<" + query,  //$NON-NLS-1$
				prefix, uri, parentTags, parent, false);

		TextProposal[] proposals = PageProcessor.getInstance().getProposals(kbQuery, fContext);
		if (proposals == null)
			return null;
		
		for(TextProposal proposal : proposals) {
			if (proposal != null && proposal.getContextInfo() != null &&
				proposal.getContextInfo().trim().length() > 0) {
				return proposal.getContextInfo();
			}
		}

		return null;
	}
	
	/**
	 * Returns IFile resource of the document
	 * 
	 * @return
	 */
	protected IFile getResource(IDocument document) {
		IStructuredModel sModel = StructuredModelManager.getModelManager().getExistingModelForRead(document);
		try {
			if (sModel != null) {
				String baseLocation = sModel.getBaseLocation();
				IPath location = new Path(baseLocation).makeAbsolute();
				return FileBuffers.getWorkspaceFileAtLocation(location);
			}
		}
		finally {
			if (sModel != null) {
				sModel.releaseFromRead();
			}
		}
		return null;
	}

	
	private String getPrefix(String tagname) {
		String prefix = null;
		
		int index = tagname == null ? -1 : tagname.indexOf(':');
		if (tagname != null && index != -1) {
			prefix = tagname.substring(0, index);
		}
		
		if (prefix != null)
			return prefix;

		String uri = getUri(""); //$NON-NLS-1$
		return uri == null ? null : "";   //$NON-NLS-1$
	}
	
	private String getUri(String prefix) {
		if (prefix == null || fContext == null)
			return null;
		if (!(fContext instanceof IPageContext))
			return null;
		
		Map<String, List<INameSpace>> nameSpaces = ((IPageContext)fContext).getNameSpaces(fDocumentPosition);
		if (nameSpaces == null || nameSpaces.isEmpty())
			return null;
		
		for (List<INameSpace> nameSpace : nameSpaces.values()) {
			for (INameSpace n : nameSpace) {
				if (prefix.equals(n.getPrefix())) {
					return n.getURI();
				}
			}
		}
		return null;
	}
}
