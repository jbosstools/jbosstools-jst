package org.jboss.tools.jst.jsp;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.eclipse.wst.html.ui.StructuredTextViewerConfigurationHTML;
import org.eclipse.wst.sse.ui.StructuredTextViewerConfiguration;
import org.jboss.tools.common.text.ext.hyperlink.HyperlinkDetector;
import org.jboss.tools.common.text.xml.contentassist.SortingCompoundContentAssistProcessor;

public class TextViewerConfigurationDelegate {
	
	ITextViewerConfiguration target; 
	
	public TextViewerConfigurationDelegate(ITextViewerConfiguration config) {
		this.target = config;
	}

    protected IContentAssistProcessor[] getContentAssistProcessors(ISourceViewer sourceViewer, String partitionType) {
		SortingCompoundContentAssistProcessor sortingCompoundProcessor = new SortingCompoundContentAssistProcessor(sourceViewer, partitionType);
		
		if (sortingCompoundProcessor.supportsPartitionType(partitionType)) {
			// Add the default WTP CA processors to our SortingCompoundContentAssistProcessor
			IContentAssistProcessor[] superProcessors = target.getContentAssistProcessorsForPartitionType(sourceViewer, partitionType);

			if (superProcessors != null && superProcessors.length > 0) {
				for (int i = 0; i < superProcessors.length; i++)
					sortingCompoundProcessor.addContentAssistProcessor(partitionType, superProcessors[i]);
			}

			return new IContentAssistProcessor[] {sortingCompoundProcessor};
		}

		
		return new IContentAssistProcessor[0];
	}	
	
	/*
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getHyperlinkDetectors(org.eclipse.jface.text.source.ISourceViewer)
	 * @since 3.1
	 */
	public IHyperlinkDetector[] getHyperlinkDetectors(ISourceViewer sourceViewer, boolean enabled) {
		IHyperlinkDetector[] result = ITextViewerConfiguration.NO_CA_DETECTORS;
		if (enabled) {
			List<IHyperlinkDetector> allDetectors = new ArrayList<IHyperlinkDetector>(0);
	
			IHyperlinkDetector extHyperlinkDetector = HyperlinkDetector.getInstance(); 
	
			if (extHyperlinkDetector != null) allDetectors.add(extHyperlinkDetector);
	
	/*		IHyperlinkDetector[] superDetectors = super.getHyperlinkDetectors(sourceViewer);
			for (int m = 0; m < superDetectors.length; m++) {
				IHyperlinkDetector detector = superDetectors[m];
				if (!allDetectors.contains(detector)) {
					allDetectors.add(detector);
				}
			}
	*/
			result = allDetectors.toArray(new IHyperlinkDetector[0]);
		}
		return result;
	}
    
}
