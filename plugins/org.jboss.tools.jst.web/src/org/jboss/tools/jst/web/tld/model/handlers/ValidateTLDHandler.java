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
package org.jboss.tools.jst.web.tld.model.handlers;

import java.util.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.meta.action.*;
import org.jboss.tools.common.meta.action.impl.*;

public class ValidateTLDHandler extends AbstractHandler {
	static SpecialWizard wizard = SpecialWizardFactory.createSpecialWizard("org.jboss.tools.common.editor.OpenEditorWizard"); //$NON-NLS-1$
	public ValidateTLDHandler() {}

	public boolean isEnabled(XModelObject object) {
		if(!"yes".equals(object.get("isIncorrect"))) return false; //$NON-NLS-1$ //$NON-NLS-2$
		String e = object.get("errors"); //$NON-NLS-1$
		return (e != null && e.length() > 0);
	}

	public void executeHandler(XModelObject object, Properties p) throws XModelException {
		if(p == null) p = new Properties();
		p.setProperty("toErrorTab", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		XActionInvoker.invoke("Open", object, p); //$NON-NLS-1$
	}
    
}
