/*******************************************************************************
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.test;

import java.util.Set;

import org.jboss.tools.common.meta.XModelEntity;
import org.jboss.tools.common.meta.action.XActionItem;
import org.jboss.tools.common.model.test.MetaModelTest;

/**
 * This test runs tests defined in MetaModelTest in context of jst module.
 * MetaModelTest is defined in common module, but to check consistency of 
 * the meta model of a concrete context, the test should be run in it.
 * 
 * Some actions used by Web Projects view do not comply the rule that 
 * requires action handler and icon to be packaged in the same module,
 * but the view is considers them as extensions that may be non-available.    
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class WebMetaModelTest extends MetaModelTest {

	protected void collectActionsWithWrongHandlers(XModelEntity entity, XActionItem item, Set<String> errors) {
		if(!entity.getName().equals("WebWorkspace") &&
		   !entity.getName().equals("WebPrjAddModuleHelper") &&
		   (!entity.getName().equals("FileSystems") || !item.getName().equals("SynchronizeModules"))
		) {
			super.collectActionsWithWrongHandlers(entity, item, errors);
		}
	}

	protected void collectActionsWithoutIcon(XModelEntity entity, XActionItem item, Set<String> actionsWithoutIcons) {
		if(!entity.getName().equals("WebWorkspace")) {
			super.collectActionsWithoutIcon(entity, item, actionsWithoutIcons);
		}
	}
}
