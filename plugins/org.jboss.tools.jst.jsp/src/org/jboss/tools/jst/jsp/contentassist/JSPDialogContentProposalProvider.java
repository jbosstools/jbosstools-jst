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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jst.jsp.ui.internal.contentassist.JSPContentAssistProcessor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.jboss.tools.common.el.core.model.ELInstance;
import org.jboss.tools.common.el.core.model.ELInvocationExpression;
import org.jboss.tools.common.el.core.model.ELModel;
import org.jboss.tools.common.el.core.model.ELUtil;
import org.jboss.tools.common.el.core.parser.ELParser;
import org.jboss.tools.common.el.core.parser.ELParserUtil;
import org.jboss.tools.common.el.core.resolver.ELResolver;
import org.jboss.tools.common.el.core.resolver.ELResolverFactoryManager;
import org.jboss.tools.common.model.ui.ModelUIPlugin;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.jsp.contentassist.AbstractXMLContentAssistProcessor.TextRegion;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.outline.ValueHelper;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageProcessor;
import org.jboss.tools.jst.web.kb.KbQuery.Type;
import org.w3c.dom.Node;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class JSPDialogContentProposalProvider implements IContentProposalProvider {
	Properties context;
	String attributeName;
	String nodeName;
	int offset = 0;
	JspContentAssistProcessor processor;
	IPageContext pageContext = null;

	public JSPDialogContentProposalProvider() {}	
	
	public void setContext(Properties context) {
		this.context = context;
        attributeName = Constants.EMPTY + context.getProperty("attributeName"); //$NON-NLS-1$
        nodeName = Constants.EMPTY + context.getProperty("nodeName"); //$NON-NLS-1$
        Node node = (Node)context.get("node"); //$NON-NLS-1$
        if (node instanceof IDOMElement) {
        	offset = ((IDOMElement)node).getStartOffset() + ("" + nodeName).length(); //approximation, attribute may be not defined //$NON-NLS-1$
        } else if(context.get("offset") != null) { //$NON-NLS-1$
        	offset = ((Integer)context.get("offset")).intValue(); //$NON-NLS-1$
        }
        ValueHelper valueHelper = (ValueHelper)context.get("valueHelper"); //$NON-NLS-1$
        if(valueHelper == null) {
        	valueHelper = new ValueHelper();
        }
//        pageContext = (IPageContext)context.get("pageContext");
        processor = valueHelper.createContentAssistProcessor();
        pageContext = valueHelper.createPageContext(processor, offset);
        context.put("pageContext", pageContext); //$NON-NLS-1$
        context.put("kbQuery", createKbQuery(Type.ATTRIBUTE_VALUE, "", "", offset, false)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	public IContentProposal[] getProposals(String contents, int position) {
		List<IContentProposal> result = new ArrayList<IContentProposal>();
		TextRegion prefix = getELPrefix(contents, position);
		if (prefix == null || !prefix.isELStarted()) {
			IContentProposal proposal = new ContentProposal("#{}", 0, "#{}", JstUIMessages.JSPDialogContentProposalProvider_NewELExpression); //$NON-NLS-1$ //$NON-NLS-2$
			result.add(proposal);
			return result.toArray(new IContentProposal[0]);
		}
		String matchString = "#{" + prefix.getText(); //$NON-NLS-1$
		String query = matchString;
		if (query == null)
			query = ""; //$NON-NLS-1$
		String stringQuery = matchString;

		int beginChangeOffset = prefix.getStartOffset() + prefix.getOffset();

		KbQuery kbQuery = createKbQuery(Type.ATTRIBUTE_VALUE, query, stringQuery, position, true);
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(kbQuery, pageContext);

		if(proposals != null) for (TextProposal textProposal: proposals) {
			int replacementOffset = beginChangeOffset;
			int replacementLength = prefix.getLength();
			String displayString = prefix.getText().substring(0, replacementLength) + textProposal.getReplacementString();
			int cursorPosition = /*replacementOffset + */ textProposal.getReplacementString().length();

			if(!prefix.isELClosed()) {
				textProposal.setReplacementString(textProposal.getReplacementString() + "}"); //$NON-NLS-1$
			}

			Image image = textProposal.getImage();

//			IContextInformation contextInformation = null;
//			String additionalProposalInfo = textProposal.getContextInfo();
//			int relevance = textProposal.getRelevance() + 10000;

			IContentProposal proposal = //new ContentProposal(replacementString, replacementOffset, replacementLength, cursorPosition, image, displayString, contextInformation, additionalProposalInfo, relevance);
				new ContentProposal(textProposal.getReplacementString(), cursorPosition, displayString, displayString);
			result.add(proposal);
		}

		if (prefix.isELStarted() && !prefix.isELClosed()) {
			IContentProposal proposal = new ContentProposal("}", 0, "}", JstUIMessages.JSPDialogContentProposalProvider_CloseELExpression); //$NON-NLS-1$ //$NON-NLS-2$
			result.add(proposal);
		}
		
		return result.toArray(new IContentProposal[0]);
	}

	class ContentProposal implements IContentProposal {
		String content;
		int pos;
		String description = ""; //$NON-NLS-1$
		String label;
	
		public ContentProposal(String content, int pos, String label, String description) {
			this.content = content;
			this.pos = pos;
			this.label = label;
			this.description = description;
		}

		public String getContent() {
			return content;
		}

		public int getCursorPosition() {
			return pos;
		}

		public String getDescription() {
			return description;
		}

		public String getLabel() {
			return label;
		}
		
	}

	protected TextRegion getELPrefix(String text, int pos) {
			int inValueOffset = pos;
			if (text.length() < inValueOffset) { // probably, the attribute value ends before the document position
				return null;
			}
			if (inValueOffset<0) {
				return null;
			}
			
			String matchString = text.substring(0, inValueOffset);
			
			ELParser p = ELParserUtil.getJbossFactory().createParser();
			ELModel model = p.parse(text);
			
			ELInstance is = ELUtil.findInstance(model, inValueOffset);// ELInstance
			ELInvocationExpression ie = ELUtil.findExpression(model, inValueOffset);// ELExpression
			
			boolean isELStarted = (model != null && is != null && (model.toString().startsWith("#{") ||  //$NON-NLS-1$
					model.toString().startsWith("${"))); //$NON-NLS-1$
			boolean isELClosed = (model != null && is != null && model.toString().endsWith("}")); //$NON-NLS-1$
			
//			boolean insideEL = startOffset + model.toString().length() 
			TextRegion tr = new TextRegion(0,  ie == null ? inValueOffset : ie.getStartPosition(), ie == null ? 0 : inValueOffset - ie.getStartPosition(), ie == null ? "" : ie.getText(), isELStarted, isELClosed); //$NON-NLS-1$
			
			return tr;
	}

	protected ELResolver[] getELResolvers(IResource resource) {
		ELResolverFactoryManager elrfm = ELResolverFactoryManager.getInstance();
		return elrfm.getResolvers(resource);
	}

	protected KbQuery createKbQuery(Type type, String query, String stringQuery, int pos, boolean addAttr) {
		KbQuery kbQuery = new KbQuery();

		String[] parentTags = processor.getParentTags(false);
		parentTags = add(parentTags, nodeName);
		if(addAttr) {
			parentTags = add(parentTags, attributeName);
		}
		kbQuery.setPrefix(getPrefix());
		kbQuery.setUri(processor.getUri(getPrefix()));
		kbQuery.setParentTags(parentTags);
		kbQuery.setParent(attributeName);
		kbQuery.setMask(true); 
		kbQuery.setType(type);
		kbQuery.setOffset(pos);
		kbQuery.setValue(query); 
		kbQuery.setStringQuery(stringQuery);
		
		return kbQuery;
	}

	private String getPrefix() {
		if(nodeName == null) return null;
		int i = nodeName.indexOf(':');
		return i < 0 ? null : nodeName.substring(0, i);
	}

	protected String[] getParentTags(JspContentAssistProcessor processor) {
		String[] result = processor.getParentTags(true);
		String[] result1 = add(result, attributeName);
		return result1;
	}

	private String[] add(String[] result, String v) {
		String[] result1 = new String[result.length + 1];
		System.arraycopy(result, 0, result1, 0, result.length);
		result1[result.length] = v;
		return result1;
	}

}
