package org.jboss.tools.jst.web.kb.internal;

import java.text.MessageFormat;
import java.util.ArrayList;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.jst.web.kb.KbMessages;
import org.jboss.tools.jst.web.kb.KbProjectFactory;
import org.jboss.tools.jst.web.kb.WebKbPlugin;

public class KbBuilderMarker {

	public static final String KB_BUILDER_PROBLEM_MARKER_TYPE = "org.jboss.tools.jst.web.kb.kbBuilderProblemMarker"; //$NON-NLS-1$

	//Default for backward compatibility.
	public static final int KIND_KB_NATURE_OR_BUILDER_MISSING = 1;

	public static final int KIND_DEPENDS_ON_NON_KB_POJECTS = 2;

	public static final String ATTR_KIND = "kind"; //$NON-NLS-1$

	public static IMarker[] getOwnedMarkers(IResource r, int kind) {
		ArrayList<IMarker> l = null;
		try {
			if(r != null && r.isAccessible()) {
				IMarker[] ms = r.findMarkers(null, false, 1);
				for (IMarker m: ms) {
					if(KB_BUILDER_PROBLEM_MARKER_TYPE.equals(m.getType())
							&& m.isSubtypeOf(IMarker.PROBLEM)
							&& (kind == m.getAttribute(ATTR_KIND, 1))) {
						if(l == null) {
							l = new ArrayList<IMarker>();
						}
						l.add(m);
					}
				}
			}
		} catch (CoreException e) {
			WebKbPlugin.getDefault().logError(e);
		}
		return (l == null) ? null : l.toArray(new IMarker[0]);
	}

	public static IMarker createOrUpdateKbProblemMarker(IMarker m, IResource r, String message, int kind) throws CoreException {
		String location = MessageFormat.format(KbMessages.KBPROBLEM_LOCATION, new Object[] {r.getProject().getName()});
		
		if (m == null) {
			m = r.createMarker(KB_BUILDER_PROBLEM_MARKER_TYPE);
			m.setAttribute(KbBuilderMarker.ATTR_KIND, KbBuilderMarker.KIND_KB_NATURE_OR_BUILDER_MISSING);
			if(kind == KIND_KB_NATURE_OR_BUILDER_MISSING) {
				r.setPersistentProperty(KbProjectFactory.NATURE_MOCK, "true"); //$NON-NLS-1$
				KbProjectFactory.getKbProject(r.getProject(), true);
			}
		}

		m.setAttribute(ATTR_KIND, kind);
		m.setAttribute(IMarker.LOCATION, location);
		m.setAttribute(IMarker.MESSAGE, message);
		m.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
		m.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_NORMAL);
		return m;
	}

}
