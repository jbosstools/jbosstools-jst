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
package org.jboss.tools.jst.web.ui.palette.internal;

import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileVersion;
import org.jboss.tools.jst.web.kb.taglib.IHTMLLibraryVersion;

/**
 * @author Alexey Kazakov
 */
public class JQMPaletteTagLib14 extends JQMPaletteTagLib13 {

	@Override
	protected IHTMLLibraryVersion getJQMVersion() {
		return JQueryMobileVersion.JQM_1_4;
	}
}