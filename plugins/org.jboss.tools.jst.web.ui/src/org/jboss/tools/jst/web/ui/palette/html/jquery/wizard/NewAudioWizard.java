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
package org.jboss.tools.jst.web.ui.palette.html.jquery.wizard;

import org.jboss.tools.common.model.ui.editors.dnd.DropWizardMessages;
import org.jboss.tools.jst.web.ui.JSTWebUIImages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewAudioWizard extends NewJQueryWidgetWizard<NewAudioWizardPage> implements JQueryConstants {
	static String prefixId = "audio-";

	public NewAudioWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.AUDIO_IMAGE));
	}

	protected NewAudioWizardPage createPage() {
		return new NewAudioWizardPage();
	}

	protected void addContent(ElementNode parent) {
		addContent(parent, false);
	}

	protected void addContent(ElementNode parent, boolean forBrowser) {
		ElementNode video = parent.addChild(TAG_AUDIO, null);
		addID(prefixId, video);
		
		for (int i = 0; i < page.items.getNumber(); i++) {
			String src = page.items.getSrc(i);
			if(forBrowser) {
				src = SRCUtil.getAbsoluteSrc(getFile(), src);
			}
			ElementNode source = video.addChild(TAG_SOURCE, null);
			source.addAttribute(ATTR_SRC, src);
			String type = page.items.getType(i);
			if(type.length() > 0) {
				source.addAttribute(ATTR_TYPE, type);
			}
		}
		
		if(isTrue(EDITOR_ID_AUTOPLAY)) {
			video.addAttribute(ATTR_AUTOPLAY, ATTR_AUTOPLAY);
		} else if(!AUTO.equals(page.getEditorValue(EDITOR_ID_PRELOAD))) {
			addAttributeIfNotEmpty(video, ATTR_PRELOAD, EDITOR_ID_PRELOAD);
		}
		if(isTrue(EDITOR_ID_CONTROLS)) {
			video.addAttribute(ATTR_CONTROLS, ATTR_CONTROLS);
		}
		if(isTrue(EDITOR_ID_LOOP)) {
			video.addAttribute(ATTR_LOOP, ATTR_LOOP);
		}
		if(isTrue(EDITOR_ID_MUTED)) {
			video.addAttribute(ATTR_MUTED, ATTR_MUTED);
		}
	}

	public String getTextForBrowser() {
		ElementNode html = new ElementNode(TAG_HTML, false);
		//no head
		ElementNode body = html.addChild(TAG_BODY);
		createBodyForBrowser(body);

		NodeWriter w = new NodeWriter(false);
		html.flush(w, 0);

		StringBuilder sb = new StringBuilder();
		sb.append(DOCTYPE).append("\n").append(w.getText());

		return sb.toString();
	}

	protected void createBodyForBrowser(ElementNode body) {
//		body = getPageContentNode(body);
		ElementNode div = body.addChild(TAG_DIV);
		div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
		div.addAttribute("align", "center");
		addContent(div, true);
	}

}
