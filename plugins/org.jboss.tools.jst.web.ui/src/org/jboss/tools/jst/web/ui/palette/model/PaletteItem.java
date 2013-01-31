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
package org.jboss.tools.jst.web.ui.palette.model;

import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.ToolEntry;

import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.ui.image.XModelObjectImageDescriptor;

public class PaletteItem extends ToolEntry implements PaletteXModelObject {
	private XModelObject xobject;
	private String description;
	private String startText;
	private String endText;
	private boolean reformat;

	public PaletteItem(XModelObject xobject) {
		super(null, null, null, null);
		setXModelObject(xobject);
	}

	public XModelObject getXModelObject() {
		return xobject;
	}
	
	public void setXModelObject(XModelObject xobject) {
		this.xobject = xobject;
		setLabel("" + xobject.getModelEntity().getRenderer().getTitle(xobject)); //$NON-NLS-1$
		XModelObjectImageDescriptor icon = new XModelObjectImageDescriptor(xobject);
		setSmallIcon(icon);
		setLargeIcon(new LargeImageDescriptor(xobject));
		String description = xobject.getAttributeValue("description"); //$NON-NLS-1$
			///XModelObjectLoaderUtil.loadFromXMLAttribute(xobject.getAttributeValue("description"));
		this.description = (description != null && description.trim().length() > 0) ? description : null;
		startText = "" + xobject.getAttributeValue("start text"); //$NON-NLS-1$ //$NON-NLS-2$
		endText = "" + xobject.getAttributeValue("end text"); //$NON-NLS-1$ //$NON-NLS-2$
		reformat = "yes".equals("" + xobject.getAttributeValue("automatically reformat tag body")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	public String getHtmlDescription() {
		return description;
	}

	public String getStartText() {
		return startText;
	}

	public String getEndText() {
		return endText;
	}
	
	public boolean getReformat() {
		return reformat;
	}

	public Tool createTool() {
		return null;
	}
}
