/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.jsp.outline;

import java.util.Properties;
import org.jboss.tools.common.model.ui.wizards.query.AbstractQueryWizard;
import org.jboss.tools.common.model.ui.wizards.query.AbstractQueryWizardView;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.jboss.tools.jst.jsp.editor.IVisualContext;

import org.jboss.tools.common.meta.action.XActionList;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.IAttributeValue;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.IAttributeValueContainer;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.ModelElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.ui.AttributeValueContentProvider;
import org.jboss.tools.jst.jsp.drop.treeviewer.ui.AttributeValueLabelProvider;
import org.jboss.tools.jst.jsp.drop.treeviewer.ui.AttributeValueSorter;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageProcessor;
import org.jboss.tools.jst.web.kb.taglib.IAttribute;

/**
 * @author Kabanovich
 * External dialog called by JSPDialogCellEditor.
 */

public class JSPTreeDialog extends AbstractQueryWizard {
	
	public JSPTreeDialog() {
		setView(new JSPTreeDialogView());
	}

}

class JSPTreeDialogView extends AbstractQueryWizardView {
	protected TreeViewer treeViewer;
	protected AttributeValueLabelProvider labelProvider;
	AttributeValueContentProvider contentProvider;
	Properties context;
	ModelElement root;

	public void setObject(Object object) {
		context = findProperties(object);
	}

	public Control createControl(Composite parent) {
		GridLayout layout = new GridLayout(1, false);
		layout.verticalSpacing = 2;
		layout.marginWidth = 0;
		layout.marginHeight = 2;
		parent.setLayout(layout);

		treeViewer = new TreeViewer(parent);
		labelProvider = new AttributeValueLabelProvider();
		treeViewer.setLabelProvider(labelProvider);
		treeViewer.setContentProvider(contentProvider = new AttributeValueContentProvider());
		treeViewer.setUseHashlookup(true);

		// layout the tree viewer below the text field
		GridData layoutData = new GridData(GridData.FILL_BOTH);
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.grabExcessVerticalSpace = true;
		treeViewer.getControl().setLayoutData(layoutData);

		String query = context.getProperty("query");
		//ValueHelper valueHelper = (ValueHelper)context.get("valueHelper");
		IPageContext pageContext = (IPageContext)context.get("pageContext");
		KbQuery kbQuery = (KbQuery)context.get("kbQuery");
		IAttribute[] attrs = PageProcessor.getInstance().getAttributes(kbQuery, pageContext);
		
		
		ValueHelper valueHelper = new ValueHelper();
		root = valueHelper.getInitalInput(query);
		treeViewer.setInput(root);
		treeViewer.setSorter(new AttributeValueSorter());
		getCommandBar().setEnabled(OK, false);
		JSPTreeMenuInvoker menuInvoker = new JSPTreeMenuInvoker();
		menuInvoker.setViewer(treeViewer);
		menuInvoker.setContext(context);
		treeViewer.getTree().addMouseListener(menuInvoker);
		ModelElement initialSelection = getInitialSelection();
		if(initialSelection != null) {
			treeViewer.setSelection(new StructuredSelection(initialSelection), true);
		}
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = event.getSelection();
				String value = null;
				if(!selection.isEmpty() && (selection instanceof StructuredSelection)) {
					Object selected = ((StructuredSelection)selection).getFirstElement();
					if(selected instanceof IAttributeValue) {
						value = ((IAttributeValue)selected).getValue();
					}
				}
				getCommandBar().setEnabled(OK, value != null);
				if(value == null) {
					context.remove("value");
				} else {
					context.setProperty("value", value);
				}
			}
		});
		return treeViewer.getControl();
	}
	
	private ModelElement getInitialSelection() {
		String value = context.getProperty("value");
		if(value == null || value.length() == 0 || value.equals("#{") || value.equals("#{}")) {
			ModelElement[] cs = ((IAttributeValueContainer)root).getChildren();
			return cs.length == 0 ? root : cs[0];
		}
		return matchChildren(root, value, Integer.MIN_VALUE + 2);
	}
	
	private ModelElement matchChildren(Object current, String value, int dv) {
		if(!(current instanceof IAttributeValueContainer)) return (ModelElement)current;
		Object[] cs = contentProvider.getChildren(current);
		ModelElement best = null;
		for (int i = cs.length - 1; (i >= 0) && (dv != 0); i--) {
			ModelElement m = (ModelElement)cs[i];
			int v = m.compareValue(value);
			if(v == 0) return m;
			if(v == Integer.MAX_VALUE) continue;
			if(isBetter(v, dv)) {
				best = (v < 0) ? m : matchChildren(m, value, dv);
				dv = (best == cs[i]) ? v : best.compareValue(value);
			}			
		}
		if(best != null) return best;
		return (ModelElement)current;
	}
	
	boolean isBetter(int v, int dv) {
		if(v == 0) return true;
		if(v < 0 && dv < 0) return true; // go up the list
		if(v < 0 && dv > 0) return true;
		if(v > 0 && dv > 0) return v <= dv;
		if(v > 0 && dv < -1000) return true;
		return false;
	}
	
}

