/*
 * Created on February 21, 2003, 12:00 PM
 */

package org.jboss.tools.jst.web.model.handlers;

import java.util.*;
import org.jboss.tools.common.meta.action.XAction;
import org.jboss.tools.common.meta.action.impl.XActionImpl;
import org.jboss.tools.common.meta.action.impl.handlers.DefaultRedirectHandler;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.filesystems.XFileObject;
import org.jboss.tools.jst.web.model.ReferenceObject;

/**
 * @author  valera
 */
public class WebRedirectHandler extends DefaultRedirectHandler {    
    protected Set<String> entities;
    
    public WebRedirectHandler() {}
    
    protected XModelObject getTargetObject(XModelObject object) {
        XModelObject target = null;
        if (object instanceof ReferenceObject) {
			ReferenceObject refObject = (ReferenceObject)object;
            target = refObject.getReference();
        }
        return target;
    }
    
    protected XModelObject checkEntity(XModelObject object) {
        if (entities == null) {
            this.entities = new HashSet<String>();
            String en = action.getProperty("entity"); //$NON-NLS-1$
            if (en != null) {
                StringTokenizer tokenizer = new StringTokenizer(en, ":"); //$NON-NLS-1$
                while (tokenizer.hasMoreTokens()) {
                    this.entities.add(tokenizer.nextToken());
                }
            }
        }
        if (object != null && this.entities.size() > 0 && !this.entities.contains(object.getModelEntity().getName())) {
            return null;
        }
        return object;
    }
    
    protected XModelObject getTrueSource(XModelObject object) {
        return checkEntity(getTargetObject(object));
    }
    
    private boolean hidden = false;
    
    public boolean isEnabled(XModelObject object) {
        if(!checkConfigVersion(object)) return false;
        XAction a = getTrueAction(object);
        if (a != null) {
            boolean enabled = a.isEnabled(getTrueSource(object));
            hidden = false;//a.hide(enabled);
            ((XActionImpl)action).setDisplayName(a.getDisplayName());
            return enabled;
        }
        hidden = true;
        return false;
    }
    
    public boolean hide(boolean enabled) {
        return hidden || super.hide(enabled);
    }

    private boolean checkConfigVersion(XModelObject object) {
        String entity = action.getProperty("configEntity"); //$NON-NLS-1$
        if(entity == null) return true;
        XModelObject f = object;
        while(f != null && f.getFileType() != XFileObject.FILE) f = f.getParent();
        if(f == null) return true;
        boolean b = f.getModelEntity().getName().equals(entity);
        if(b) return true;
        hidden = true;
        return b;
    }

}
