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

import java.util.Set;
import org.jboss.tools.jst.web.tiles.model.TilesProcessImpl;

public class TilesUpdateHelper implements ITilesDefinitionSetListener{
	TilesProcessImpl process;
	TilesProcessHelper helper;

	public TilesUpdateHelper(TilesProcessImpl process) {
		this.process = process;
		this.helper = process.getHelper();
		TilesUpdateManager.getInstance(process.getModel()).register("", this); //$NON-NLS-1$
		TilesDefinitionSet.getInstance(process.getModel()).addTilesDefinitionSetListener(this);
	}
	
	public void unregister() {
		TilesUpdateManager.getInstance(process.getModel()).unregister(this);
		TilesDefinitionSet.getInstance(process.getModel()).removeTilesDefinitionSetListener(this);
	}

	public void update() {
		helper.updateProcess();
	}

	public void definitionsChanged(Set removed, Set added) {
		if(helper.isDisposed()) {
			unregister();
		} else {
			helper.updateTargets();
		}
	}

}
