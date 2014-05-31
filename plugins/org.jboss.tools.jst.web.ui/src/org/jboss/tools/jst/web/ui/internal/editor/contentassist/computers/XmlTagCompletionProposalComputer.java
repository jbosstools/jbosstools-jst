/*******************************************************************************
 * Copyright (c) 2010-2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.internal.editor.contentassist.computers;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.wst.dtd.core.internal.contentmodel.DTDImpl.DTDBaseAdapter;
import org.eclipse.wst.dtd.core.internal.contentmodel.DTDImpl.DTDElementReferenceContentAdapter;
import org.eclipse.wst.html.core.internal.contentmodel.HTMLPropertyDeclaration;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.ui.contentassist.CompletionProposalInvocationContext;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.document.TextImpl;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.eclipse.wst.xml.ui.internal.contentassist.XMLContentModelGenerator;
import org.eclipse.wst.xml.ui.internal.editor.XMLEditorPluginImageHelper;
import org.eclipse.wst.xml.ui.internal.editor.XMLEditorPluginImages;
import org.jboss.tools.common.el.core.ELReference;
import org.jboss.tools.common.el.core.model.ELInstance;
import org.jboss.tools.common.el.core.model.ELInvocationExpression;
import org.jboss.tools.common.el.core.model.ELModel;
import org.jboss.tools.common.el.core.model.ELUtil;
import org.jboss.tools.common.el.core.parser.ELParser;
import org.jboss.tools.common.el.core.parser.ELParserUtil;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.el.core.resolver.ELResolver;
import org.jboss.tools.common.el.core.resolver.ELResolverFactoryManager;
import org.jboss.tools.common.el.core.resolver.Var;
import org.jboss.tools.common.model.ui.views.palette.PaletteInsertHelper;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.common.text.ext.util.Utils;
import org.jboss.tools.common.ui.CommonUIPlugin;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.ui.internal.editor.contentassist.AutoContentAssistantProposal;
import org.jboss.tools.jst.web.ui.internal.editor.contentassist.ELPrefixUtils.ELTextRegion;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.dnd.JSPPaletteInsertHelper;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.dnd.PaletteTaglibInserter;
import org.jboss.tools.jst.web.kb.IKbProject;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.IResourceBundle;
import org.jboss.tools.jst.web.kb.KbProjectFactory;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.KbQuery.Type;
import org.jboss.tools.jst.web.kb.PageContextFactory;
import org.jboss.tools.jst.web.kb.PageProcessor;
import org.jboss.tools.jst.web.kb.internal.taglib.NameSpace;
import org.jboss.tools.jst.web.kb.internal.taglib.TLDLibrary;
import org.jboss.tools.jst.web.kb.taglib.INameSpace;
import org.jboss.tools.jst.web.kb.taglib.INameSpaceExtended;
import org.jboss.tools.jst.web.kb.taglib.INameSpaceStorage;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * Tag Proposal computer for XML pages
 * 
 * @author Victor V. Rubezhny
 *
 */
@SuppressWarnings("restriction")
public class XmlTagCompletionProposalComputer  extends AbstractXmlCompletionProposalComputer {
	public static final String HTML_TAGNAME = "html"; //$NON-NLS-1$
	public static final String XMLNS_ATTRIBUTE_NAME_PREFIX = "xmlns:"; //$NON-NLS-1$
	public static final String EMPTY_ATTRIBUTE_VALUE = "=\"\""; //$NON-NLS-1$
	private static final String XMLNS_ATTRIBUTE_NAME = "xmlns"; //$NON-NLS-1$
	private static final String XMLNS_ATTRIBUTE_VALUE = "http://www.w3.org/1999/xhtml"; //$NON-NLS-1$
	protected static final ICompletionProposal[] EMPTY_PROPOSAL_LIST = new ICompletionProposal[0];
	
	@Override
	protected XMLContentModelGenerator getContentGenerator() {
		return new ELXMLContentModelGenerator();
	}

	@Override
	protected boolean validModelQueryNode(CMNode node) {
		boolean isValid = false;
		if(node instanceof DTDElementReferenceContentAdapter) {
			DTDElementReferenceContentAdapter content = (DTDElementReferenceContentAdapter)node;
			if(content.getCMDocument() instanceof DTDBaseAdapter) {
				DTDBaseAdapter dtd = (DTDBaseAdapter)content.getCMDocument();
				//this maybe a little hacky, but it works, if you have a better idea go for it
				String spec = dtd.getSpec();
				isValid = spec.indexOf(HTML_TAGNAME) != -1; //$NON-NLS-1$
			}
		} else if(node instanceof HTMLPropertyDeclaration) {
			HTMLPropertyDeclaration propDec = (HTMLPropertyDeclaration)node;
			isValid = !propDec.isJSP();
		} else if (node instanceof CMAttributeDeclaration) {
			isValid = true;
		}
		return isValid;
	}
	
	/**
	 * Calculates and adds the tag proposals to the Content Assist Request object
	 * 
	 * @param contentAssistRequest Content Assist Request object
	 * @param childPosition the 
	 */

	@Override
	protected void addTagInsertionProposals(
			ContentAssistRequest contentAssistRequest, int childPosition, CompletionProposalInvocationContext context) {
		
		// Need to check if an EL Expression is opened here.
		// If it is true we don't need to start any new tag proposals
		ELTextRegion prefix = getELPrefix(contentAssistRequest);
		if (prefix != null && prefix.isELStarted()) {
			return;
		}
		
		addTagNameProposals(contentAssistRequest, childPosition, true, context);
	}
	
