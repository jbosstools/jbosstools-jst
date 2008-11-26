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
package org.jboss.tools.jst.jsp.outline;

import java.util.Properties;

import org.eclipse.jdt.internal.ui.refactoring.contentassist.ControlContentAssistHelper;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.contentassist.ContentAssistHandler;
import org.jboss.tools.common.meta.key.WizardKeys;
import org.jboss.tools.common.model.ui.attribute.editor.DialogCellEditorEx;
import org.jboss.tools.common.model.ui.objecteditor.AttributeWrapper;
import org.jboss.tools.common.model.ui.objecteditor.ExtendedCellEditorProvider;
import org.jboss.tools.jst.jsp.contentassist.FaceletsHtmlContentAssistProcessor;
import org.jboss.tools.jst.jsp.contentassist.JSPDialogCellEditorContentAssistProcessor;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.RootElement;
import org.jboss.tools.jst.jsp.outline.cssdialog.CSSClassDialog;
import org.jboss.tools.jst.jsp.outline.cssdialog.CSSStyleDialog;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.CSSConstants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;

/**
 * @author Kabanovich Cell Editor for JSP attributes, of which values can be
 *         provided by prompting knoledgebase.
 */
public class JSPDialogCellEditor extends DialogCellEditorEx implements ExtendedCellEditorProvider.StoppableCellEditor {
    Properties context;

    // ValueHelper valueHelper;
    JSPDialogCellEditorContentAssistProcessor contentAssistentProcessor;
    ContentAssistHandler handler = null;
    boolean hasProposals = false;

    /**
     * Constructor.
     * @param parent
     * @param context
     */
    public JSPDialogCellEditor(Composite parent, Properties context) {
        super(parent);
        this.context = context;

        ValueHelper valueHelper = (ValueHelper) context.get("valueHelper");

        contentAssistentProcessor = new JSPDialogCellEditorContentAssistProcessor();

        if (valueHelper != null) {
            contentAssistentProcessor.setContext(context);
        }

        handler = ContentAssistHandler.createHandlerForText(getTextField(),
                ControlContentAssistHelper.createJavaContentAssistant(contentAssistentProcessor));
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

        // valueHelper = (ValueHelper)context.get("valueHelper");
        // if(valueHelper == null) return;
        ValueHelper valueHelper = new ValueHelper();
        String attributeName = Constants.EMPTY + context.getProperty("attributeName");
        String nodeName = Constants.EMPTY + context.getProperty("nodeName");
        String query = Constants.SLASH;

        if (valueHelper.isFacetets() && (nodeName.indexOf(':') < 0)) {
            query += FaceletsHtmlContentAssistProcessor.faceletHtmlPrefixStart;
        }

        query += (nodeName + "@" + attributeName);

        RootElement root = (RootElement) valueHelper.getInitalInput(query);
        hasProposals = ((root != null) && (root.getChildren().length > 0))
        		|| attributeName.equalsIgnoreCase(CSSConstants.STYLE)
        		|| attributeName.equalsIgnoreCase(CSSConstants.CLASS);
    }

    private void checkButtonEnablement() {
        if (context == null) {
            return;
        }

//        valueHelper = (ValueHelper)context.get("valueHelper");
//        if(valueHelper == null) return;
//        ValueHelper valueHelper = new ValueHelper();
        Button button = getButtonControl();

        if ((button == null) || button.isDisposed()) {
            return;
        }

        button.setVisible(hasProposals);
        handler.setEnabled(hasProposals);
    }

    protected Object openDialogBox(Control cellEditorWindow) {
        externalEditing = true;

        String attributeName = Constants.EMPTY + context.getProperty("attributeName");
        String nodeName = Constants.EMPTY + context.getProperty("nodeName");
        String query = Constants.SLASH;
        ValueHelper valueHelper = new ValueHelper();

        if ((valueHelper != null) && valueHelper.isFacetets() && (nodeName.indexOf(Constants.COLON) < 0)) {
            query += FaceletsHtmlContentAssistProcessor.faceletHtmlPrefixStart;
        }

        query += (nodeName + "@" + attributeName);
        context.setProperty("query", query);
        context.setProperty("help", query);
        context.setProperty("title", "Edit " + WizardKeys.toDisplayName(attributeName));
        context.setProperty("subtitle", "<" + context.getProperty("nodeName") + ">");

        String value = (getValue() instanceof String) ? getValue().toString()
                                                      : ((getValue() instanceof AttributeWrapper)
            ? ((AttributeWrapper) getValue()).value : Constants.EMPTY);

        context.put("value", value);

        attributeName = attributeName.toLowerCase();
        if (attributeName.equalsIgnoreCase(CSSConstants.CLASS)
        		|| attributeName.endsWith(CSSConstants.CLASS.toLowerCase())) {
            CSSClassDialog dialog = new CSSClassDialog(cellEditorWindow.getShell(), null, false);
            dialog.setCurrentStyleClass(value);

            if (dialog.open() == Window.OK) {
                externalEditing = false;

                return dialog.getSelectorName();
            }
        } else if (attributeName.equalsIgnoreCase(CSSConstants.STYLE)
        		|| attributeName.endsWith(CSSConstants.STYLE.toLowerCase())) {
            CSSStyleDialog dialog = new CSSStyleDialog(cellEditorWindow.getShell(),
                    ((value == null) ? Constants.EMPTY : value));

            if (dialog.open() == Window.OK) {
                externalEditing = false;

                return dialog.getNewStyle();
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
        value = context.getProperty("value");

        return value;
    }

    public void stopEditing() {
        super.fireApplyEditorValue();
    }

    protected Text getTextField() {
        return text;
    }
}
