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
import org.jboss.tools.common.meta.action.impl.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.jst.web.tld.model.TLDValidatorImpl;

public class DeleteValidatorHandler extends AbstractHandler {

	public void executeHandler(XModelObject object, Properties p) throws Exception {
		if(!isEnabled(object)) return;
		((TLDValidatorImpl)object).delete();
	}

	public boolean getSignificantFlag(XModelObject object) {
		return true;
	}

	public boolean isEnabled(XModelObject object) {
		return (object instanceof TLDValidatorImpl && object.isActive() && !((TLDValidatorImpl)object).isEmpty());
	}

}
