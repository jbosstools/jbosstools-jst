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
package org.jboss.tools.jst.jsp.editor;

import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.w3c.dom.Node;


public interface IVisualController {
	public IStructuredModel getModel();
	public void drop(Node node, Node parentNode, int offset);
	public IVisualContext getPageContext();
	public void postLongOperation();
	public void preLongOperation();
	public void selectionChanged(SelectionChangedEvent event);
	public void refreshExternalLinks();
	public void visualRefresh();
}
