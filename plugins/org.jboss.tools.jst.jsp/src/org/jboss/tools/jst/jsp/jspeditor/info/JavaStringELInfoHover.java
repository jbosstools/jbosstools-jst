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
package org.jboss.tools.jst.jsp.jspeditor.info;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.text.FastJavaPartitionScanner;
import org.eclipse.jdt.internal.ui.text.JavaWordFinder;
import org.eclipse.jdt.internal.ui.text.java.hover.AbstractJavaEditorTextHover;
import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.sse.ui.internal.derived.HTMLTextPresenter;
import org.jboss.tools.common.el.core.model.ELInvocationExpression;
import org.jboss.tools.common.el.core.model.ELModel;
import org.jboss.tools.common.el.core.model.ELUtil;
import org.jboss.tools.common.el.core.parser.ELParser;
import org.jboss.tools.common.el.core.parser.ELParserUtil;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.contentassist.Utils;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageContextFactory;
import org.jboss.tools.jst.web.kb.PageProcessor;
import org.jboss.tools.jst.web.kb.KbQuery.Type;

/**
 * 
 * @author Victor Rubezhny
 *
 */
@SuppressWarnings("restriction")
public class JavaStringELInfoHover extends AbstractJavaEditorTextHover {

	/*
	 * @see ITextHover#getHoverRegion(ITextViewer, int)
	 */
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		return JavaWordFinder.findWord(textViewer.getDocument(), offset);
	}
	
	/*
	 * @see JavaElementHover
	 */
	public String getHoverInfo(ITextViewer textViewer, IRegion region) {
		// find a region of __java_string, if we're in it - use it
		IDocument document = textViewer == null ? null : textViewer.getDocument();
		if (document == null)
			return null;
		
		int rangeStart = -1;
		int rangeLength = 0;
		IToken rangeToken = null;
		FastJavaPartitionScanner scanner = new FastJavaPartitionScanner();
		scanner.setRange(document, 0, document.getLength());
		while(true) {
			IToken token = scanner.nextToken();
			if(token == null || token.isEOF()) break;
			int start = scanner.getTokenOffset();
			int length = scanner.getTokenLength();
			int end = start + length;
			if(start <= region.getOffset() && end >= region.getOffset()) {
				rangeStart = start;
				rangeLength = length;
				rangeToken = token;
				break;
			}
			if(start > region.getOffset()) break;
		}

		if (rangeToken == null || rangeStart == -1 || rangeLength <=0 ||
				!IJavaPartitions.JAVA_STRING.equals(rangeToken.getData()))
			return null;

		// OK. We've found JAVA_STRING token  
		// Check that the position is in the EL 
		if (!checkStartPosition(document, region.getOffset()))
			return null;
		
		// Calculate and prepare KB-query parameters
		String text = null;
		try {
			text = document.get(rangeStart, rangeLength);
		} catch (BadLocationException e) {
			JspEditorPlugin.getPluginLog().logError(e);
		}
		int inValueOffset = region.getOffset() - rangeStart;
	
		ELParser p = ELParserUtil.getJbossFactory().createParser();
		ELModel model = p.parse(text);
		
		ELInvocationExpression ie = ELUtil.findExpression(model, inValueOffset);// ELExpression
		if (ie == null) 
			return null;

		String query = "#{" + ie.getText(); //$NON-NLS-1$
		
		KbQuery kbQuery = Utils.createKbQuery(Type.ATTRIBUTE_VALUE, region.getOffset() + region.getLength(), 
				query, query, "", "", null, null, false); //$NON-NLS-1$ //$NON-NLS-2$

		ITypeRoot input= getEditorInputJavaElement();
		if (input == null)
			return null;
		
		IFile file = null;
		
		try {
			IResource resource = input.getCorrespondingResource();
			if (resource instanceof IFile)
				file = (IFile) resource;
		} catch (JavaModelException e) {
			// Ignore. It is probably because of Java element's resource is not found 
		}

		ELContext context = PageContextFactory.createPageContext(file, JavaCore.JAVA_SOURCE_CONTENT_TYPE);
		
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(kbQuery, context);
		if (proposals == null)
			return null;
		
		for(TextProposal proposal : proposals) {
			String label = proposal == null ? null : proposal.getLabel();
			label = (label == null || label.indexOf(':') == -1) ? label : label.substring(0, label.indexOf(':')).trim(); 
			if (label != null && query.endsWith(label) && 
					proposal != null && proposal.getContextInfo() != null &&
					proposal.getContextInfo().trim().length() > 0) {
				return proposal.getContextInfo();
			}
		}

		return null;
	}

	/*
	 * Checks if the EL start starting characters are present
	 * @param viewer
	 * @param offset
	 * @return
	 * @throws BadLocationException
	 */
	private boolean checkStartPosition(IDocument document, int offset) {
		try {
			while (--offset >= 0) {
				if ('}' == document.getChar(offset))
					return false;


				if ('"' == document.getChar(offset) &&
						(offset - 1) >= 0 && '\\' != document.getChar(offset - 1)) {
					return false;
				}


				if ('{' == document.getChar(offset) &&
						(offset - 1) >= 0 && 
						('#' == document.getChar(offset - 1) || 
								'$' == document.getChar(offset - 1))) {
					return true;
				}
			}
		} catch (BadLocationException e) {
			JspEditorPlugin.getPluginLog().logError(e);
		}
		return false;
	}
	
	/*
	 * @see org.eclipse.jface.text.ITextHoverExtension#getHoverControlCreator()
	 * @since 3.0
	 */
	public IInformationControlCreator getHoverControlCreator() {
		return new IInformationControlCreator() {
			public IInformationControl createInformationControl(Shell parent) {
				return new DefaultInformationControl(parent, new HTMLTextPresenter(true));
			}
		};
	}

	/*
	 * @see org.eclipse.jface.text.ITextHoverExtension2#getInformationPresenterControlCreator()
	 * @since 3.0
	 */
	public IInformationControlCreator getInformationPresenterControlCreator() {
		return new IInformationControlCreator() {
			public IInformationControl createInformationControl(Shell parent) {
				return new DefaultInformationControl(parent, new HTMLTextPresenter(true));
			}
		};
	}
}
