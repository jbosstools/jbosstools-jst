/*******************************************************************************
 * Copyright (c) 2007-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.internal.editor.jspeditor.dnd;

import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.XModelObjectConstants;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.options.SharableConstants;
import org.jboss.tools.common.model.ui.ModelUIPlugin;
import org.jboss.tools.common.model.ui.dnd.DnDUtil;
import org.jboss.tools.common.model.ui.editor.IModelObjectEditorInput;
import org.jboss.tools.common.model.ui.editors.dnd.DropData;
import org.jboss.tools.common.model.ui.editors.dnd.DropWizard;
import org.jboss.tools.common.model.ui.editors.dnd.IDropWizard;
import org.jboss.tools.common.model.ui.editors.dnd.IDropWizardModel;
import org.jboss.tools.common.model.ui.editors.dnd.PaletteDropWizardModel;
import org.jboss.tools.common.model.ui.editors.dnd.composite.TagAttributesComposite.AttributeDescriptorValue;
import org.jboss.tools.common.model.ui.views.palette.PaletteInsertHelper;
import org.jboss.tools.common.model.ui.views.palette.PaletteInsertManager;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.tld.IWebProject;
import org.jboss.tools.jst.web.tld.URIConstants;
import org.jboss.tools.jst.web.tld.WebProjectFactory;
import org.jboss.tools.jst.web.tld.model.helpers.TLDToPaletteHelper;
import org.jboss.tools.jst.web.ui.palette.model.PaletteModel;

/**
 * 
 * @author eskimo
 */
public class PaletteDropCommand extends FileDropCommand {
	Properties initialValues = new Properties();
	String startText;
	String endText;	
	String newLine;
	String reformat = "no"; //$NON-NLS-1$

	Properties properties = new Properties();
	boolean callPaletteWizard = false;

	/**
	 * 
	 */
	protected IDropWizardModel createSpecificModel() {
		return new PaletteDropWizardModel(tagProposalFactory);
	}

	protected void addCustomProperties(Properties runningProperties) {		
		newLine = properties.getProperty(PaletteInsertHelper.PROPERTY_NEW_LINE);
		if (newLine == null) newLine="true"; //$NON-NLS-1$
		runningProperties.setProperty(PaletteInsertHelper.PROPERTY_NEW_LINE, newLine);
		String addTaglib = properties.getProperty(JSPPaletteInsertHelper.PROPOPERTY_ADD_TAGLIB);
		if(addTaglib == null) addTaglib = "true"; //$NON-NLS-1$
		runningProperties.setProperty(JSPPaletteInsertHelper.PROPOPERTY_ADD_TAGLIB, addTaglib);
	}
	
	public void execute() {
		if(getDefaultModel().getTagProposal()==IDropWizardModel.UNDEFINED_TAG_PROPOSAL) {
			if(startText == null && endText == null) return;
			int pos = ((ITextSelection)getDefaultModel().getDropData().getSelectionProvider().getSelection()).getOffset();
			getDefaultModel().getDropData().getSourceViewer().setSelectedRange(pos, 0);
			if(startText != null) properties.setProperty(PaletteInsertHelper.PROPERTY_START_TEXT, startText);
			if(endText != null) properties.setProperty(PaletteInsertHelper.PROPERTY_END_TEXT, endText);
			if(reformat != null) properties.setProperty(PaletteInsertHelper.PROPERTY_REFORMAT_BODY, reformat);
			if(newLine != null) properties.setProperty(PaletteInsertHelper.PROPERTY_NEW_LINE, newLine);
			JSPPaletteInsertHelper.getInstance().insertIntoEditor(
					getDefaultModel().getDropData().getSourceViewer(),
					properties
			);
		} else {
			DropData data = getDefaultModel().getDropData();
			ISourceViewer viewer = data.getSourceViewer();
			if(data.getContainer() != null){
				if (data.getContainer() instanceof ElementImpl) {
					ElementImpl container = (ElementImpl)data.getContainer();
					if(!container.hasEndTag()){
						try{
							IDocument document = viewer.getDocument();
							int containerOffset = container.getStartOffset();
							int containerLenght = container.getStartEndOffset()-containerOffset;
							String containerString = document.get(containerOffset, containerLenght);
							int slashPosition = containerString.lastIndexOf("/"); //$NON-NLS-1$
							if(slashPosition >= 0){
								int deltaOffset =  (containerString.length()-1)-slashPosition;
								String text = ""; //$NON-NLS-1$
								for(int i=0; i < deltaOffset;i++) text += " "; //$NON-NLS-1$
								text += "></"+container.getNodeName()+">"; //$NON-NLS-1$ //$NON-NLS-2$
								document.replace(containerOffset+slashPosition, containerString.length()-slashPosition, text);
							}
						}catch(BadLocationException ex){
							ModelUIPlugin.getDefault().logError(ex);
						}
					}
				}
			}		
			super.execute();
		}
	}

