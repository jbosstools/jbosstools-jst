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

package org.jboss.tools.jst.web.ui.internal.preferences;

import java.util.Properties;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.jst.web.el.GlobalElVariablesComposite;
import org.jboss.tools.jst.web.rreferences.ResourceReference;
import org.jboss.tools.jst.web.rreferences.VpeResourcesDialog;

/**
 * Page for the El preferences.
 * 
 * @author Evgenij Stherbin
 */
public class ELVariablesPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
    

    private static final ResourceReference[] EMPTY_RESOURCE = new ResourceReference[0];
    /** The el. */
    private GlobalElVariablesComposite   el  = new GlobalElVariablesComposite();
    
    @Override
    protected void performApply() {
        super.performApply();
        this.performOk();
    }

    @Override
    protected void performDefaults() {
        super.performDefaults();
        el.clearAll();
        el.update();
    }

    /**
     * Creates the contents.
     * 
     * @param parent the parent
     * 
     * @return the control
     * 
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent) {
        
        GridData data;
        Composite c = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(1, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        c.setLayout(layout);
       
        setUpVariableComposite(el);
        data = new GridData(GridData.FILL_BOTH);
        final Control elControl =  el.createControl(c);
        elControl.setLayoutData(data);
        
        return c;
    }
    
    /**
     * Perform ok.
     * 
     * @return true, if perform ok
     */
    @Override
    public boolean performOk() {
        boolean rst = super.performOk();
        
        el.commit();
        return rst;
    }

    /**
     * Sets the up variable composite.
     * 
     * @param el the el
     */
    private void setUpVariableComposite(GlobalElVariablesComposite el){
        final VpeResourcesDialog dialog = new VpeResourcesDialog();
        final Properties p = new Properties();
        
        p.setProperty("help", "VpeResourcesDialog"); //$NON-NLS-1$ //$NON-NLS-2$
        p.put("path", Platform.getLocation()); //$NON-NLS-1$
        p.put("model", PreferenceModelUtilities.getPreferenceModel()); //$NON-NLS-1$
        dialog.setObject(p);
        el.setObject(p);
    }

    /**
     * Init.
     * 
     * @param workbench the workbench
     * 
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
        // TODO Auto-generated method stub

    }

}