	@Override
	protected void addAttributeNameProposals(ContentAssistRequest contentAssistRequest, 
			CompletionProposalInvocationContext context) {
		if (!(contentAssistRequest.getNode() instanceof Element))
			return;
		
		String matchString = contentAssistRequest.getMatchString();
		String query = matchString;
		if (query == null)
			query = ""; //$NON-NLS-1$
		String stringQuery = matchString;

		KbQuery kbQuery = createKbQuery(Type.ATTRIBUTE_NAME, query, stringQuery);
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(kbQuery, getContext());

		for (int i = 0; proposals != null && i < proposals.length; i++) {
			TextProposal textProposal = proposals[i];
			
			if (isExistingAttribute(textProposal.getLabel())) 
				continue;

			String replacementString = textProposal.getReplacementString() + EMPTY_ATTRIBUTE_VALUE; //$NON-NLS-1$

			int replacementOffset = contentAssistRequest.getReplacementBeginPosition();
			int replacementLength = contentAssistRequest.getReplacementLength();
			int cursorPosition = getCursorPositionForProposedText(replacementString);
			Image image = CommonUIPlugin.getImageDescriptorRegistry().get(textProposal.getImageDescriptor());
			if (textProposal.getImageDescriptor() == null) {
				image = XMLEditorPluginImageHelper.getInstance().getImage(XMLEditorPluginImages.IMG_OBJ_ATTRIBUTE);
			}

			String displayString = textProposal.getLabel() == null ? 
					replacementString : 
						textProposal.getLabel();
			IContextInformation contextInformation = null;
			String additionalProposalInfo = textProposal.getContextInfo();
			int relevance = textProposal.getRelevance();

			AutoContentAssistantProposal proposal = new AutoContentAssistantProposal(
					textProposal, true, replacementString, 
					replacementOffset, replacementLength, cursorPosition, image, displayString, 
					contextInformation, additionalProposalInfo, relevance);
			
			contentAssistRequest.addProposal(proposal);
		}
		
		addXmlnsPrefixProposals(contentAssistRequest, 
			context);
	}

	/**
	 * If the match string has the form of xmlns:* then we can suggest the possible namespace prefix/uri 
	 * proposals
	 * 
	 * @param contentAssistRequest
	 * @param context
	 */
	protected void addXmlnsPrefixProposals(ContentAssistRequest contentAssistRequest, 
			CompletionProposalInvocationContext context) {
		if (!(fContext instanceof IPageContext))
			return;
		
		if (!(contentAssistRequest.getNode() instanceof Element))
			return;
		
		String matchString = contentAssistRequest.getMatchString();

		String attrName = matchString;
		String prefixBeginning = "";
		
		if (attrName.startsWith(XMLNS_ATTRIBUTE_NAME_PREFIX)) {
			// probably this is not finished prefix name
			prefixBeginning = attrName.substring(XMLNS_ATTRIBUTE_NAME_PREFIX.length());
		} else if (XMLNS_ATTRIBUTE_NAME_PREFIX.startsWith(attrName)){
			prefixBeginning = "";
		}
		
		if (!isExistingAttribute(XMLNS_ATTRIBUTE_NAME, XMLNS_ATTRIBUTE_VALUE) && !attrName.startsWith(XMLNS_ATTRIBUTE_NAME_PREFIX))
			return;
		
		IFile file = PageContextFactory.getResource(context.getDocument());
		if (file != null && file.getProject() != null) {
			Collection<INameSpace> namespaces = getPossibleNamespacesForPrefix(
					file.getProject(), prefixBeginning, false,
					((IPageContext)fContext).getNameSpaces(context.getInvocationOffset()));
			
			for (INameSpace ns : namespaces) {
				String replacementString = XMLNS_ATTRIBUTE_NAME_PREFIX + ns.getPrefix() + "=\"" + ns.getURI() + "\"";
	
				int replacementOffset = contentAssistRequest.getReplacementBeginPosition();
				int replacementLength = contentAssistRequest.getReplacementLength();
				int cursorPosition = getCursorPositionForProposedText(replacementString);
				Image image = XMLEditorPluginImageHelper.getInstance().getImage(XMLEditorPluginImages.IMG_OBJ_ATTRIBUTE);
	
				String displayString = replacementString;
				IContextInformation contextInformation = null;
				int relevance = TextProposal.R_XML_ATTRIBUTE_VALUE;
	
				AutoContentAssistantProposal proposal = new AutoContentAssistantProposal(
						null, true, replacementString, 
						replacementOffset, replacementLength, cursorPosition, image, displayString, 
						contextInformation, null, relevance);
	
				contentAssistRequest.addProposal(proposal);
			}
		}
	}
	
	/**
	 * If the match string has the form of xmlns:* then we can suggest the possible namespace prefix/uri 
	 * proposals
	 * 
	 * @param contentAssistRequest
	 * @param context
	 */
	protected void addXmlnsUriProposals(ContentAssistRequest contentAssistRequest, 
			CompletionProposalInvocationContext context) {
		if (!(fContext instanceof IPageContext))
			return;
		
		if (!(contentAssistRequest.getNode() instanceof Element))
			return;

		String prefix = getParent(true, true);
		if (prefix == null || !prefix.startsWith(XMLNS_ATTRIBUTE_NAME_PREFIX))
			return;
		
		prefix = prefix.substring(XMLNS_ATTRIBUTE_NAME_PREFIX.length());

		String matchString = contentAssistRequest.getMatchString();

		IFile file = PageContextFactory.getResource(context.getDocument());
		if (file != null && file.getProject() != null) {
			Collection<INameSpace> namespaces = getPossibleNamespacesForPrefix(
					file.getProject(), prefix, true,
					((IPageContext)fContext).getNameSpaces(context.getInvocationOffset()));
			
			for (INameSpace ns : namespaces) {
				String replacementString = "\"" + ns.getURI() + "\"";
	
				int replacementOffset = contentAssistRequest.getReplacementBeginPosition();
				// Replacement length is incorrectly calculated by the WTP's CA in case of attribute value has no closing quote in it.
				// And in this case it kills the text right after the cursor position. 
				// Probably we shell to correct the length of the replacement in such case.
				int replacementLength = getCheckedAttributeValueReplacementLength(replacementOffset, contentAssistRequest.getReplacementLength());
				
				int cursorPosition = getCursorPositionForProposedText(replacementString);
				Image image = XMLEditorPluginImageHelper.getInstance().getImage(XMLEditorPluginImages.IMG_OBJ_ATTRIBUTE);
	
				String displayString = replacementString;
				IContextInformation contextInformation = null;
				int relevance = TextProposal.R_XML_ATTRIBUTE_VALUE + 100;
	
				AutoContentAssistantProposal proposal = new AutoContentAssistantProposal(
						null, true, replacementString, 
						replacementOffset, replacementLength, cursorPosition, image, displayString, 
						contextInformation, null, relevance);
	
				contentAssistRequest.addProposal(proposal);
			}
		}
	}
	
