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
package org.jboss.tools.jst.web.tiles.model.handlers.refactoring;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.impl.FileAnyImpl;
import org.jboss.tools.common.model.filesystems.impl.FolderImpl;
import org.jboss.tools.jst.web.tiles.Messages;
import org.jboss.tools.jst.web.tiles.model.helpers.TilesRegistrationHelper;

public class RenameTilesRegistrationChange extends Change {
	XModelObject folder;
	XModelObject object;
	String newName;
	String oldPath;
	
	public RenameTilesRegistrationChange(XModelObject object, String oldName, String newName) {
		this.object = object;
		folder = object.getParent();
		this.newName = newName;
    	oldPath = ((FileAnyImpl)object).getAbsolutePath();
	}

	public String getName() {
		return Messages.RenameTilesRegistrationChange_UpdateRegistration;
	}

	public void initializeValidationData(IProgressMonitor pm) {}

	public RefactoringStatus isValid(IProgressMonitor pm) throws CoreException, OperationCanceledException {
		return null;
	}

	public Change perform(IProgressMonitor pm) throws CoreException {
		if(folder instanceof FolderImpl) ((FolderImpl)folder).update();
		TilesRegistrationHelper.update(object.getModel(), object, oldPath);
		return null;
	}

	public Object getModifiedElement() {
		return null;
	}

}
