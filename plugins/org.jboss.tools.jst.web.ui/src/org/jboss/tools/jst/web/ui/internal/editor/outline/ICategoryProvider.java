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
package org.jboss.tools.jst.web.ui.internal.editor.outline;

import java.util.Map;

import org.eclipse.jface.text.IDocument;
import org.jboss.tools.jst.web.kb.KbQuery;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
@Deprecated
public interface ICategoryProvider {
	public boolean init(IDocument document, KbQuery query);
	public String getCategory(String attributeName);
	public boolean isExpert(String category);
	public void fillAttributeWeights(Map<String, Integer> weights);
}
