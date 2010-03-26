package org.jboss.tools.jst.jsp.outline.cssdialog.cssselector;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wst.css.core.internal.modelhandler.CSSModelLoader;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSModel;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSStyleRule;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSStyleSheet;
import org.eclipse.wst.css.ui.StructuredTextViewerConfigurationCSS;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.StructuredTextViewerConfiguration;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSRuleContainer;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.selection.CSSClassSelectionChangedEvent;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.selection.ICSSClassSelectionChangedListener;
import org.w3c.dom.css.CSSRuleList;

@SuppressWarnings("restriction")
public class CSSSelectorPreview extends Composite implements
		ICSSClassSelectionChangedListener, IElementComparer {

	private ISelection prevSelection;
	private StructuredTextViewer viewer;
	private ICSSModel model;
	private ICSSStyleSheet styleSheet;

	public CSSSelectorPreview(Composite parent) {
		super(parent, SWT.BORDER);
		setLayout(new FillLayout());
		initPreview();
	}

	private void initPreview() {
		StructuredTextViewerConfiguration baseConfiguration = new StructuredTextViewerConfigurationCSS();

		viewer = new StructuredTextViewer(this, null, null, false, SWT.BORDER
				| SWT.V_SCROLL | SWT.H_SCROLL);
		((StructuredTextViewer) viewer).getTextWidget().setFont(
				JFaceResources.getFont("org.eclipse.wst.sse.ui.textfont")); //$NON-NLS-1$

		viewer.configure(baseConfiguration);
		CSSModelLoader cssModelLoader = new CSSModelLoader();
		IStructuredModel model = cssModelLoader.createModel();
		this.model = (ICSSModel) model;
		styleSheet = (ICSSStyleSheet) this.model.getDocument();
		viewer.setDocument(model.getStructuredDocument());
		viewer.setEditable(false);
	}

	public void classSelectionChanged(final CSSClassSelectionChangedEvent event) {
		CSSRuleContainer[] containers = event.getSelectedRuleContainers();
		if (containers.length != 0) {
			List<CSSRuleContainer> ruleContainerList = new ArrayList<CSSRuleContainer>(0);
			for (int i = 0; i < containers.length; i++) {
				ruleContainerList.add(containers[i]);
			}
			ISelection newSelection = new StructuredSelection(ruleContainerList, this);
			if (prevSelection == null) {
				prevSelection = new StructuredSelection(ruleContainerList, this);
				clearPreview();
				for (int i = 0; i < containers.length; i++) {
					appendRuleFromContainer(containers[i]);
				}
			}
			if (!prevSelection.equals(newSelection)) {
				clearPreview();
				for (int i = 0; i < containers.length; i++) {
					appendRuleFromContainer(containers[i]);
				}
				prevSelection = newSelection;
			}
		}
	}

	private void appendRuleFromContainer(CSSRuleContainer container) {
		StringBuilder builder = new StringBuilder(""); //$NON-NLS-1$
		ICSSStyleRule rule = (ICSSStyleRule) container.getRule();
		String text = rule.getCssText();
		String styleDef = text.substring(text.indexOf('{'));
		builder.append("." + container.getSelectorName() + styleDef); //$NON-NLS-1$
		styleSheet.appendRule(styleSheet.createCSSRule(builder.toString()));
	}

	private void clearPreview() {
		CSSRuleList ruleList = styleSheet.getCssRules();
		for (int i = 0; i < ruleList.getLength(); i++) {
			styleSheet.removeRule(ruleList.item(i));
		}
	}

	public boolean equals(Object a, Object b) {
		return a.equals(b);
	}

	public int hashCode(Object element) {
		return 0;
	}

}
