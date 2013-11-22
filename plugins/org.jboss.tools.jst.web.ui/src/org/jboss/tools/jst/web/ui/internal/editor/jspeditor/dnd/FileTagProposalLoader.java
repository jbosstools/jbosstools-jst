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
package org.jboss.tools.jst.web.ui.internal.editor.jspeditor.dnd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IFileEditorInput;
import org.jboss.tools.common.model.ui.editors.dnd.AbsoluteFilePathAttributeValueLoader;
import org.jboss.tools.common.model.ui.editors.dnd.DropData;
import org.jboss.tools.common.model.ui.editors.dnd.DropURI;
import org.jboss.tools.common.model.ui.editors.dnd.IAttributeValueLoader;
import org.jboss.tools.common.model.ui.editors.dnd.IDropWizardModel;
import org.jboss.tools.common.model.ui.editors.dnd.ITagProposal;
import org.jboss.tools.common.model.ui.editors.dnd.ITagProposalLoader;
import org.jboss.tools.common.model.ui.editors.dnd.LoadBundleBaseNameAttributeValueLoader;
import org.jboss.tools.common.model.ui.editors.dnd.composite.TagAttributesComposite;
import org.jboss.tools.common.model.ui.editors.dnd.context.DropContext;

public class FileTagProposalLoader implements ITagProposalLoader {

	public static String FACELETS_URI = "http://www.w3.org/1999/xhtml/facelets"; //$NON-NLS-1$

	private static final Map<String,TagProposal[]> extensionMap = new HashMap<String,TagProposal[]>();
	
	private static Map<String,String[]> requiredAttributes = new HashMap<String, String[]>();

	static {
		requiredAttributes.put("h:graphicImage", new String[]{"value"});
		requiredAttributes.put("html:img", new String[]{"page"});
		requiredAttributes.put("s:graphicImage", new String[]{"url"});
		requiredAttributes.put("s:decorate", new String[]{"template"});
	}
	static TagProposal[] IMG_TAG_PROPOSALS = new TagProposal[]{
		new TagProposal(
			DropURI.JSF_HTML_URI,
			"h", //$NON-NLS-1$
			"graphicImage", //$NON-NLS-1$
			new AbsoluteFilePathAttributeValueLoader("value","","") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		),
		new TagProposal(
//			DropURI.HTML_4_0_URI,
			FACELETS_URI,
			ITagProposal.EMPTY_PREFIX,
			"img", //$NON-NLS-1$
			new AbsoluteFilePathAttributeValueLoader("src","","") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		),
		new TagProposal(
			DropURI.STRUTS_HTML_URI,
			"html", //$NON-NLS-1$
			"img", //$NON-NLS-1$
			new AbsoluteFilePathAttributeValueLoader("page","","") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		),
		new TagProposal(
			DropURI.SEAM_URI,
			"s", //$NON-NLS-1$
			"graphicImage", //$NON-NLS-1$
			new AbsoluteFilePathAttributeValueLoader("url","","") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		)
// yradtsevich: fix of JBIDE-3984: Exclude t:graphicImage option from Insert tag dialog 		
//		,
//		new TagProposal(
//			DropURI.TOMOHAWK_URI,
//			"t",
//			"graphicImage",
//			new AbsoluteFilePathAttributeValueLoader("url","","")
//		)
	};
	
	static TagProposal[] CSS_TAG_PROPOSALS = new TagProposal[]{
		new TagProposal(
//			DropURI.HTML_4_0_URI,
			FACELETS_URI,
			ITagProposal.EMPTY_PREFIX,
			"link", //$NON-NLS-1$
			new CssLinkAttributeValueLoader("href") //$NON-NLS-1$
		),
	};
	
	static TagProposal[] JS_TAG_PROPOSALS = new TagProposal[]{
		new TagProposal(
			FACELETS_URI,
			ITagProposal.EMPTY_PREFIX,
			"script", //$NON-NLS-1$
			new JsLinkAttributeValueLoader("src") //$NON-NLS-1$
		),
	};
	
