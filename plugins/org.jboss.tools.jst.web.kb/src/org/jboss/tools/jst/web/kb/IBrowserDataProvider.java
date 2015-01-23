/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.kb;

/**
 * @author Alexey Kazakov
 */
public interface IBrowserDataProvider {

	/**
	 * Evaluates a script containing javascript commands in the context of the current document of the browser.
	 * @param js
	 * @param context
	 * @return the return value of executing the script
	 */
	Object evaluate(String js, IPageContext context);
}