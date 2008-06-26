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
package org.jboss.tools.jst.web.tiles.ui.editor.model;

public interface ILink extends ITilesElement {

   public boolean setTarget();

   public IDefinition getFromDefinition();
   public IDefinition getToDefinition();

   public void addLinkListener(ILinkListener l);
   public void removeLinkListener(ILinkListener l);
}

