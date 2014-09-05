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
package org.jboss.tools.jst.web.ui.test;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.jst.web.ui.internal.preferences.js.JSLib;
import org.jboss.tools.jst.web.ui.internal.preferences.js.JSLibFactory;
import org.jboss.tools.jst.web.ui.internal.preferences.js.JSLibModel;
import org.jboss.tools.jst.web.ui.internal.preferences.js.JSLibVersion;

import junit.framework.TestCase;

public class DefaultJSLibsText extends TestCase {

	public DefaultJSLibsText() {
	}

	/**
	 * Two models contain the same objects added in different order.
	 * 
	 * Assert: models are equal.
	 */
	public void testComparingModel() {
		JSLibModel model1 = createModel("MyLib", "1.0", "u10", "u11");
		createVersion(model1, "MyLib", "1.1", "u10", "u11");
		createVersion(model1, "MyLib", "1.2", "u20", "u21", "u22");
		createVersion(model1, "MyLib2", "1.0.0", "ux0", "ux1");

		JSLibModel model2 = createModel("MyLib2", "1.0.0", "ux0", "ux1");
		createVersion(model2, "MyLib", "1.1", "u10", "u11");
		createVersion(model2, "MyLib", "1.0", "u10", "u11");
		createVersion(model2, "MyLib", "1.2", "u21", "u22", "u20");
		
		assertTrue(model1.equals(model2));
	}

	/**
	 * The preference model at first start is set to a copy of the default model.
	 * 
	 * Assert: models are equal.
	 */
	public void testComparingDefaultAndPreferencesModels() {
		JSLibModel model1 = JSLibFactory.getInstance().getDefaultModel();
		JSLibModel model2 = JSLibFactory.getInstance().getPreferenceModel();

		assertTrue(model1.equals(model2));
	}

	/**
	 * Current equals to old default.
	 * New default adds new library.
	 * 
	 * Assert: new library is added; old library is not removed
	 */
	public void testMergeDefaultModelLibraryAdded() {
		JSLibModel current = createModel("MyLib", "1.0", "u1", "u2");
		JSLibModel oldDefault = createModel("MyLib", "1.0", "u1", "u2");
		JSLibModel newDefault = createModel("MyLib", "1.0", "u1", "u2");
		createVersion(newDefault, "MyLib2", "1.0", "u3", "u4");

		assertMerge(current, oldDefault, newDefault, 
				"A MyLib2");
	}

	/**
	 * Current model equals to old default model.
	 * New default model replaces one url.
	 * 
	 * Assert: new url is added, old url is removed.
	 */
	public void testMergeDefaultModelUrlReplaced() {
		JSLibModel current = createModel("MyLib", "1.0", "u1", "u2");
		JSLibModel oldDefault = createModel("MyLib", "1.0", "u1", "u2");
		JSLibModel newDefault = createModel("MyLib", "1.0", "u1", "u3");

		assertMerge(current, oldDefault, newDefault, 
				"A MyLib:1.0:u3", "R MyLib:1.0:u2");
	}

	/**
	 * Current model equals to old default model.
	 * New default model adds new version to a library.
	 * 
	 * Assert: new version is added.
	 */
	public void testMergeDefaultModelVersionAdded() {
		JSLibModel current = createModel("MyLib", "1.0", "u1", "u2");
		JSLibModel oldDefault = createModel("MyLib", "1.0", "u1", "u2");
		JSLibModel newDefault = createModel("MyLib", "1.0", "u1", "u2");
		createVersion(newDefault, "MyLib", "1.1", "u3", "u4");

		assertMerge(current, oldDefault, newDefault, 
				"A MyLib:1.1");
	}

	/**
	 * Current model equals to old default model.
	 * New default model replaces version in a library.
	 * 
	 * Assert: new version is added; old version is removed
	 */
	public void testMergeDefaultModelVersionReplaced() {
		JSLibModel current = createModel("MyLib", "1.0", "u1", "u2");
		JSLibModel oldDefault = createModel("MyLib", "1.0", "u1", "u2");
		JSLibModel newDefault = createModel("MyLib", "1.1", "u1", "u2");

		assertMerge(current, oldDefault, newDefault, 
				"A MyLib:1.1", "R MyLib:1.0");
	}

	/**
	 * Current model removes a library version.
	 * New default model modifies an url in that version.
	 * 
	 * Assert: modified version is added again
	 */
	public void testMergeDefaultModelVersionReplacedOldVersionModified() {
		JSLibModel current = createModel("MyLib", "1.1", "u3", "u4");
		JSLibModel oldDefault = createModel("MyLib", "1.0", "u1", "u2");
		createVersion(oldDefault, "MyLib", "1.1", "u3", "u4");
		JSLibModel newDefault = createModel("MyLib", "1.0", "u1", "u2m");
		createVersion(newDefault, "MyLib", "1.1", "u3", "u4");

		assertMerge(current, oldDefault, newDefault, 
				"A MyLib:1.0");
	}

	/**
	 * Current model modifies a library version.
	 * New default model removes that version and adds new version instead.
	 * 
	 * Assert: new version is added; old version is not removed
	 */
	public void testMergeDefaultModelVersionModifiedOldVersionRemoved() {
		JSLibModel current = createModel("MyLib", "1.0", "u1", "u2");
		JSLibModel oldDefault = createModel("MyLib", "1.0", "u1", "u3");
		JSLibModel newDefault = createModel("MyLib", "1.1", "u1", "u2");

		assertMerge(current, oldDefault, newDefault, 
				"A MyLib:1.1");
	}

	private void assertMerge(JSLibModel current, JSLibModel oldDefault, JSLibModel newDefault, String... expectedChanges) {
		List<String> diff = new ArrayList<String>();
		JSLibFactory.getInstance().mergeDefaultModel(diff, current, oldDefault, newDefault);

		for (String s: expectedChanges) {
			assertTrue("Expected change " + s + " is not found.", diff.contains(s));
			diff.remove(s);
		}
		if(!diff.isEmpty()) {
			fail("Unexpected change " + diff.get(0) + " is found.");
		}
	}

	private JSLibModel createModel(String libName, String version, String... urls) {
		JSLibModel model = new JSLibModel();
		createVersion(model, libName, version, urls);
		return model;
	}

	private void createVersion(JSLibModel model, String libName, String version, String... urls) {
		JSLib lib = model.getOrCreateLib(libName);
		JSLibVersion v = lib.getOrCreateVersion(version);
		for (String url: urls) v.getURLs().add(url);
	}
}
