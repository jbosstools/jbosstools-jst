/*******************************************************************************
 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.i18n;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.ui.internal.provisional.extensions.ISourceEditingTextTools;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.ui.internal.provisional.IDOMSourceEditingTextTools;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.model.util.ModelFeatureFactory;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.bundle.BundleMap;
import org.jboss.tools.jst.jsp.editor.IVisualContext;
import org.jboss.tools.jst.jsp.jspeditor.JSPTextEditor;
import org.jboss.tools.jst.jsp.jspeditor.SourceEditorPageContext;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.util.Constants;
import org.jboss.tools.jst.jsp.util.FaceletsUtil;
import org.jboss.tools.jst.web.project.WebProject;
import org.jboss.tools.jst.web.project.list.IWebPromptingProvider;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;
import org.jboss.tools.jst.web.tld.TaglibData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * The Utils class for externalize strings routine.
 */
public class ExternalizeStringsUtils {
	
	public static final char[] REPLACED_CHARACTERS = new char[] {'~', '!', '@', '#',
			'$', '%', '^', '&', '*', '(', ')', '-', '+', '=', '{', '}', '[', ']', ':', ';', ',', '.', '?', '\\', '/'};
	public static final char[] LINE_DELEMITERS = new char[] {'\r', '\n', '\t'};

	/**
	 * Checks that the user has selected a correct string.
	 *  
	 * @param selection the current selection
	 * @return <code>true</code> if correct
	 */
	public static boolean isSelectionCorrect(ISelection selection) {
		boolean isSelectionCorrect = false;
		if ((selection instanceof TextSelection)
				&& (selection instanceof IStructuredSelection)
				&& (((IStructuredSelection) selection).size() == 1)) {
			/*
			 * In general selection is correct now except one case:
			 */
			isSelectionCorrect = true;
			/*
			 * If the whole tag is selected -- check if it has children.
			 * The wizard could not find a proper string to externalize
			 * among several tags.
			 */
			Object selectedElement = ((IStructuredSelection) selection).getFirstElement();
			if (selectedElement instanceof Element) {
				Element element = (Element) selectedElement;
				if (element.hasChildNodes()) {
					isSelectionCorrect = false;
				} 
			}
		} 
		return isSelectionCorrect;
	}

	public static XModelObject findFacesConfig(XModel model) {
		IWebPromptingProvider provider = (IWebPromptingProvider)ModelFeatureFactory.getInstance().createFeatureInstance("org.jboss.tools.jsf.model.pv.JSFPromptingProvider"); //$NON-NLS-1$
		if(provider == null) {
			return null;
		}
		List<Object> result = provider.getList(model, IWebPromptingProvider.JSF_FACES_CONFIG, "", new Properties()); //$NON-NLS-1$
		if(result != null && !result.isEmpty()) {
			return (XModelObject)result.get(0);
		}
		return null;
	}

