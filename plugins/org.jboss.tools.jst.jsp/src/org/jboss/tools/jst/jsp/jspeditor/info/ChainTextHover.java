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
package org.jboss.tools.jst.jsp.jspeditor.info;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.wst.sse.ui.internal.Logger;
import org.eclipse.wst.sse.ui.internal.taginfo.AnnotationHoverProcessor;
import org.eclipse.wst.sse.ui.internal.taginfo.DebugInfoHoverProcessor;
import org.eclipse.wst.sse.ui.internal.taginfo.ProblemAnnotationHoverProcessor;


/**
 * Provides the best hover help documentation (by using other hover help
 * processors) Priority of hover help processors is: ProblemHoverProcessor,
 * FaceletTagInfoProcessor, TagInfoProcessor, AnnotationHoverProcessor
 * 
 * The processors are acquired in order of their priorities. If a hover doesn'n returns an information 
 * (i.e. returns null as a display string) the next processor will be acquired.
 *
 * @author Victor Rubezhny
 *
 */
@SuppressWarnings("restriction")
public class ChainTextHover implements ITextHover, ITextHoverExtension {
	private ITextHover fBestMatchHover; // current best match text hover
	private ITextHover[] fTagInfoHovers; // documentation/information hover
	private List<ITextHover> fTextHovers; // list of text hovers to consider in best
	// match

	public ChainTextHover(ITextHover infoTagHover) {
		this(new ITextHover[]{infoTagHover});
	}

	public ChainTextHover(ITextHover[] infoTagHovers) {
		fTagInfoHovers = infoTagHovers;
	}

	/**
	 * Create a list of text hovers applicable to this best match hover
	 * processor
	 * 
	 * @return List of ITextHover - in abstract class this is empty list
	 */
	private List<ITextHover> createTextHoversList() {
		List<ITextHover> hoverList = new ArrayList<ITextHover>();
		// if currently debugging, then add the debug hover to the list of
		// best match
		if (Logger.isTracing(DebugInfoHoverProcessor.TRACEFILTER)) {
			hoverList.add(new DebugInfoHoverProcessor());
		}

		hoverList.add(new ProblemAnnotationHoverProcessor());

		if (fTagInfoHovers != null) {
			for (int i = 0; i < fTagInfoHovers.length; i++) {
				if (fTagInfoHovers[i] instanceof FaceletTagInfoHoverProcessor) {
					hoverList.add(fTagInfoHovers[i]);
				}
			}
			for (int i = 0; i < fTagInfoHovers.length; i++) {
				if (!(fTagInfoHovers[i] instanceof FaceletTagInfoHoverProcessor)) {
					hoverList.add(fTagInfoHovers[i]);
				}
			}
		}
		hoverList.add(new AnnotationHoverProcessor());
		return hoverList;
	}

	public IInformationControlCreator getHoverControlCreator() {
		IInformationControlCreator creator = null;

		if (fBestMatchHover instanceof ITextHoverExtension) {
			creator = ((ITextHoverExtension) fBestMatchHover).getHoverControlCreator();
		}
		return creator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.ITextHover#getHoverInfo(org.eclipse.jface.text.ITextViewer,
	 *      org.eclipse.jface.text.IRegion)
	 */
	public String getHoverInfo(ITextViewer viewer, IRegion hoverRegion) {
		String displayInfo = null;

		// already have a best match hover picked out from getHoverRegion call
		if (fBestMatchHover != null) {
			displayInfo = fBestMatchHover.getHoverInfo(viewer, hoverRegion);
		}
		// either had no best match hover or best match hover returned null
		if (displayInfo == null) {
			// go through list of text hovers and return first display string
			Iterator<ITextHover> i = getTextHovers().iterator();
			while ((i.hasNext()) && (displayInfo == null)) {
				ITextHover hover = (ITextHover) i.next();
				displayInfo = hover.getHoverInfo(viewer, hoverRegion);
			}
		}
		return displayInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.ITextHover#getHoverRegion(org.eclipse.jface.text.ITextViewer,
	 *      int)
	 */
	public IRegion getHoverRegion(ITextViewer viewer, int offset) {
		IRegion hoverRegion = null;

		// go through list of text hovers and return first hover region
		ITextHover hover = null;
		Iterator<ITextHover> i = getTextHovers().iterator();
		while ((i.hasNext()) && (hoverRegion == null)) {
			hover = i.next();
			hoverRegion = hover.getHoverRegion(viewer, offset);
		}

		// store the text hover processor that found region
		if (hoverRegion != null)
			fBestMatchHover = hover;
		else
			fBestMatchHover = null;

		return hoverRegion;
	}

	private List<ITextHover> getTextHovers() {
		if (fTextHovers == null) {
			fTextHovers = createTextHoversList();
		}
		return fTextHovers;
	}
}
