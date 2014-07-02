package org.jboss.tools.jst.web.ui.test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.ui.IEditorPart;
import org.jboss.tools.common.model.ui.internal.editors.PaletteItemResult;
import org.jboss.tools.jst.jsp.test.palette.AbstractPaletteEntryTest;
import org.jboss.tools.jst.web.html.HTMLConstants;
import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileVersion;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
import org.jboss.tools.jst.web.ui.palette.internal.PaletteManager;
import org.jboss.tools.jst.web.ui.palette.internal.RunnablePaletteItem;

public class PaletteManagerTest extends AbstractPaletteEntryTest {
	IEditorPart editor = null;

	public PaletteManagerTest() {}

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

	public void testCategories() {
		String[] categories = PaletteManager.getInstance().getCategories();
		assertEquals(JQueryConstants.JQM_CATEGORY, categories[categories.length - 2]);
		assertEquals(HTMLConstants.HTML_CATEGORY, categories[categories.length - 1]);
	}

	public void testVersions() {
		String[] versions = PaletteManager.getInstance().getVersions(JQueryConstants.JQM_CATEGORY);
		assertEquals(2, versions.length);
		assertEquals(JQueryMobileVersion.JQM_1_3.toString(), versions[0]);
		assertEquals(JQueryMobileVersion.JQM_1_4.toString(), versions[1]);
		
		versions = PaletteManager.getInstance().getVersions(HTMLConstants.HTML_CATEGORY);
		assertEquals(1, versions.length);
		assertEquals("5.0", versions[0]);
	}

	public void testItemsInJQuery14() {
		String category = JQueryConstants.JQM_CATEGORY;
		String version = JQueryMobileVersion.JQM_1_4.toString();
		doTestItems(category, version, 20, "Button");
	}

	public void testItemsInJQuery13() {
		String category = JQueryConstants.JQM_CATEGORY;
		String version = JQueryMobileVersion.JQM_1_3.toString();
		doTestItems(category, version, 20, "Button");
	}

	public void testItemsInHTML50() {
		String category = HTMLConstants.HTML_CATEGORY;
		String version = "5.0";
		doTestItems(category, version, 5, "Form");
	}

	public void doTestItems(String category, String version, int minSize, String... testItemNames) {
		openEditor("a14.html");
		Collection<RunnablePaletteItem> items = PaletteManager.getInstance().getItems(category, version);
		Set<String> names = new HashSet<String>();
		for (RunnablePaletteItem i: items) {
			assertEquals(category, i.getCategory());
			assertEquals(version, i.getVersion());
			PaletteItemResult result = i.getResult(textEditor);
			assertNotNull("Failed on item " + category + " " + version + " " + i.getName(), result);
			names.add(i.getName());
		}
		assertTrue(names.size() >= minSize);
		for (String testItemName: testItemNames) {
			assertTrue(names.contains(testItemName));
		}
	}

}
