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
package org.jboss.tools.jst.web.tiles.model.handlers;

import java.text.MessageFormat;
import java.util.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.files.handlers.CreateFileSupport;
import org.jboss.tools.common.model.undo.*;
import org.jboss.tools.jst.web.tiles.Messages;
import org.jboss.tools.jst.web.tiles.model.TilesConstants;
import org.jboss.tools.jst.web.tiles.model.TilesProcessImpl;
import org.jboss.tools.jst.web.tiles.model.helpers.TilesRegistrationHelper;

public class CreateTilesSupport extends CreateFileSupport {
	static String REGISTER = "register"; //$NON-NLS-1$

	public void reset() {
		super.reset();
		setAttributeValue(0, "register", (canRegisterInternal()) ? "yes" : "no"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	protected void execute() throws XModelException {
		Properties p0 = extractStepData(0);
		XUndoManager undo = getTarget().getModel().getUndoManager();
		XTransactionUndo u = new XTransactionUndo(MessageFormat.format(Messages.CreateTilesSupport_CreateTiles, getTarget().getAttributeValue("element type"), getTarget().getPresentationString()), XTransactionUndo.ADD); //$NON-NLS-1$
		undo.addUndoable(u);
		try {
			doExecute(p0);
		} catch (RuntimeException e) {
			undo.rollbackTransactionInProgress();
			throw e;
		} finally {
			u.commit();
		}
	}
	
	private void doExecute(Properties p0) throws XModelException {
		Properties p = extractStepData(0);
		String path = p.getProperty("name"); //$NON-NLS-1$
		path = revalidatePath(path);
		XModelObject file = createFile(path);
		if(file == null) return;
		
		if(canRegisterInternal() && "yes".equals(getAttributeValue(0, REGISTER))) { //$NON-NLS-1$
			registerInternal(file);
		}

		TilesProcessImpl process = (TilesProcessImpl)file.getChildByPath("process"); //$NON-NLS-1$
		process.firePrepared();

		open(file);	
	}

	protected XModelObject modifyCreatedObject(XModelObject o) {
		XModelObject d = o.getModel().createModelObject(TilesConstants.ENT_DEFINITION, null);
		d.setAttributeValue("name", o.getAttributeValue("name")); //$NON-NLS-1$ //$NON-NLS-2$
		o.addChild(d);
		return o;
	}

	private boolean canRegisterInternal() {
		return TilesRegistrationHelper.isEnabled(getTarget().getModel());
	}

    public boolean isFieldEditorEnabled(int stepId, String name, Properties values) {
    	if(REGISTER.equals(name)) return canRegisterInternal();
    	return true;
    }
    
    void registerInternal(XModelObject file) {
    	TilesRegistrationHelper.register(getTarget().getModel(), file);
    }
    
}
