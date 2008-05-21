/*
 * Created on February 25, 2003, 10:34 AM
 */

package org.jboss.tools.jst.web.model.helpers;

import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.event.*;
import java.util.*;

public class WebProcessUpdateManager implements XModelTreeListener {
    private HashMap<String,Registry> binds = new HashMap<String,Registry>(1);
    private boolean active = true;

    protected WebProcessUpdateManager() {}

    public void register(String path, WebProcessUpdateHelper node) {
        HashMap<String,Registry> b = binds;
        while (path != null) {
            String[] q = reduce(path);
            Registry o = (Registry)b.get(q[0]);
            if (o == null) {
                o = new Registry();
                b.put(q[0], o);
            }
            path = q[1];
            if (path == null) o.objects.add(node);
            b = o.binds;
        }
    }

    public void unregister(String path, WebProcessUpdateHelper node) {
        HashMap b = binds;
        while (path != null) {
            String[] q = reduce(path);
            Registry o = (Registry)b.get(q[0]);
            if (o == null) return;
            path = q[1];
            if (path == null) o.objects.remove(node);
            b = o.binds;
        }
    }

    private String[] reduce(String path) {
        int i = path.indexOf('/');
        if (i <= 0) return new String[] {path.substring(i + 1), null};
        return new String[] {path.substring(0, i), path.substring(i + 1)};
    }

    public Map<WebProcessUpdateHelper,String> getObjects(XModelTreeEvent event) {
        Map<WebProcessUpdateHelper,String> m = new HashMap<WebProcessUpdateHelper,String>();
        HashMap<String,Registry> b = binds;
        String np = event.getModelObject().getPath();
        String op = (event.getInfo() instanceof String) ? (String)event.getInfo() : np;
        Registry o = null;
        while (op != null) {
            String[] nq = (np == null) ? new String[] {null, null} : reduce(np);
            String[] oq = reduce(op);
            o = b.get(oq[0]);
            if (o == null) return m;
            if (nq[0] != null && !nq[0].equals(oq[0])) {
                b.remove(oq[0]);
                b.put(nq[0], o);
            }
            np = nq[1];
            op = oq[1];
            Iterator<WebProcessUpdateHelper> it = o.objects.iterator();
            while (it.hasNext()) {
				WebProcessUpdateHelper n = it.next();
                if (n != null) {
                    if (!n.isActive()) it.remove(); else m.put(n, op);
                }
            }
            b = o.binds;
        }
        return m;
    }

    public void structureChanged(XModelTreeEvent event) {
    	if(!active) return;
        XModel model = event.getModelObject().getModel();
        if (event.kind() == XModelTreeEvent.STRUCTURE_CHANGED &&
                event.getModelObject() == model.getRoot()) {
            binds.clear();
            model.removeModelTreeListener(this);
            active = false;
            return;
        }
        Map ps = getObjects(event);
        Iterator it = ps.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
			WebProcessUpdateHelper helper = (WebProcessUpdateHelper)entry.getKey();
            helper.structureChanged(event, (String)entry.getValue());
        }
    }

    public void nodeChanged(XModelTreeEvent event) {
		if(!active) return;
        Map<WebProcessUpdateHelper,String> ps = getObjects(event);
        Iterator it = ps.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
			WebProcessUpdateHelper helper = (WebProcessUpdateHelper)entry.getKey();
            helper.nodeChanged(event, (String)entry.getValue());
        }
    }

}

class Registry {
    HashMap<String,Registry> binds = new HashMap<String,Registry>(1);
    HashSet<WebProcessUpdateHelper> objects = new HashSet<WebProcessUpdateHelper>(1);
}
