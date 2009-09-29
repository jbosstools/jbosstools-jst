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

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.formatter.MultiPassContentFormatter;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.eclipse.wst.html.core.text.IHTMLPartitions;
import org.eclipse.wst.html.ui.StructuredTextViewerConfigurationHTML;
import org.eclipse.wst.sse.ui.internal.format.StructuredFormattingStrategy;
import org.jboss.tools.common.text.xml.contentassist.SortingCompoundContentAssistProcessor;
import org.jboss.tools.jst.jsp.format.HTMLFormatProcessor;
import org.osgi.framework.Bundle;

public class HTMLTextViewerConfiguration extends StructuredTextViewerConfigurationHTML implements ITextViewerConfiguration{

	TextViewerConfigurationDelegate configurationDelegate;	
	public HTMLTextViewerConfiguration() {
		super();
		configurationDelegate = new TextViewerConfigurationDelegate(this);
	}

	@SuppressWarnings("restriction")
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
}