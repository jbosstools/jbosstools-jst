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
package org.jboss.tools.jst.web.ui.internal.editor.outline;

import java.text.MessageFormat;
import java.util.Properties;

import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener2;
import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.common.meta.key.WizardKeys;
import org.jboss.tools.common.model.ui.attribute.AttributeContentProposalProviderFactory;
import org.jboss.tools.common.model.ui.attribute.editor.DialogCellEditorEx;
import org.jboss.tools.common.model.ui.objecteditor.AttributeWrapper;
import org.jboss.tools.common.model.ui.objecteditor.ExtendedCellEditorProvider;
import org.jboss.tools.jst.web.ui.internal.editor.contentassist.JSPDialogContentProposalProvider;
import org.jboss.tools.jst.web.ui.internal.editor.messages.JstUIMessages;
import org.jboss.tools.jst.web.ui.internal.editor.outline.css.CSSStyleClassSelectorDecorator;
import org.jboss.tools.jst.web.ui.internal.editor.outline.css.CSSStyleDialogDecorator;
import org.jboss.tools.jst.web.ui.internal.editor.util.Constants;

/**
 * @author Kabanovich Cell Editor for JSP attributes, of which values can be
 *         provided by prompting knoledgebase.
 */
public class JSPDialogCellEditor extends DialogCellEditorEx implements ExtendedCellEditorProvider.StoppableCellEditor {
    Properties context;

    JSPDialogContentProposalProvider cppEL;
    JSPDialogContentProposalProvider cppAttr;
    boolean hasProposals = false;

    /**
     * Constructor.
     * @param parent
     * @param context
     */
    public JSPDialogCellEditor(Composite parent, Properties context) {
        super(parent);
        this.context = context;

        addContentAssist(getTextField());
    }

    public void activate() {
        checkHasProposals();
        checkButtonEnablement();
        super.activate();
    }

    private void checkHasProposals() {
        hasProposals = false;

        if (context == null) {
            return;
        }
        
        String attributeName = Constants.EMPTY + context.getProperty("attributeName"); //$NON-NLS-1$
        if(attributeName.equalsIgnoreCase(Constants.STYLE)
        		|| attributeName.equalsIgnoreCase(Constants.CLASS)) {
        	hasProposals = true;
        	return;
        }

        if(cppEL == null) {
        	cppEL = new JSPDialogContentProposalProvider();
        }
		cppEL.setContext(context);
		if(cppAttr == null) {
			cppAttr = new JSPDialogContentProposalProvider();
			cppAttr.setAttrMode();
		}
		cppAttr.setContext(context);
		IContentProposal[] ps = cppEL.getProposals("#{}", 2); //$NON-NLS-1$
        hasProposals = ((ps != null) && (ps.length > 0));
    }

    private void checkButtonEnablement() {
        if (context == null) {
            return;
        }
        Button button = getButtonControl();

        if ((button == null) || button.isDisposed()) {
            return;
        }

        button.setVisible(hasProposals);
    }

