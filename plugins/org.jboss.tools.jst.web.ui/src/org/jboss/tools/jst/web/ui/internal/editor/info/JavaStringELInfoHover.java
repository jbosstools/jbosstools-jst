/******************************************************************************* 
 * Copyright (c) 2010-2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.internal.editor.info;

import java.util.Arrays;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.text.FastJavaPartitionScanner;
import org.eclipse.jdt.internal.ui.text.JavaWordFinder;
import org.eclipse.jdt.internal.ui.text.java.hover.JavadocHover;
import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.rules.IToken;
import org.jboss.tools.common.el.core.model.ELInvocationExpression;
import org.jboss.tools.common.el.core.model.ELModel;
import org.jboss.tools.common.el.core.model.ELUtil;
import org.jboss.tools.common.el.core.parser.ELParser;
import org.jboss.tools.common.el.core.parser.ELParserUtil;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.el.core.resolver.ELResolution;
import org.jboss.tools.common.el.core.resolver.ELResolver;
import org.jboss.tools.common.el.core.resolver.ELSegment;
import org.jboss.tools.common.el.core.resolver.JavaMemberELSegmentImpl;
import org.jboss.tools.common.el.ui.ca.ELProposalProcessor;
import org.jboss.tools.common.el.ui.internal.info.ELInfoHover;
import org.jboss.tools.jst.web.kb.PageContextFactory;
import org.jboss.tools.jst.web.kb.el.MessagePropertyELSegmentImpl;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

/**
 * 
 * @author Victor Rubezhny
 *
 */
@SuppressWarnings("restriction")
public class JavaStringELInfoHover extends JavadocHover {
	/*
	 * @see ITextHover#getHoverRegion(ITextViewer, int)
	 */
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		return JavaWordFinder.findWord(textViewer.getDocument(), offset);
	}

	/*
	 * @see org.eclipse.jface.text.ITextHoverExtension2#getHoverInfo2(org.eclipse.jface.text.ITextViewer, org.eclipse.jface.text.IRegion)
	 * @since 3.4
	 */
	public Object getHoverInfo2(ITextViewer textViewer, IRegion region) {
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
			WebUiPlugin.getDefault().logError(e);
		}
		int inValueOffset = region.getOffset() - rangeStart;
	
		ELParser p = ELParserUtil.getJbossFactory().createParser();
		ELModel model = p.parse(text);
		
		ELInvocationExpression ie = ELUtil.findExpression(model, inValueOffset);// ELExpression
		if (ie == null) 
			return null;

		ITypeRoot input= getEditorInputJavaElement();
		if (input == null)
			return null;
		
		IResource r = input.getResource();
		if(!(r instanceof IFile) || !r.exists() || r.getName().endsWith(".jar")) { //$NON-NLS-1$
			return null;
		}
		IFile file = (IFile)r;

		ELContext context = PageContextFactory.createPageContext(file, JavaCore.JAVA_SOURCE_CONTENT_TYPE);
		
		ELResolver[] resolvers =  context.getElResolvers();
		
		for (int i = 0; resolvers != null && i < resolvers.length; i++) {
			ELResolution resolution = resolvers[i] == null ? null : resolvers[i].resolve(context, ie, region.getOffset() + region.getLength());
			if (resolution == null)
				continue;
			
			ELSegment segment = resolution.getLastSegment();
			if (segment==null || !segment.isResolved()) continue;
			
			if(segment instanceof JavaMemberELSegmentImpl) {
				JavaMemberELSegmentImpl jmSegment = (JavaMemberELSegmentImpl)segment;
				
				IJavaElement[] javaElements = jmSegment.getAllJavaElements();
				if (javaElements == null || javaElements.length == 0) {
					if (jmSegment.getJavaElement() == null)
						continue;
					
					javaElements = new IJavaElement[] {jmSegment.getJavaElement()};
				}
				if (javaElements == null || javaElements.length == 0)
					continue;
				
				Arrays.sort(javaElements, ELProposalProcessor.CASE_INSENSITIVE_ORDER);
				return ELInfoHover.getHoverInfo(javaElements, null);
			} else if (segment instanceof MessagePropertyELSegmentImpl) {
				MessagePropertyELSegmentImpl mpSegment = (MessagePropertyELSegmentImpl)segment;
				return ELInfoHover.getHoverInfo(mpSegment.getBaseName(), mpSegment.getBaseName(),
						mpSegment.getObjects(), null);
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
			WebUiPlugin.getDefault().logError(e);
		}
		return false;
	}
}