	private int getCheckedAttributeValueReplacementLength(int offset, int length) {
		try {
			String existingValue = fCurrentContext.getViewer().getDocument().get(offset, length);

			if (existingValue.indexOf('\n') != -1)
				existingValue = existingValue.substring(0, existingValue.indexOf('\n'));
			if (existingValue.indexOf('\r') != -1)
					existingValue = existingValue.substring(0, existingValue.indexOf('\r'));
			
			while(existingValue.length() > 0 && Character.isWhitespace(existingValue.charAt(existingValue.length() - 1)))
				existingValue = existingValue.substring(0, existingValue.length() - 1);

			char quote = 0;
			if (existingValue.charAt(0) == '"' || existingValue.charAt(0) == '\'')
				quote = existingValue.charAt(0);
			
			if (quote == 0)
				return 0;
			
			if (quote != existingValue.charAt(existingValue.length() - 1))
				return 1;
			
			return existingValue.length();
		} catch (BadLocationException e) {
			WebUiPlugin.getPluginLog().logError(e);
		}
		return length;
	}
	
	private Map<String, TreeSet<String>> getProjectDefinedAndCustomNamespaces(
			IKbProject kbProject, String prefixBeginning, boolean strictPrefix) {
		Map<String, TreeSet<String>> result = new HashMap<String, TreeSet<String>>();

		INameSpaceStorage nsStorage = kbProject.getNameSpaceStorage();
		Set<String> prefixes = nsStorage.getPrefixes(prefixBeginning);
		for (String prefix : prefixes) {
			if (strictPrefix && !prefix.equalsIgnoreCase(prefixBeginning))
				continue;
			
			Set<String> prefixUris = nsStorage.getURIs(prefix);
			if (prefixUris.isEmpty())
				continue;
			
			if (!result.containsKey(prefix)) {
				TreeSet<String> sortedUris = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
				sortedUris.addAll(prefixUris);
				result.put(prefix, sortedUris);
			} else {
				TreeSet<String> sortedUris = result.get(prefix);
				for (String uri : prefixUris) {
					if (!sortedUris.contains(uri))
						sortedUris.add(uri);
				}				
			}
		}
		return result;
	}
	
	private Collection<INameSpace> getPossibleNamespacesForPrefix(IProject project, String prefixBeginning, boolean strictPrefix, Map<String, List<INameSpace>> existingNamespaces) {
		Collection<INameSpace> namespaces = new ArrayList<INameSpace>();

		IKbProject kbProject = KbProjectFactory.getKbProject(project, true);
		if (kbProject == null)
			return namespaces;
			
		prefixBeginning = prefixBeginning == null ? "" : prefixBeginning;

		//
		// Get the all the gathered custom prefixes/uris as well as the prefixes/uris defined on the pages
		//
		Map<String, TreeSet<String>> prefix2uris = getProjectDefinedAndCustomNamespaces(kbProject, prefixBeginning, strictPrefix);
		
		//
		// Get all the prefixes/uris from the project libraries
		//
		List<ITagLibrary> libraries = kbProject.getAllTagLibraries();
		for(ITagLibrary l : libraries){
			if(l instanceof TLDLibrary){
				((TLDLibrary) l).createDefaultNameSpace();
			}
			INameSpace ns = l.getDefaultNameSpace();
			if(ns != null && ns.getPrefix() != null && 
					((strictPrefix && ns.getPrefix().equals(prefixBeginning)) || 
							(!strictPrefix && ns.getPrefix().startsWith(prefixBeginning) && 
									!isExistingNamespace(ns, existingNamespaces)))) {
				String prefix = ns.getPrefix();
				String uri = ns.getURI();
				
				if (!prefix2uris.containsKey(prefix)) {
					TreeSet<String> sortedUris = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
					sortedUris.add(uri);
					prefix2uris.put(prefix, sortedUris);
				} else {
					TreeSet<String> sortedUris = prefix2uris.get(prefix);
					if (!sortedUris.contains(uri))
						sortedUris.add(uri);
				}
			}
		}
		
		// 
		// Create a sorted set of gathered prefixes...
		//
		Set<String> prefixes = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		prefixes.addAll(prefix2uris.keySet());
		
		//
		// And finally create a collection of namespaces
		//
		for (String prefix : prefixes) {
			Set<String> uris = prefix2uris.get(prefix);
			for (String uri : uris) {
				ITagLibrary[] libs = kbProject.getTagLibraries(uri);
				if (libs == null || libs.length == 0) {
					namespaces.add(new NameSpace(uri, prefix));
				} else {
					namespaces.add(new NameSpace(uri, prefix, libs));
				}
			}
		}

		return namespaces;
	}
	
	private boolean isExistingNamespace(INameSpace ns, Map<String, List<INameSpace>> existingNamespaces) {
		if (existingNamespaces == null)
			return false;

		List<INameSpace> namespaces = existingNamespaces.get(ns.getURI());
		if (namespaces == null)
			return false;
		for (INameSpace existingNS : namespaces) {
			if (ns.getPrefix().equalsIgnoreCase(existingNS.getPrefix()))
				return true;
		}
		return false;
	}

	protected String getMatchString(IStructuredDocumentRegion parent, ITextRegion aRegion, int offset) {
		if ((aRegion == null) || isCloseRegion(aRegion)) {
			return ""; //$NON-NLS-1$
		}
		String matchString = null;
		String regionType = aRegion.getType();
		if ((regionType == DOMRegionContext.XML_TAG_ATTRIBUTE_EQUALS) || (regionType == DOMRegionContext.XML_TAG_OPEN) || (offset > parent.getStartOffset(aRegion) + aRegion.getTextLength())) {
			matchString = ""; //$NON-NLS-1$
		}
		else if (regionType == DOMRegionContext.XML_CONTENT) {
			matchString = ""; //$NON-NLS-1$
		}
		else {
			if ((parent.getText(aRegion).length() > 0) && (parent.getStartOffset(aRegion) < offset)) {
				matchString = parent.getText(aRegion).substring(0, offset - parent.getStartOffset(aRegion));
			}
			else {
				matchString = ""; //$NON-NLS-1$
			}
		}
		if(regionType == DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE && matchString.startsWith("\"")) { //$NON-NLS-1$
			matchString = matchString.substring(1);
		}
		return matchString;
	}