class JSPTreeMenuInvoker implements MouseListener {
	protected TreeViewer viewer;
	protected boolean onKeyRelease = false;
	Properties context;
	
	public void setContext(Properties context) {
		this.context = context;
	}

	public void setViewer(TreeViewer viewer) {
		this.viewer = viewer;
	}
	
	public void setOnKeyRelease(boolean b) {
		onKeyRelease = b;
	}

	public void mouseDoubleClick(MouseEvent e) {
	}

	public void mouseDown(MouseEvent e) {
	}

	public void mouseUp(MouseEvent e) {
		handleMouseUp(e);
	}
	
	protected void handleMouseUp(MouseEvent e) {
		if(e.button == 3) { 
			ModelElement eo = getModelObjectAt(new Point(e.x, e.y));
			ModelElement o = getSelectedModelObject();
			if(o == null && eo == null) return;
			if(eo != null && o == null) {
				o = eo;
			}
			String[] actions = o.getActions();
			if(actions == null || actions.length == 0) return;
			Menu menu = createMenu(viewer.getControl(), o);
			menu.setVisible(true);
		}
	}
	
	protected XActionList getActionList(XModelObject o) {
		return o.getModelEntity().getActionList();
	}

	public ModelElement getSelectedModelObject() {
		TreeItem[] ti = ((TreeViewer)viewer).getTree().getSelection();
		return (ti == null || ti.length == 0) ? null : getObjectByItem(ti[0]);
	}
	
	private ModelElement getObjectByItem(TreeItem i) {
		Object data = i.getData();
		if(data instanceof ModelElement) return (ModelElement)data;
		return null;
	}
	
	public ModelElement getModelObjectAt(Point p) {
		TreeItem i = ((TreeViewer)viewer).getTree().getItem(p);
		return (i == null) ? null : getObjectByItem(i);
	}
	
	private Menu createMenu(Control parent, ModelElement o) {
		String[] actions = o.getActions();
		Menu menu = new Menu(parent);
		for (int i = 0; i < actions.length; i++) {
			MenuItem item = new MenuItem(menu, SWT.CASCADE);
			item.setText(actions[i]);
			item.setEnabled(true);
			item.addSelectionListener(new AL(o, actions[i]));
		}
		return menu;		
	}
	
	class AL implements SelectionListener {
		ModelElement o;
		String action;
		AL(ModelElement o, String action) {
			this.o = o;
			this.action = action;
		}
		public void widgetSelected(SelectionEvent e) {
			Properties p = new Properties();
			o.action(action, p);
			viewer.refresh(o);
			Object c = p.get("select");
			if(c != null) {
				viewer.setSelection(new StructuredSelection(c), true);
				if(context != null) {
					ValueHelper valueHelper = (ValueHelper)context.get("valueHelper");
					if(valueHelper != null && valueHelper.getController() != null) {
						if(valueHelper.getTaglibManager() instanceof IVisualContext) {
							((IVisualContext)valueHelper.getTaglibManager()).refreshBundleValues();
						}
						valueHelper.getController().refreshExternalLinks();
						valueHelper.getController().visualRefresh();
					}
				}
			}
		}
		public void widgetDefaultSelected(SelectionEvent e) {}
	}
	
}