	protected void fillPropertiesForRun(Properties properties) {
		if(this.properties.containsKey(SharableConstants.PALETTE_PATH)) {
			properties.put(SharableConstants.PALETTE_PATH, this.properties.getProperty(SharableConstants.PALETTE_PATH));
		}
		if(this.properties.containsKey(MobilePaletteInsertHelper.PROPOPERTY_JQUERY_MOBILE_INSERT_JS_CSS)) {
			properties.put(MobilePaletteInsertHelper.PROPOPERTY_JQUERY_MOBILE_INSERT_JS_CSS, this.properties.get(MobilePaletteInsertHelper.PROPOPERTY_JQUERY_MOBILE_INSERT_JS_CSS));
		}
	}

	public void initialize() {
		DropData data = getDefaultModel().getDropData();		
		
		IEditorInput input = data.getEditorInput();
		XModelObject target = null;
		IFile f = null;
		if(input instanceof IFileEditorInput) {
			f = ((IFileEditorInput)input).getFile();
			target = EclipseResourceUtil.getObjectByResource(f);
			if(target == null && f.exists()) {
				target = EclipseResourceUtil.createObjectForResource(f);
			}
		} else if(input instanceof IModelObjectEditorInput) {
			target = ((IModelObjectEditorInput)input).getXModelObject();
		}
		if(target == null) {
			initialize2();
		} else {
			ISourceViewer viewer = data.getSourceViewer();
			
			properties = new Properties();
			properties.put("viewer", viewer); //$NON-NLS-1$
			properties.setProperty("text", viewer.getDocument().get()); //$NON-NLS-1$
			properties.setProperty("isDrop", "true"); //$NON-NLS-1$ //$NON-NLS-2$
			properties.setProperty("actionSourceGUIComponentID", "editor"); //$NON-NLS-1$ //$NON-NLS-2$
			properties.setProperty("accepsAsString", "true"); //$NON-NLS-1$ //$NON-NLS-2$
				
			if(f != null) {
				properties.put("file", f); //$NON-NLS-1$
			}
			ISelection selection = data.getSelectionProvider().getSelection();
			
			int offset = 0;
			//int length = 0;
			if(selection instanceof ITextSelection) {
				offset = ((ITextSelection)selection).getOffset();
				//length = ((ITextSelection)selection).getLength();
			} else {
				offset = viewer.getTextWidget().getCaretOffset();
			}
			properties.setProperty("pos", "" + offset); //$NON-NLS-1$ //$NON-NLS-2$
			if(selection instanceof IStructuredSelection && !selection.isEmpty()) {
				Object so = ((IStructuredSelection)selection).getFirstElement();
				if(so instanceof IDOMElement) {
					String en = ((IDOMElement)so).getNodeName();
					properties.setProperty("context:tagName", en); //$NON-NLS-1$
					String attrName = data.getAttributeName();
					if(attrName != null) {
						properties.setProperty("context:attrName", attrName); //$NON-NLS-1$
					}
				}
			}
			try {
				if(DnDUtil.isPasteEnabled(target)) {
					DnDUtil.paste(target, properties);
				} else {
					XModelObject s = PreferenceModelUtilities.getPreferenceModel().getModelBuffer().source();
					if(s != null) {
						properties.setProperty(XModelObjectConstants.START_TEXT, "" + getDefaultText(s)); //$NON-NLS-1$
						properties.setProperty(XModelObjectConstants.END_TEXT, ""); //$NON-NLS-1$
						properties.setProperty("new line", "newLine"); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
			} catch (CoreException e) {
				ModelUIPlugin.getDefault().logError(e);
			}
			startText = properties.getProperty(TLDToPaletteHelper.START_TEXT);
			endText = properties.getProperty(TLDToPaletteHelper.END_TEXT);
			reformat = properties.getProperty(TLDToPaletteHelper.REFORMAT);
			String uri = properties.getProperty(URIConstants.LIBRARY_URI);
			String libraryVersion = properties.getProperty(URIConstants.LIBRARY_VERSION);
			String defaultPrefix = properties.getProperty(URIConstants.DEFAULT_PREFIX);
			String tagname = properties.getProperty("tag name"); //$NON-NLS-1$
			if(tagname == null && getCustomWizardName() != null) {
				if (properties.getProperty(SharableConstants.PALETTE_PATH, "").startsWith(PaletteModel.MOBILE_PATH)) {
					data.setValueProvider(null);
				}
				tagname = "div";
				properties.setProperty("tag name", tagname); //$NON-NLS-1$
			}
			
			callPaletteWizard = PaletteInsertManager.getInstance().getWizardName(properties) != null;
			
			boolean isWizardEnabled = (!"FileJAVA".equals(target.getModelEntity().getName())); //$NON-NLS-1$
			if(getDefaultModel() instanceof PaletteDropWizardModel) {
				((PaletteDropWizardModel)getDefaultModel()).setWizardEnabled(isWizardEnabled);
			}
			
			if(uri == null || tagname == null) {
				getDefaultModel().setTagProposal(IDropWizardModel.UNDEFINED_TAG_PROPOSAL);
			} else {
				getDefaultModel().setTagProposal(new TagProposal(uri, libraryVersion, defaultPrefix, tagname));
				insertInitialValues();
			}
		}		
	}

	private void initialize2() {
		XModelObject object = PreferenceModelUtilities.getPreferenceModel().getModelBuffer().source();
		String tagname = object.getAttributeValue("name"); //$NON-NLS-1$
		XModelObject parent = object.getParent();
		String uri = (parent == null) ? "" : parent.getAttributeValue(URIConstants.LIBRARY_URI); //$NON-NLS-1$
		String libraryVersion = (parent == null) ? "" : parent.getAttributeValue(URIConstants.LIBRARY_VERSION); //$NON-NLS-1$
		String defaultPrefix = (parent == null) ? "" : parent.getAttributeValue(URIConstants.DEFAULT_PREFIX); //$NON-NLS-1$
		this.getDefaultModel().setTagProposal(new TagProposal(uri, libraryVersion,defaultPrefix,tagname));
		startText = object.getAttributeValue(XModelObjectConstants.START_TEXT);
		endText = object.getAttributeValue(XModelObjectConstants.END_TEXT);
	}
	
	private void insertInitialValues() {
		parseInitialValues(startText);
		AttributeDescriptorValue[] vs = getDefaultModel().getAttributeValueDescriptors();
		for (int i = 0; i < vs.length; i++) {
			String v = initialValues.getProperty(vs[i].getName());
			if(v != null) {
				vs[i].setPreferable(true);
				vs[i].setValue(v);
			}
		}
	}
	
	private void parseInitialValues(String startText) {
		if(startText == null || startText.length() == 0) return;
		int bi = startText.indexOf('<');
		if(bi < 0) return;
		int ei = startText.indexOf('>', bi);
		if(ei < 0) return;
		String header = startText.substring(bi + 1, ei);
		int NOTHING = 0;
		int ATT_NAME = 1;
		int ATT_VALUE = 2;
		char quote = '\0';
		int state = NOTHING;
		boolean whitespace = false;
		String name = null;
		String value = null;
		for (int i = 0; i < header.length(); i++) {
			char c = header.charAt(i);
			if(state == NOTHING) {
				if(Character.isJavaIdentifierStart(c)) {
					name = "" + c; //$NON-NLS-1$
					state = ATT_NAME;
					whitespace = false;
				}
			} else if(state == ATT_NAME) {
				if(Character.isJavaIdentifierPart(c) || c == ':') {
					if(whitespace) {
						whitespace = false;
						name = "";
					}
					name += c;
				} else if(c == '=') {
					state = ATT_VALUE;
					quote = '\0';
					whitespace = false;
				} else if(Character.isWhitespace(c)) {
					whitespace = true;
				}
			} else if(state == ATT_VALUE) {
				if(c == quote) {
					int q = value.indexOf("|");
					if(q >= 0) value = value.substring(0, q) + value.substring(q + 1);
					initialValues.setProperty(name, value);
					name = null;
					value = null;
					state = NOTHING;
					quote = '\0';
				} else if(c == '"' || c == '\'') {
					quote = c;
					value = ""; //$NON-NLS-1$
				} else if(quote != '\0') {
					value += c;
				}
			}
		}
	}	

	protected String generateStartText() {
		if(getDefaultModel().getTagProposal()==IDropWizardModel.UNDEFINED_TAG_PROPOSAL) {
			return startText = properties.getProperty(XModelObjectConstants.START_TEXT);
		}
		return super.generateStartText();
	}

	protected String generateEndText() {
		if(getDefaultModel().getTagProposal()==IDropWizardModel.UNDEFINED_TAG_PROPOSAL) {
			endText = properties.getProperty(XModelObjectConstants.END_TEXT);
			return (endText != null) ? endText : ""; //$NON-NLS-1$
		}
		return super.generateEndText();
	}

	protected String getReformatBodyProperty() {
		return reformat;
	}

	protected String getCustomWizardName() {
		return PaletteInsertManager.getInstance().getWizardName(properties);
	}

	protected IDropWizard createDropWizard() {
		String wizardName = getCustomWizardName();
		
		IDropWizard wizard = null;
		if(wizardName != null) {
			wizard = (IDropWizard)PaletteInsertManager.getInstance().createWizardInstance(properties);
		}
		if(wizard == null) wizard =	new DropWizard();
		wizard.setCommand(this);
		return wizard;
	}
	
	public Properties getProperties() {
		return properties;
	}
	
	private String getDefaultText(XModelObject o) {
		if(o == null) return ""; //$NON-NLS-1$
		if(o.getFileType() != XModelObject.FILE) return o.getPresentationString();
		IWebProject p = WebProjectFactory.instance.getWebProject(o.getModel());
		String path = p.getPathInWebRoot(o);
		return path == null ? o.getPresentationString() : path;
	}
}