	protected boolean isCloseRegion(ITextRegion region) {
		String type = region.getType();
		return ((type == DOMRegionContext.XML_PI_CLOSE) || 
				(type == DOMRegionContext.XML_TAG_CLOSE) || 
				(type == DOMRegionContext.XML_EMPTY_TAG_CLOSE) || 
				(type == DOMRegionContext.XML_CDATA_CLOSE) || 
				(type == DOMRegionContext.XML_COMMENT_CLOSE) || 
				(type == DOMRegionContext.XML_ATTLIST_DECL_CLOSE) || 
				(type == DOMRegionContext.XML_ELEMENT_DECL_CLOSE) || 
				(type == DOMRegionContext.XML_DOCTYPE_DECLARATION_CLOSE) || 
				(type == "JSP_CLOSE") ||  //$NON-NLS-1$
				(type == "JSP_COMMENT_CLOSE") ||  //$NON-NLS-1$
				(type.equals("JSP_DIRECTIVE_CLOSE")) ||  //$NON-NLS-1$
				(type == DOMRegionContext.XML_DECLARATION_CLOSE));
	}

	/*
	 * Checks if the specified attribute exists 
	 * 
	 * @param attrName Name of attribute to check
	 */
	protected boolean isExistingAttribute(String attrName) {
		return isExistingAttribute(attrName, null);
	}
	
	
	/*
	 * Checks if the specified attribute exists 
	 * 
	 * @param attrName Name of attribute to check
	 */
	protected boolean isExistingAttribute(String attrName, String value) {
		IStructuredModel sModel = StructuredModelManager.getModelManager()
				.getExistingModelForRead(getDocument());
		try {
			if (sModel == null)
				return false;

			Document xmlDocument = (sModel instanceof IDOMModel) ? ((IDOMModel) sModel)
					.getDocument()
					: null;

			if (xmlDocument == null)
				return false;

			// Get Fixed Structured Document Region
			IStructuredDocumentRegion sdFixedRegion = this.getStructuredDocumentRegion(getOffset());
			if (sdFixedRegion == null)
				return false;
			
			Node n = findNodeForOffset(xmlDocument, sdFixedRegion.getStartOffset());
			if (n == null)
				return false;
			
			// Find the first parent tag
			if (!(n instanceof Element)) {
				if (n instanceof Attr) {
					n = ((Attr) n).getOwnerElement();
				} else {
					return false;
				}
			}
			
			if (n == null)
				return false;

			Attr a = ((Element)n).getAttributeNode(attrName);
			if (a == null)
				return false;
			
			if (value == null)
				return true;
			
			String v = Utils.trimQuotes(a.getNodeValue());
			
			return (v != null && v.equalsIgnoreCase(Utils.trimQuotes(value)));
		} finally {
			if (sModel != null) {
				sModel.releaseFromRead();
			}
		}
	}

	@Override
	protected void addAttributeValueProposals(
			ContentAssistRequest contentAssistRequest,
			CompletionProposalInvocationContext context) {
		
		fCurrentContext = context;
		
		// Need to check if an EL Expression is opened here.
		// If it is true we don't need to start any new tag proposals
		ELTextRegion prefix = getELPrefix(contentAssistRequest);
		if (prefix != null && prefix.isELStarted()) {
			return;
		}
		
		String matchString = contentAssistRequest.getMatchString();
		String query = Utils.trimQuotes(matchString);
		if (query == null)
			query = ""; //$NON-NLS-1$
		String stringQuery = matchString;
//		query = matchString.endsWith(" ")?query + " ":query;

		KbQuery kbQuery = createKbQuery(Type.ATTRIBUTE_VALUE, query, stringQuery);
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(kbQuery, getContext());

		for (int i = 0; proposals != null && i < proposals.length; i++) {
			TextProposal textProposal = proposals[i];
			int replacementOffset = contentAssistRequest.getReplacementBeginPosition();
			int replacementLength = getCheckedAttributeValueReplacementLength(replacementOffset, contentAssistRequest.getReplacementLength());
			if(textProposal.getStart() >= 0 && textProposal.getEnd() >= 0) {
				replacementOffset += textProposal.getStart() + 1;
				replacementLength = textProposal.getEnd() - textProposal.getStart();
			}
			String replacementString = "\"" + textProposal.getReplacementString() + "\""; //$NON-NLS-1$ //$NON-NLS-2$
			String alternativeMatch = textProposal.getAlternateMatch();
			if(alternativeMatch!=null) {
				alternativeMatch = "\"" + textProposal.getAlternateMatch() + "\""; //$NON-NLS-1$ //$NON-NLS-2$
			}
			if(textProposal.getStart() >= 0 && textProposal.getEnd() >= 0) {
				replacementString = textProposal.getReplacementString();
				alternativeMatch = textProposal.getAlternateMatch();
			}
			int cursorPosition = getCursorPositionForProposedText(replacementString);
			Image image = CommonUIPlugin.getImageDescriptorRegistry().get(textProposal.getImageDescriptor());
			String displayString = textProposal.getLabel() == null ? 
					replacementString : 
						textProposal.getLabel();
			IContextInformation contextInformation = null;
			String additionalProposalInfo = textProposal.getContextInfo();
			int relevance = textProposal.getRelevance();
			if (relevance == TextProposal.R_NONE) {
				relevance = TextProposal.R_JSP_ATTRIBUTE_VALUE;
			}

			AutoContentAssistantProposal proposal = new AutoContentAssistantProposal(
					textProposal, replacementString, 
					replacementOffset, replacementLength, cursorPosition, image, displayString, alternativeMatch, 
					contextInformation, additionalProposalInfo, relevance);

			contentAssistRequest.addProposal(proposal);
		}
		
		addXmlnsUriProposals(contentAssistRequest, 
				context);
	}

