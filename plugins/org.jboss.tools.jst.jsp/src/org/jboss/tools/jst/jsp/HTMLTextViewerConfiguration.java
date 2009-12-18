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
package org.jboss.tools.jst.jsp;

import java.util.List;

import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.formatter.MultiPassContentFormatter;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.information.IInformationProvider;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.eclipse.wst.html.core.text.IHTMLPartitions;
import org.eclipse.wst.html.ui.StructuredTextViewerConfigurationHTML;
import org.eclipse.wst.sse.ui.internal.ExtendedConfigurationBuilder;
import org.eclipse.wst.sse.ui.internal.SSEUIPlugin;
import org.eclipse.wst.sse.ui.internal.format.StructuredFormattingStrategy;
import org.eclipse.wst.sse.ui.internal.taginfo.AnnotationHoverProcessor;
import org.eclipse.wst.sse.ui.internal.taginfo.ProblemAnnotationHoverProcessor;
import org.eclipse.wst.sse.ui.internal.taginfo.TextHoverManager;
import org.jboss.tools.jst.jsp.format.HTMLFormatProcessor;
import org.jboss.tools.jst.jsp.jspeditor.info.ChainTextHover;

@SuppressWarnings("restriction")
public class HTMLTextViewerConfiguration extends StructuredTextViewerConfigurationHTML implements ITextViewerConfiguration{

	TextViewerConfigurationDelegate configurationDelegate;	
	public HTMLTextViewerConfiguration() {
		super();
		configurationDelegate = new TextViewerConfigurationDelegate(this);
	}

    protected IContentAssistProcessor[] getContentAssistProcessors(ISourceViewer sourceViewer, String partitionType) {
		return configurationDelegate.getContentAssistProcessors(sourceViewer, partitionType);
	}

	/*
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getHyperlinkDetectors(org.eclipse.jface.text.source.ISourceViewer)
	 * @since 3.1
	 */
	public IHyperlinkDetector[] getHyperlinkDetectors(ISourceViewer sourceViewer) {
			return configurationDelegate.getHyperlinkDetectors(
					sourceViewer,
					fPreferenceStore.getBoolean(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_HYPERLINKS_ENABLED));
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.html.ui.StructuredTextViewerConfigurationHTML#getContentFormatter(org.eclipse.jface.text.source.ISourceViewer)
	 */
	public IContentFormatter getContentFormatter(ISourceViewer sourceViewer) {
		MultiPassContentFormatter formatter = new MultiPassContentFormatter(getConfiguredDocumentPartitioning(sourceViewer), IHTMLPartitions.HTML_DEFAULT);
		formatter.setMasterStrategy(new StructuredFormattingStrategy(new HTMLFormatProcessor()));
		return formatter;
	}

	public IContentAssistProcessor[] getContentAssistProcessorsForPartitionType(
			ISourceViewer sourceViewer, String partitionType) {
		return super.getContentAssistProcessors(sourceViewer, partitionType);
	}

	/**
	 * Create documentation hovers based on hovers contributed via
	 * <code>org.eclipse.wst.sse.ui.editorConfiguration</code> extension
	 * point
	 * 
	 * Copied from {@link org.eclipse.wst.sse.ui.StructuredTextViewerConfiguration} because of private modifier
	 * 
	 * @param partitionType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ITextHover[] createDocumentationHovers(String partitionType) {
		List extendedTextHover = ExtendedConfigurationBuilder.getInstance().getConfigurations(ExtendedConfigurationBuilder.DOCUMENTATIONTEXTHOVER, partitionType);
		ITextHover[] hovers = (ITextHover[]) extendedTextHover.toArray(new ITextHover[extendedTextHover.size()]);
		return hovers;
	}

	@Override
	protected IInformationProvider getInformationProvider(
			ISourceViewer sourceViewer, String partitionType) {
		
		ITextHover chainTextHover = new ChainTextHover(createDocumentationHovers(partitionType));
		return new TextHoverInformationProvider(chainTextHover);
	}

	@Override
	public ITextHover getTextHover(ISourceViewer sourceViewer,
			String contentType, int stateMask) {
		ITextHover textHover = null;

		/*
		 * Returns a default problem, annotation, and best match hover
		 * depending on stateMask
		 */
		TextHoverManager.TextHoverDescriptor[] hoverDescs = SSEUIPlugin.getDefault().getTextHoverManager().getTextHovers();
		int i = 0;
		while (i < hoverDescs.length && textHover == null) {
			if (hoverDescs[i].isEnabled() && computeStateMask(hoverDescs[i].getModifierString()) == stateMask) {
				String hoverType = hoverDescs[i].getId();
				if (TextHoverManager.PROBLEM_HOVER.equalsIgnoreCase(hoverType))
					textHover = new ProblemAnnotationHoverProcessor();
				else if (TextHoverManager.ANNOTATION_HOVER.equalsIgnoreCase(hoverType))
					textHover = new AnnotationHoverProcessor();
				else if (TextHoverManager.COMBINATION_HOVER.equalsIgnoreCase(hoverType))
					textHover = new ChainTextHover(createDocumentationHovers(contentType));
				else if (TextHoverManager.DOCUMENTATION_HOVER.equalsIgnoreCase(hoverType)) {
					ITextHover[] hovers = createDocumentationHovers(contentType);
					if (hovers.length > 0) {
						textHover = hovers[0];
					}
				}
			}
			i++;
		}
		return textHover;
	}
	
	
}