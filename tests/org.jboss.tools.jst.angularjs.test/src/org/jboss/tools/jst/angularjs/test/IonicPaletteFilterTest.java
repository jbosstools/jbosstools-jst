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
package org.jboss.tools.jst.angularjs.test;

import org.jboss.tools.jst.web.ui.test.PaletteFilterTest;

public class IonicPaletteFilterTest extends PaletteFilterTest{
	public IonicPaletteFilterTest(){
		super();
	}
	public void testIonicRecognizer(){
		checkRecognizer("recogn_ionic.html", new String[]{"", "Ionic", "HTML"}, true);
	}

	public void testIonicRecognizer2(){
		checkRecognizer("recogn_ionic.html", new String[]{"", "A Test", "Ionic", "jQuery Mobile", "HTML"}, false);
	}
	
	public void testJQM13Recognizer2(){
		checkRecognizer("recogn_jqm13.html", new String[]{"", "A Test", "Ionic", "jQuery Mobile", "HTML"}, false);
	}
	
	public void testJQM14Recognizer2(){
		checkRecognizer("recogn_jqm14.html", new String[]{"", "A Test", "Ionic", "jQuery Mobile", "HTML"}, false);
	}
	
	public void testHTML5Recognizer2(){
		checkRecognizer("recogn_html5.html", new String[]{"", "A Test", "Ionic", "jQuery Mobile", "HTML"}, false);
	}

}
