/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jst.css.view;

import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.IContributedContentsView;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.jboss.tools.jst.css.common.CSSStyleListener;
import org.jboss.tools.jst.css.common.ICSSViewListner;
import org.jboss.tools.jst.css.common.StyleContainer;
import org.jboss.tools.jst.css.properties.CSSPropertyPage;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class CSSEditorView extends PropertySheet implements ICSSViewListner {

	static public String CONTRIBUTOR_ID = "org.eclipse.wst.css.core.csssource.source"; //$NON-NLS-1$

	@Override
	public void init(IViewSite site) throws PartInitException {

		super.init(site);
		getSite().getPage().removeSelectionListener(this);
		CSSStyleListener.getInstance().addSelectionListener(this);
	}

	@Override
	public void dispose() {

		super.dispose();
		CSSStyleListener.getInstance().removeSelectionListener(this);

	}

	protected IPage createDefaultPage(PageBook book) {
		return createCssPropertyPage();
	}

	@Override
	protected PageRec doCreatePage(final IWorkbenchPart part) {
		IPage page = createCssPropertyPage();

		if (page != null) {
			return new PageRec(part, page);
		}

		return null;
	}

	private IPage createCssPropertyPage() {

		IPropertySheetPage page = new CSSPropertyPage(
				new ITabbedPropertySheetPageContributor() {

					public String getContributorId() {
						return CONTRIBUTOR_ID;
					}
				}, this);
		if (page != null) {
			if (page instanceof IPageBookViewPage) {
				initPage((IPageBookViewPage) page);
			}
			page.createControl(getPageBook());
		}

		return page;

	}

	@Override
	public SelectionProvider getSelectionProvider() {
		return super.getSelectionProvider();
	}

	@Override
	public Object getAdapter(Class key) {
		if (key == IContributedContentsView.class) {
			return new IContributedContentsView() {
				public IWorkbenchPart getContributingPart() {
					return getCurrentContributingPart();
				}
			};
		}
		return super.getAdapter(key);
	}

	public void styleChanged(StyleContainer styleContainer) {
		if (getCurrentPage() instanceof CSSPropertyPage)
			((CSSPropertyPage) getCurrentPage()).update();

	}

	@Override
	protected boolean isImportant(IWorkbenchPart part) {
		if ((part instanceof IEditorPart) || (part instanceof ContentOutline))
			return true;
		return false;
	}
}
