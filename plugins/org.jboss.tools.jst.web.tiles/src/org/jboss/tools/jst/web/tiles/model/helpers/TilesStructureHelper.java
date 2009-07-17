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
package org.jboss.tools.jst.web.tiles.model.helpers;

import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.markers.XMarkerManager;
import org.jboss.tools.jst.web.model.ReferenceObject;
import org.jboss.tools.jst.web.model.helpers.WebProcessStructureHelper;
import org.jboss.tools.jst.web.tiles.model.*;

public class TilesStructureHelper extends WebProcessStructureHelper implements TilesConstants {
	public static final TilesStructureHelper instance = new TilesStructureHelper(); 

    public TilesStructureHelper() {}
    
	public XModelObject getParentProcess(XModelObject element) {
		XModelObject p = element;
		while(p != null && p.getFileType() == XModelObject.NONE &&
			  !ENT_PROCESS.equals(p.getModelEntity().getName())) p = p.getParent();
		return p;
	}

	public XModelObject[] getItems(XModelObject process) {
		return process.getChildren(ENT_PROCESS_ITEM);
	}
	
	public XModelObject[] getOutputs(XModelObject item) {
		return item.getChildren(ENT_PROCESS_ITEM_OUTPUT);
	}

	public String getPath(XModelObject element) {
		return element.getAttributeValue(ATT_PATH);
	}

	public XModelObject getItemOutputTarget(XModelObject itemOutput) {
		return itemOutput.getParent().getParent().getChildByPath(itemOutput.getAttributeValue(ATT_TARGET));
	}
	
	public boolean isNotDefinedInThisFile(XModelObject item) {
		if(!(item instanceof TilesProcessItemImpl)) return false;
		return (((TilesProcessItemImpl)item).getReference() == null);
	}
	
	public XModelObject findItemInOtherFile(XModelObject item) {
		if(!isNotDefinedInThisFile(item)) return null;
		XModelObject d = (XModelObject)TilesDefinitionSet.getInstance(item.getModel()).getDefinitions().get(item.getAttributeValue(ATT_NAME));
		XModelObject p = (d == null) ? null : getProcess(d);
		return (p == null) ? null : p.getChildByPath(d.getPathPart());
	}
	
	public boolean isUnconfirmedItem(XModelObject item) {
		if(!(item instanceof TilesProcessItemImpl)) return false;
		if(getReference(item) != null) return false;
		return !"true".equals(item.getAttributeValue("confirmed")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public XModelObject getReference(XModelObject diagramObject) {
		if(diagramObject instanceof ReferenceObject) return ((ReferenceObject)diagramObject).getReference();
		return null; 
	}
	
	public boolean hasErrors(XModelObject diagramObject){
		if(diagramObject instanceof ReferenceObject) {
			XModelObject reference = ((ReferenceObject)diagramObject).getReference();
			return XMarkerManager.getInstance().hasErrors(reference);
		}
		return XMarkerManager.getInstance().hasErrors(diagramObject);
	}
	
	public boolean canMakeLink(XModelObject sourceItem, XModelObject targetItem) {
		if(sourceItem == null || !sourceItem.isObjectEditable()) return false;
		if(isNotDefinedInThisFile(sourceItem)) return false;
		
		//check loops;
		XModelObject object= targetItem;
		XModelObject[] outputs;
		while(object != null){
			if(sourceItem.equals(object)) return false;
			outputs = getOutputs(object);
			if(outputs.length > 0)object = getItemOutputTarget(outputs[0]);
			else break;
		}
		return true;
	}

	public void makeLink(XModelObject sourceItem, XModelObject targetItem) throws XModelException {
		XModelObject sourceReference = getReference(sourceItem);
		if(sourceReference == null) return;
		sourceReference.getModel().editObjectAttribute(sourceReference, ATT_EXTENDS, targetItem.getAttributeValue(ATT_NAME));
	}

}
