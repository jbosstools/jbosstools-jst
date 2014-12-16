/*******************************************************************************
 * Copyright (c) 2009-2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.tools.common.base.test.model.XProjectImportTestSetUp;
import org.jboss.tools.common.base.test.validation.ValidationProjectTestSetup;
import org.jboss.tools.jst.web.kb.test.validation.BuilderOrderMarkerResolutionTest;
import org.jboss.tools.jst.web.kb.test.validation.BuilderOrderValidationTest;
import org.jboss.tools.jst.web.kb.test.validation.KBValidationTest;
import org.jboss.tools.test.util.ProjectImportTestSetup;

/**
 * @author Alexey Kazakov
 */
public class JstWebKbAllTests {

	public static Test suite() {
		TestSuite suiteAll = new TestSuite("KB Tests");
		TestSuite suite = new TestSuite(KbModelStorageTest.class.getName());
		suite.addTestSuite(KbModelStorageTest.class);
		ProjectImportTestSetup testSetup = new XProjectImportTestSetUp(suite,
				"org.jboss.tools.jst.web.kb.test",
				new String[]{"projects/TestKbModel2"},
				new String[]{"TestKbModel2"});
		suiteAll.addTest(testSetup);
		suite = new TestSuite(JstWebKbAllTests.class.getName());
		suite.addTestSuite(IncludeModelTest.class);
		suite.addTestSuite(KbMockModelTest.class);
		suite.addTestSuite(JQueryDataTest.class);
		suite.addTestSuite(JQueryDataTest14.class);
		suite.addTestSuite(JQueryIDTest.class);
		suite.addTestSuite(JQueryRecognizerTest.class);
		suite.addTestSuite(FileNamesCATest.class);
		suite.addTestSuite(DatalistsCATest.class);
		suite.addTestSuite(AngularJSTest.class);
		suite.addTestSuite(KbModelWithSeveralJarCopiesTest.class);
		suite.addTestSuite(CSSMediaRuleTest.class);
		suite.addTestSuite(CSSClassNamesTest.class);
		suite.addTestSuite(RemoteFileManagerTest.class);
		testSetup = new XProjectImportTestSetUp(suite,
				"org.jboss.tools.jst.web.kb.test",
				new String[]{"projects/TestKbModel", "projects/MyFaces", "projects/MyFaces2", "projects/TestKbModel3", "projects/TestKbModel4"},
				new String[]{"TestKbModel", "MyFaces", "MyFaces2", "TestKbModel3", "TestKbModel4"});
		suiteAll.addTest(testSetup);
		suiteAll.addTestSuite(KBValidationTest.class);
		suite = new TestSuite(BuilderOrderValidationTest.class.getName());
		suite.addTestSuite(BuilderOrderValidationTest.class);
		testSetup = new ValidationProjectTestSetup(suite,
				"org.jboss.tools.jst.web.kb.test",
				new String[]{"projects/TestBrokenBuilderOrder"},
				new String[]{"TestBrokenBuilderOrder"});
		suiteAll.addTest(testSetup);
		suite = new TestSuite(BuilderOrderMarkerResolutionTest.class.getName());
		suite.addTestSuite(BuilderOrderMarkerResolutionTest.class);
		testSetup = new ValidationProjectTestSetup(suite,
				"org.jboss.tools.jst.web.kb.test",
				new String[]{"projects/TestBrokenBuilderOrder"},
				new String[]{"TestBrokenBuilderOrder"});
		suiteAll.addTest(testSetup);
		return suiteAll;
	}
}