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
package org.jboss.tools.jst.web.kb.internal.taglib.html.jq;

import static org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileAttrConstants13.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper.ICommand;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.internal.taglib.CustomTagLibAttribute;
import org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttribute;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author Alexey Kazakov
 */
public class LinkAttributeProvider extends JQueryMobileAttrProvider {

	private static final ImageDescriptor IMAGE = WebKbPlugin.getImageDescriptor(WebKbPlugin.class, "EnumerationProposal.gif"); //$NON-NLS-1$

	static final HtmlAttribute[] DATA_ATTRIBUTES = new HtmlAttribute[] {DATA_AJAX_ATTRIBUTE,
		DATA_DIRECTION_ATTRIBUTE,
		DATA_DOM_CACHE_ATTRIBUTE,
		DATA_PREFETCH_ATTRIBUTE,
		DATA_REL_ATTRIBUTE,
		LINK_DATA_TRANSITION_ATTRIBUTE,
		DATA_POSITION_TO_ATTRIBUTE};

	static final CustomTagLibAttribute[] ATTRIBUTES = new CustomTagLibAttribute[] {DATA_AJAX_ATTRIBUTE,
		DATA_DIRECTION_ATTRIBUTE,
		DATA_DOM_CACHE_ATTRIBUTE,
		DATA_PREFETCH_ATTRIBUTE,
		DATA_REL_ATTRIBUTE,
		LINK_DATA_TRANSITION_ATTRIBUTE,
		DATA_POSITION_TO_ATTRIBUTE,
		new HrefAttribute()};

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.attribute.AbstractAttributeProvider#checkComponent(org.jboss.tools.jst.web.kb.KbQuery)
	 */
	@Override
	protected boolean checkComponent() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.attribute.AbstractAttributeProvider#getAttributes()
	 */
	@Override
	protected CustomTagLibAttribute[] getConditionalAttributes() {
		return EMPTY;
	}

	@Override
	protected CustomTagLibAttribute[] getRequiredAttributes() {
		return ATTRIBUTES;
	}
	/**
	 * Returns an array of all ID of pages, dialogs and popups (<tagname id="..."/>) defined in the page
	 * @param context
	 * @return
	 */
	public static List<ElementID> findAllIds(IPageContext context, String mask) {
		IFile file = context.getResource();
		if(file==null) {
			return Collections.emptyList();
		}
		return findAllIds(file, mask, true);		
	}

	public static List<ElementID> findAllIds(IFile file, final boolean escapeHTML) {
		return findAllIds(file, "", escapeHTML);
	}

	public static List<ElementID> findAllIds(IFile file, final String mask, final boolean escapeHTML) {
		final List<ElementID> ids = new ArrayList<ElementID>();

		StructuredModelWrapper.execute(file, new ICommand() {
			public void execute(IDOMDocument xmlDocument) {
				try {
					NodeList list = (NodeList) XPathFactory.newInstance().newXPath().compile("//*/@id[starts-with(.,'" + mask + "')]").evaluate(xmlDocument,XPathConstants.NODESET);
					for (int i = 0; i < list.getLength(); i++) {
						IDOMAttr attr = ((IDOMAttr)  list.item(i));
						Element element = (Element)attr.getOwnerElement();
						String dataRole = element.getAttribute(DATA_ROLE);
						if(PAGE.equalsIgnoreCase(dataRole) || DIALOG.equalsIgnoreCase(dataRole) || POPUP.equalsIgnoreCase(dataRole) || PANEL.equalsIgnoreCase(dataRole)) {
							IStructuredDocumentRegion s = ((IDOMNode)element).getStartStructuredDocumentRegion();
							String id = attr.getNodeValue();
							int offset = ((IDOMAttr)attr).getValueRegionStartOffset() + 1;
							String nodeString = s.getText();
							ids.add(new ElementID(id, offset, nodeString, escapeHTML));
						}
					}
				} catch (XPathExpressionException e) {
					WebKbPlugin.getDefault().logError(e);
				}
			}
		});

		return Collections.unmodifiableList(ids);
	}

	static class HrefAttribute extends CustomTagLibAttribute {

		public HrefAttribute() {
			setName("href");
		}

		/*
		 * (non-Javadoc)
		 * @see org.jboss.tools.jst.web.kb.internal.taglib.CustomTagLibAttribute#getProposals(org.jboss.tools.jst.web.kb.KbQuery, org.jboss.tools.jst.web.kb.IPageContext)
		 */
		@Override
		public TextProposal[] getProposals(KbQuery query, IPageContext context) {
			List<TextProposal> proposals = new ArrayList<TextProposal>();

			if(query.getType()==KbQuery.Type.ATTRIBUTE_VALUE) {
				String mask = query.getValue();
				String idMask = null;
			    if(mask.startsWith("#")) {
			    	idMask = mask.substring(1);
			    } else if(mask.length()==0) {
					idMask = mask;
				}
				if(idMask!=null) {
					List<ElementID> ids = findAllIds(context, idMask);
					for (ElementID id : ids) {
						String idText = id.getId();
						String proposaltext = "#" + idText;
						TextProposal proposal = new TextProposal();
						proposal.setLabel(proposaltext);
						proposal.setReplacementString(proposaltext);
						proposal.setImageDescriptor(IMAGE);
						proposal.setContextInfo(id.getDescription());

						proposals.add(proposal);
					}
				}
			}

			return proposals.toArray(new TextProposal[proposals.size()]);
		}
	}

	public static class ElementID {

		private String id;
		private int offset;
		private String description;

		public ElementID(String id, int offset, String description, boolean escapeHTML) {
			this.id = id;
			this.offset = offset;
			if(escapeHTML && description != null) {
				description = description.replace("<", "&lt;").replace(">", "&gt;");
			}
			this.description = description;
		}

		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @return the offset
		 */
		public int getOffset() {
			return offset;
		}
	}
}