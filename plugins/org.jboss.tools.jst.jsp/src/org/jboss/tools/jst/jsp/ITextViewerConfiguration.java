package org.jboss.tools.jst.jsp;

import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.source.ISourceViewer;

public interface ITextViewerConfiguration {
	static final IHyperlinkDetector[] NO_CA_DETECTORS = new IHyperlinkDetector[0];
	IContentAssistProcessor[] getContentAssistProcessorsForPartitionType(ISourceViewer sourceViewer, String partitionType);
	IHyperlinkDetector[] getHyperlinkDetectors(ISourceViewer sourceViewer);
}
