package org.jboss.tools.jst.web.ui.palette;

import org.eclipse.gef.palette.PaletteDrawer;

public class MobileDrawerEditPart extends CustomDrawerEditPart {

	public MobileDrawerEditPart(PaletteDrawer drawer) {
		super(drawer);
	}

	@Override
	protected int getLayoutSetting() {
		return PaletteViewerPreferences.LAYOUT_ICONS;
	}

}
