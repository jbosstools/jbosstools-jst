/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.angularjs.test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.XModelObjectConstants;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.ui.editors.dnd.IDropWizard;
import org.jboss.tools.common.model.ui.internal.editors.PaletteItemResult;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.IonicConstants;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.IonicVersion;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewButtonWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewButtonWizardPage;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewCheckBoxWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewCheckBoxWizardPage;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewContentWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewContentWizardPage;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewFooterBarWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewFooterBarWizardPage;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewHeaderBarWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewHeaderBarWizardPage;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewIonicWidgetWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewIonicWidgetWizardPage;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewListWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewListWizardPage;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewNavigationWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewNavigationWizardPage;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewRadioWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewRadioWizardPage;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewRefresherWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewRefresherWizardPage;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewScrollWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewScrollWizardPage;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewSideMenuWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewSideMenuWizardPage;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewSlideboxWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewSlideboxWizardPage;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewTabWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewTabWizardPage;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewTabsWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewTabsWizardPage;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewTextInputWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewTextInputWizardPage;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewToggleWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewToggleWizardPage;
import org.jboss.tools.jst.jsp.test.palette.AbstractPaletteEntryTest;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewJQueryWidgetWizard;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizard;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteCategory;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteGroup;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteItem;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteVersionGroup;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteModelImpl;
import org.jboss.tools.jst.web.ui.palette.model.PaletteModel;
import org.jboss.tools.jst.web.ui.test.HTML5PaletteWizardTest;

/**
 * @author Alexey Kazakov
 */
public class IonicPaletteTest extends AbstractPaletteEntryTest implements IonicConstants {
	IEditorPart editor = null;

	public IonicPaletteTest() {}

	@Override
	public void setUp() {
		super.setUp();
		editor = openEditor(getFileName());
	}

	@Override
	protected void tearDown() throws Exception {
		if(currentDialog != null) {
			currentDialog.close();
		}
		if(editor != null) {
			editor.getSite().getPage().closeEditor(editor, false);
			editor = null;
		}
		super.tearDown();
	}

	protected String getFileName() {
		return "a14.html";
	}

	protected IonicVersion getVersion() {
		return IonicVersion.IONIC_1_0;
	}

	public IWizardPage runToolEntry(String entry, boolean wizardExpected) {
		IWizardPage result = runToolEntry("Ionic", entry, wizardExpected);
		if(wizardExpected) {
			assertTrue(result instanceof NewIonicWidgetWizardPage);
			NewIonicWidgetWizardPage page = (NewIonicWidgetWizardPage)result;
			assertEquals(getVersion(), page.getWizard().getVersion());
		}
		return result;
	}

