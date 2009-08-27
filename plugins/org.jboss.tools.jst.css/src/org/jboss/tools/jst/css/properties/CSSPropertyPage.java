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

package org.jboss.tools.jst.css.properties;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSNode;
import org.jboss.tools.jst.css.view.CSSEditorView;
import org.jboss.tools.jst.css.view.CSSViewUtil;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.StyleAttributes;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class CSSPropertyPage extends TabbedPropertySheetPage implements
		IChangeListener {

	private DataBindingContext bindingContext;

	private StyleAttributes styleAttributes;

	private CSSEditorView part;

	private Object selectedObject;

	public CSSPropertyPage(
			ITabbedPropertySheetPageContributor tabbedPropertySheetPageContributor,
			CSSEditorView part) {
		super(tabbedPropertySheetPageContributor);
		bindingContext = new DataBindingContext();
		styleAttributes = new StyleAttributes();
		((IObservable) styleAttributes.getAttributeMap())
				.addChangeListener(this);
		this.part = part;

	}

	@Override
	public void init(IPageSite pageSite) {
		super.init(pageSite);
		pageSite.getWorkbenchWindow().getSelectionService()
				.addPostSelectionListener(this);
	}

	@Override
	public void dispose() {
		getSite().getWorkbenchWindow().getSelectionService()
				.removePostSelectionListener(this);
		super.dispose();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {

		if ((this.part != part) && (selection instanceof IStructuredSelection)) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Object newSelectedObject = structuredSelection.getFirstElement();
			if (structuredSelection.getFirstElement() instanceof ICSSNode) {

				CSSStyleRule styleRule = CSSViewUtil
						.getStyleRule((ICSSNode) structuredSelection
								.getFirstElement());

				if (styleRule != null) {
					((IObservable) styleAttributes.getAttributeMap())
							.removeChangeListener(this);
					updateStyleAttributes(styleRule);
					((IObservable) styleAttributes.getAttributeMap())
							.addChangeListener(this);
					newSelectedObject = styleRule;
				}

			}

			if (selectedObject != newSelectedObject)
				super.selectionChanged(part, selection);
			selectedObject = newSelectedObject;

		}

	}

	private void updateStyleAttributes(CSSStyleRule styleRule) {

		getStyleAttributes().setStyleProperties(
				CSSViewUtil.getStyleAttributes(styleRule));

	}

	public void handleChange(ChangeEvent event) {

		if (selectedObject instanceof CSSStyleRule) {

			final CSSStyleDeclaration declaration = ((CSSStyleRule) selectedObject)
					.getStyle();

			// set properties
			final Set<Entry<String, String>> set = styleAttributes.entrySet();

			if ((set.size() == 0) && (declaration.getLength() > 0)) {
				declaration.setCssText(Constants.EMPTY);
			} else {
				for (final Map.Entry<String, String> me : set) {
					if ((me.getValue() == null)
							|| (me.getValue().length() == 0)) {
						declaration.removeProperty(me.getKey());
					} else {

						// FIX FOR BIDE-4790 in that case simple setting of new
						// value leads to error
						if (declaration.getPropertyValue(me.getKey()) != null)
							declaration.removeProperty(me.getKey());

						declaration.setProperty(me.getKey(), me.getValue(),
								Constants.EMPTY);
					}
				}
			}
		}

		notifySelectionChanged(new StructuredSelection(selectedObject));

	}

	protected void notifySelectionChanged(StructuredSelection selection) {
		part.postSelectionChanged(new SelectionChangedEvent(part
				.getSelectionProvider(), selection));
	}

	/**
	 * 
	 * @return
	 */
	public DataBindingContext getBindingContext() {
		return bindingContext;
	}

	public StyleAttributes getStyleAttributes() {
		return styleAttributes;
	}

}
