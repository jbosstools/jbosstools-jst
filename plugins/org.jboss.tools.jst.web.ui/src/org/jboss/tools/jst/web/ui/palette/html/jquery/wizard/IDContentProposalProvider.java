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
package org.jboss.tools.jst.web.ui.palette.html.jquery.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.common.model.ui.attribute.AttributeContentProposalProviderFactory;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.kb.internal.taglib.jq.JQueryTagLib.ElementID;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractContentProposalProvider;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class IDContentProposalProvider extends AbstractContentProposalProvider {
	ElementID[] ids;

	public IDContentProposalProvider(ElementID[] ids, IFieldEditor editor) {
		this.ids = ids;
		registerContentAssist(editor);
	}

	@Override
	public IContentProposal[] getProposals(String contents, int position) {
		List<IContentProposal> result = new ArrayList<IContentProposal>();
		String prefix = contents.substring(0, position);
		for (ElementID id: ids) {
			String v = "#" + id.getId();
			if(v.startsWith(prefix)) {
				IContentProposal cp = AttributeContentProposalProviderFactory.makeContentProposal(v, v, id.getDescription());
				result.add(cp);
			}
		}
		return result.toArray(new IContentProposal[0]);
	}

	public void registerContentAssist(IFieldEditor editor) {
		Object[] os = editor.getEditorControls();
		if(os.length > 1 && os[1] instanceof Text) {
			registerContentAssist((Text)os[1]);
		}
	}

}
