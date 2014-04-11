/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.test;

import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.jsp.test.ca.HTML5Test;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageProcessor;

/**
 * @author Alexey Kazakov
 */
public class FileNamesCATest extends HTML5Test {

	private static final String TAGLIB_URI = "htmlFile";

	@Override
	protected String getFilePath() {
		return "WebContent/pages/filenamesCA.html";
	}

	@Override
	protected String getTaglibUri() {
		return TAGLIB_URI;
	}

	public void testScriptFullPaths() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("script")}, "src", "../resources/app");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, "../resources/app.js", "../resources/app-min.js");

		query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("script")}, "src", "../resources/app-");
		proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, "../resources/app-min.js");
	}

	public void testScriptFileNames() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("script")}, "src", "app");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, "../resources/app.js", "../resources/app-min.js");

		query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("script")}, "src", "app-");
		proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, "../resources/app-min.js");
	}

	public void testLink() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("link", "rel", "stylesheet")}, "href", "app");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, "../resources/app.css");

		query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("link", "rel", "stylesheet")}, "href", "test");
		proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, "../resources/test.css");
	}

	public void testImage() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("img")}, "src", "image.");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, "../resources/image.png");
	}

	public void testVideo() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("video")}, "src", "video.");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, "../resources/video.mp4");
	}

	public void testAudio() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("audio")}, "src", "audio.");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, "../resources/audio.mp3");
	}

	public void testSource() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("video"), createTag("source")}, "src", "video.");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, "../resources/video.mp4");

		query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("audio"), createTag("source")}, "src", "video.");
		proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, "../resources/video.mp3");
	}

	public void testA() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("a")}, "href", "../resources/test");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, "../resources/test.html");
	}
}