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

import java.util.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.files.handlers.CreateFileSupport;
import org.jboss.tools.common.model.undo.*;
import org.jboss.tools.jst.web.tiles.model.TilesProcessImpl;
import org.jboss.tools.jst.web.tiles.model.helpers.TilesRegistrationHelper;

public class CreateTilesSupport extends CreateFileSupport {
	static String REGISTER = "register";

	public void reset() {
		super.reset();
		setAttributeValue(0, "register", (canRegisterInternal()) ? "yes" : "no");
	}
	
	protected void execute() throws Exception {
		Properties p0 = extractStepData(0);
		XUndoManager undo = getTarget().getModel().getUndoManager();
		XTransactionUndo u = new XTransactionUndo("Create tiles " + getTarget().getAttributeValue("element type")+" "+getTarget().getPresentationString(), XTransactionUndo.ADD);
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
	
	private void doExecute(Properties p0) throws Exception {
		Properties p = extractStepData(0);
		String path = p.getProperty("name");
		path = revalidatePath(path);
		XModelObject file = createFile(path);
		if(file == null) return;
		
		if(canRegisterInternal() && "yes".equals(getAttributeValue(0, REGISTER))) {
			registerInternal(file);
		}

		TilesProcessImpl process = (TilesProcessImpl)file.getChildByPath("process");
		process.firePrepared();

		open(file);	
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
