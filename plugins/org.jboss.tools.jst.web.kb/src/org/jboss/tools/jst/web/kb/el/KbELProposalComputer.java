/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.el;

import org.jboss.tools.common.el.ui.ca.ELProposalProcessor;
import org.jboss.tools.common.el.ui.ca.JavaELProposalComputer;

public class KbELProposalComputer extends JavaELProposalComputer {
	DefaultKbELProposalProcessor processor = new DefaultKbELProposalProcessor();

	@Override
	protected ELProposalProcessor getELProcessor() {
		return processor;
	}

}
