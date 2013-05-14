package org.jboss.tools.jst.web.ui.palette.html.wizard;

import org.eclipse.jface.fieldassist.ComboContentAdapter;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.common.model.ui.attribute.AttributeContentProposalProviderFactory;

public abstract class AbstractContentProposalProvider implements IContentProposalProvider {

	public AbstractContentProposalProvider() {}

	public void registerContentAssist(Control control) {
		IControlContentAdapter controlAdapter = 
				  (control instanceof Text)  ? new TextContentAdapter()
				: (control instanceof Combo) ? new ComboContentAdapter()
				: null;
		if(controlAdapter == null) {
			return;
		}
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

	protected void decorate(Control control) {
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