	public void testNewContentWizard() {
		IWizardPage currentPage = runToolEntry("Content", true);

		assertTrue(currentPage instanceof NewContentWizardPage);

		NewContentWizardPage wizardPage = (NewContentWizardPage)currentPage;
		NewContentWizard wizard = (NewContentWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Content");

		assertTextExists(wizard, TAG_ION_CONTENT);

		assertTextDoesNotExist(wizard, ATTR_SCROLL);
		wizardPage.setEditorValue(ATTR_SCROLL, FALSE);
		assertAttrExists(wizard, ATTR_SCROLL, FALSE);
		wizardPage.setEditorValue(ATTR_SCROLL, TRUE);
		assertTextDoesNotExist(wizard, ATTR_SCROLL);

		assertTextDoesNotExist(wizard, ATTR_OVERFLOW_SCROLL);
		wizardPage.setEditorValue(ATTR_OVERFLOW_SCROLL, TRUE);
		assertAttrExists(wizard, ATTR_OVERFLOW_SCROLL, TRUE);
		wizardPage.setEditorValue(ATTR_OVERFLOW_SCROLL, FALSE);
		assertTextDoesNotExist(wizard, ATTR_OVERFLOW_SCROLL);

		assertTextDoesNotExist(wizard, ATTR_DIRECTION);
		wizardPage.setEditorValue(ATTR_DIRECTION, "xy");
		assertAttrExists(wizard, ATTR_DIRECTION, "xy");
		wizardPage.setEditorValue(ATTR_DIRECTION, "");
		assertTextDoesNotExist(wizard, ATTR_DIRECTION);
		wizardPage.setEditorValue(ATTR_DIRECTION, "xy");

		assertTextDoesNotExist(wizard, ATTR_PADDING);
		wizardPage.setEditorValue(ATTR_PADDING, TRUE);
		assertAttrExists(wizard, ATTR_PADDING, TRUE);
		wizardPage.setEditorValue(ATTR_PADDING, FALSE);
		assertAttrExists(wizard, ATTR_PADDING, FALSE);
		wizardPage.setEditorValue(ATTR_PADDING, "");
		assertTextDoesNotExist(wizard, ATTR_PADDING);

		assertTextDoesNotExist(wizard, ATTR_START_Y);
		wizardPage.setEditorValue(ATTR_START_Y, "11");
		assertAttrExists(wizard, ATTR_START_Y, "11");
		wizardPage.setEditorValue(ATTR_START_Y, "");
		assertTextDoesNotExist(wizard, ATTR_START_Y);

		assertTextDoesNotExist(wizard, ATTR_SCROLLBAR_X);
		wizardPage.setEditorValue(ATTR_SCROLLBAR_X, FALSE);
		assertAttrExists(wizard, ATTR_SCROLLBAR_X, FALSE);
		wizardPage.setEditorValue(ATTR_SCROLLBAR_X, TRUE);
		assertTextDoesNotExist(wizard, ATTR_SCROLLBAR_X);

		assertTextDoesNotExist(wizard, ATTR_SCROLLBAR_Y);
		wizardPage.setEditorValue(ATTR_SCROLLBAR_Y, FALSE);
		assertAttrExists(wizard, ATTR_SCROLLBAR_Y, FALSE);
		wizardPage.setEditorValue(ATTR_SCROLLBAR_Y, TRUE);
		assertTextDoesNotExist(wizard, ATTR_SCROLLBAR_Y);

		assertTextDoesNotExist(wizard, ATTR_ON_SCROLL);
		wizardPage.setEditorValue(ATTR_ON_SCROLL, "x()");
		assertAttrExists(wizard, ATTR_ON_SCROLL, "x()");
		wizardPage.setEditorValue(ATTR_ON_SCROLL, "");
		assertTextDoesNotExist(wizard, ATTR_ON_SCROLL);

		assertTextDoesNotExist(wizard, ATTR_ON_SCROLL_COMPLETE);
		wizardPage.setEditorValue(ATTR_ON_SCROLL_COMPLETE, "x()");
		assertAttrExists(wizard, ATTR_ON_SCROLL_COMPLETE, "x()");
		wizardPage.setEditorValue(ATTR_ON_SCROLL_COMPLETE, "");
		assertTextDoesNotExist(wizard, ATTR_ON_SCROLL_COMPLETE);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewScrollWizard() {
		IWizardPage currentPage = runToolEntry("Scroll", true);

		assertTrue(currentPage instanceof NewScrollWizardPage);

		NewScrollWizardPage wizardPage = (NewScrollWizardPage)currentPage;
		NewScrollWizard wizard = (NewScrollWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Scroll");

		assertTextExists(wizard, TAG_ION_SCROLL);

		assertTextDoesNotExist(wizard, ATTR_DIRECTION);
		wizardPage.setEditorValue(ATTR_DIRECTION, "xy");
		assertAttrExists(wizard, ATTR_DIRECTION, "xy");
		wizardPage.setEditorValue(ATTR_DIRECTION, "");
		assertTextDoesNotExist(wizard, ATTR_DIRECTION);
		wizardPage.setEditorValue(ATTR_DIRECTION, "xy");

		assertTextDoesNotExist(wizard, ATTR_HAS_BOUNCING);
		wizardPage.setEditorValue(ATTR_HAS_BOUNCING, TRUE);
		assertAttrExists(wizard, ATTR_HAS_BOUNCING, TRUE);
		wizardPage.setEditorValue(ATTR_HAS_BOUNCING, FALSE);
		assertAttrExists(wizard, ATTR_HAS_BOUNCING, FALSE);
		wizardPage.setEditorValue(ATTR_HAS_BOUNCING, "");
		assertTextDoesNotExist(wizard, ATTR_HAS_BOUNCING);

		assertTextDoesNotExist(wizard, ATTR_SCROLLBAR_X);
		wizardPage.setEditorValue(ATTR_SCROLLBAR_X, FALSE);
		assertAttrExists(wizard, ATTR_SCROLLBAR_X, FALSE);
		wizardPage.setEditorValue(ATTR_SCROLLBAR_X, TRUE);
		assertTextDoesNotExist(wizard, ATTR_SCROLLBAR_X);

		assertTextDoesNotExist(wizard, ATTR_SCROLLBAR_Y);
		wizardPage.setEditorValue(ATTR_SCROLLBAR_Y, FALSE);
		assertAttrExists(wizard, ATTR_SCROLLBAR_Y, FALSE);
		wizardPage.setEditorValue(ATTR_SCROLLBAR_Y, TRUE);
		assertTextDoesNotExist(wizard, ATTR_SCROLLBAR_Y);

		assertTextDoesNotExist(wizard, ATTR_PAGING);
		wizardPage.setEditorValue(ATTR_PAGING, TRUE);
		assertAttrExists(wizard, ATTR_PAGING, TRUE);
		wizardPage.setEditorValue(ATTR_PAGING, FALSE);
		assertTextDoesNotExist(wizard, ATTR_PAGING);

		assertTextDoesNotExist(wizard, ATTR_ZOOMING);
		wizardPage.setEditorValue(ATTR_ZOOMING, TRUE);
		assertAttrExists(wizard, ATTR_ZOOMING, TRUE);
		wizardPage.setEditorValue(ATTR_ZOOMING, FALSE);
		assertTextDoesNotExist(wizard, ATTR_ZOOMING);
		wizardPage.setEditorValue(ATTR_ZOOMING, TRUE);
		assertAttrExists(wizard, ATTR_ZOOMING, TRUE);

		assertTextDoesNotExist(wizard, ATTR_MIN_ZOOM);
		wizardPage.setEditorValue(ATTR_MIN_ZOOM, "0.1");
		assertAttrExists(wizard, ATTR_MIN_ZOOM, "0.1");
		wizardPage.setEditorValue(ATTR_MIN_ZOOM, "");
		assertTextDoesNotExist(wizard, ATTR_MIN_ZOOM);

		assertTextDoesNotExist(wizard, ATTR_MAX_ZOOM);
		wizardPage.setEditorValue(ATTR_MAX_ZOOM, "10");
		assertAttrExists(wizard, ATTR_MAX_ZOOM, "10");
		wizardPage.setEditorValue(ATTR_MAX_ZOOM, "");
		assertTextDoesNotExist(wizard, ATTR_MAX_ZOOM);

		assertTextDoesNotExist(wizard, ATTR_ON_SCROLL + "=");
		wizardPage.setEditorValue(ATTR_ON_SCROLL, "x()");
		assertAttrExists(wizard, ATTR_ON_SCROLL, "x()");
		wizardPage.setEditorValue(ATTR_ON_SCROLL, "");
		assertTextDoesNotExist(wizard, ATTR_ON_SCROLL + "=");

		assertTextDoesNotExist(wizard, ATTR_ON_REFRESH);
		wizardPage.setEditorValue(ATTR_ON_REFRESH, "x()");
		assertAttrExists(wizard, ATTR_ON_REFRESH, "x()");
		wizardPage.setEditorValue(ATTR_ON_REFRESH, "");
		assertTextDoesNotExist(wizard, ATTR_ON_REFRESH);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewHeaderWizard() {
		IWizardPage currentPage = runToolEntry("Header Bar", true);
		assertTrue(currentPage instanceof NewHeaderBarWizardPage);

		NewHeaderBarWizardPage wizardPage = (NewHeaderBarWizardPage)currentPage;
		NewHeaderBarWizard wizard = (NewHeaderBarWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Header Bar");

		assertTextExists(wizard, ">Title<");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_TITLE, "Title1");
		assertTextExists(wizard, ">Title1<");

		wizardPage.setEditorValue(EDITOR_ID_BAR_COLOR, "bar-calm");
		assertAttrExists(wizard, ATTR_CLASS, "bar-calm");
		wizardPage.setEditorValue(EDITOR_ID_BAR_COLOR, "");
		
		assertTextDoesNotExist(wizard, ATTR_ALIGN_TITLE);
		wizardPage.setEditorValue(ATTR_ALIGN_TITLE, "left");
		assertAttrExists(wizard, ATTR_ALIGN_TITLE, "left");
		wizardPage.setEditorValue(ATTR_ALIGN_TITLE, "");
		assertTextDoesNotExist(wizard, ATTR_ALIGN_TITLE);
	
		assertTextExists(wizard, "Left button");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_LEFT_BUTTON, FALSE);
		assertTextDoesNotExist(wizard, "Left button");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_LEFT_BUTTON, TRUE);
		assertTextExists(wizard, "Left button");

		assertTextExists(wizard, "Right button");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_RIGHT_BUTTON, FALSE);
		assertTextDoesNotExist(wizard, "Right button");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_RIGHT_BUTTON, TRUE);
		assertTextExists(wizard, "Right button");

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewFooterWizard() {
		IWizardPage currentPage = runToolEntry("Footer Bar", true);
		assertTrue(currentPage instanceof NewFooterBarWizardPage);

		NewFooterBarWizardPage wizardPage = (NewFooterBarWizardPage)currentPage;
		NewFooterBarWizard wizard = (NewFooterBarWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Footer Bar");

		assertTextExists(wizard, ">Title<");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_TITLE, "Title1");
		assertTextExists(wizard, ">Title1<");

		wizardPage.setEditorValue(EDITOR_ID_BAR_COLOR, "bar-calm");
		assertAttrExists(wizard, ATTR_CLASS, "bar-calm");
		wizardPage.setEditorValue(EDITOR_ID_BAR_COLOR, "");
		
		assertTextDoesNotExist(wizard, ATTR_ALIGN_TITLE);
		wizardPage.setEditorValue(ATTR_ALIGN_TITLE, "left");
		assertAttrExists(wizard, ATTR_ALIGN_TITLE, "left");
		wizardPage.setEditorValue(ATTR_ALIGN_TITLE, "");
		assertTextDoesNotExist(wizard, ATTR_ALIGN_TITLE);
	
		assertTextExists(wizard, "Left button");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_LEFT_BUTTON, FALSE);
		assertTextDoesNotExist(wizard, "Left button");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_LEFT_BUTTON, TRUE);
		assertTextExists(wizard, "Left button");

