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
package org.jboss.tools.jst.web.ui.test;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.common.ui.widget.editor.CompositeEditor;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewButtonWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewButtonWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewCheckBoxWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewCheckBoxWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewDialogWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewDialogWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewFooterWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewFooterWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewHeaderBarWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewHeaderBarWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewLinkWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewLinkWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewNavbarWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewNavbarWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewPageWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewPageWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewRangeSliderWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewRangeSliderWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewTextInputWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewTextInputWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewToggleWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewToggleWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizard;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewCheckboxWizardTest extends AbstractPaletteEntryTest implements JQueryConstants {
	IEditorPart editor = null;
	
	public NewCheckboxWizardTest() {}

	public void setUp() {
		super.setUp();
		editor = openEditor("a.html");
	}

	protected void tearDown() throws Exception {
		if(editor != null) {
			editor.getSite().getPage().closeEditor(editor, false);
			editor = null;
		}
		super.tearDown();
	}

	public void testNewPageWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Page", true);

		assertTrue(currentPage instanceof NewPageWizardPage);

		NewPageWizardPage wizardPage = (NewPageWizardPage)currentPage;
		NewPageWizard wizard = (NewPageWizard)wizardPage.getWizard();

		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_HEADER);
		assertTextExists(wizard, wizardPage.getEditorValue(EDITOR_ID_HEADER_TITLE));
		wizardPage.setEditorValue(EDITOR_ID_ADD_HEADER, FALSE);
		assertTextDoesNotExist(wizard, ROLE_HEADER);
		assertTextDoesNotExist(wizard, wizardPage.getEditorValue(EDITOR_ID_HEADER_TITLE));
		wizardPage.setEditorValue(EDITOR_ID_ADD_HEADER, TRUE);
		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_HEADER);
		assertTextExists(wizard, wizardPage.getEditorValue(EDITOR_ID_HEADER_TITLE));

		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_FOOTER);
		assertTextExists(wizard, wizardPage.getEditorValue(EDITOR_ID_FOOTER_TITLE));
		wizardPage.setEditorValue(EDITOR_ID_ADD_FOOTER, FALSE);
		assertTextDoesNotExist(wizard, ROLE_FOOTER);
		assertTextDoesNotExist(wizard, wizardPage.getEditorValue(EDITOR_ID_FOOTER_TITLE));
		wizardPage.setEditorValue(EDITOR_ID_ADD_FOOTER, TRUE);
		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_FOOTER);
		assertTextExists(wizard, wizardPage.getEditorValue(EDITOR_ID_FOOTER_TITLE));

		wizard.performFinish();
		WizardDialog dialog = (WizardDialog)wizard.getContainer();
		dialog.close();

		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertTrue(text.indexOf(ROLE_CONTENT) > 0);
		assertTrue(text.indexOf(ROLE_PAGE) > 0);
	}

	public void testNewCheckboxWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Checkbox", true);

		assertTrue(currentPage instanceof NewCheckBoxWizardPage);

		NewCheckBoxWizardPage wizardPage = (NewCheckBoxWizardPage)currentPage;
		NewCheckBoxWizard wizard = (NewCheckBoxWizard)wizardPage.getWizard();
		
		String label = "My Favorite Checkbox";
		wizardPage.setEditorValue(EDITOR_ID_LABEL, label);
		wizardPage.setEditorValue(EDITOR_ID_THEME, "b");
	
		assertTrue(wizard.getTextForBrowser().indexOf(label) > 0);
		assertTrue(wizard.getTextForTextView().indexOf(label) > 0);

		wizardPage.setEditorValue(EDITOR_ID_MINI, TRUE);
		assertAttrExists(wizard, ATTR_DATA_MINI, TRUE);
		wizardPage.setEditorValue(EDITOR_ID_MINI, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DATA_MINI);

		wizard.performFinish();
		WizardDialog dialog = (WizardDialog)wizard.getContainer();
		dialog.close();

		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertTrue(text.indexOf(label) > 0);
	}

	public void testNewToggleWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Flip Toggle Switch", true);

		assertTrue(currentPage instanceof NewToggleWizardPage);

		NewToggleWizardPage wizardPage = (NewToggleWizardPage)currentPage;
		NewToggleWizard wizard = (NewToggleWizard)wizardPage.getWizard();
		
		String label = "My Switch:";
		wizardPage.setEditorValue(EDITOR_ID_LABEL, label);
		wizardPage.setEditorValue(EDITOR_ID_THEME, "b");

		assertTextExists(wizard, label);
		assertTrue(wizard.getTextForBrowser().indexOf(label) > 0);

		wizardPage.setEditorValue(EDITOR_ID_MINI, TRUE);
		assertAttrExists(wizard, ATTR_DATA_MINI, TRUE);
		wizardPage.setEditorValue(EDITOR_ID_MINI, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DATA_MINI);
		
		assertEquals("Off", wizardPage.getEditorValue(EDITOR_ID_OFF));
		assertEquals("On", wizardPage.getEditorValue(EDITOR_ID_ON));
		wizardPage.setEditorValue(EDITOR_ID_OFF, "Off-1");
		assertTextExists(wizard, "Off-1");

		wizard.performFinish();
		WizardDialog dialog = (WizardDialog)wizard.getContainer();
		dialog.close();

		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertTrue(text.indexOf(label) > 0);
	}

	public void testNewDialogWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Dialog", true);
		assertTrue(currentPage instanceof NewDialogWizardPage);

		NewDialogWizardPage wizardPage = (NewDialogWizardPage)currentPage;
		NewDialogWizard wizard = (NewDialogWizard)wizardPage.getWizard(); 

		assertTextDoesNotExist(wizard, ATTR_DATA_CLOSE_BTN);
		wizardPage.setEditorValue(EDITOR_ID_CLOSE_BUTTON, CLOSE_RIGHT);
		assertAttrExists(wizard, ATTR_DATA_CLOSE_BTN, CLOSE_RIGHT);

		String title = wizardPage.getEditorValue(EDITOR_ID_TITLE);

		wizard.performFinish();
		WizardDialog dialog = (WizardDialog)wizard.getContainer();
		dialog.close();

		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertTrue(text.indexOf(title) > 0);
	}

	public void testNewButtonWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Button", true);
		assertTrue(currentPage instanceof NewButtonWizardPage);

		NewButtonWizardPage wizardPage = (NewButtonWizardPage)currentPage;
		NewButtonWizard wizard = (NewButtonWizard)wizardPage.getWizard(); 

		wizardPage.setEditorValue(EDITOR_ID_MINI, TRUE);
		assertAttrExists(wizard, ATTR_DATA_MINI, TRUE);
		wizardPage.setEditorValue(EDITOR_ID_MINI, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DATA_MINI);

		wizardPage.setEditorValue(EDITOR_ID_DISABLED, TRUE);
		assertAttrExists(wizard, ATTR_CLASS, CLASS_DISABLED);
		wizardPage.setEditorValue(EDITOR_ID_DISABLED, FALSE);
		assertTextDoesNotExist(wizard, ATTR_CLASS);

		wizardPage.setEditorValue(EDITOR_ID_INLINE, TRUE);
		assertAttrExists(wizard, ATTR_DATA_INLINE, TRUE);
		wizardPage.setEditorValue(EDITOR_ID_INLINE, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DATA_INLINE);

		wizardPage.setEditorValue(EDITOR_ID_ICON_ONLY, TRUE);
		assertAttrExists(wizard, ATTR_DATA_ICONPOS, "notext");
		wizardPage.setEditorValue(EDITOR_ID_ICON_POS, "arrow-r");
		assertAttrExists(wizard, ATTR_DATA_ICONPOS, "notext");
		wizardPage.setEditorValue(EDITOR_ID_ICON_ONLY, FALSE);
		assertAttrExists(wizard, ATTR_DATA_ICONPOS, "arrow-r");

		wizardPage.setEditorValue(EDITOR_ID_ACTION, WizardMessages.actionDialogLabel);
		assertAttrExists(wizard, ATTR_DATA_REL, DATA_REL_DIALOG);
		wizardPage.setEditorValue(EDITOR_ID_ACTION, "");
		assertTextDoesNotExist(wizard, ATTR_DATA_REL);

		String label = wizardPage.getEditorValue(EDITOR_ID_LABEL);

		wizard.performFinish();
		WizardDialog dialog = (WizardDialog)wizard.getContainer();
		dialog.close();

		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertTrue(text.indexOf(label) > 0);
	}

	public void testNewLinkWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Link", true);
		assertTrue(currentPage instanceof NewLinkWizardPage);

		NewLinkWizardPage wizardPage = (NewLinkWizardPage)currentPage;
		NewLinkWizard wizard = (NewLinkWizard)wizardPage.getWizard(); 

		String label = wizardPage.getEditorValue(EDITOR_ID_LABEL);

		wizardPage.setEditorValue(EDITOR_ID_ACTION, WizardMessages.actionPopupLabel);
		assertAttrExists(wizard, ATTR_DATA_REL, DATA_REL_POPUP);
		wizardPage.setEditorValue(EDITOR_ID_ACTION, "");
		assertTextDoesNotExist(wizard, ATTR_DATA_REL);

		wizardPage.setEditorValue(EDITOR_ID_TRANSITION, TRANSITION_FLIP);
		assertAttrExists(wizard, ATTR_DATA_TRANSITION, TRANSITION_FLIP);
		wizardPage.setEditorValue(EDITOR_ID_TRANSITION, "");
		assertTextDoesNotExist(wizard, ATTR_DATA_TRANSITION);

		wizard.performFinish();
		WizardDialog dialog = (WizardDialog)wizard.getContainer();
		dialog.close();

		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertTrue(text.indexOf(label) > 0);
	}

	public void testNewRangeSliderWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Range Slider", true);

		assertTrue(currentPage instanceof NewRangeSliderWizardPage);

		NewRangeSliderWizardPage wizardPage = (NewRangeSliderWizardPage)currentPage;
		NewRangeSliderWizard wizard = (NewRangeSliderWizard)wizardPage.getWizard();
		
		String label = "My Switch:";
		wizardPage.setEditorValue(EDITOR_ID_LABEL, label);
		wizardPage.setEditorValue(EDITOR_ID_THEME, "b");
	
		assertTrue(wizard.getTextForBrowser().indexOf(label) > 0);
		assertTrue(wizard.getTextForTextView().indexOf(label) > 0);

		wizardPage.setEditorValue(EDITOR_ID_MINI, TRUE);
		assertAttrExists(wizard, ATTR_DATA_MINI, TRUE);
		wizardPage.setEditorValue(EDITOR_ID_MINI, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DATA_MINI);

		assertAttrExists(wizard, ATTR_DATA_MIN, "0");
		wizardPage.setEditorValue(EDITOR_ID_MIN, "10");
		assertAttrExists(wizard, ATTR_DATA_MIN, "10");

		assertAttrExists(wizard, ATTR_DATA_MAX, "100");
		wizardPage.setEditorValue(EDITOR_ID_MAX, "200");
		assertAttrExists(wizard, ATTR_DATA_MAX, "200");

		assertTextDoesNotExist(wizard, ATTR_DATA_STEP);
		wizardPage.setEditorValue(EDITOR_ID_STEP, "2");
		assertAttrExists(wizard, ATTR_DATA_STEP, "2");

		assertAttrExists(wizard, ATTR_DATA_VALUE, "40");
		wizardPage.setEditorValue(EDITOR_ID_VALUE, "50");
		assertAttrExists(wizard, ATTR_DATA_VALUE, "50");

		assertTextDoesNotExist(wizard, ATTR_DATA_VALUE + "=\"60\"");
		wizardPage.setEditorValue(EDITOR_ID_RANGE, TRUE);
		assertAttrExists(wizard, ATTR_DATA_VALUE, "60");

		assertTextDoesNotExist(wizard, ATTR_DISABLED);
		wizardPage.setEditorValue(EDITOR_ID_DISABLED, TRUE);
		assertAttrExists(wizard, ATTR_DISABLED, ATTR_DISABLED);

		assertTextDoesNotExist(wizard, CLASS_HIDDEN_ACCESSIBLE);
		wizardPage.setEditorValue(EDITOR_ID_HIDE_LABEL, TRUE);
		assertTextExists(wizard, CLASS_HIDDEN_ACCESSIBLE);

		wizard.performFinish();
		WizardDialog dialog = (WizardDialog)wizard.getContainer();
		dialog.close();

		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertTrue(text.indexOf(label) > 0);
	}

	public void testNewTextInputWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Text Input", true);

		assertTrue(currentPage instanceof NewTextInputWizardPage);

		NewTextInputWizardPage wizardPage = (NewTextInputWizardPage)currentPage;
		NewTextInputWizard wizard = (NewTextInputWizard)wizardPage.getWizard();
		
		wizardPage.setEditorValue(EDITOR_ID_MINI, TRUE);
		assertAttrExists(wizard, ATTR_DATA_MINI, TRUE);
		wizardPage.setEditorValue(EDITOR_ID_MINI, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DATA_MINI);

		assertAttrExists(wizard, ATTR_TYPE, TYPE_TEXT);
		wizardPage.setEditorValue(EDITOR_ID_TEXT_TYPE, TYPE_COLOR);
		assertAttrExists(wizard, ATTR_TYPE, TYPE_COLOR);
		wizardPage.setEditorValue(EDITOR_ID_TEXT_TYPE, TYPE_TEXTAREA);
		assertTextDoesNotExist(wizard, ATTR_TYPE);
		wizardPage.setEditorValue(EDITOR_ID_TEXT_TYPE, TYPE_SEARCH);
		assertAttrExists(wizard, ATTR_TYPE, TYPE_SEARCH);
		
		assertAttrExists(wizard, ATTR_DATA_CLEAR_BTN, TRUE);
		wizardPage.setEditorValue(EDITOR_ID_CLEAR_INPUT, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DATA_CLEAR_BTN);
		wizardPage.setEditorValue(EDITOR_ID_CLEAR_INPUT, TRUE);
		assertAttrExists(wizard, ATTR_DATA_CLEAR_BTN, TRUE);

		wizardPage.setEditorValue(EDITOR_ID_VALUE, "777");
		assertAttrExists(wizard, ATTR_DATA_VALUE, "777");

		wizardPage.setEditorValue(EDITOR_ID_PLACEHOLDER, "abcdef");
		assertAttrExists(wizard, ATTR_PLACEHOLDER, "abcdef");

		assertAttrExists(wizard, ATTR_DATA_ROLE, ROLE_FIELDCONTAIN);
		wizardPage.setEditorValue(EDITOR_ID_LAYOUT, LAYOUT_VERTICAL);
		assertTextDoesNotExist(wizard, ROLE_FIELDCONTAIN);

		wizard.performFinish();
		WizardDialog dialog = (WizardDialog)wizard.getContainer();
		dialog.close();

		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertTrue(text.indexOf(wizardPage.getEditorValue(EDITOR_ID_LABEL)) > 0);
	}

	public void testNewHeaderBarWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Header Bar", true);

		assertTrue(currentPage instanceof NewHeaderBarWizardPage);

		NewHeaderBarWizardPage wizardPage = (NewHeaderBarWizardPage)currentPage;
		NewHeaderBarWizard wizard = (NewHeaderBarWizard)wizardPage.getWizard();
		
		assertTextDoesNotExist(wizard, ATTR_DATA_POSITION);
		wizardPage.setEditorValue(EDITOR_ID_FIXED_POSITION, TRUE);
		assertAttrExists(wizard, ATTR_DATA_POSITION, POSITION_FIXED);

		assertTextDoesNotExist(wizard, ATTR_DATA_FULL_SCREEN);
		wizardPage.setEditorValue(EDITOR_ID_FULL_SCREEN, TRUE);
		assertAttrExists(wizard, ATTR_DATA_FULL_SCREEN, TRUE);

		assertTextDoesNotExist(wizard, CLASS_BUTTON_RIGHT);
		wizardPage.setEditorValue(EDITOR_ID_LEFT_BUTTON, FALSE);
		assertTextExists(wizard, CLASS_BUTTON_RIGHT);
		wizardPage.setEditorValue(EDITOR_ID_LEFT_BUTTON, TRUE);
		assertTextDoesNotExist(wizard, CLASS_BUTTON_RIGHT);

		wizard.performFinish();
		WizardDialog dialog = (WizardDialog)wizard.getContainer();
		dialog.close();

		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertTrue(text.indexOf(wizardPage.getEditorValue(EDITOR_ID_TITLE)) > 0);
	}

	public void testNewNavbarWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Navbar", true);

		assertTrue(currentPage instanceof NewNavbarWizardPage);

		NewNavbarWizardPage wizardPage = (NewNavbarWizardPage)currentPage;
		NewNavbarWizard wizard = (NewNavbarWizard)wizardPage.getWizard();
		Display.getCurrent().readAndDispatch();
		
		assertEquals("3", wizardPage.getEditorValue(EDITOR_ID_NUMBER_OF_ITEMS));

		IFieldEditor iconPos = ((CompositeEditor)wizardPage.getEditor(EDITOR_ID_ICON_POS)).getEditors().get(0);
		assertFalse(iconPos.isEnabled());
		wizardPage.setEditorValue(EDITOR_ID_ICON, "delete");
		assertTrue(iconPos.isEnabled());
		assertTextDoesNotExist(wizard, ATTR_DATA_ICONPOS);
		wizardPage.setEditorValue(EDITOR_ID_ICON_POS, "left");
		assertAttrExists(wizard, ATTR_DATA_ICONPOS, "left");

		wizardPage.setEditorValue(EDITOR_ID_LABEL, "Run Test");

		wizard.performFinish();
		WizardDialog dialog = (WizardDialog)wizard.getContainer();
		dialog.close();

		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertTrue(text.indexOf("Run Test") > 0);
	}

	public void testNewFooterBarWizard() {
		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Footer Bar", true);

		assertTrue(currentPage instanceof NewFooterWizardPage);

		NewFooterWizardPage wizardPage = (NewFooterWizardPage)currentPage;
		NewFooterWizard wizard = (NewFooterWizard)wizardPage.getWizard();
		Display.getCurrent().readAndDispatch();
		
		assertTextDoesNotExist(wizard, ATTR_DATA_POSITION);
		wizardPage.setEditorValue(EDITOR_ID_FIXED_POSITION, TRUE);
		assertAttrExists(wizard, ATTR_DATA_POSITION, POSITION_FIXED);

		assertTextDoesNotExist(wizard, ATTR_DATA_FULL_SCREEN);
		wizardPage.setEditorValue(EDITOR_ID_FULL_SCREEN, TRUE);
		assertAttrExists(wizard, ATTR_DATA_FULL_SCREEN, TRUE);

		String label = "Run Footer Bar Test";
		wizardPage.setEditorValue(EDITOR_ID_TITLE, label);

		wizard.performFinish();
		WizardDialog dialog = (WizardDialog)wizard.getContainer();
		dialog.close();

		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertTrue(text.indexOf(label) > 0);
	}

	void assertAttrExists(AbstractNewHTMLWidgetWizard wizard, String attr, String value) {
		assertTextExists(wizard, attr + "=\"" + value + "\"");
	}

	void assertTextExists(AbstractNewHTMLWidgetWizard wizard, String text) {
		assertTrue(wizard.getTextForTextView().indexOf(text) > 0);
	}

	void assertTextDoesNotExist(AbstractNewHTMLWidgetWizard wizard, String text) {
		assertTrue(wizard.getTextForTextView().indexOf(text) < 0);
	}

}