	/**
	 * Creates new bundle map if no one was specified 
	 * during initialization of the page.
	 *
	 * @param editor the source editor 
	 * @return the new bundle map
	 */
	public static BundleMap createBundleMap(ITextEditor editor) {
		String uri = null;
		String prefix = null;
		int hash;
		Map<?, ?> map = null;
		BundleMap bm = new BundleMap();
		bm.init(editor.getEditorInput());

		/*
		 * Check JSF Nature
		 */
		boolean hasJsfProjectNatureType = false;
		try {
			IEditorInput ei = editor.getEditorInput();
			if(ei instanceof IFileEditorInput) {
				IProject project = ((IFileEditorInput)ei).getFile().getProject();
				if (project.exists() && project.isOpen()) {
					if (project.hasNature(WebProject.JSF_NATURE_ID)) {
						hasJsfProjectNatureType = true;
					}
				}
			}
		} catch (CoreException e) {
			JspEditorPlugin.getPluginLog().logError(e);
		}
		/*
		 * Get Bundles from faces-config.xml
		 */
		if (hasJsfProjectNatureType
				&& (editor.getEditorInput() instanceof IFileEditorInput)) {
			IProject project = ((IFileEditorInput) editor.getEditorInput())
			.getFile().getProject();
			IModelNature modelNature = EclipseResourceUtil.getModelNature(project);
			if (modelNature != null) {
				XModel model = modelNature.getModel();
				List<Object> l = WebPromptingProvider.getInstance().getList(model,
						WebPromptingProvider.JSF_REGISTERED_BUNDLES, null, null);
				if (l != null && l.size() > 1 && (l.get(1) instanceof Map)) {
					map = (Map<?, ?>) l.get(1);
					if ((null != map) && (map.keySet().size() > 0)) {
						Iterator<?> it = map.keySet().iterator();
						while (it.hasNext()) {
							uri = it.next().toString();
							prefix = map.get(uri).toString();
							hash = (prefix + ":" + uri).hashCode(); //$NON-NLS-1$
							bm.addBundle(hash, prefix, uri, false);
						}
					}
				} 
			}
		}
		ISourceEditingTextTools sourceEditingTextTools = 
			(ISourceEditingTextTools) editor
			.getAdapter(ISourceEditingTextTools.class);
		IDOMSourceEditingTextTools domSourceEditingTextTools = 
			(IDOMSourceEditingTextTools) sourceEditingTextTools;
		Document documentWithBundles = domSourceEditingTextTools.getDOMDocument();
		
		/*
		 * When facelets are used -- get bundles from the template file
		 */
		if (editor instanceof JSPTextEditor) {
			IVisualContext context =  ((JSPTextEditor) editor).getPageContext();
			List<TaglibData> taglibs = null;
			if (context instanceof SourceEditorPageContext) {
				SourceEditorPageContext sourcePageContext = (SourceEditorPageContext) context;
				taglibs = sourcePageContext.getTagLibs();
			}
			if (null == taglibs) {
				JspEditorPlugin.getDefault().logError(
						JstUIMessages.CANNOT_LOAD_TAGLIBS_FROM_PAGE_CONTEXT);
			} else {
				Element root = FaceletsUtil.findComponentElement(documentWithBundles.getDocumentElement());
				if ((root != null) && FaceletsUtil.isFacelet(root, taglibs)
						&& root.hasAttribute("template")) { //$NON-NLS-1$
					String filePath= root.getAttributeNode("template").getNodeValue(); //$NON-NLS-1$
					if (((JSPTextEditor) editor).getEditorInput() instanceof FileEditorInput) {
						FileEditorInput fei = (FileEditorInput) ((JSPTextEditor) editor).getEditorInput();
						IResource webContentResource = EclipseResourceUtil.getFirstWebContentResource(fei.getFile().getProject());
						if (webContentResource instanceof IContainer) {
							IFile templateFile = (IFile) ((IContainer) webContentResource).findMember(filePath);
							Document document = null;
							IDOMModel wtpModel = null;
							try {
								wtpModel = (IDOMModel)StructuredModelManager.getModelManager().getModelForRead(templateFile);
								if (wtpModel != null) {
									document = wtpModel.getDocument();
								}
							} catch(IOException e) {
								JspEditorPlugin.getPluginLog().logError(e);
							} catch(CoreException e) {
								JspEditorPlugin.getPluginLog().logError(e);
							} finally {
								if(wtpModel!=null) {
									wtpModel.releaseFromRead();
								}
							}
							if (null != document) {
								/*
								 * Change the document where to look bundles
								 */
								documentWithBundles = document;
							}
						}
					}
				}
			}
		}
		/*
		 * Add bundles from <f:loadBundle> tags on the current page
		 */
		NodeList list = documentWithBundles.getElementsByTagName("f:loadBundle"); //$NON-NLS-1$
		for (int i = 0; i < list.getLength(); i++) {
			Element node = (Element) list.item(i);
			uri = node.getAttribute("basename"); //$NON-NLS-1$
			prefix = node.getAttribute("var"); //$NON-NLS-1$
			hash = node.hashCode();
			bm.addBundle(hash, prefix, uri, false);
		}
		return bm;
	}

	public static Table createPropertiesTable(Composite parent, int style) {
		Table table = new Table(parent, style);
        TableLayout layout = new TableLayout();
        table.setLayout(layout);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
		
        ColumnLayoutData columnLayoutData;
        TableColumn propNameColumn = new TableColumn(table, SWT.NONE);
        propNameColumn.setText(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPERTY_NAME);
        columnLayoutData = new ColumnWeightData(200, true);
        layout.addColumnData(columnLayoutData);
        TableColumn propValueColumn = new TableColumn(table, SWT.NONE);
        propValueColumn.setText(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPERTY_VALUE);
        columnLayoutData = new ColumnWeightData(200, true);
        layout.addColumnData(columnLayoutData);
        
        return table;
	}
	
