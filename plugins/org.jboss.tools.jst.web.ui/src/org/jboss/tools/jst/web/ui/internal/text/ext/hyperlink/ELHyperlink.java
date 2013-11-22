/*******************************************************************************
 * Copyright (c) 2011-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.internal.text.ext.hyperlink;

import java.text.MessageFormat;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.jboss.tools.common.el.core.ELReference;
import org.jboss.tools.common.el.core.resolver.ELSegment;
import org.jboss.tools.common.el.core.resolver.IOpenableReference;
import org.jboss.tools.common.el.core.resolver.JavaMemberELSegment;
import org.jboss.tools.common.el.core.resolver.MessagePropertyELSegment;
import org.jboss.tools.common.text.ITextSourceReference;
import org.jboss.tools.common.text.ext.hyperlink.AbstractHyperlink;
import org.jboss.tools.common.text.ext.hyperlink.HyperlinkRegion;
import org.jboss.tools.common.text.ext.hyperlink.xpl.Messages;
import org.jboss.tools.common.text.ext.util.StructuredSelectionHelper;
import org.jboss.tools.common.util.StringUtil;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

public class ELHyperlink extends AbstractHyperlink{
	private ELReference reference;
	private ELSegment segment;
	IOpenableReference openable;
	
	public ELHyperlink(IDocument document, ELReference reference, ELSegment segment, IOpenableReference openable) {
		this.reference = reference;
		this.segment = segment;
		this.openable = openable;
		setDocument(document);
	}

	/**
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.text.ext.hyperlink.AbstractHyperlink#doGetHyperlinkRegion(int)
	 */ 
	@Override
	protected IRegion doGetHyperlinkRegion(int offset) {
		IRegion region = super.doGetHyperlinkRegion(offset);
		return (region != null ? region : new HyperlinkRegion(
					reference.getStartPosition()+segment.getSourceReference().getStartPosition(),
					segment.getSourceReference().getLength()));
	}

	@Override
	protected void doHyperlink(IRegion region) {
		
		if(openable != null) {
			if(!openable.open()) {
				openFileFailed();
			}
			//If openables.length > 1 - show menu.
			return;
		}
		
		// Open by ITextSourceReference
		ITextSourceReference ref = segment.getSourceReference();
		if (ref != null) {
			IResource r = ref.getResource();
			IFile file = r instanceof IFile ? (IFile)r : null;
			if (file == null || !file.exists() || !file.isAccessible()) {
				openFileFailed();
				return;
			}
			if (openFileInEditor(file) != null && ref.getStartPosition() >=0 && ref.getLength() > 0) {
				StructuredSelectionHelper.setSelectionAndRevealInActiveEditor(new Region(ref.getStartPosition(), ref.getLength()));
				return;
			}
		}
	}
	
	@Override
	public String getHyperlinkText() {
		if(openable != null) {
			return openable.getLabel();
		}
		if(segment instanceof JavaMemberELSegment){
			return "Should not get here."; //$NON-NLS-1$
		}else if(segment instanceof MessagePropertyELSegment){
			//TODO move to getLabel() in openable in MessagePropertyELSegmentImpl
			String baseName = ((MessagePropertyELSegment)segment).getBaseName();
			String propertyName = ((MessagePropertyELSegment)segment).isBundle() ? null : StringUtil.trimQuotes(((MessagePropertyELSegment)segment).getToken().getText());
			if (propertyName == null)
				return  MessageFormat.format(Messages.Open, baseName);
			
			return MessageFormat.format(Messages.OpenBundleProperty, propertyName, baseName);
		}
		
		return ""; //$NON-NLS-1$
	}

	@Override
	public IFile getReadyToOpenFile() {
		IFile file = null;
		if(segment instanceof JavaMemberELSegment){
			
			try {
				file = (IFile)((JavaMemberELSegment) segment).getJavaElement().getUnderlyingResource();
			} catch (JavaModelException e) {
				WebUiPlugin.getDefault().logError(e);
			}
		}else if(segment instanceof MessagePropertyELSegment){
			file = (IFile)((MessagePropertyELSegment)segment).getMessageBundleResource();
		}
		return file;
	}
}
