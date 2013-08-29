/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal.taglib.html;

import org.jboss.tools.jst.web.kb.internal.taglib.AbstractAttributeProvider;
import org.jboss.tools.jst.web.kb.internal.taglib.CustomTagLibAttribute;

/**
 * @author Alexey Kazakov
 */
public abstract class DynamicAttributeValueProvider extends AbstractAttributeProvider {

	@Override
	protected boolean checkComponent() {
		return true;
	}

	@Override
	protected CustomTagLibAttribute[] getConditionalAttributes() {
		CustomTagLibAttribute attribute = getAttribute();
		if(attribute==null) {
			return new CustomTagLibAttribute[0];
		}
		CustomTagLibAttribute[] attributes = new CustomTagLibAttribute[]{attribute};
		return attributes;
	}

	abstract protected CustomTagLibAttribute getAttribute();
}