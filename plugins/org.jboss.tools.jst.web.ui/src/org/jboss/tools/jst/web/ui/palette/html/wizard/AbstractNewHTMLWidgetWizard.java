/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette.html.wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorInput;
import org.jboss.tools.common.model.ui.editors.dnd.DefaultDropWizardPage;
import org.jboss.tools.common.model.ui.editors.dnd.IDropCommand;
import org.jboss.tools.common.model.ui.editors.dnd.IDropWizard;
import org.jboss.tools.common.model.ui.editors.dnd.IDropWizardModel;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.ElementNode;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.NodeWriter;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.RootNode;
import org.jboss.tools.jst.jsp.jspeditor.dnd.PaletteDropCommand;
import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.LinkAttributeProvider;
import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.LinkAttributeProvider.ElementID;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class AbstractNewHTMLWidgetWizard extends Wizard implements PropertyChangeListener, IDropWizard, HTMLConstants {
	protected IDropCommand command;
	Set<String> ids = new HashSet<String>();

	public AbstractNewHTMLWidgetWizard() {}

	@Override
	public final void addPages() {
		doAddPages();
		getWizardModel().addPropertyChangeListener(this);	
		getWizardModel().setElementGenerator(g);
	}

	protected void doAddPages() {
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		IWizardPage[] pages = getPages();
		for (int i = 0; i < pages.length; i++) {
			DefaultDropWizardPage page = (DefaultDropWizardPage)pages[i];
			page.runValidation();
		}
	}
	
	@Override
	public final boolean performFinish() {
		IRunnableWithProgress runnable = new IRunnableWithProgress() {			
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException,
					InterruptedException {
				doPerformFinish();
			}
		};
		try {
			getContainer().run(false, false, runnable);
		} catch (InterruptedException e) {
			WebUiPlugin.getDefault().logError(e);
			return false;
		} catch (InvocationTargetException e) {
			WebUiPlugin.getDefault().logError(e.getCause());
			return false;
		}
		return true;
	}

	protected void doPerformFinish() {
		command.execute();		
	}

	/**
	 * Generates data to be inserted into the edited file.
	 * Text is split into startText and endText so that 
	 * after insertion cursor in editor is put between
	 * startText and endText. If there is no need to add to
	 * inner content of the widget, all data may be provided 
	 * in startText.
	 * This method may be overridden by subclasses, but 
	 * in most cases it is not needed.
	 */
	protected String[] generateData() {
		ElementNode root = createRoot();
		addContent(root);
		NodeWriter w = new NodeWriter(true);
		root.flush(w, 0);
		String[] result = w.getResult();
		String startText = result[0];
		String endText = result.length < 2 ? "" : result[1];
		return new String[]{startText, endText};
	}

	protected Properties getCommandProperties() {
		return ((PaletteDropCommand)command).getProperties();
	}

	/**
	 * Should be overrided to provide html presentation of the widget. 
	 * @return
	 */
	protected void addContent(ElementNode parent) {
	}

	/**
	 * Should be overrided to provide required environment for widget. 
	 * @return
	 */
	public String getTextForBrowser() {
		ElementNode html = new ElementNode(TAG_HTML, false);
		ElementNode body = html.addChild(TAG_BODY);
		addContent(body);
		NodeWriter sb = new NodeWriter(false);
		body.flush(sb, 0);
		return sb.getText();
	}

	public String getTextForTextView() {
		ElementNode root = createRoot();
		addContent(root);
		NodeWriter sb = new NodeWriter(false);
		root.flush(sb, 0);
		return sb.getText();
	}
	@Override
	public void setCommand(IDropCommand command) {
		this.command = command;
		collectAllIDs();
	}

	public boolean isIDAvailable(String id) {
		return !ids.contains(id);
	}

	@Override
	public IDropWizardModel getWizardModel() {
		return command.getDefaultModel();
	}

	public void dispose() {
		getWizardModel().removePropertyChangeListener(this);
		super.dispose();
	}

	/**
	 * Finds index such that String id = maskPrefix + index + maskSuffix;
	 * will be new id and/or name on the page.
	 * 
	 * @param maskPrefix
	 * @param maskSuffix
	 * @param countFrom
	 * @return
	 */
	protected int generateIndex(String maskPrefix, String maskSuffix, int countFrom) {
		while(ids.contains(maskPrefix + countFrom + maskSuffix)) countFrom++;
		return countFrom;
	}

	/**
	 * Collects all values of attributes 'id' and 'name' on the page.
	 * Result set is used by generateIndex.
	 * @see generateIndex()
	 */
	private void collectAllIDs() {
		String text = command.getDefaultModel().getDropData().getSourceViewer().getDocument().get();
		int index = 0;
		while(index >= 0 && index < text.length()) {
			int i1 = next(text, ATTR_NAME, index);
			int i2 = next(text, ATTR_ID, index);
			index = (i1 < 0) ? i2 : (i2 < 0) ? i1 : (i1 < i2) ? i1 : i2;
			if(index < 0) return;
			readValue(text, index);
		}
	}

	public List<ElementID> getIDs() {
		IEditorInput input = command.getDefaultModel().getDropData().getEditorInput();
		IFile file = (IFile)input.getAdapter(IFile.class);
		if(file==null) {
			return Collections.emptyList();
		}
		return LinkAttributeProvider.findAllIds(file, false);
	}

	public IFile getFile() {
		IEditorInput input = command.getDefaultModel().getDropData().getEditorInput();
		return (IFile)input.getAdapter(IFile.class);
	}

	private int next(String text, String attrName, int from) {
		int i = text.indexOf(attrName, from);
		while(i >= 0) {
			if(i == 0 || Character.isWhitespace(text.charAt(i - 1))) {
				i += attrName.length();
				for (; ; i++) {
					if(i >= text.length()) {
						return -1;
					}
					char ch = text.charAt(i);
					if(ch == '=') {
						return i;
					}
					if(!Character.isWhitespace(ch)) {
						break;
					}
				}
			}
			i = text.indexOf(attrName, i + attrName.length());
		}
		return -1;
	}
	private void readValue(String text, int from) {
		int i = text.indexOf("\"", from);
		if(i < 0) {
			return;
		}
		int j = text.indexOf("\"", i + 1);
		if(j < 0) {
			return;
		}
		String value = text.substring(i + 1, j).trim();
		ids.add(value);
	}

	public static ElementNode createRoot() {
		return RootNode.newRoot();
	}
	
	protected static IElementGenerator.ElementNode SEPARATOR = IElementGenerator.SEPARATOR;

	ElementGenerator g = new ElementGenerator();

	class ElementGenerator implements IElementGenerator {
		String startText;
		String endText;

		public String generateStartTag() {
			String[] results = generateData();
			startText = results[0];
			endText = results[1];
			return startText;
		}

		@Override
		public void setDataModel(Object object) {
		}

		@Override
		public String generateEndTag() {
			return endText;
		}
	}

}

