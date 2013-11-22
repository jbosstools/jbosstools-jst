package org.jboss.tools.jst.web.ui.internal.css.common;

import org.eclipse.ui.ISelectionListener;

public interface ICSSViewListner extends ISelectionListener {

	public void styleChanged(StyleContainer styleContainer);

}
