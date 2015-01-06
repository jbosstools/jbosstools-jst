/******************************************************************************* 
 * Copyright (c) 2013 - 2015 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.angularjs.internal.taglib.html;

import java.util.HashSet;
import java.util.Set;

import org.jboss.tools.jst.web.kb.KbQuery;

/**
 * @author Alexey Kazakov
 */
public class NgAnyClassAttribute extends NgClassAttribute {

	private static final String END_TEXT = "};"; //$NON-NLS-1$

	private static Set<IDirective> DIRECTIVES = new HashSet<IDirective>();
	static {
		DIRECTIVES.add(new ClassDirective(Messages.NgAnyClassAttribute_NgBind, "ng-bind: {", END_TEXT, "bind")); //$NON-NLS-1$ //$NON-NLS-2$
		DIRECTIVES.add(new ClassDirective(Messages.NgAnyClassAttribute_NgClass, "ng-class: {", END_TEXT, "class")); //$NON-NLS-1$ //$NON-NLS-2$
		DIRECTIVES.add(new ClassDirective(Messages.NgAnyClassAttribute_NgClassOdd, "ng-class-odd: {", END_TEXT, "class-odd")); //$NON-NLS-1$ //$NON-NLS-2$
		DIRECTIVES.add(new ClassDirective(Messages.NgAnyClassAttribute_NgClassEven, "ng-class-even: {", END_TEXT, "class-even")); //$NON-NLS-1$ //$NON-NLS-2$
		DIRECTIVES.add(new ClassDirective(Messages.NgAnyClassAttribute_NgCloak, "ng-cloak", "", "cloak")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		DIRECTIVES.add(new ClassDirective(Messages.NgAnyClassAttribute_NgForm, "ng-form", "", "form")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		DIRECTIVES.add(new ClassDirective(Messages.NgAnyClassAttribute_NgInclude, "ng-include: {", END_TEXT, "include")); //$NON-NLS-1$ //$NON-NLS-2$
		DIRECTIVES.add(new ClassDirective(Messages.NgAnyClassAttribute_NgInit, "ng-init: {", END_TEXT, "init")); //$NON-NLS-1$ //$NON-NLS-2$
		DIRECTIVES.add(new ClassDirective(Messages.NgAnyClassAttribute_NgStyle, "ng-style: {", END_TEXT, "style")); //$NON-NLS-1$ //$NON-NLS-2$
		DIRECTIVES.add(new ClassDirective(Messages.NgAnyClassAttribute_NgTransclude, "ng-transclude: {", END_TEXT, "transclude")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	protected boolean checkComponent(KbQuery query) {
		return true;
	}

	@Override
	protected Set<IDirective> getDirectives(KbQuery query) {
		return DIRECTIVES;
	}
}