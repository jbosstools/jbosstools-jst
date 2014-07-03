/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *       Red Hat, Inc. - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.jst.jsdt.utils;

import org.eclipse.ui.IStartup;

/**
 * 
 * @author Victor V. Rubezhny
 *
 */
public class JstJsdtStartUp implements IStartup {

	@Override
	public void earlyStartup() {
		// Empty, but exists here to make sure that early start up takes place
	}
}