	/**
	 *  Update resource bundle table according to the selected file:
	 *  put key and value pairs to the table
	 *  
	 * @param table the UI table
	 * @param propertiesFile  resource bundle file
	 */
	public static void populatePropertiesTable(Table table, IFile propertiesFile) {
		if ((propertiesFile != null) && propertiesFile.exists()) {
			BufferedReader in = null;
			try {
				/*
				 * Read the file content
				 */
				String encoding = FileUtil.getEncoding(propertiesFile);
				in = new BufferedReader(new InputStreamReader(
						propertiesFile.getContents(), encoding));
				readFileToProperies(table, in);
				in.close();
			} catch (CoreException e) {
				JspEditorPlugin.getDefault().logError(
						"Could not load file content for '" + propertiesFile + "'", e); //$NON-NLS-1$ //$NON-NLS-2$
			} catch (IOException e) {
				JspEditorPlugin.getDefault().logError(
						"Could not read file: '" + propertiesFile + "'", e); //$NON-NLS-1$ //$NON-NLS-2$
			} finally {
				in = null;
			}
			
			} else {
				JspEditorPlugin.getDefault().logError(
						"Bundle File '" + propertiesFile + "' does not exist!"); //$NON-NLS-1$ //$NON-NLS-2$
			}
	}
	
	public static void populatePropertiesTable(Table table, File propertiesFile) {
		if ((propertiesFile != null) && (propertiesFile.exists())) {
			/*
			 * Read the file content
			 */
			FileReader fr = null;
			try {
				fr = new FileReader(propertiesFile);
				readFileToProperies(table, fr);
				fr.close();
			} catch (FileNotFoundException e) {
				JspEditorPlugin.getDefault().logError(e);
			} catch (IOException e) {
				JspEditorPlugin.getDefault().logError(
						"Could not read file: '" + propertiesFile + "'", e); //$NON-NLS-1$ //$NON-NLS-2$
			} finally {
				fr = null;
			}
		} else {
			JspEditorPlugin.getDefault().logError(
					"Bundle File '" + propertiesFile + "' does not exist!"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	private static void readFileToProperies(Table table, Reader r) {
		Properties properties =  new Properties();
		try {
			properties.load(r);
			r.close();
		} catch (IOException e) {
			JspEditorPlugin.getDefault().logError(
					"Could not parse properties file.", e); //$NON-NLS-1$
		}
		/*
		 * Clear the table
		 */
		table.removeAll();
		/*
		 * Fill in new values
		 */
		int k = 0;
		Set<String> keys = properties.stringPropertyNames();
		List<String> keysList = new ArrayList<String>(keys);  
		Collections.sort(keysList);
		for (String key : keysList) {
			TableItem tableItem = null;
			tableItem = new TableItem(table, SWT.BORDER, k);
			k++;
			tableItem.setText(new String[] {key, properties.getProperty(key)});
		}
	}
	
	/**
	 * Generate properties key.
	 * Replaces all non-word characters with 
	 * underline character.
	 *
	 * @param text the text
	 * @return the result string
	 */
	public static String generatePropertyKey(String text) {
		String result = text.trim();
		/*
		 * Update text string field.
		 * Trim the text to remove line breaks and caret returns.
		 * Replace line delimiters white space
		 */
		for (char ch : LINE_DELEMITERS) {
			text = text.trim().replace(ch, ' ');
		}
		/*
		 * Replace all other symbols with '_'
		 */
		for (char ch : REPLACED_CHARACTERS) {
			result = result.replace(ch, '_');
		}
		/*
		 * Replace all white spaces with '_'
		 */
		result = result.replaceAll(Constants.WHITE_SPACE,
				Constants.UNDERSCORE);
		/*
		 * Correct underline symbols:
		 * show only one of them
		 */
		result = result.replaceAll("_+", Constants.UNDERSCORE); //$NON-NLS-1$
		/*
		 * Remove leading and trailing '_'
		 */
		if (result.startsWith(Constants.UNDERSCORE)) {
			result = result.substring(1);
		}
		if (result.endsWith(Constants.UNDERSCORE)) {
			result = result.substring(0, result.length() - 1);
		}
		/*
		 * Return the result
		 */
		return result;
	}
}
