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

import java.util.List;

import org.jboss.tools.jst.web.tiles.model.helpers.TilesStructureHelper;

public interface ITilesModel extends ITilesElement {
	
	public TilesStructureHelper getHelper();

   public void updateLinks();

   public IDefinition getSelectedDefinition();
   public void  setSelectedDefinition(IDefinition definition);

   public ITilesElement findElement(String key);
   public IDefinition getDefinition(String name);
   public IDefinition getDefinition(Object source);
   public ITilesElementList getDefinitionList();
   
   public List<IDefinition> getVisibleDefinitionList();
   public void setHidden(IDefinition definition);
   public void setVisible(IDefinition definition);

   public void setData(Object object) throws Exception;
   
   public boolean isModified();
   public void setModified(boolean set);

   public void addTilesModelListener(ITilesModelListener listener);
   public void removeTilesModelListener(ITilesModelListener listener);

   public boolean isEditable();
   public ITilesOptions getOptions();
}

