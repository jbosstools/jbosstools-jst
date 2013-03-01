package org.jboss.tools.jst.web.ui.palette;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.jboss.tools.jst.web.ui.palette.xpl.MobileToolEntryEditPart;


public class MobilePaletteEditPartFactory extends CustomPaletteEditPartFactory{
	protected EditPart createDrawerEditPart(EditPart parentEditPart, Object model) {
		return new MobileDrawerEditPart((PaletteDrawer)model);
	}
	
	protected EditPart createEntryEditPart(EditPart parentEditPart, Object model) {
		return new MobileToolEntryEditPart((PaletteEntry) model);
	}
}
