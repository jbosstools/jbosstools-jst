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

import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.common.model.ui.attribute.AttributeContentProposalProviderFactory;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.kb.internal.taglib.jq.JQueryTagLib.ElementID;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class IDContentProposalProvider implements IContentProposalProvider {
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

	public void registerContentAssist(Control control) {
		IControlContentAdapter controlAdapter = new TextContentAdapter();
		ContentProposalAdapter adapter = new ContentProposalAdapter(control, controlAdapter, this,
				AttributeContentProposalProviderFactory.getCtrlSpaceKeyStroke(), null);
		adapter.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof IContentProposal) {
					IContentProposal proposal = (IContentProposal) element;
					return proposal.getLabel() == null ? proposal.getContent()
							: proposal.getLabel();
				}
				return super.getText(element);
			}
		});
		adapter.setPropagateKeys(true);
		adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
		decorate(control);
	}

	void decorate(Control control) {
		int bits = SWT.TOP | SWT.LEFT;
		ControlDecoration controlDecoration = new ControlDecoration(control, bits);
		// Configure text widget decoration
		// No margin
		controlDecoration.setMarginWidth(0);
		// Custom hover tip text
		controlDecoration.setDescriptionText("code assist" /*PDEUIMessages.PDEJavaHelper_msgContentAssistAvailable*/);
		// Custom hover properties
		controlDecoration.setShowHover(true);
		controlDecoration.setShowOnlyOnFocus(true);
		// Hover image to use
		FieldDecoration contentProposalImage = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_CONTENT_PROPOSAL);
		controlDecoration.setImage(contentProposalImage.getImage());
	}

}