	protected void addTagNameProposals(ContentAssistRequest contentAssistRequest, int childPosition,
			CompletionProposalInvocationContext context) {
		addTagNameProposals(contentAssistRequest, childPosition, false, context);
	}
	/**
	 * Calculates and adds the tag name proposals to the Content Assist Request object
	 * 
	 * @param contentAssistRequest Content Assist Request object
	 * @param childPosition the 
	 */
	protected void addTagNameProposals(
			ContentAssistRequest contentAssistRequest, 
			int childPosition, boolean insertTagOpenningCharacter,
			CompletionProposalInvocationContext context) {

		String mainPrefix = getTagPrefix();
		String mainURI = getTagUri();
		// - calculate correct replacenemt begin position
		// - calculate correct replacenment length
		// - calculate correct match string
		
		String matchString = contentAssistRequest.getMatchString();

		/*
		 * Jeremy: Add attribute name proposals before  empty tag close
		 */
		IStructuredDocumentRegion sdRegion = getStructuredDocumentRegion(getOffset());
		ITextRegion completionRegion = getCompletionRegion(getOffset(), 
				contentAssistRequest.getNode());

		matchString = getMatchString(sdRegion, completionRegion, 
								getOffset());
				
		String query = matchString;
		addTagNameProposalsForPrefix(contentAssistRequest, childPosition, query, mainPrefix, mainURI, TextProposal.R_TAG_INSERTION, insertTagOpenningCharacter);

		if (query == null || query.length() == 0)
			return;
		
		if (query.indexOf(':') != -1) {
			// Make additional proposals to allow prefixed tags to be entered for namespaces that aren't defined on the page
			// The complete prefix is to be typed in onto the page due to get these proposals
			
			// Create a fake IPageContext fulfilled with all the project TagLibraries (excluding those are defined within the page)
			String prefix = query.indexOf(':') != -1 ? query.substring(0, query.indexOf(':')) : query; 
			IFile file = PageContextFactory.getResource(context.getDocument());
			if (file != null && file.getProject() != null) {
				Collection<INameSpace> namespaces = new ArrayList<INameSpace>();
				Map<String, List<INameSpace>> existing = ((IPageContext)fContext).getNameSpaces(context.getInvocationOffset());
				for (String uri: existing.keySet()) {
					List<INameSpace> ns = existing.get(uri);
					for (INameSpace n: ns) {
						if(prefix.equals(n.getPrefix())) {
							namespaces.add(n);
						}
					}
				}
				if(namespaces.isEmpty()) {
					namespaces = getPossibleNamespacesForPrefix(
						file.getProject(), prefix, query.indexOf(':') != -1,
						existing);
				}
				Map<String, List<INameSpace>> nsMap = new HashMap<String, List<INameSpace>>();
				for (INameSpace ns : namespaces) {
					List<INameSpace> uNamespaces = nsMap.get(ns.getURI());
					if (uNamespaces == null) {
						uNamespaces = new ArrayList<INameSpace>(1);
						nsMap.put(ns.getURI(), uNamespaces);
					}
					uNamespaces.add(ns);
				}
	
				IPageContext fakeContext = createFakePageContext(nsMap, context.getDocument(), file);
				for (INameSpace namespace : namespaces) {
					String possiblePrefix = namespace.getPrefix(); 
					if (possiblePrefix == null || possiblePrefix.length() == 0)
						continue;	// Don't query proposals for the default value here
					
					String possibleURI = namespace.getURI();
					if (possibleURI == null || possibleURI.length() == 0)
						continue;
					
					addTagNameProposalsForPrefix(contentAssistRequest, 
							fakeContext, 
							childPosition, 
							query, possiblePrefix, possibleURI, 
							TextProposal.R_TAG_INSERTION - 1, 
							insertTagOpenningCharacter);
				}
				
			}
		} else {
			// Make an additional proposals to allow prefixed tags to be entered with no prefix typed
			ELContext elContext = getContext();
			if (elContext instanceof IPageContext) {
				IPageContext pageContext = (IPageContext)elContext;
				Map<String, List<INameSpace>> nsMap = pageContext.getNameSpaces(contentAssistRequest.getReplacementBeginPosition());
				if (nsMap == null) return;
				
				for (List<INameSpace> namespaces : nsMap.values()) {
					if (namespaces == null) continue;
					
					for (INameSpace namespace : namespaces) {
						String possiblePrefix = namespace.getPrefix(); 
						if (possiblePrefix == null || possiblePrefix.length() == 0)
							continue;	// Don't query proposals for the default value here
						
						String possibleURI = namespace.getURI();
						if (possibleURI == null || possibleURI.length() == 0)
							continue;
						
						addTagNameProposalsForPrefix(contentAssistRequest, childPosition, 
								query, possiblePrefix, possibleURI, 
								TextProposal.R_TAG_INSERTION - 1, 
								insertTagOpenningCharacter);
					}
				}
			}
		}
	}

	class NameSpaceInserter implements IRunnableWithProgress {
		ITextViewer viewer;
		String prefix;
		String uri;
		
		public NameSpaceInserter(ITextViewer viewer, String prefix, String uri) {
			this.viewer = viewer;
			this.prefix = prefix;
			this.uri = uri;
		}

		public void run(IProgressMonitor monitor)
				throws InvocationTargetException, InterruptedException {
			if (uri == null || prefix == null) // No need to add a namespace in case of no prefix/uri is specified
				return;
			
			Properties properties = new Properties();
			
			properties.put(JSPPaletteInsertHelper.PROPOPERTY_ADD_TAGLIB, "true"); //$NON-NLS-1$
			properties.put(PaletteInsertHelper.PROPERTY_START_TEXT, ""); //$NON-NLS-1$
			properties.put(JSPPaletteInsertHelper.PROPOPERTY_TAGLIBRARY_URI, uri);
			properties.put(JSPPaletteInsertHelper.PROPOPERTY_DEFAULT_PREFIX, prefix);
			properties.put(JSPPaletteInsertHelper.PROPOPERTY_FORCE_PREFIX, "true");
			properties.put("viewer", viewer);

			PaletteTaglibInserter inserter = new PaletteTaglibInserter();
			inserter.inserTaglib(viewer.getDocument(), properties);
		}
	}
	
	class FakePageContext implements IPageContext {
		private IDocument document;
		private IFile file;
		private Map<String, List<INameSpace>> namespaces;
		private ITagLibrary[] tagLibraries;
		
		
		FakePageContext(Map<String, List<INameSpace>> namespaces, IDocument document, IFile file) {
			this.namespaces = namespaces;
			this.document = document;
			this.file = file;
			Set<ITagLibrary> libraries = new HashSet<ITagLibrary>();
			for (List<INameSpace> nsValues : namespaces.values()) {
				for (INameSpace ns : nsValues) {
					if (ns instanceof INameSpaceExtended) {
						ITagLibrary[] libs = ((INameSpaceExtended)ns).getTagLibraries();
						if (libs != null) {
							for (ITagLibrary lib : libs) {
								libraries.add(lib);
							}
						}
					}
				}
			}
			tagLibraries = libraries.toArray(new ITagLibrary[0]);
		}
		
		public void setVars(List<Var> vars) {
			// Do nothing
		}
		
		public void setResource(IFile file) {
			// Do nothing (we'll use only a file passed as createFakePageContext() method argument
		}
		
