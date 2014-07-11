/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.angularjs.internal.ionic.palette;

import java.util.Collection;

import org.eclipse.jface.resource.ImageDescriptor;
import org.jboss.tools.jst.angularjs.AngularJsPlugin;
import org.jboss.tools.jst.angularjs.internal.ionic.IonicRecognizer;
import org.jboss.tools.jst.angularjs.internal.palette.wizard.IonicConstants;
import org.jboss.tools.jst.angularjs.internal.palette.wizard.IonicVersion;
import org.jboss.tools.jst.web.kb.taglib.ITagLibRecognizer;
import org.jboss.tools.jst.web.ui.palette.internal.PaletteManager;
import org.jboss.tools.jst.web.ui.palette.internal.PaletteTagLibrary;
import org.jboss.tools.jst.web.ui.palette.internal.RunnablePaletteItem;

/**
 * @author Alexey Kazakov
 */
@SuppressWarnings("restriction")
public class IonicTagLib extends PaletteTagLibrary {

	private static final ImageDescriptor IMAGE = ImageDescriptor.createFromFile(AngularJsPlugin.class, "ionic.png");
	private final static int RELEVANCE = generateUniqueRelevance();

	public IonicTagLib() {
		super(null, "ionic", null, "ionicpalette", true);
		String version = IonicVersion.IONIC_1_0.toString();
		this.name = "Ionic " + version + " templates";
		setVersion(version);
	}

	@Override
	public ImageDescriptor getImage() {
		return IMAGE;
	}

	@Override
	public Collection<RunnablePaletteItem> getItems() {
		return PaletteManager.getInstance().getItems(IonicConstants.IONIC_CATEGORY, getVersion());
	}

	@Override
	public ITagLibRecognizer getTagLibRecognizer() {
		return new IonicRecognizer();
	}

	@Override
	protected int getRelevance() {
		return RELEVANCE;
	}
}