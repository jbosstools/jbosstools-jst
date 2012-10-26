/*******************************************************************************
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.test.commands;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.internal.HandlerServiceImpl;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.bindings.EBindingService;
import org.eclipse.e4.ui.bindings.keys.KeyBindingDispatcher;
import org.eclipse.jface.bindings.Binding;
import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.internal.PartSite;
import org.jboss.tools.jst.jsp.test.ca.ContentAssistantTestCase;
import org.jboss.tools.test.util.ProjectImportTestSetup;

/**
 * Test case for JBIDE-12534 (See also: JBIDE-12833)
 * 
 * @author Victor V. Rubezhny
 *
 */
public class KeyBindingsTest extends ContentAssistantTestCase {
	private static final String PROJECT_NAME = "Jbide6061Test"; //$NON-NLS-1$
	private static final String PAGE_NAMES[] = {
		"/WebContent/pages/xhtml_page.xhtml", //$NON-NLS-1$
		"/WebContent/pages/jsp_page.jsp" //$NON-NLS-1$
	};
	
	class KeyCombination {
		int mask;
		int key;
		
		public KeyCombination(int mask, int key) {
			this.mask = mask;
			this.key = key;
		}
	}

	KeyCombination[] keys = new KeyCombination[] {
    		new KeyCombination(SWT.CTRL, 'd'),
    		new KeyCombination(SWT.CTRL | SWT.ALT, 16777217),
    		new KeyCombination(SWT.CTRL | SWT.ALT, 16777218),
    		new KeyCombination(SWT.ALT, 16777217),
    		new KeyCombination(SWT.ALT, 16777218)
    	};

    
	public void setUp() throws Exception {
		project = ProjectImportTestSetup.loadProject(PROJECT_NAME);
	}

	public void testKeyBindings () {
		for (String pageName : PAGE_NAMES) {
			doTestKeyBindingsOnPage(pageName);
		}
	}
	
	private void doTestKeyBindingsOnPage(String pageName) {
        openEditor(pageName);
        try {
			PartSite partSite = (PartSite)(editorPart.getEditorSite() instanceof PartSite ? editorPart.getEditorSite() : null);
			assertNotNull("Cannot get PartSite instance for " + pageName, partSite);
			IEclipseContext context = partSite.getContext(); 
			assertNotNull("Cannot get EclipseContext instance for " + pageName, context);
	    	EBindingService bindingService = (EBindingService) context.get(EBindingService.class.getName());
			assertNotNull("Cannot get BindingService instance for " + pageName, bindingService);

	    	for (KeyCombination key : keys) {
				Event event = createKeyCombinationEvent(key.mask, key.key);
				
				List<KeyStroke> keyStrokes = KeyBindingDispatcher.generatePossibleKeyStrokes(event);
		
				boolean found = false;
				KeySequence sequenceBeforeKeyStroke = KeySequence.getInstance();
				for (Iterator<KeyStroke> iterator = keyStrokes.iterator(); iterator.hasNext();) {
					KeySequence sequenceAfterKeyStroke = KeySequence.getInstance(sequenceBeforeKeyStroke,
							iterator.next());
					if (isPerfectMatch(bindingService, sequenceAfterKeyStroke)) {
						final ParameterizedCommand cmd = getPerfectMatch(bindingService, sequenceAfterKeyStroke);
						assertNotNull("Command not found for 'Perfect Match' keystroke " + sequenceAfterKeyStroke, cmd);
						assertNotNull("Command found but cannot be executed for 'Perfect Match' keystroke " + sequenceAfterKeyStroke, HandlerServiceImpl.lookUpHandler(context, cmd.getId()));
						found = true;
						break;
					}
				}
				assertTrue("Command not found for key combination " + key.mask + "/" + key.key, found);
	    	}
        } finally {
        	closeEditor();
        }
	}
	
	
    // stateMask is one or combination of following: SWT.ALT, SWT.SHIFT, SWT.CONTROL, SWT.BUTTON1, SWT.BUTTON2, SWT.BUTTON3
    // keyval is the key value ('A' ... 'Z', 'a' ... 'z','0' ... '9' and so on
	private static int TICK_TIMER = 1;
	private Event createKeyCombinationEvent(int mask, int keyval) {
		Event event = new Event ();
		event.time = TICK_TIMER++;
		event.keyCode = keyval;
		int key = keyval;
		if ('a'  <= key && key <= 'z') key -= 'a' - 'A';
		if (64 <= key && key <= 95) key -= 64;
		event.character = (char) key;
		event.keyLocation = 0;
		if ((mask & SWT.ALT) != 0) event.stateMask |= SWT.ALT;
		if ((mask & SWT.SHIFT) != 0) event.stateMask |= SWT.SHIFT;
		if ((mask & SWT.CONTROL) != 0) event.stateMask |= SWT.CONTROL;
		if ((mask & SWT.BUTTON1) != 0) event.stateMask |= SWT.BUTTON1;
		if ((mask & SWT.BUTTON2) != 0) event.stateMask |= SWT.BUTTON2;
		if ((mask & SWT.BUTTON3) != 0) event.stateMask |= SWT.BUTTON3;
		
		return event;
    }
    
    boolean isPerfectMatch(EBindingService bindingService, KeySequence keySequence) {
		return bindingService.isPerfectMatch(keySequence);
    }

    private ParameterizedCommand getPerfectMatch(EBindingService bindingService, KeySequence keySequence) {
		Binding perfectMatch = bindingService.getPerfectMatch(keySequence);
		return perfectMatch == null ? null : perfectMatch.getParameterizedCommand();
	}

	
    static boolean emulateSendEvent(Widget widget, int type, Event event) throws Exception {
		Method m = Widget.class.getMethod("sendEvent", int.class, Event.class);
		m.setAccessible(true);
		Object result = m.invoke(widget, type, event);
		if (result instanceof Boolean) {
			return ((Boolean)result);
		}
		return false;
    }
}
