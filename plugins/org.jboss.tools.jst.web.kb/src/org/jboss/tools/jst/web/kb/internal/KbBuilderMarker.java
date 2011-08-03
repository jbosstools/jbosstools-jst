package org.jboss.tools.jst.web.kb.internal;

import java.text.MessageFormat;
import java.util.ArrayList;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.common.preferences.SeverityPreferences;
import org.jboss.tools.jst.web.kb.KbMessages;
import org.jboss.tools.jst.web.kb.KbProjectFactory;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.preferences.KBSeverityPreferences;

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
		String preferenceValue = getPreference(r.getProject(), kind);
		
		if(SeverityPreferences.IGNORE.equals(preferenceValue)) {
			if(m != null) {
				m.delete();
			}
			return null;
		}
		
		if (m == null) {
			m = r.createMarker(KB_BUILDER_PROBLEM_MARKER_TYPE);
			m.setAttribute(KbBuilderMarker.ATTR_KIND, KbBuilderMarker.KIND_KB_NATURE_OR_BUILDER_MISSING);
			if(kind == KIND_KB_NATURE_OR_BUILDER_MISSING) {
				r.setPersistentProperty(KbProjectFactory.NATURE_MOCK, "true"); //$NON-NLS-1$
				KbProjectFactory.getKbProject(r.getProject(), true);
			}
		}
		
		int severity = IMarker.SEVERITY_WARNING;

		if(SeverityPreferences.ERROR.equals(preferenceValue)) {
			severity = IMarker.SEVERITY_ERROR;
		}
		String location = MessageFormat.format(KbMessages.KBPROBLEM_LOCATION, new Object[] {r.getProject().getName()});

		m.setAttribute(ATTR_KIND, kind);
		m.setAttribute(IMarker.LOCATION, location);
		m.setAttribute(IMarker.MESSAGE, message);
		m.setAttribute(IMarker.SEVERITY, severity);
		m.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_NORMAL);
		return m;
	}

	protected static String getPreference(IProject project, int kind) {
		if(kind == KIND_KB_NATURE_OR_BUILDER_MISSING) {
			return getPreference(project, KBSeverityPreferences.REQUIRED_KB_CAPABILITIES_ARE_MISSING);
		} else if(kind == KIND_DEPENDS_ON_NON_KB_POJECTS) {
			return getPreference(project, KBSeverityPreferences.KB_CAPABILITIES_ARE_NOT_ENABLED_IN_JAVA_MODULE);
		}
		return getPreference(project, KBSeverityPreferences.REQUIRED_KB_CAPABILITIES_ARE_MISSING);
	}

	protected static String getPreference(IProject project, String preferenceKey) {
		return KBSeverityPreferences.getInstance().getProjectPreference(project, preferenceKey);
	}
}