		public void setElResolvers(ELResolver[] resolvers) {
			// Do nothing
		}
		
		public void setDirty(boolean dirty) {
			// Do nothing
		}
		
		public boolean isDirty() {
			return false;
		}
		
		public Var[] getVars(int offset) {
			return new Var[0];
		}
		
		public IFile getResource() {
			return file;
		}
		
		public ELResolver[] getElResolvers() {
			return new ELResolver[0];
		}
		
		public Collection<ELReference> getELReferences(IRegion region) {
			return new ArrayList<ELReference>();
		}
		
		public ELReference[] getELReferences() {
			return new ELReference[0];
		}
		
		public ELReference getELReference(int offset) {
			return null;
		}
		
		public Set<String> getURIs() {
			return namespaces.keySet();
		}
		
		public IResourceBundle[] getResourceBundles() {
			return new IResourceBundle[0];
		}
		
		public Map<String, List<INameSpace>> getNameSpaces(int offset) {
			return namespaces;
		}
		
		public ITagLibrary[] getLibraries() {
			return tagLibraries;
		}
		
		public IDocument getDocument() {
			return document;
		}
	}
	
	private IPageContext createFakePageContext(Map<String, List<INameSpace>> namespaces, IDocument document, IFile file) {
		return new FakePageContext(namespaces, document, file);	
	}
	 
	private void addTagNameProposalsForPrefix(
			ContentAssistRequest contentAssistRequest,
			int childPosition, 
			String query,
			String prefix,
			String uri, 
			int defaultRelevance,
			boolean insertTagOpenningCharacter) {
		addTagNameProposalsForPrefix(contentAssistRequest, getContext(), 
				childPosition, query, prefix, uri, defaultRelevance, 
				insertTagOpenningCharacter);
	}
	
	private void addTagNameProposalsForPrefix(
			ContentAssistRequest contentAssistRequest, 
			ELContext context,
			int childPosition, 
			String query,
			String prefix,
			String uri, 
			int defaultRelevance,
			boolean insertTagOpenningCharacter) {
		if (query == null)
			query = ""; //$NON-NLS-1$

		String template = getTemplate(contentAssistRequest, query);

		StringBuilder stringQuery = new StringBuilder();
		if (query.indexOf(':') == -1 && prefix != null && prefix.length() > 0) {
			stringQuery.append(prefix).append(':');
		}
		stringQuery.append(query);
		
		KbQuery kbQuery = createKbQuery(Type.TAG_NAME, stringQuery.toString(), '<' + stringQuery.toString(), prefix, uri);
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(kbQuery, context, true);

		ELContext originalContext = createContext();
		Map<String, List<INameSpace>> namespaces = null;
		IPageContext pageContext = null;
		if (originalContext instanceof IPageContext) {
			pageContext = (IPageContext)originalContext;
			namespaces = pageContext.getNameSpaces(getOffset());
		}
		for (int i = 0; proposals != null && i < proposals.length; i++) {
			TextProposal textProposal = proposals[i];
			boolean useAutoActivation = true;
	
			String replacementString = textProposal.getReplacementString();
			String closingTag = textProposal.getLabel();
			if (closingTag != null && closingTag.startsWith("<")) { //$NON-NLS-1$
				closingTag = closingTag.substring(1);
			}
			
			int replacementOffset = getOffset() - query.length() - (insertTagOpenningCharacter?0:1);
			int replacementLength = query.length() + (insertTagOpenningCharacter?0:1);

			if (!insertTagOpenningCharacter && replacementString.startsWith("<")) { //$NON-NLS-1$
				String replacementTagName = extractTagName(replacementString.substring(1));
				int start = getStartOfTagName();
				int end = getEndOfTagName();
				if (getDocumentText(getDocument(), start, end).equalsIgnoreCase(extractTagName(replacementString))) {
					// Do no insert a new tag ending chars (and/or closing tag) with the same name 
					// (just shift the cursor position to the end of the name)
					
					replacementString = '<' + replacementTagName; 
					replacementLength += end - getOffset();
				} else {
					if (!replacementString.endsWith("/>")) { //$NON-NLS-1$
						replacementString += "</" + closingTag + ">"; //$NON-NLS-1$ //$NON-NLS-2$
						useAutoActivation = false;	// JBIDE-6285: Don't invoke code assist automatically if user inserts <tag></tag>.
					}
				}
			}
		
			int cursorPosition = getCursorPositionForProposedText(replacementString);
			Image image = CommonUIPlugin.getImageDescriptorRegistry().get(textProposal.getImageDescriptor());
			if(textProposal.getImageDescriptor()==null) {
				image = XMLEditorPluginImageHelper.getInstance().getImage(XMLEditorPluginImages.IMG_OBJ_TAG_GENERIC);
			}

			String displayString = closingTag;
			IContextInformation contextInformation = null;
			String additionalProposalInfo = textProposal.getContextInfo();
			int relevance = textProposal.getRelevance();
			if (relevance == TextProposal.R_NONE) {
				relevance = defaultRelevance == TextProposal.R_NONE? TextProposal.R_TAG_INSERTION : defaultRelevance;
			}
			if(template.length() > 0 && textProposal.isAlternativeMatchStart(template)) {
				relevance = TextProposal.R_TAG_TEMPLATE;
				replacementOffset -= template.length();
				replacementLength += template.length();
			}

			// If the xmlns isn't defined for the page
			// the proposal's apply method should add it 
			//
			NameSpaceInserter nameSpaceInserter = (namespaces != null && !isExistingNameSpace(namespaces, prefix, uri)) ? 
					new NameSpaceInserter(fCurrentContext.getViewer(), prefix, uri) : null;
						
			AutoContentAssistantProposal proposal = new AutoContentAssistantProposal(
						textProposal, useAutoActivation, replacementString, replacementOffset,
						replacementLength, cursorPosition, image, displayString,
						contextInformation, additionalProposalInfo, relevance,
						nameSpaceInserter);
			proposal.setAlternativeMatches(textProposal.getAlternativeMatches());

			contentAssistRequest.addProposal(proposal);
		}
	}