		assertTextExists(wizard, "Right button");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_RIGHT_BUTTON, FALSE);
		assertTextDoesNotExist(wizard, "Right button");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_RIGHT_BUTTON, TRUE);
		assertTextExists(wizard, "Right button");

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewTabsWizard() {
		IWizardPage currentPage = runToolEntry("Tabs", true);
		assertTrue(currentPage instanceof NewTabsWizardPage);

		NewTabsWizardPage wizardPage = (NewTabsWizardPage)currentPage;
		NewTabsWizard wizard = (NewTabsWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Tabs");

		assertEquals("3", wizardPage.getEditorValue(JQueryConstants.EDITOR_ID_NUMBER_OF_ITEMS));

		assertTextDoesNotExist(wizard, "Tab 4");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_NUMBER_OF_ITEMS, "4");
		assertEquals("4", wizardPage.getEditorValue(JQueryConstants.EDITOR_ID_NUMBER_OF_ITEMS));
		assertTextExists(wizard, "Tab 4");		
	
		wizardPage.setEditorValue(EDITOR_ID_TABS_COLOR, "tabs-calm");
		assertAttrExists(wizard, ATTR_CLASS, "tabs-calm");
		wizardPage.setEditorValue(EDITOR_ID_TABS_COLOR, "");

		String tabsIconLeft = "tabs-icon-left";
		assertTextDoesNotExist(wizard, tabsIconLeft);
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_ICON_POS, "left");
		assertAttrExists(wizard, ATTR_CLASS, tabsIconLeft);
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_ICON_POS, "");
		assertTextDoesNotExist(wizard, tabsIconLeft);

		assertTextDoesNotExist(wizard, CLASS_TABS_ITEM_HIDE);
		wizardPage.setEditorValue(CLASS_TABS_ITEM_HIDE, TRUE);
		assertAttrExists(wizard, ATTR_CLASS, CLASS_TABS_ITEM_HIDE);
		wizardPage.setEditorValue(CLASS_TABS_ITEM_HIDE, FALSE);
		assertTextDoesNotExist(wizard, CLASS_TABS_ITEM_HIDE);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewTabWizard() {
		IWizardPage currentPage = runToolEntry("Tab", true);
		assertTrue(currentPage instanceof NewTabWizardPage);

		NewTabWizardPage wizardPage = (NewTabWizardPage)currentPage;
		NewTabWizard wizard = (NewTabWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Tab");

		assertAttrExists(wizard, ATTR_TITLE, "Title");
		wizardPage.setEditorValue(ATTR_TITLE, "x");
		assertAttrExists(wizard, ATTR_TITLE, "x");
		wizardPage.setEditorValue(ATTR_TITLE, "Title");

		assertTextDoesNotExist(wizard, ATTR_HREF);
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_URL, "www");
		assertAttrExists(wizard, ATTR_HREF, "www");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_URL, "");
		assertTextDoesNotExist(wizard, ATTR_HREF);

		assertTextDoesNotExist(wizard, ATTR_ICON);
		wizardPage.setEditorValue(ATTR_ICON, "ion-alert");
		assertAttrExists(wizard, ATTR_ICON, "ion-alert");
		wizardPage.setEditorValue(ATTR_ICON, "");
		assertTextDoesNotExist(wizard, ATTR_ICON);

		assertTextDoesNotExist(wizard, ATTR_ICON_ON);
		wizardPage.setEditorValue(ATTR_ICON_ON, "ion-alert");
		assertAttrExists(wizard, ATTR_ICON_ON, "ion-alert");
		wizardPage.setEditorValue(ATTR_ICON_ON, "");
		assertTextDoesNotExist(wizard, ATTR_ICON_ON);

		assertTextDoesNotExist(wizard, ATTR_ICON_OFF);
		wizardPage.setEditorValue(ATTR_ICON_OFF, "ion-alert");
		assertAttrExists(wizard, ATTR_ICON_OFF, "ion-alert");
		wizardPage.setEditorValue(ATTR_ICON_OFF, "");
		assertTextDoesNotExist(wizard, ATTR_ICON_OFF);

		assertTextDoesNotExist(wizard, ATTR_BADGE);
		wizardPage.setEditorValue(ATTR_BADGE, "1");
		assertAttrExists(wizard, ATTR_BADGE, "1");
		wizardPage.setEditorValue(ATTR_BADGE, "");
		assertTextDoesNotExist(wizard, ATTR_BADGE);

		assertTextDoesNotExist(wizard, ATTR_BADGE_STYLE);
		wizardPage.setEditorValue(ATTR_BADGE_STYLE, "tabs-positive");
		assertAttrExists(wizard, ATTR_BADGE_STYLE, "tabs-positive");
		wizardPage.setEditorValue(ATTR_BADGE_STYLE, "");
		assertTextDoesNotExist(wizard, ATTR_BADGE_STYLE);

		assertTextDoesNotExist(wizard, ATTR_ON_SELECT);
		wizardPage.setEditorValue(ATTR_ON_SELECT, "x()");
		assertAttrExists(wizard, ATTR_ON_SELECT, "x()");
		wizardPage.setEditorValue(ATTR_ON_SELECT, "");
		assertTextDoesNotExist(wizard, ATTR_ON_SELECT);

		assertTextDoesNotExist(wizard, ATTR_ON_DESELECT);
		wizardPage.setEditorValue(ATTR_ON_DESELECT, "x()");
		assertAttrExists(wizard, ATTR_ON_DESELECT, "x()");
		wizardPage.setEditorValue(ATTR_ON_DESELECT, "");
		assertTextDoesNotExist(wizard, ATTR_ON_DESELECT);

		assertTextDoesNotExist(wizard, ATTR_NG_CLICK);
		wizardPage.setEditorValue(ATTR_NG_CLICK, "c()");
		assertAttrExists(wizard, ATTR_NG_CLICK, "c()");
		wizardPage.setEditorValue(ATTR_NG_CLICK, "");
		assertTextDoesNotExist(wizard, ATTR_NG_CLICK);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewSlideboxWizard() {
		IWizardPage currentPage = runToolEntry("Slidebox", true);
		assertTrue(currentPage instanceof NewSlideboxWizardPage);

		NewSlideboxWizardPage wizardPage = (NewSlideboxWizardPage)currentPage;
		NewSlideboxWizard wizard = (NewSlideboxWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Slidebox");

		assertEquals("3", wizardPage.getEditorValue(JQueryConstants.EDITOR_ID_NUMBER_OF_ITEMS));

		assertTextDoesNotExist(wizard, ATTR_DELEGATE_HANDLE);
		wizardPage.setEditorValue(ATTR_DELEGATE_HANDLE, "mySlidebox");
		assertAttrExists(wizard, ATTR_DELEGATE_HANDLE, "mySlidebox");
		wizardPage.setEditorValue(ATTR_DELEGATE_HANDLE, "");
		assertTextDoesNotExist(wizard, ATTR_DELEGATE_HANDLE);

		assertTextDoesNotExist(wizard, ATTR_DOES_CONTINUE);
		wizardPage.setEditorValue(ATTR_DOES_CONTINUE, TRUE);
		assertAttrExists(wizard, ATTR_DOES_CONTINUE, TRUE);
			assertTextDoesNotExist(wizard, ATTR_SLIDE_INTERVAL);
			wizardPage.setEditorValue(ATTR_SLIDE_INTERVAL, "3333");
			assertAttrExists(wizard, ATTR_SLIDE_INTERVAL, "3333");
			wizardPage.setEditorValue(ATTR_SLIDE_INTERVAL, "");
			assertTextDoesNotExist(wizard, ATTR_SLIDE_INTERVAL);
		wizardPage.setEditorValue(ATTR_DOES_CONTINUE, FALSE);
		assertTextDoesNotExist(wizard, ATTR_DOES_CONTINUE);

		assertTextDoesNotExist(wizard, ATTR_AUTO_PLAY);
		wizardPage.setEditorValue(ATTR_AUTO_PLAY, TRUE);
		assertAttrExists(wizard, ATTR_AUTO_PLAY, TRUE);
		wizardPage.setEditorValue(ATTR_AUTO_PLAY, FALSE);
		assertAttrExists(wizard, ATTR_AUTO_PLAY, FALSE);
		wizardPage.setEditorValue(ATTR_AUTO_PLAY, "");
		assertTextDoesNotExist(wizard, ATTR_AUTO_PLAY);

		assertTextDoesNotExist(wizard, ATTR_SHOW_PAGER);
		wizardPage.setEditorValue(ATTR_SHOW_PAGER, FALSE);
		assertAttrExists(wizard, ATTR_SHOW_PAGER, FALSE);
		wizardPage.setEditorValue(ATTR_SHOW_PAGER, TRUE);
		assertTextDoesNotExist(wizard, ATTR_SHOW_PAGER);

		assertTextDoesNotExist(wizard, ATTR_PAGER_CLICK);
		wizardPage.setEditorValue(ATTR_PAGER_CLICK, "a($index)");
		assertAttrExists(wizard, ATTR_PAGER_CLICK, "a($index)");
		wizardPage.setEditorValue(ATTR_PAGER_CLICK, "");
		assertTextDoesNotExist(wizard, ATTR_PAGER_CLICK);

		assertTextDoesNotExist(wizard, ATTR_ON_SLIDE_CHANGED);
		wizardPage.setEditorValue(ATTR_ON_SLIDE_CHANGED, "a($index)");
		assertAttrExists(wizard, ATTR_ON_SLIDE_CHANGED, "a($index)");
		wizardPage.setEditorValue(ATTR_ON_SLIDE_CHANGED, "");
		assertTextDoesNotExist(wizard, ATTR_ON_SLIDE_CHANGED);

		assertTextDoesNotExist(wizard, ATTR_ACTIVE_SLIDE);
		wizardPage.setEditorValue(ATTR_ACTIVE_SLIDE, "slideModel");
		assertAttrExists(wizard, ATTR_ACTIVE_SLIDE, "slideModel");
		wizardPage.setEditorValue(ATTR_ACTIVE_SLIDE, "");
		assertTextDoesNotExist(wizard, ATTR_ACTIVE_SLIDE);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewCheckboxWizard() {
		IWizardPage currentPage = runToolEntry("Checkbox", true);
		assertTrue(currentPage instanceof NewCheckBoxWizardPage);

		NewCheckBoxWizardPage wizardPage = (NewCheckBoxWizardPage)currentPage;
		NewCheckBoxWizard wizard = (NewCheckBoxWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Checkbox");

		assertTextExists(wizard, "I agree");
		wizardPage.setEditorValue(TAG_LABEL, "someLabel");
		assertTextExists(wizard, "someLabel");
		wizardPage.setEditorValue(TAG_LABEL, "I agree");
		
		assertTextDoesNotExist(wizard, ATTR_NG_MODEL);
		wizardPage.setEditorValue(ATTR_NG_MODEL, "aModel");
		assertAttrExists(wizard, ATTR_NG_MODEL, "aModel");
		wizardPage.setEditorValue(ATTR_NG_MODEL, "");
		assertTextDoesNotExist(wizard, ATTR_NG_MODEL);

		assertTextDoesNotExist(wizard, ATTR_NAME);
		wizardPage.setEditorValue(ATTR_NAME, "nn");
		assertAttrExists(wizard, ATTR_NAME, "nn");
		wizardPage.setEditorValue(ATTR_NAME, "");
		assertTextDoesNotExist(wizard, ATTR_NAME);

		assertTextDoesNotExist(wizard, ATTR_NG_CHECKED);
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_CHECKED, TRUE);
		assertAttrExists(wizard, ATTR_NG_CHECKED, TRUE);
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_CHECKED, FALSE);
		assertTextDoesNotExist(wizard, ATTR_NG_CHECKED);

		assertTextDoesNotExist(wizard, ATTR_NG_DISABLED);
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_DISABLED, TRUE);
		assertAttrExists(wizard, ATTR_NG_DISABLED, TRUE);
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_DISABLED, FALSE);
		assertTextDoesNotExist(wizard, ATTR_NG_DISABLED);

		assertTextDoesNotExist(wizard, ATTR_NG_TRUE_VALUE);
		wizardPage.setEditorValue(ATTR_NG_TRUE_VALUE, "yes");
		assertAttrExists(wizard, ATTR_NG_TRUE_VALUE, "yes");
		wizardPage.setEditorValue(ATTR_NG_TRUE_VALUE, "");
		assertTextDoesNotExist(wizard, ATTR_NG_TRUE_VALUE);

		assertTextDoesNotExist(wizard, ATTR_NG_FALSE_VALUE);
		wizardPage.setEditorValue(ATTR_NG_FALSE_VALUE, "no");
		assertAttrExists(wizard, ATTR_NG_FALSE_VALUE, "no");
		wizardPage.setEditorValue(ATTR_NG_FALSE_VALUE, "");
		assertTextDoesNotExist(wizard, ATTR_NG_FALSE_VALUE);

		assertTextDoesNotExist(wizard, ATTR_NG_CHANGE);
		wizardPage.setEditorValue(ATTR_NG_CHANGE, "a()");
		assertAttrExists(wizard, ATTR_NG_CHANGE, "a()");
		wizardPage.setEditorValue(ATTR_NG_CHANGE, "");
		assertTextDoesNotExist(wizard, ATTR_NG_CHANGE);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewToggleWizard() {
		IWizardPage currentPage = runToolEntry("Toggle", true);
		assertTrue(currentPage instanceof NewToggleWizardPage);

		NewToggleWizardPage wizardPage = (NewToggleWizardPage)currentPage;
		NewToggleWizard wizard = (NewToggleWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Toggle");

		assertTextExists(wizard, "Label");
		wizardPage.setEditorValue(TAG_LABEL, "someLabel");
		assertTextExists(wizard, "someLabel");
		wizardPage.setEditorValue(TAG_LABEL, "Label");
		
		assertTextDoesNotExist(wizard, ATTR_NG_MODEL);
		wizardPage.setEditorValue(ATTR_NG_MODEL, "aModel");
		assertAttrExists(wizard, ATTR_NG_MODEL, "aModel");
		wizardPage.setEditorValue(ATTR_NG_MODEL, "");
		assertTextDoesNotExist(wizard, ATTR_NG_MODEL);

		assertTextDoesNotExist(wizard, ATTR_NAME);
		wizardPage.setEditorValue(ATTR_NAME, "nn");
		assertAttrExists(wizard, ATTR_NAME, "nn");
		wizardPage.setEditorValue(ATTR_NAME, "");
		assertTextDoesNotExist(wizard, ATTR_NAME);

		assertTextDoesNotExist(wizard, ATTR_NG_CHECKED);
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_CHECKED, TRUE);
		assertAttrExists(wizard, ATTR_NG_CHECKED, TRUE);
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_CHECKED, FALSE);
		assertTextDoesNotExist(wizard, ATTR_NG_CHECKED);

		assertTextDoesNotExist(wizard, ATTR_NG_DISABLED);
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_DISABLED, TRUE);
		assertAttrExists(wizard, ATTR_NG_DISABLED, TRUE);
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_DISABLED, FALSE);
		assertTextDoesNotExist(wizard, ATTR_NG_DISABLED);

		assertTextDoesNotExist(wizard, ATTR_NG_TRUE_VALUE);
		wizardPage.setEditorValue(ATTR_NG_TRUE_VALUE, "yes");
		assertAttrExists(wizard, ATTR_NG_TRUE_VALUE, "yes");
		wizardPage.setEditorValue(ATTR_NG_TRUE_VALUE, "");
		assertTextDoesNotExist(wizard, ATTR_NG_TRUE_VALUE);

		assertTextDoesNotExist(wizard, ATTR_NG_FALSE_VALUE);
		wizardPage.setEditorValue(ATTR_NG_FALSE_VALUE, "no");
		assertAttrExists(wizard, ATTR_NG_FALSE_VALUE, "no");
		wizardPage.setEditorValue(ATTR_NG_FALSE_VALUE, "");
		assertTextDoesNotExist(wizard, ATTR_NG_FALSE_VALUE);

		assertTextDoesNotExist(wizard, ATTR_NG_CHANGE);
		wizardPage.setEditorValue(ATTR_NG_CHANGE, "a()");
		assertAttrExists(wizard, ATTR_NG_CHANGE, "a()");
		wizardPage.setEditorValue(ATTR_NG_CHANGE, "");
		assertTextDoesNotExist(wizard, ATTR_NG_CHANGE);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewRadioWizard() {
		IWizardPage currentPage = runToolEntry("Radio", true);
		assertTrue(currentPage instanceof NewRadioWizardPage);

		NewRadioWizardPage wizardPage = (NewRadioWizardPage)currentPage;
		NewRadioWizard wizard = (NewRadioWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Radio");

		assertEquals("3", wizardPage.getEditorValue(JQueryConstants.EDITOR_ID_NUMBER_OF_ITEMS));

		assertTextDoesNotExist(wizard, ATTR_NG_MODEL);
		wizardPage.setEditorValue(ATTR_NG_MODEL, "aModel");
		assertAttrExists(wizard, ATTR_NG_MODEL, "aModel");
		wizardPage.setEditorValue(ATTR_NG_MODEL, "");
		assertTextDoesNotExist(wizard, ATTR_NG_MODEL);

		assertTextDoesNotExist(wizard, ATTR_NAME);
		wizardPage.setEditorValue(ATTR_NAME, "nn");
		assertAttrExists(wizard, ATTR_NAME, "nn");
		wizardPage.setEditorValue(ATTR_NAME, "");
		assertTextDoesNotExist(wizard, ATTR_NAME);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewSideMenuWizard() {
		IWizardPage currentPage = runToolEntry("Side Menu", true);
		assertTrue(currentPage instanceof NewSideMenuWizardPage);

		NewSideMenuWizardPage wizardPage = (NewSideMenuWizardPage)currentPage;
		NewSideMenuWizard wizard = (NewSideMenuWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Side Menu");

		assertTextDoesNotExist(wizard, ATTR_DELEGATE_HANDLE);
		wizardPage.setEditorValue(ATTR_DELEGATE_HANDLE, "mySidemenu");
		assertAttrExists(wizard, ATTR_DELEGATE_HANDLE, "mySidemenu");
		wizardPage.setEditorValue(ATTR_DELEGATE_HANDLE, "");
		assertTextDoesNotExist(wizard, ATTR_DELEGATE_HANDLE);

		assertTextDoesNotExist(wizard, ATTR_DRAG_CONTENT);
		wizardPage.setEditorValue(ATTR_DRAG_CONTENT, FALSE);
		assertAttrExists(wizard, ATTR_DRAG_CONTENT, FALSE);
		wizardPage.setEditorValue(ATTR_DRAG_CONTENT, TRUE);
		assertTextDoesNotExist(wizard, ATTR_DRAG_CONTENT);

		assertTextDoesNotExist(wizard, ATTR_EDGE_DRAG_THRESHOLD);
		wizardPage.setEditorValue(ATTR_EDGE_DRAG_THRESHOLD, "0");
		assertAttrExists(wizard, ATTR_EDGE_DRAG_THRESHOLD, "0");
		wizardPage.setEditorValue(ATTR_EDGE_DRAG_THRESHOLD, "");
		assertTextDoesNotExist(wizard, ATTR_EDGE_DRAG_THRESHOLD);

		assertEquals(TRUE, wizardPage.getEditorValue(EDITOR_ID_LEFT_MENU));

		assertTextDoesNotExist(wizard, ATTR_IS_ENABLED);
		wizardPage.setEditorValue(EDITOR_ID_LEFT_IS_ENABLED, FALSE);
		assertAttrExists(wizard, ATTR_IS_ENABLED, FALSE);
		wizardPage.setEditorValue(EDITOR_ID_LEFT_IS_ENABLED, TRUE);
		assertTextDoesNotExist(wizard, ATTR_IS_ENABLED);

		assertTextDoesNotExist(wizard, ATTR_WIDTH);
		wizardPage.setEditorValue(EDITOR_ID_LEFT_WIDTH, "100");
		assertAttrExists(wizard, ATTR_WIDTH, "100");
		wizardPage.setEditorValue(EDITOR_ID_LEFT_WIDTH, "");
		assertTextDoesNotExist(wizard, ATTR_WIDTH);

		String rightMenuTitle = "<h4>Right</h4>";
		assertTextDoesNotExist(wizard, rightMenuTitle);
		assertEquals(FALSE, wizardPage.getEditorValue(EDITOR_ID_RIGHT_MENU));
		wizardPage.setEditorValue(EDITOR_ID_RIGHT_MENU, TRUE);
		assertTextExists(wizard, rightMenuTitle);
		wizardPage.setEditorValue(EDITOR_ID_RIGHT_MENU, FALSE);
		assertTextDoesNotExist(wizard, rightMenuTitle);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewListWizard() {
		IWizardPage currentPage = runToolEntry("List", true);
		assertTrue(currentPage instanceof NewListWizardPage);

		NewListWizardPage wizardPage = (NewListWizardPage)currentPage;
		NewListWizard wizard = (NewListWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "List");

		assertEquals("3", wizardPage.getEditorValue(JQueryConstants.EDITOR_ID_NUMBER_OF_ITEMS));

		assertTextDoesNotExist(wizard, ATTR_DELEGATE_HANDLE);
		wizardPage.setEditorValue(ATTR_DELEGATE_HANDLE, "mySlidebox");
		assertAttrExists(wizard, ATTR_DELEGATE_HANDLE, "mySlidebox");
		wizardPage.setEditorValue(ATTR_DELEGATE_HANDLE, "");
		assertTextDoesNotExist(wizard, ATTR_DELEGATE_HANDLE);

		assertTextDoesNotExist(wizard, ATTR_SHOW_DELETE);
		wizardPage.setEditorValue(ATTR_SHOW_DELETE, TRUE);
		assertAttrExists(wizard, ATTR_SHOW_DELETE, TRUE);
		wizardPage.setEditorValue(ATTR_SHOW_DELETE, FALSE);
		assertTextDoesNotExist(wizard, ATTR_SHOW_DELETE);

		assertTextDoesNotExist(wizard, ATTR_SHOW_REORDER);
		wizardPage.setEditorValue(ATTR_SHOW_REORDER, TRUE);
		assertAttrExists(wizard, ATTR_SHOW_REORDER, TRUE);
		wizardPage.setEditorValue(ATTR_SHOW_REORDER, FALSE);
		assertTextDoesNotExist(wizard, ATTR_SHOW_REORDER);

		assertTextDoesNotExist(wizard, ATTR_CAN_SWIPE);
		wizardPage.setEditorValue(ATTR_CAN_SWIPE, FALSE);
		assertAttrExists(wizard, ATTR_CAN_SWIPE, FALSE);
		wizardPage.setEditorValue(ATTR_CAN_SWIPE, TRUE);
		assertTextDoesNotExist(wizard, ATTR_CAN_SWIPE);

		assertTextDoesNotExist(wizard, "Item 4");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_NUMBER_OF_ITEMS, "4");
		assertEquals("4", wizardPage.getEditorValue(JQueryConstants.EDITOR_ID_NUMBER_OF_ITEMS));
		assertTextExists(wizard, "Item 4");		
	
		assertTextDoesNotExist(wizard, CLASS_ITEM_DIVIDER);
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_DIVIDER, TRUE);
		assertAttrExists(wizard, ATTR_CLASS, CLASS_ITEM_DIVIDER);
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_DIVIDER, FALSE);
		assertTextDoesNotExist(wizard, CLASS_ITEM_DIVIDER);

		assertTextDoesNotExist(wizard, "icon ion-alert");
		wizardPage.setEditorValue(ATTR_ICON, "ion-alert");
		assertAttrExists(wizard, ATTR_CLASS, "icon ion-alert");
		wizardPage.setEditorValue(ATTR_ICON, "");
		assertTextDoesNotExist(wizard, "icon ion-alert");

		assertTextExists(wizard, TAG_ION_DELETE_BUTTON);
		wizardPage.setEditorValue(EDITOR_ID_DELETE_BUTTON, FALSE);
		assertTextDoesNotExist(wizard, TAG_ION_DELETE_BUTTON);
		wizardPage.setEditorValue(EDITOR_ID_DELETE_BUTTON, TRUE);
		assertTextExists(wizard, TAG_ION_DELETE_BUTTON);

		assertTextExists(wizard, TAG_ION_REORDER_BUTTON);
		wizardPage.setEditorValue(EDITOR_ID_REORDER_BUTTON, FALSE);
		assertTextDoesNotExist(wizard, TAG_ION_REORDER_BUTTON);
		wizardPage.setEditorValue(EDITOR_ID_REORDER_BUTTON, TRUE);
		assertTextExists(wizard, TAG_ION_REORDER_BUTTON);

		assertTextDoesNotExist(wizard, TAG_ION_OPTION_BUTTON);
		wizardPage.setEditorValue(EDITOR_ID_OPTION_BUTTON, "abc");
		assertTextExists(wizard, TAG_ION_OPTION_BUTTON);
		wizardPage.setEditorValue(EDITOR_ID_OPTION_BUTTON, "");
		assertTextDoesNotExist(wizard, TAG_ION_OPTION_BUTTON);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewButtonWizard() {
		IWizardPage currentPage = runToolEntry("Button", true);
		assertTrue(currentPage instanceof NewButtonWizardPage);

		NewButtonWizardPage wizardPage = (NewButtonWizardPage)currentPage;
		NewButtonWizard wizard = (NewButtonWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Button");

		assertTextExists(wizard, "Home");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_LABEL, "Push it");
		assertTextExists(wizard, "Push it");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_LABEL, "Home");
		assertTextExists(wizard, "Home");

		assertTextExists(wizard, "<button ");
		wizardPage.setEditorValue(EDITOR_ID_BUTTON_TYPE, "a");
		assertTextExists(wizard, "<a ");
		wizardPage.setEditorValue(EDITOR_ID_BUTTON_TYPE, "button");
		assertTextExists(wizard, "<button ");

		assertTextDoesNotExist(wizard, "button-block");
		wizardPage.setEditorValue(EDITOR_ID_BUTTON_WIDTH, "button-block");
		assertTextExists(wizard, "button-block");
		wizardPage.setEditorValue(EDITOR_ID_BUTTON_WIDTH, "button-full");
		assertTextExists(wizard, "button-full");
		wizardPage.setEditorValue(EDITOR_ID_BUTTON_WIDTH, "normal");
		assertTextDoesNotExist(wizard, "button-block");

		assertTextDoesNotExist(wizard, "button-small");
		wizardPage.setEditorValue(EDITOR_ID_BUTTON_SIZE, "button-small");
		assertTextExists(wizard, "button-small");
		wizardPage.setEditorValue(EDITOR_ID_BUTTON_SIZE, "button-large");
		assertTextExists(wizard, "button-large");
		wizardPage.setEditorValue(EDITOR_ID_BUTTON_SIZE, "normal");
		assertTextDoesNotExist(wizard, "button-small");

		assertTextDoesNotExist(wizard, "button-outline");
		wizardPage.setEditorValue(EDITOR_ID_BUTTON_FILL, "button-outline");
		assertTextExists(wizard, "button-outline");
		wizardPage.setEditorValue(EDITOR_ID_BUTTON_FILL, "button-clear");
		assertTextExists(wizard, "button-clear");
		wizardPage.setEditorValue(EDITOR_ID_BUTTON_FILL, "normal");
		assertTextDoesNotExist(wizard, "button-outline");

		assertTextDoesNotExist(wizard, "icon-left");
		wizardPage.setEditorValue(JQueryConstants.ATTR_ICON, "ion-waterdrop");
		assertTextExists(wizard, "icon-left ion-waterdrop");
		wizardPage.setEditorValue(JQueryConstants.ATTR_ICON, "");
		assertTextDoesNotExist(wizard, "icon-left");


		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewTextInputWizard() {
		IWizardPage currentPage = runToolEntry("Text Input", true);
		assertTrue(currentPage instanceof NewTextInputWizardPage);

		NewTextInputWizardPage wizardPage = (NewTextInputWizardPage)currentPage;
		NewTextInputWizard wizard = (NewTextInputWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Text Input");

		assertAttrExists(wizard, ATTR_TYPE, "text");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_TEXT_TYPE, "number");
		assertAttrExists(wizard, ATTR_TYPE, "number");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_TEXT_TYPE, "text");
		assertAttrExists(wizard, ATTR_TYPE, "text");

		assertTextDoesNotExist(wizard, ATTR_NAME);
		wizardPage.setEditorValue(ATTR_NAME, "myInput");
		assertAttrExists(wizard, ATTR_NAME, "myInput");
		wizardPage.setEditorValue(ATTR_NAME, "");
		assertTextDoesNotExist(wizard, ATTR_NAME);

		assertTextDoesNotExist(wizard, ATTR_NG_MODEL);
		wizardPage.setEditorValue(ATTR_NG_MODEL, "model");
		assertAttrExists(wizard, ATTR_NG_MODEL, "model");
		wizardPage.setEditorValue(ATTR_NG_MODEL, "");
		assertTextDoesNotExist(wizard, ATTR_NG_MODEL);

		assertTextExists(wizard, "Input:");
		assertAttrExists(wizard, ATTR_CLASS, "input-label");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_LABEL, "Price");
		assertTextExists(wizard, "Price");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_LABEL, "");
		assertTextDoesNotExist(wizard, "input-label");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_LABEL, "Input:");
		assertTextExists(wizard, "Input:");

		assertAttrExists(wizard, ATTR_CLASS, "item item-input");
		wizardPage.setEditorValue(EDITOR_ID_INPUT_LABEL_STYLE, "");
		assertTextDoesNotExist(wizard, "item item-input");
		wizardPage.setEditorValue(EDITOR_ID_INPUT_LABEL_STYLE, "stacked");
		assertAttrExists(wizard, ATTR_CLASS, "item item-input item-stacked-label");
		wizardPage.setEditorValue(EDITOR_ID_INPUT_LABEL_STYLE, "floating");
		assertAttrExists(wizard, ATTR_CLASS, "item item-input item-floating-label");
		wizardPage.setEditorValue(EDITOR_ID_INPUT_LABEL_STYLE, "inline");
		assertAttrExists(wizard, ATTR_CLASS, "item item-input");

		assertAttrExists(wizard, JQueryConstants.ATTR_PLACEHOLDER, "Text");
		wizardPage.setEditorValue(JQueryConstants.ATTR_PLACEHOLDER, "xx");
		assertAttrExists(wizard, JQueryConstants.ATTR_PLACEHOLDER, "xx");
		wizardPage.setEditorValue(JQueryConstants.ATTR_PLACEHOLDER, "Text");
		assertAttrExists(wizard, JQueryConstants.ATTR_PLACEHOLDER, "Text");

		assertTextDoesNotExist(wizard, JQueryConstants.ATTR_REQUIRED);
		wizardPage.setEditorValue(JQueryConstants.ATTR_REQUIRED, TRUE);
		assertAttrExists(wizard, JQueryConstants.ATTR_REQUIRED, TRUE);
		wizardPage.setEditorValue(JQueryConstants.ATTR_REQUIRED, FALSE);
		assertTextDoesNotExist(wizard, JQueryConstants.ATTR_REQUIRED);

		assertTextDoesNotExist(wizard, JQueryConstants.ATTR_DISABLED);
		wizardPage.setEditorValue(JQueryConstants.ATTR_DISABLED, TRUE);
		assertAttrExists(wizard, JQueryConstants.ATTR_DISABLED, JQueryConstants.ATTR_DISABLED);
		wizardPage.setEditorValue(JQueryConstants.ATTR_DISABLED, FALSE);
		assertTextDoesNotExist(wizard, JQueryConstants.ATTR_DISABLED);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewRefresherWizard() {
		IWizardPage currentPage = runToolEntry("Refresher", true);
		assertTrue(currentPage instanceof NewRefresherWizardPage);

		NewRefresherWizardPage wizardPage = (NewRefresherWizardPage)currentPage;
		NewRefresherWizard wizard = (NewRefresherWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Refresher");

		assertTextDoesNotExist(wizard, ATTR_ON_PULLING);
		wizardPage.setEditorValue(ATTR_ON_PULLING, "doPull()");
		assertAttrExists(wizard, ATTR_ON_PULLING, "doPull()");
		wizardPage.setEditorValue(ATTR_ON_PULLING, "");
		assertTextDoesNotExist(wizard, ATTR_ON_PULLING);

		assertTextDoesNotExist(wizard, ATTR_ON_REFRESH + "=");
		wizardPage.setEditorValue(ATTR_ON_REFRESH, "doRefresh()");
		assertAttrExists(wizard, ATTR_ON_REFRESH, "doRefresh()");
		wizardPage.setEditorValue(ATTR_ON_REFRESH, "");
		assertTextDoesNotExist(wizard, ATTR_ON_REFRESH + "=");

		assertTextDoesNotExist(wizard, ATTR_PULLING_ICON);
		wizardPage.setEditorValue(ATTR_PULLING_ICON, "ion-a");
		assertAttrExists(wizard, ATTR_PULLING_ICON, "ion-a");
		wizardPage.setEditorValue(ATTR_PULLING_ICON, "");
		assertTextDoesNotExist(wizard, ATTR_PULLING_ICON);

		assertTextDoesNotExist(wizard, ATTR_PULLING_TEXT);
		wizardPage.setEditorValue(ATTR_PULLING_TEXT, "pulling...");
		assertAttrExists(wizard, ATTR_PULLING_TEXT, "pulling...");
		wizardPage.setEditorValue(ATTR_PULLING_TEXT, "");
		assertTextDoesNotExist(wizard, ATTR_PULLING_TEXT);

		assertTextDoesNotExist(wizard, ATTR_REFRESHING_ICON);
		wizardPage.setEditorValue(ATTR_REFRESHING_ICON, "ion-b");
		assertAttrExists(wizard, ATTR_REFRESHING_ICON, "ion-b");
		wizardPage.setEditorValue(ATTR_REFRESHING_ICON, "");
		assertTextDoesNotExist(wizard, ATTR_REFRESHING_ICON);

		assertTextDoesNotExist(wizard, ATTR_REFRESHING_TEXT);
		wizardPage.setEditorValue(ATTR_REFRESHING_TEXT, "refrehing...");
		assertAttrExists(wizard, ATTR_REFRESHING_TEXT, "refrehing...");
		wizardPage.setEditorValue(ATTR_REFRESHING_TEXT, "");
		assertTextDoesNotExist(wizard, ATTR_REFRESHING_TEXT);

		assertTextDoesNotExist(wizard, JQueryConstants.ATTR_DISABLED);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewNavigationWizard() {
		IWizardPage currentPage = runToolEntry("Navigation", true);
		assertTrue(currentPage instanceof NewNavigationWizardPage);

		NewNavigationWizardPage wizardPage = (NewNavigationWizardPage)currentPage;
		NewNavigationWizard wizard = (NewNavigationWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Navigation");

		assertTextDoesNotExist(wizard, ATTR_DELEGATE_HANDLE);
		wizardPage.setEditorValue(ATTR_DELEGATE_HANDLE, "myNavBar");
		assertAttrExists(wizard, ATTR_DELEGATE_HANDLE, "myNavBar");
		wizardPage.setEditorValue(ATTR_DELEGATE_HANDLE, "");
		assertTextDoesNotExist(wizard, ATTR_DELEGATE_HANDLE);

		assertTextExists(wizard, TAG_ION_NAV_BACK_BUTTON);
		wizardPage.setEditorValue(TAG_ION_NAV_BACK_BUTTON, FALSE);
		assertTextDoesNotExist(wizard, TAG_ION_NAV_BACK_BUTTON);
		wizardPage.setEditorValue(TAG_ION_NAV_BACK_BUTTON, TRUE);
		assertTextExists(wizard, TAG_ION_NAV_BACK_BUTTON);

		assertTextDoesNotExist(wizard, ATTR_ALIGN_TITLE);
		wizardPage.setEditorValue(ATTR_ALIGN_TITLE, "left");
		assertAttrExists(wizard, ATTR_ALIGN_TITLE, "left");
		wizardPage.setEditorValue(ATTR_ALIGN_TITLE, "");
		assertTextDoesNotExist(wizard, ATTR_ALIGN_TITLE);
	
		assertTextDoesNotExist(wizard, ATTR_NO_TAP_SCROLL);
		wizardPage.setEditorValue(ATTR_NO_TAP_SCROLL, TRUE);
		assertAttrExists(wizard, ATTR_NO_TAP_SCROLL, TRUE);
		wizardPage.setEditorValue(ATTR_NO_TAP_SCROLL, FALSE);
		assertTextDoesNotExist(wizard, ATTR_NO_TAP_SCROLL);
	
		assertTextDoesNotExist(wizard, ATTR_ANIMATION);
		wizardPage.setEditorValue(EDITOR_ID_NAV_BAR_ANIMATION, "a");
		assertAttrExists(wizard, ATTR_ANIMATION, "a");
		wizardPage.setEditorValue(EDITOR_ID_NAV_BAR_ANIMATION, "");
		assertTextDoesNotExist(wizard, ATTR_ANIMATION);

		wizardPage.setEditorValue(EDITOR_ID_BAR_COLOR, "bar-calm");
		assertAttrExists(wizard, ATTR_CLASS, "bar-calm");
		wizardPage.setEditorValue(EDITOR_ID_BAR_COLOR, "");
		
		assertTextDoesNotExist(wizard, ATTR_NAME);
		wizardPage.setEditorValue(ATTR_NAME, "n");
		assertAttrExists(wizard, ATTR_NAME, "n");
		wizardPage.setEditorValue(ATTR_NAME, "");
		assertTextDoesNotExist(wizard, ATTR_NAME);
	

		assertTextDoesNotExist(wizard, ATTR_ANIMATION);
		wizardPage.setEditorValue(EDITOR_ID_NAV_VIEW_ANIMATION, "b");
		assertAttrExists(wizard, ATTR_ANIMATION, "b");
		wizardPage.setEditorValue(EDITOR_ID_NAV_VIEW_ANIMATION, "");
		assertTextDoesNotExist(wizard, ATTR_ANIMATION);

		compareGeneratedAndInsertedText(wizard);
	}

	private void compareUIAndNonUIWizards(IDropWizard wizard, String itemName) {
		String startText = wizard.getWizardModel().getElementGenerator().generateStartTag();
		String endText = wizard.getWizardModel().getElementGenerator().generateEndTag();
		IDropWizard newWizard = ((NewIonicWidgetWizard)wizard).getPaletteItem().createWizard();
		PaletteItemResult result = ((NewIonicWidgetWizard)newWizard).runWithoutUi(textEditor);
		assertNotNull(result);
		assertEquals(startText, result.getStartText());
		assertEquals(endText, result.getEndText());
	}

	public void testPaletteItemsWithoutUI() {
		PaletteModelImpl model = new PaletteModelImpl();
		model.load();
		IPaletteGroup jqmGroup = model.getPaletteGroup("Ionic");
		assertNotNull(jqmGroup);
		
		List<String> failures = new ArrayList<String>();
		for(IPaletteVersionGroup vGroup : jqmGroup.getPaletteVersionGroups()){
			for(IPaletteCategory category : vGroup.getCategories()){
				for(IPaletteItem item : category.getItems()){
					HTML5PaletteWizardTest.runPaletteItemsWithoutUI(failures, textEditor, item);
				}
			}
		}
		if(!failures.isEmpty()) {
			StringBuilder text = new StringBuilder();
			text.append("The following Palette wizards failed:\n");
			for (String s: failures) {
				text.append(s).append("\n");
			}
			fail(text.toString());
		}
	}
	protected void compareGeneratedAndInsertedText(NewIonicWidgetWizard<?> wizard) {
		String generatedText = wizard.getTextForTextView();

		wizard.performFinish();

		String insertedText = getInsertedText();
		assertTrue(isSameHTML(generatedText, insertedText));
	}

	void assertAttrExists(AbstractNewHTMLWidgetWizard wizard, String attr, String value) {
		assertTextExists(wizard, attr + "=\"" + value + "\"");
	}

	void assertTextExists(AbstractNewHTMLWidgetWizard wizard, String text) {
		assertTrue(wizard.getTextForTextView().indexOf(text) >= 0);
	}

	void assertTextDoesNotExist(AbstractNewHTMLWidgetWizard wizard, String text) {
		assertTrue(wizard.getTextForTextView().indexOf(text) < 0);
	}

	void assertTextIsInserted(String text) {
		String content = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertTrue(content.indexOf(text) > 0);
	}

	void assertAttrIsInserted(String attr, String value) {
		assertTextIsInserted(attr + "=\"" + value + "\"");
	}
}