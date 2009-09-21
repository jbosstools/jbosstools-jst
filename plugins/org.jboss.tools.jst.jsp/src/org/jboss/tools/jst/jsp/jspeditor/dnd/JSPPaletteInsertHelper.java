package org.jboss.tools.jst.jsp.jspeditor.dnd;

import org.jboss.tools.common.model.ui.views.palette.PaletteInsertHelper;

public class JSPPaletteInsertHelper extends PaletteInsertHelper {

	static JSPPaletteInsertHelper instance = new JSPPaletteInsertHelper();

	public static JSPPaletteInsertHelper getInstance() {
		return instance;
	}

	public JSPPaletteInsertHelper() {}

}