	/**
	 * Returns word before cursor as template name in case if query string is empty 
	 * and cursor is in or near text region.
	 * @param contentAssistRequest
	 * @param query
	 * @return
	 */
	private String getTemplate(ContentAssistRequest contentAssistRequest, String query) {
		String template = "";
		TextImpl textNode = null;
		if(query.length() == 0 && contentAssistRequest.getNode() instanceof Text) {
			textNode = (TextImpl)contentAssistRequest.getNode();
		} else if(query.length() == 0 && contentAssistRequest.getNode() instanceof IDOMElement
				&& (((IDOMElement)contentAssistRequest.getNode()).getStartOffset() == getOffset()
				   || ((IDOMElement)contentAssistRequest.getNode()).getEndStartOffset() == getOffset())
				&& getOffset() > 0 
				&& ((IDOMElement)contentAssistRequest.getNode()).getModel().getIndexedRegion(getOffset() - 1) instanceof Text
				) {
			textNode = (TextImpl)((IDOMElement)contentAssistRequest.getNode()).getModel().getIndexedRegion(getOffset() - 1);
		} else if(query.length() == 0 && contentAssistRequest.getNode() instanceof Comment
				&& getOffset() > 0
				&& ((IDOMNode)contentAssistRequest.getNode()).getModel().getIndexedRegion(getOffset() - 1) instanceof Text) {
			textNode = (TextImpl)((IDOMElement)contentAssistRequest.getNode()).getModel().getIndexedRegion(getOffset() - 1);
		}
		if(textNode != null) {
			String text = textNode.getTextContent();
			int start = textNode.getStartOffset();
			if(start < getOffset()) {
				for (int i = getOffset() - 1; i >= start; i--) {
					if(Character.isLetter(text.charAt(i - start))) {
						template = "" + text.charAt(i - start) + template;
					} else {
						break;
					}
				}
			}
		}

		return template;
	}
	 
	boolean isExistingNameSpace(Map<String, List<INameSpace>> namespaces, String prefix, String uri) {
		if (namespaces == null || prefix == null || uri == null)
			return false;
		
		List<INameSpace> uriNamespaces = namespaces.get(uri);
		if (uriNamespaces == null)
			return false;
		
		for (INameSpace namespace : uriNamespaces) {
			if (prefix.equals(namespace.getPrefix()) && uri.equals(namespace.getURI())) 
				return true;
		}
		return false;
	}
	
	private String getDocumentText(IDocument document, int start, int end) {
		try {
			return document.get(start, end - start);
		} catch (BadLocationException e) {
			return ""; //$NON-NLS-1$
		}
	}
	
	private int getStartOfTagName () {
		try {
			int start = getOffset();
			while (start > 0 && (Character.isJavaIdentifierPart(getDocument().getChar(start - 1)) || ':' == getDocument().getChar(start - 1)))
				start--;
			
			return start;
		} catch (BadLocationException e) {
			return -1;
		}
	}
	
	private int getEndOfTagName() {
		try {
			int end = getOffset();
			while (end < getDocument().getLength() && (Character.isJavaIdentifierPart(getDocument().getChar(end)) || ':' == getDocument().getChar(end)))
				end++;
			
			return end;
		} catch (BadLocationException e) {
			return -1;
		}
	}

	private String extractTagName(String tag) {
		int start = tag.startsWith("<") ? 1 : 0;
		int offset = start;
		while (offset < tag.length() && !Character.isWhitespace(tag.charAt(offset)) &&
				'>' != tag.charAt(offset) && '<' != tag.charAt(offset))
			offset++;
		return tag.substring(start, offset);
	}
	
	@Override
	protected void addAttributeValueELProposals(ContentAssistRequest contentAssistRequest,
			CompletionProposalInvocationContext context) {
		// No EL proposals are to be added here
	}


	protected void addTextELProposals(ContentAssistRequest contentAssistRequest,
			CompletionProposalInvocationContext context) {
		// No EL proposals are to be added here
	}

	protected ELContext createContext() {
		return createContext(PageContextFactory.XML_PAGE_CONTEXT_TYPE);
	}
	
	protected KbQuery createKbQuery(Type type, String query, String stringQuery) {
		return createKbQuery(type, query, stringQuery, getTagPrefix(), getTagUri());
	}

	protected KbQuery createKbQuery(Type type, String query, String stringQuery, String prefix, String uri) {
		KbQuery kbQuery = new KbQuery();

		KbQuery.Tag[] parentTags = getParentTagsWithAttributes(type == Type.ATTRIBUTE_NAME || type == Type.ATTRIBUTE_VALUE || type == Type.TAG_BODY);
		String	parent = getParent(type == Type.ATTRIBUTE_VALUE, type == Type.ATTRIBUTE_NAME || type == Type.TAG_BODY);
		String queryValue = query;
		String queryStringValue = stringQuery;
		
		kbQuery.setPrefix(prefix);
		kbQuery.setUri(uri);
		kbQuery.setParentTagsWithAttributes(parentTags);
		kbQuery.setParent(parent); 
		kbQuery.setMask(true); 
		kbQuery.setType(type);
		kbQuery.setOffset(fCurrentContext.getInvocationOffset());
		kbQuery.setValue(queryValue); 
		kbQuery.setStringQuery(queryStringValue);
		kbQuery.setAttributes(getAttributes());
		
		return kbQuery;
	}


	/**
	 * this is the position the cursor should be in after the proposal is
	 * applied
	 * 
	 * @param proposedText
	 * @return the position the cursor should be in after the proposal is
	 *         applied
	 */
	protected static int getCursorPositionForProposedText(String proposedText) {
		int cursorAdjustment;
		cursorAdjustment = proposedText.indexOf("\"\"") + 1; //$NON-NLS-1$
		// otherwise, after the first tag
		if (cursorAdjustment == 0) {
			cursorAdjustment = proposedText.indexOf('>') + 1;
		}
		if (cursorAdjustment == 0) {
			cursorAdjustment = proposedText.lastIndexOf("{}") + 1; //$NON-NLS-1$			
		}
		if (cursorAdjustment == 0) {
			cursorAdjustment = proposedText.length();
		}

		return cursorAdjustment;
	}

	/**
	 * Returns array of the <code>org.jboss.tools.common.el.core.resolver.ELResolver</code> 
	 * instances.
	 * 
	 * @param resource
	 * @return
	 */
	protected ELResolver[] getELResolvers(IResource resource) {
		if (resource == null)
			return null;
		
		ELResolverFactoryManager elrfm = ELResolverFactoryManager.getInstance();
		return elrfm.getResolvers(resource);
	}
	
