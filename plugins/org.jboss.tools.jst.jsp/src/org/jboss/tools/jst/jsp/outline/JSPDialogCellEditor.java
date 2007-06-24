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
import org.jboss.tools.common.model.ui.attribute.editor.DialogCellEditorEx;
import org.jboss.tools.common.model.ui.objecteditor.AttributeWrapper;
import org.jboss.tools.common.model.ui.objecteditor.ExtendedCellEditorProvider;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.*;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.common.meta.key.WizardKeys;
import org.jboss.tools.jst.jsp.contentassist.JSPDialogCellEditorContentAssistProcessor;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.RootElement;

/**
 * @author Kabanovich
 * Cell Editor for JSP attributes, of which values can be 
 * provided by prompting knoledgebase.
 */

public class JSPDialogCellEditor extends DialogCellEditorEx implements ExtendedCellEditorProvider.StoppableCellEditor {
	Properties context;
	ValueHelper valueHelper;
	JSPDialogCellEditorContentAssistProcessor contentAssistentProcessor;
	
	public JSPDialogCellEditor(Composite parent, Properties context) {
		super(parent);
		this.context = context;
		valueHelper = (ValueHelper)context.get("valueHelper");

		contentAssistentProcessor = new JSPDialogCellEditorContentAssistProcessor();
		if(valueHelper != null) contentAssistentProcessor.setContext(context);
		ControlContentAssistHelper.createTextContentAssistant(getTextField(), contentAssistentProcessor);
	}
	
    public void activate() {
    	checkButtonEnablement();
    	super.activate();
	}
    
    private void checkButtonEnablement() {
		if(context == null) return;
		valueHelper = (ValueHelper)context.get("valueHelper");
		if(valueHelper == null) return;
		Button button = getButtonControl();
		if(button == null || button.isDisposed()) return;
		String attributeName = "" + context.getProperty("attributeName");
		String nodeName = "" + context.getProperty("nodeName");
		String query = "/" + nodeName + "@" + attributeName;
		RootElement root = (RootElement)valueHelper.getInitalInput(query);
		boolean enabled = root != null && root.getChildren().length > 0;
		getButtonControl().setVisible(enabled);
    }

	protected Object openDialogBox(Control cellEditorWindow) {
		externalEditing = true;
		String attributeName = "" + context.getProperty("attributeName");
		String nodeName = "" + context.getProperty("nodeName");
		String query = "/" + nodeName + "@" + attributeName;
		context.setProperty("query", query);
		context.setProperty("help", query);
		context.setProperty("title", "Edit " + WizardKeys.toDisplayName(attributeName));
		context.setProperty("subtitle", "<" + context.getProperty("nodeName") + ">");

		String value = (getValue() instanceof String) 
			? getValue().toString() 
			: getValue() instanceof AttributeWrapper ? ((AttributeWrapper)getValue()).value 
			: "";
		
		context.put("value", value);
		JSPTreeDialog dialog = new JSPTreeDialog();
		dialog.setObject(context);
		if(dialog.execute() != 0) {
			externalEditing = false;
			return null;
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
