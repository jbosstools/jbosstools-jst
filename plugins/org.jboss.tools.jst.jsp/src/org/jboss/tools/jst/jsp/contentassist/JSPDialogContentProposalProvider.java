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
	JSPDialogContentAssistProcessor processor;
	IPageContext pageContext = null;

	public JSPDialogContentProposalProvider() {}	
	
	public void setContext(Properties context) {
		this.context = context;
        attributeName = Constants.EMPTY + context.getProperty("attributeName");
        nodeName = Constants.EMPTY + context.getProperty("nodeName");
        Node node = (Node)context.get("node");
        if (node instanceof IDOMElement) {
        	offset = ((IDOMElement)node).getStartEndOffset(); //approximation, attribute may be not defined
        }
        processor = new JSPDialogContentAssistProcessor();
        processor.computeCompletionProposals(getTextViewer(), offset);
        pageContext = processor.getContext();
	}

	public IContentProposal[] getProposals(String contents, int position) {
		List<IContentProposal> result = new ArrayList<IContentProposal>();
		TextRegion prefix = getELPrefix(contents, position);
		if (prefix == null || !prefix.isELStarted()) {
			IContentProposal proposal = new ContentProposal("#{}", 0, "#{}", "New EL Expression");
			result.add(proposal);
			return result.toArray(new IContentProposal[0]);
		}
		String matchString = "#{" + prefix.getText();
		String query = matchString;
		if (query == null)
			query = "";
		String stringQuery = matchString;

		int beginChangeOffset = prefix.getStartOffset() + prefix.getOffset();

		KbQuery kbQuery = createKbQuery(Type.ATTRIBUTE_VALUE, query, stringQuery, position);
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(kbQuery, pageContext);

		if(proposals != null) for (TextProposal textProposal: proposals) {
			int replacementOffset = beginChangeOffset;
			int replacementLength = prefix.getLength();
			String displayString = prefix.getText().substring(0, replacementLength) + textProposal.getReplacementString();
			int cursorPosition = /*replacementOffset + */ textProposal.getReplacementString().length();

			if(!prefix.isELClosed()) {
				textProposal.setReplacementString(textProposal.getReplacementString() + "}");
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
			IContentProposal proposal = new ContentProposal("}", 0, "}", "Close EL Expression");
			result.add(proposal);
		}
		
		return result.toArray(new IContentProposal[0]);
	}

	class ContentProposal implements IContentProposal {
		String content;
		int pos;
		String description = "";
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
			
			boolean isELStarted = (model != null && is != null && (model.toString().startsWith("#{") || 
					model.toString().startsWith("${")));
			boolean isELClosed = (model != null && is != null && model.toString().endsWith("}"));
			
//			boolean insideEL = startOffset + model.toString().length() 
			TextRegion tr = new TextRegion(0,  ie == null ? inValueOffset : ie.getStartPosition(), ie == null ? 0 : inValueOffset - ie.getStartPosition(), ie == null ? "" : ie.getText(), isELStarted, isELClosed);
			
			return tr;
	}

	protected ELResolver[] getELResolvers(IResource resource) {
		ELResolverFactoryManager elrfm = ELResolverFactoryManager.getInstance();
		return elrfm.getResolvers(resource);
	}

	protected ITextViewer getTextViewer() {
		IEditorPart editor = ModelUIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(editor == null) return null;
		if (editor instanceof JSPMultiPageEditor) {
			JSPMultiPageEditor jsp = (JSPMultiPageEditor)editor;
			return jsp.getSourceEditor().getTextViewer();
		}
		return null;
	}

	protected KbQuery createKbQuery(Type type, String query, String stringQuery, int pos) {
		KbQuery kbQuery = new KbQuery();

		String prefix = processor.getTagPrefix();
		String  uri = processor.getTagUri();
		String[] parentTags = processor.getParentTags(attributeName);
		String	parent = attributeName;
		String queryValue = query;
		String queryStringValue = stringQuery;
		
		kbQuery.setPrefix(prefix);
		kbQuery.setUri(uri);
		kbQuery.setParentTags(parentTags);
		kbQuery.setParent(parent); 
		kbQuery.setMask(true); 
		kbQuery.setType(type);
		kbQuery.setOffset(pos);
		kbQuery.setValue(queryValue); 
		kbQuery.setStringQuery(queryStringValue);
		
		return kbQuery;
	}

	protected int getOffset() {
		return offset;
	}

	static class JSPDialogContentAssistProcessor extends JspContentAssistProcessor {
		public String getTagPrefix() {
			return super.getTagPrefix();
		}
		public String getTagUri() {
			return super.getTagUri();
		}
		protected String[] getParentTags(String attr) {
			String[] result = super.getParentTags(true);
			String[] result1 = new String[result.length + 1];
			System.arraycopy(result, 0, result1, 0, result.length);
			result1[result.length] = attr;
			return result1;
		}

	}

}