	/**
	 * Returns URI for the current/parent tag
	 * @return
	 */
	public String getTagUri() {
		String nodePrefix = getTagPrefix();
		return getUri(nodePrefix);
	}

	/**
	 * Returns URI string for the prefix specified using the namespaces collected for 
	 * the {@link IPageContext} context.
	 * 
	 * 	@Override org.jboss.tools.jst.web.ui.internal.editor.contentassist.AbstractXMLContentAssistProcessor#getUri(String)
	 */
	protected String getUri(String prefix) {
		return null;
	}

	/**
	 * Returns EL Prefix Text Region Information Object
	 * 
	 * @return
	 */
	protected ELTextRegion getELPrefix(ContentAssistRequest request) {
		if (!DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE.equals(request.getRegion().getType()) &&
				!DOMRegionContext.XML_CONTENT.equals(request.getRegion().getType()) &&
				!DOMRegionContext.BLOCK_TEXT.equals(request.getRegion().getType())) 
			return null;
		
		String text = request.getDocumentRegion().getFullText(request.getRegion());
		int startOffset = request.getDocumentRegion().getStartOffset() + request.getRegion().getStart();

		boolean isAttributeValue = false;
		boolean hasOpenQuote = false;
		boolean hasCloseQuote = false;
		char quoteChar = (char)0;
		if (DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE.equals(request.getRegion().getType())) {
			isAttributeValue = true;
			if (text.startsWith("\"") || text.startsWith("'")) {//$NON-NLS-1$ //$NON-NLS-2$
				quoteChar = text.charAt(0);
				hasOpenQuote = true;
			}
			if (hasOpenQuote && text.trim().endsWith(String.valueOf(quoteChar))) {
				hasCloseQuote = true;
			}
		}
		
		int inValueOffset = getOffset() - startOffset;
		if (text != null && text.length() < inValueOffset) { // probably, the attribute value ends before the document position
			return null;
		}
		if (inValueOffset<0) {
			return null;
		}
		
//			String matchString = text.substring(0, inValueOffset);
		
		ELParser p = ELParserUtil.getJbossFactory().createParser();
		ELModel model = p.parse(text);
		
		ELInstance is = ELUtil.findInstance(model, inValueOffset);// ELInstance
		ELInvocationExpression ie = ELUtil.findExpression(model, inValueOffset);// ELExpression
		boolean isELStarted = (model != null && is != null && startsWithELBeginning(is.getFirstToken() == null ? null : is.getFirstToken().getText()));
		boolean isELClosed = (model != null && is != null && endsWithELBeginning(is.getFirstToken() == null ? null : is.getLastToken().getText())); 
		
//			boolean insideEL = startOffset + model.toString().length() 
		ELTextRegion tr = new ELTextRegion(startOffset,  ie == null ? inValueOffset : ie.getStartPosition(), 
				ie == null ? 0 : inValueOffset - ie.getStartPosition(), ie == null ? "" : ie.getText(),  //$NON-NLS-1$ 
				isELStarted, isELClosed,
				isAttributeValue, hasOpenQuote, hasCloseQuote, quoteChar);
		
		return tr;
	}
	
	/**
	 * Returns EL Predicate Text Region Information Object
	 * 
	 * 
	 * @return
	 */
	protected ELTextRegion getELPredicatePrefix(ContentAssistRequest request) {
		if (request == null || request.getRegion() == null)
			return null;

		IStructuredDocumentRegion documentRegion = request.getDocumentRegion();
		ITextRegion completionRegion = request.getRegion();
		String regionType = completionRegion.getType();
		
		if (DOMRegionContext.XML_END_TAG_OPEN.equals(regionType) || DOMRegionContext.XML_TAG_OPEN.equals(regionType)) {
			documentRegion = documentRegion.getPrevious();
			completionRegion = getCompletionRegion(request.getDocumentRegion().getStartOffset() + request.getRegion().getStart() - 1, request.getParent());
		}
		if(documentRegion==null || completionRegion==null) {
			return null;
		}
		if (!DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE.equals(completionRegion.getType()) &&
				!DOMRegionContext.XML_CONTENT.equals(completionRegion.getType()) &&
				!DOMRegionContext.BLOCK_TEXT.equals(completionRegion.getType())) {
				return null;
		}
		String text = documentRegion.getFullText(completionRegion);
		int startOffset = documentRegion.getStartOffset() + completionRegion.getStart();
		
		boolean isAttributeValue = false;
		boolean hasOpenQuote = false;
		boolean hasCloseQuote = false;
		char quoteChar = (char)0;
		if (DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE.equals(request.getRegion().getType())) {
			isAttributeValue = true;
			if (text.startsWith("\"") || text.startsWith("'")) {//$NON-NLS-1$ //$NON-NLS-2$
				quoteChar = text.charAt(0);
				hasOpenQuote = true;
			}
			if (hasOpenQuote && text.trim().endsWith(String.valueOf(quoteChar))) {
				hasCloseQuote = true;
			}
		}
		
		int inValueOffset = getOffset() - startOffset;
		if (inValueOffset<0 || // There is no a word part before cursor 
				(text != null && text.length() < inValueOffset)) { // probably, the attribute value ends before the document position
			return null;
		}

		String matchString = getELPredicateMatchString(text, inValueOffset);
		if (matchString == null)
			return null;
		
		ELTextRegion tr = new ELTextRegion(startOffset, getOffset() - matchString.length() - startOffset, 
				matchString.length(), matchString, false, false,
				isAttributeValue, hasOpenQuote, hasCloseQuote, quoteChar);
		
		return tr;
	}

	/**
	 * Returns predicate string for the EL-related query. 
	 * The predicate string is the word/part of word right before the cursor position, including the '.' and '_' characters, 
	 * which is to be replaced by the EL CA proposal ('#{' and '}' character sequences are to be inserted too)
	 *  
	 * @param text
	 * @param offset
	 * @return
	 */
	protected String getELPredicateMatchString(String text, int offset) {
		int beginningOffset = offset - 1;
		while(beginningOffset >=0 && 
				(Character.isJavaIdentifierPart(text.charAt(beginningOffset)) ||
						'.' == text.charAt(beginningOffset) ||
						'_' == text.charAt(beginningOffset))) {
			beginningOffset--;
		}
		beginningOffset++; // move it to point the first valid character
		return text.substring(beginningOffset, offset);
	}
	
	public static class ELXMLContentModelGenerator extends XMLContentModelGenerator {
		
	}
}