    protected Object openDialogBox(Control cellEditorWindow) {
        externalEditing = true;

        String attributeName = Constants.EMPTY + context.getProperty("attributeName"); //$NON-NLS-1$
        String nodeName = Constants.EMPTY + context.getProperty("nodeName"); //$NON-NLS-1$
        String query = Constants.SLASH;
        ValueHelper valueHelper = new ValueHelper();

//        if ((valueHelper != null) && valueHelper.isFacetets() && (nodeName.indexOf(Constants.COLON) < 0)) {
//            query += FaceletsHtmlContentAssistProcessor.faceletHtmlPrefixStart;
//        }

        query += (nodeName + "@" + attributeName); //$NON-NLS-1$
        context.setProperty("query", query); //$NON-NLS-1$
        context.setProperty("help", query); //$NON-NLS-1$
        context.setProperty("title", MessageFormat.format(JstUIMessages.JSPDialogCellEditor_EditAttribute, WizardKeys.toDisplayName(attributeName))); //$NON-NLS-1$
        context.setProperty("subtitle", "<" + context.getProperty("nodeName") + ">"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        String value = (getValue() instanceof String) ? getValue().toString()
                                                      : ((getValue() instanceof AttributeWrapper)
            ? ((AttributeWrapper) getValue()).value : Constants.EMPTY);

        context.put("value", value); //$NON-NLS-1$

        attributeName = attributeName.toLowerCase();
        if (attributeName.equalsIgnoreCase(Constants.CLASS)
        		|| attributeName.endsWith(Constants.CLASS.toLowerCase())) {
            CSSStyleClassSelectorDecorator dialog = new CSSStyleClassSelectorDecorator(cellEditorWindow.getShell());
            dialog.setCurrentStyleClass(value);

            if (dialog.open() == Window.OK) {
                externalEditing = false;

                return dialog.getCSSStyleClasses();
            }
        } else
        	if (attributeName.equalsIgnoreCase(Constants.STYLE)
        		|| attributeName.endsWith(Constants.STYLE.toLowerCase())) {
            CSSStyleDialogDecorator dialog = new CSSStyleDialogDecorator(cellEditorWindow.getShell(),
                    ((value == null) ? Constants.EMPTY : value));

            if (dialog.open() == Window.OK) {
                externalEditing = false;

                return dialog.getStyle();
            }
        } else {
            JSPTreeDialog dialog = new JSPTreeDialog();
            dialog.setObject(context);

            if (dialog.execute() != 0) {
                externalEditing = false;

                return null;
            }
        }

        externalEditing = false;
        value = context.getProperty("value"); //$NON-NLS-1$

        return value;
    }

    public void stopEditing() {
        super.fireApplyEditorValue();
    }

    protected Text getTextField() {
        return text;
    }

	protected Control createContents(Composite cell) {
		super.createContents(cell);
		
		return getTextControl();
	}

	protected void addContentAssist(Text text) {
		IControlContentAdapter controlAdapter = new TextContentAdapter();
		cppEL = new JSPDialogContentProposalProvider();
		cppEL.setContext(context);

		ContentProposalAdapter adapter = new ContentProposalAdapter(
				text, 
				controlAdapter, 
				cppEL,
				AttributeContentProposalProviderFactory.getCtrlSpaceKeyStroke(), 
				null);
		adapter.setPropagateKeys(true);
		adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_INSERT);
		if(popup != null) {
			adapter.addContentProposalListener(popup);
		}		

		cppAttr = new JSPDialogContentProposalProvider();
		cppAttr.setAttrMode();
		cppAttr.setContext(context);

		adapter = new ContentProposalAdapter(
				text, 
				controlAdapter, 
				cppAttr,
				AttributeContentProposalProviderFactory.getCtrlSpaceKeyStroke(), 
				null);
		adapter.setPropagateKeys(true);
		adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
		if(popup != null) {
			adapter.addContentProposalListener(popup);
		}		

		int bits = SWT.TOP | SWT.LEFT;
		ControlDecoration controlDecoration = new ControlDecoration(getTextControl(), bits) {
			public Image getImage() {
				return super.getImage();
			}
		};
		// Configure text widget decoration
		// No margin
		controlDecoration.setMarginWidth(0);
		// Custom hover tip text
		controlDecoration.setDescriptionText(JstUIMessages.JSPDialogCellEditor_CodeAssist /*PDEUIMessages.PDEJavaHelper_msgContentAssistAvailable*/);
		// Custom hover properties
		controlDecoration.setShowHover(true);
		controlDecoration.setShowOnlyOnFocus(true);
		// Hover image to use
		FieldDecoration contentProposalImage = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_CONTENT_PROPOSAL);
		controlDecoration.setImage(contentProposalImage.getImage());
	}

	public static void addContentAssist(Text text, Properties context, IContentProposalListener2 popup) {
		IControlContentAdapter controlAdapter = new TextContentAdapter();
		JSPDialogContentProposalProvider cppEL = new JSPDialogContentProposalProvider();
		cppEL.setContext(context);

		ContentProposalAdapter adapter = new ContentProposalAdapter(
				text, 
				controlAdapter, 
				cppEL,
				AttributeContentProposalProviderFactory.getCtrlSpaceKeyStroke(), 
				null);
		adapter.setPropagateKeys(true);
		adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_INSERT);
		if(popup != null) {
			adapter.addContentProposalListener(popup);
		}		

		JSPDialogContentProposalProvider cppAttr = new JSPDialogContentProposalProvider();
		cppAttr.setAttrMode();
		cppAttr.setContext(context);

		adapter = new ContentProposalAdapter(
				text, 
				controlAdapter, 
				cppAttr,
				AttributeContentProposalProviderFactory.getCtrlSpaceKeyStroke(), 
				null);
		adapter.setPropagateKeys(true);
		adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
		if(popup != null) {
			adapter.addContentProposalListener(popup);
		}		

		int bits = SWT.TOP | SWT.LEFT;
		ControlDecoration controlDecoration = new ControlDecoration(text, bits) {
			public Image getImage() {
				return super.getImage();
			}
		};
		// Configure text widget decoration
		// No margin
		controlDecoration.setMarginWidth(0);
		// Custom hover tip text
		controlDecoration.setDescriptionText(JstUIMessages.JSPDialogCellEditor_CodeAssist /*PDEUIMessages.PDEJavaHelper_msgContentAssistAvailable*/);
		// Custom hover properties
		controlDecoration.setShowHover(true);
		controlDecoration.setShowOnlyOnFocus(true);
		// Hover image to use
		FieldDecoration contentProposalImage = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_CONTENT_PROPOSAL);
		controlDecoration.setImage(contentProposalImage.getImage());
	}

}
