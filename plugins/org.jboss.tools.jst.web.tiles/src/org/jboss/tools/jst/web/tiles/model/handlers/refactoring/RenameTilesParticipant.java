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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.*;
import org.eclipse.ltk.core.refactoring.*;
import org.eclipse.ltk.core.refactoring.participants.*;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.impl.FileAnyImpl;
import org.jboss.tools.common.model.util.*;
import org.jboss.tools.jst.web.tiles.model.helpers.TilesRegistrationHelper;

public class RenameTilesParticipant extends RenameParticipant {
	public static final String PARTICIPANT_NAME="tiles-RenameTilesParticipant"; //$NON-NLS-1$
	XModelObject object;

	protected boolean initialize(Object element) {
		if(!(element instanceof IFile)) return false;
		IFile f = (IFile)element;
		object = EclipseResourceUtil.getObjectByResource(f);
		if(object == null) return false;
		String entity = object.getModelEntity().getName();
		if(!entity.startsWith("FileTiles")) return false; //$NON-NLS-1$
		return true;
	}

	public String getName() {
		return PARTICIPANT_NAME;
	}

	public RefactoringStatus checkConditions(IProgressMonitor pm, CheckConditionsContext context) throws OperationCanceledException {
		return null;
	}

	public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
		if (!pm.isCanceled()) {
			if(!TilesRegistrationHelper.isRegistered(object.getModel(), object)) return null;
			String newName = getArguments().getNewName();
			if(newName == null || newName.trim().length() == 0) return null;
			String oldName = FileAnyImpl.toFileName(object);
			RenameTilesRegistrationChange change = new RenameTilesRegistrationChange(object, oldName, newName);
			return change;
		}
		return null;
	}

}
