 /*******************************************************************************
  * Copyright (c) 2010 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/

package org.jboss.tools.jst.web.kb.internal;

/**
 * The interface is used by the nature which are require the KB Nature and Builder
 * to be installed due to the correct information to be provided for Content Assist 
 * and/or Validation facilities  
 * 
 * @author Jeremy
 *
 */
public interface IKBBuilderRequiredNature {
	boolean isKBBuilderRequired();
	String  getNatureDescription();
}
