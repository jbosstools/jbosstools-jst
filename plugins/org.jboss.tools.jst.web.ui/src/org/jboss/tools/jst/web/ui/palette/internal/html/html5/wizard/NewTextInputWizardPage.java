/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.ElementNode;
import org.jboss.tools.common.model.ui.editors.dnd.ValidationException;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper.ICommand;
import org.jboss.tools.common.ui.widget.editor.ButtonFieldEditor.ButtonPressedAction;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.LinkAttributeProvider.ElementID;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil.TwoColumns;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.IDContentProposalProvider;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryFieldEditorFactory;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.WizardDescriptions;
import org.jboss.tools.jst.web.ui.palette.html.wizard.NewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteItem;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewTextInputWizardPage extends NewHTMLWidgetWizardPage {
	static String DATALIST_NODE_EVENT = "datalistNode";
	Set<String> datalists = new HashSet<String>();
	ElementNode datalistNode = null;
	Button createDatalistButton = null;

	public NewTextInputWizardPage() {
		super("newText", WizardMessages.newTextInputWizardTitle);
		setDescription(WizardMessages.newHTML5TextInputWizardDescription);
	}

	public ElementNode getDatalistNode() {
		return datalistNode;
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		addEditor(JQueryFieldEditorFactory.createTextTypeEditor(), parent);
		addEditor(JQueryFieldEditorFactory.createNameEditor(), parent);

		createIDEditor(parent, false);

		createSeparator(parent);

		IFieldEditor label = JQueryFieldEditorFactory.createLabelEditor();
		label.setValue("Input:");
		addEditor(label, parent);

		IFieldEditor placeholder = JQueryFieldEditorFactory.createPlaceholderEditor();
		placeholder.setValue("Text");
		addEditor(placeholder, parent);

		createSeparator(parent);

		IFieldEditor list = HTMLFieldEditorFactory.createInputListEditor(new CreateDatalistAction());
		addEditor(list, parent);
		if(parent != null) {
			IDContentProposalProvider p = new IDContentProposalProvider(getDatalistIDs("", true), list);
			p.setSharp(false);
			for (Object o: list.getEditorControls()) {
				if(o instanceof Button) {
					createDatalistButton = (Button)o;
				}
			}
		}

		addEditor(JQueryFieldEditorFactory.createValueEditor(), parent);
		
		TwoColumns columns = createTwoColumns(parent);
		addEditor(JQueryFieldEditorFactory.createPatternEditor(), columns.left());
		IFieldEditor maxlength = JQueryFieldEditorFactory.createMaxlengthEditor();
		addEditor(maxlength, columns.right());
		
		if(parent != null) {
			Object[] cs = maxlength.getEditorControls();
			GridData d = (GridData)((Text)cs[1]).getLayoutData();
			d.widthHint = 20;
			((Text)cs[1]).setLayoutData(d);
		}
		
		Group panel = null;
		if(parent != null) {
			panel = new Group(parent,SWT.BORDER);
			panel.setText(WizardMessages.inputTypeNumberLabel);
			GridData d = new GridData(GridData.FILL_HORIZONTAL);
			d.horizontalSpan = 3;
			panel.setLayoutData(d);		
			panel.setLayout(new GridLayout(3, false));
		}

		Composite[] columns3 = createColumns(panel, 3);
		
		IFieldEditor min = JQueryFieldEditorFactory.createMinEditor(WizardDescriptions.textInputMin);
		min.setValue("");
		addEditor(min, columns3[0]);

		IFieldEditor max = JQueryFieldEditorFactory.createMaxEditor(WizardDescriptions.textInputMax);
		max.setValue("");
		addEditor(max, columns3[1]);

		IFieldEditor step = JQueryFieldEditorFactory.createStepEditor(WizardDescriptions.textInputStep);
		addEditor(step, columns3[2]);

		createSeparator(parent);
		
		columns3 = createColumns(parent, 3);
		addEditor(JQueryFieldEditorFactory.createRequiredEditor(), columns3[0]);
		addEditor(JQueryFieldEditorFactory.createDisabledEditor(), columns3[1]);
		addEditor(JQueryFieldEditorFactory.createAutofocusEditor(), columns3[2]);

		updateNumberFieldsEnablement();
		updateListButtonEnablement();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String name = evt.getPropertyName();
		if(JQueryConstants.EDITOR_ID_TEXT_TYPE.equals(name)) {
			updateNumberFieldsEnablement();
		}
		if(ATTR_LIST.equals(name)) {
			datalistNode = null;
			updateListButtonEnablement();
		} else if(DATALIST_NODE_EVENT.equals(name)) {
			updateListButtonEnablement();
		}
		super.propertyChange(evt);
	}

	void updateNumberFieldsEnablement() {
		boolean isNumber = JQueryConstants.TYPE_NUMBER.equals(getEditorValue(JQueryConstants.EDITOR_ID_TEXT_TYPE));
		setEnabled(JQueryConstants.EDITOR_ID_MIN, isNumber);
		setEnabled(JQueryConstants.EDITOR_ID_MAX, isNumber);
		setEnabled(JQueryConstants.EDITOR_ID_STEP, isNumber);
	}

	void updateListButtonEnablement() {
		if(createDatalistButton != null && !createDatalistButton.isDisposed()) {
			boolean enabled = !datalists.contains(getEditorValue(ATTR_LIST)) && datalistNode == null;
			createDatalistButton.setEnabled(enabled);
		}		
	}

	@Override
	public void validate() throws ValidationException {
		String pattern = getEditorValue(JQueryConstants.EDITOR_ID_PATTERN);
		if(pattern != null && pattern.length() > 0) {
			try {
				Pattern.compile(pattern);
			} catch (PatternSyntaxException e) {
				throw new ValidationException(e.getMessage());
			}
		}
		super.validate();
	}

	class CreateDatalistAction extends ButtonPressedAction {

		public CreateDatalistAction() {
			super(WizardMessages.createLabel);
		}

		@Override
		public void run() {
			createDatalist(getControl() != null);
		}
	}

	public void createDatalist(boolean showDialog) {
		new NewDatalistWizardEx(getWizard().getPaletteItem(), showDialog);
	}


	public List<ElementID> getDatalistIDs(final String mask, final boolean escapeHTML) {
		final List<ElementID> ids = new ArrayList<ElementID>();
		datalists = new HashSet<String>();

		StructuredModelWrapper.execute(getWizard().getFile(), new ICommand() {
			public void execute(IDOMDocument xmlDocument) {
				try {
					NodeList list = (NodeList) XPathFactory.newInstance().newXPath().compile("//*/@id[starts-with(.,'" + mask + "')]").evaluate(xmlDocument,XPathConstants.NODESET);
					for (int i = 0; i < list.getLength(); i++) {
						IDOMAttr attr = ((IDOMAttr)  list.item(i));
						Element element = (Element)attr.getOwnerElement();
						if(TAG_DATALIST.equals(element.getNodeName())) {
							IStructuredDocumentRegion s = ((IDOMNode)element).getStartStructuredDocumentRegion();
							String id = attr.getNodeValue();
							int offset = ((IDOMAttr)attr).getValueRegionStartOffset() + 1;
							String nodeString = s.getText();
							ids.add(new ElementID(id, offset, nodeString, escapeHTML));
							datalists.add(id);
						}
					}
				} catch (XPathExpressionException e) {
					WebKbPlugin.getDefault().logError(e);
				}
			}
		});
		return Collections.unmodifiableList(ids);
	}

	class NewDatalistWizardEx extends NewDatalistWizard {
		NewDatalistWizardEx(IPaletteItem item, boolean showDialog) {
			JSPMultiPageEditor e = (JSPMultiPageEditor)WebUiPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			setPaletteItem(item);
			setCommand(createDropCommand(e.getJspEditor()));
			WizardDialog d = new WizardDialog(NewTextInputWizardPage.this.getShell(), this);
			d.create();
			page.setEditorValue(EDITOR_ID_ID, getEditorValue(ATTR_LIST));
			if(getControl() != null) {
				IFieldEditor input = page.getEditor(TAG_INPUT);
				input.setEnabled(false);
				for(Object o: input.getEditorControls()) {
					if(o instanceof Control) {
						((Control)o).setVisible(false);
					}
				}
			}
			if(showDialog) {
				d.open();
			} else {
				doPerformFinish();
			}
			dispose();
		}

		@Override
		protected void doPerformFinish() {
			ElementNode root = createRoot();
			addContent(root);
			String id = getID(prefix);
			setEditorValue(ATTR_LIST, id);
			datalistNode = (ElementNode)root.getChildren().get(0);
			NewTextInputWizardPage.this.propertyChange(new PropertyChangeEvent(createDatalistButton, DATALIST_NODE_EVENT, "", ""));
		}
	}

}
