/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *       Red Hat, Inc. - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.jst.js.bower.internal.launch.shortcut;

import org.jboss.tools.jst.js.bower.BowerCommands;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class BowerInstall extends GenericBowerLaunch {
	private static final String LAUNCH_NAME = "Bower Install"; //$NON-NLS-1$
	
	@Override
	protected String getLaunchName() {
		return LAUNCH_NAME;
	}
	
	@Override
	protected String getCommandName() {
		return BowerCommands.INSTALL.getValue();
	}

}