	static TagProposal JSP_INCLUDE = new TagProposal(
		DropURI.JSP_URI,
		"jsp", //$NON-NLS-1$
		"include", //$NON-NLS-1$
		new AbsoluteFilePathAttributeValueLoader("page","","")						 //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	);
	
	static TagProposal JSP_FORWARD = new TagProposal(
		DropURI.JSP_URI,
		"jsp", //$NON-NLS-1$
		"forward", //$NON-NLS-1$
		new AbsoluteFilePathAttributeValueLoader("page","","") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	);
	
	static TagProposal UI_INCLUDE = new TagProposal(
		PaletteTaglibInserter.faceletUri,
		"ui", //$NON-NLS-1$
		"include", //$NON-NLS-1$
		new AbsoluteFilePathAttributeValueLoader("src","","")						 //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	);
	
	static TagProposal S_DECORATE = new TagProposal(
		DropURI.SEAM_URI,
		"s", //$NON-NLS-1$
		"decorate", //$NON-NLS-1$
		new AbsoluteFilePathAttributeValueLoader("template","","")						 //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	);
		
	static TagProposal[] PAGE_TAG_PROPOSALS = new TagProposal[]{
		JSP_INCLUDE,
		JSP_FORWARD
	};
	
	static TagProposal[] XHTML_PAGE_TAG_PROPOSALS = new TagProposal[]{
		JSP_INCLUDE,
		JSP_FORWARD,
		UI_INCLUDE,
		S_DECORATE
	};
	
	static {
		// There is the question here what store HTML or TLD will been asked about TagDescription    
		extensionMap.put("jpg", IMG_TAG_PROPOSALS); //$NON-NLS-1$

		extensionMap.put("jpeg", IMG_TAG_PROPOSALS); //$NON-NLS-1$

		extensionMap.put("gif",IMG_TAG_PROPOSALS); //$NON-NLS-1$
		
		extensionMap.put("bmp",IMG_TAG_PROPOSALS); //$NON-NLS-1$
		
		extensionMap.put("png",IMG_TAG_PROPOSALS); //$NON-NLS-1$
		
		extensionMap.put("jsp",PAGE_TAG_PROPOSALS); //$NON-NLS-1$
		extensionMap.put("html",PAGE_TAG_PROPOSALS); //$NON-NLS-1$
		extensionMap.put("xhtml",XHTML_PAGE_TAG_PROPOSALS); //$NON-NLS-1$

		extensionMap.put(
				"properties",new TagProposal[]{ //$NON-NLS-1$
					new TagProposal(
							DropURI.JSF_CORE_URI,
							"f", //$NON-NLS-1$
							"loadBundle", //$NON-NLS-1$
							new LoadBundleBaseNameAttributeValueLoader()
						)
					}
				);
		extensionMap.put("css",CSS_TAG_PROPOSALS); //$NON-NLS-1$
		extensionMap.put("js",JS_TAG_PROPOSALS); //$NON-NLS-1$
		extensionMap.put(
			"inc", new TagProposal[]{ //$NON-NLS-1$
				new TagProposal(
					DropURI.JSP_URI,
					"jsp", //$NON-NLS-1$
					"include", //$NON-NLS-1$
					new AbsoluteFilePathAttributeValueLoader("page","","")						 //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				)
			}
		);

		DropContext.mappedExtensions.addAll(extensionMap.keySet());
	}
	
	public static boolean isExtensionMapped(String extension) {
		return extension != null && extensionMap.containsKey(extension.toLowerCase());
	}
	
	public TagProposal[] getTagProposals(Object data) {
		DropData dropData = (DropData)data;
		String fileName = dropData.getMimeData();
		String extension = fileName.substring(fileName.lastIndexOf(".")+1); //$NON-NLS-1$
		TagProposal[] tagProposals = (TagProposal[])extensionMap.get(extension.toLowerCase());
		if(tagProposals==null) {
			tagProposals = new TagProposal[0];
		}
		if(dropData.getEditorInput() instanceof IFileEditorInput) {
			IFile f = ((IFileEditorInput)dropData.getEditorInput()).getFile();
			List<TagProposal> result = new ArrayList<TagProposal>();
			for (int i = 0; i < tagProposals.length; i++) {
				dropData.getValueProvider().setProposal(tagProposals[i], true);
				TagAttributesComposite.AttributeDescriptorValue[] values = dropData.getValueProvider().getValues();
				if(values == null || values.length == 0) continue;
				Set<String> as = new HashSet<String>();
				for (int k = 0; k < values.length; k++) {
					as.add(values[k].getName());
				}
				String tag = tagProposals[i].getPrefix() + ":" + tagProposals[i].getName();
				String[] atrs = requiredAttributes.get(tag);
				boolean ok = true;
				if(atrs != null) for (int k = 0; k < atrs.length && ok; k++) {
					if(!as.contains(atrs[k])) ok = false;
				}
				if(!ok) continue;
				result.add(tagProposals[i]);
			}
			
			return result.toArray(new TagProposal[0]);
		}
		return tagProposals;
	}
	
	public boolean isTagProposalExists(Object data) {
		return true;
	}

	public static class ImageFileAttributesValuesLoader implements IAttributeValueLoader {

		public void fillTagAttributes(IDropWizardModel model) {
		}
		
	}
	
	public static class JspFileAttributesValuesLoader implements IAttributeValueLoader {

		public void fillTagAttributes(IDropWizardModel model) {
		}
	}
}