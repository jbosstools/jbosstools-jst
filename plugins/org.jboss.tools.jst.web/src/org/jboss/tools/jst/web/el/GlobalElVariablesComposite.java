/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jst.web.el;

import java.util.List;

import org.jboss.tools.jst.web.rreferences.ResourceReference;
import org.jboss.tools.jst.web.rreferences.ResourceReferenceList;
import org.jboss.tools.jst.web.rreferences.ResourceReferencesComposite;
import org.jboss.tools.jst.web.rreferences.ResourceReferencesTableProvider;


/**
 * Composite class for the global el variables.
 * @author Evgenij Stherbin
 *
 */
public class GlobalElVariablesComposite extends ResourceReferencesComposite {

    /**
     * @see org.jboss.tools.jst.web.rreferences.ResourceReferencesComposite#createGroupLabel()
     */
    @Override
    protected String createGroupLabel() {
        return "";
    }

    /**
     * @see org.jboss.tools.jst.web.rreferences.ResourceReferencesComposite#createTableProvider(java.util.List)
     */
    @Override
    protected ResourceReferencesTableProvider createTableProvider(List dataList) {
        return ResourceReferencesTableProvider.getGlobalELTableProvider(dataList);
    }

    /**
     * @see org.jboss.tools.jst.web.rreferences.ResourceReferencesComposite#getEntity()
     */
    @Override
    protected String getEntity() {
        return (file != null) ? "VPEGlobalElReference" : "VPEGlobalElReferenceExt";
    }

    /**
     * @see org.jboss.tools.jst.web.rreferences.ResourceReferencesComposite#getReferenceList()
     */
    @Override
    protected ResourceReferenceList getReferenceList() {
        return GlobalELReferenceList.getInstance();
    }
    
    @Override
    protected ResourceReference getDefaultResourceReference() {
        ResourceReference rf = new ResourceReference("", ResourceReference.GLOBAL_SCOPE);
        rf.setGlobal(true);
        return rf;
    }

}
