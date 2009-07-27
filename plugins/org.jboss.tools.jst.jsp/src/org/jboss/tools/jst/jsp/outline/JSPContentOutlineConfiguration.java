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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.HTMLTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.wst.common.ui.internal.dnd.ObjectTransfer;
import org.eclipse.wst.common.ui.internal.dnd.ViewerDropAdapter;
import org.eclipse.wst.html.core.internal.contentmodel.HTMLAttributeDeclaration;
import org.eclipse.wst.html.core.internal.contentmodel.HTMLCMDataType;
import org.eclipse.wst.html.core.internal.provisional.HTMLCMProperties;
import org.eclipse.wst.html.ui.internal.contentoutline.HTMLNodeActionManager;
import org.eclipse.wst.html.ui.views.contentoutline.HTMLContentOutlineConfiguration;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.xml.core.internal.contentmodel.CMDataType;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQueryAction;
import org.eclipse.wst.xml.ui.internal.XMLUIMessages;
import org.eclipse.wst.xml.ui.internal.contentoutline.XMLNodeActionManager;
import org.jboss.tools.common.model.ui.dnd.ModelTransfer;
import org.jboss.tools.common.model.ui.editors.dnd.context.DropContext;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.contentassist.JspContentAssistProcessor;
import org.jboss.tools.jst.jsp.editor.IJSPTextEditor;
import org.jboss.tools.jst.jsp.editor.IViewerDropAdapterFactory;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageProcessor;
import org.jboss.tools.jst.web.kb.KbQuery.Type;
import org.jboss.tools.jst.web.kb.internal.taglib.CustomTagLibComponent;
import org.jboss.tools.jst.web.kb.taglib.IAttribute;
import org.jboss.tools.jst.web.kb.taglib.IComponent;
import org.jboss.tools.jst.web.kb.taglib.ICustomTagLibComponent;
import org.osgi.framework.Bundle;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class JSPContentOutlineConfiguration extends HTMLContentOutlineConfiguration {
	IJSPTextEditor editor;
	
	static IViewerDropAdapterFactory dropAdapterFactory;
	
	static {
		try {
			Bundle b = Platform.getBundle("org.jboss.tools.vpe"); //$NON-NLS-1$
			if(b!=null) {
				Class cls = b.loadClass("org.jboss.tools.vpe.editor.dnd.context.ViewerDropAdapterFactory"); //$NON-NLS-1$
				dropAdapterFactory = (IViewerDropAdapterFactory)cls.newInstance();
			}
		} catch (IllegalAccessException e) {
			JspEditorPlugin.getPluginLog().logError(e);
		} catch (ClassNotFoundException e) {
			JspEditorPlugin.getPluginLog().logError(e);
		} catch (InstantiationException e) {
			JspEditorPlugin.getPluginLog().logError(e);
		}
	}
	
	public JSPContentOutlineConfiguration(IJSPTextEditor editor) {
		this.editor = editor;
	}

	private TransferDropTargetListener[] fTransferDropTargetListeners;

	public TransferDropTargetListener[] getTransferDropTargetListeners(
			TreeViewer treeViewer) {

		if (fTransferDropTargetListeners == null) {
			final TransferDropTargetListener[] defaultListeners =
				super.getTransferDropTargetListeners(treeViewer);


			if(dropAdapterFactory != null) {
				Transfer[] transfers = new Transfer[]{
						ModelTransfer.getInstance(),
						HTMLTransfer.getInstance(),
						// Commented as fix of JBIDE-4142.
						// The default implementation of the listener should
						// be used for local DnD events
						// LocalSelectionTransfer.getTransfer(),
						ObjectTransfer.getInstance(),
						FileTransfer.getInstance(),
						TextTransfer.getInstance()
				};
				fTransferDropTargetListeners = new TransferDropTargetListener[
						transfers.length + defaultListeners.length];

				for (int i = 0; i < transfers.length; i++) {
					final Transfer transfer = transfers[i];
					final DropContext dropContext = new DropContext();
					final ViewerDropAdapter dropAdapter
							= dropAdapterFactory.createDropAdapter(
									transfer, treeViewer,
									editor, transfer, dropContext);

					fTransferDropTargetListeners[i]
							= new OutlineTransferDropTargetListener(
									transfer, dropAdapter);
				}

				// add default listeners to the end of the array (JBIDE-4142)
				System.arraycopy(defaultListeners, 0,
						fTransferDropTargetListeners,
						transfers.length, defaultListeners.length);
			} else {
				fTransferDropTargetListeners = defaultListeners;
			}
		}

		return fTransferDropTargetListeners;
	}

	public void unconfigure(TreeViewer viewer) {
		super.unconfigure(viewer);
		fTransferDropTargetListeners = null;
	}

	ValueHelper valueHelper;

	protected XMLNodeActionManager createNodeActionManager(TreeViewer treeViewer) {
		return new HTMLNodeActionManager((IStructuredModel) treeViewer.getInput(), treeViewer) {
			protected void contributeAddChildActions(IMenuManager menu, Node node, int ic, int vc) {
				int nodeType = node.getNodeType();
				
				if(valueHelper == null) valueHelper = new ValueHelper();

				if (nodeType == Node.ELEMENT_NODE) {
					// 'Add Child...' and 'Add Attribute...' actions
					//
					Element element = (Element) node;

					IMenuManager addAttributeMenu = new MyMenuManager(XMLUIMessages._UI_MENU_ADD_ATTRIBUTE); 
					IMenuManager addChildMenu = new MyMenuManager(XMLUIMessages._UI_MENU_ADD_CHILD); 
					menu.add(addAttributeMenu);
					menu.add(addChildMenu);

					CMElementDeclaration ed = modelQuery.getCMElementDeclaration(element);
					if (ed != null) {
						// add insert attribute actions
						//
						List modelQueryActionList = new ArrayList();
						modelQuery.getInsertActions(element, ed, -1, ModelQuery.INCLUDE_ATTRIBUTES, vc, modelQueryActionList);
						addActionHelper(addAttributeMenu, modelQueryActionList);

						// add insert child node actions
						//
						modelQueryActionList = new ArrayList();
						modelQuery.getInsertActions(element, ed, -1, ic, vc, modelQueryActionList);
						addActionHelper(addChildMenu, modelQueryActionList);
					} else {
						List modelQueryActionList = new ArrayList();
						
						JspContentAssistProcessor processor = valueHelper.createContentAssistProcessor();
						int offset = 0;
						if(element instanceof IndexedRegion) {
							offset = ((IndexedRegion)element).getStartOffset() + 1;
						}
						IPageContext pageContext = valueHelper.createPageContext(processor, offset);
						KbQuery kbQuery = createKbQuery(processor, element, offset);
						IComponent[] components = PageProcessor.getInstance().getComponents(kbQuery, pageContext, true);
						IComponent d = null;
						for (IComponent c: components) {
							if(c instanceof ICustomTagLibComponent) {
								d = c;
								break;
							}
						}
						if(d == null && components.length > 0) d = components[0];
						
						
						String query = "/" + element.getNodeName(); //$NON-NLS-1$

						if(d != null) {
							IAttribute[] as = d.getAttributes();
							for (IAttribute a: as) {
								String attribute = a.getName();
								if(element.hasAttribute(attribute)) continue;
								HTMLAttrDeclImpl ad = new HTMLAttrDeclImpl(attribute, new HTMLCMDataTypeImpl(attribute), 0);
								modelQueryActionList.add(new Action(Action.INSERT, element, ad));
							}
						}
						addActionHelper(addAttributeMenu, modelQueryActionList);
					}

					// add PI and COMMENT
					contributePIAndCommentActions(addChildMenu, element, ed, -1);

					// add PCDATA, CDATA_SECTION
					contributeTextNodeActions(addChildMenu, element, ed, -1);

					// add NEW ELEMENT
					contributeUnconstrainedAddElementAction(addChildMenu, element, ed, -1);

					// add ATTRIBUTE
					contributeUnconstrainedAttributeActions(addAttributeMenu, element, ed);
				}
			}
		};
	}

	protected KbQuery createKbQuery(JspContentAssistProcessor processor, Node fNode, int offset) {
		KbQuery kbQuery = new KbQuery();

		String[] parentTags = processor.getParentTags(false);
		parentTags = add(parentTags, fNode.getNodeName());
		String prefix = getPrefix(fNode);
		kbQuery.setPrefix(prefix);
		kbQuery.setUri(processor.getUri(prefix));
		kbQuery.setParentTags(parentTags);
		kbQuery.setParent(fNode.getNodeName());
		kbQuery.setMask(false); 
		kbQuery.setType(Type.ATTRIBUTE_NAME);
		kbQuery.setOffset(offset);
		kbQuery.setValue("");  //$NON-NLS-1$
		kbQuery.setStringQuery(""); //$NON-NLS-1$
		
		return kbQuery;
	}
	private String[] add(String[] result, String v) {
		String[] result1 = new String[result.length + 1];
		System.arraycopy(result, 0, result1, 0, result.length);
		result1[result.length] = v;
		return result1;
	}
	private String getPrefix(Node fNode) {
		int i = fNode.getNodeName().indexOf(':');
		return i < 0 ? null : fNode.getNodeName().substring(0, i);
	}


	public static class OutlineTransferDropTargetListener implements
			TransferDropTargetListener {

		private final Transfer transfer;
		private final ViewerDropAdapter dropAdapter;

		public OutlineTransferDropTargetListener(Transfer transfer,
				ViewerDropAdapter dropAdapter) {
			this.transfer = transfer;
			this.dropAdapter = dropAdapter;
		}
		public void dragEnter(DropTargetEvent event) {
			dropAdapter.dragEnter(event);
		}
		public void dragLeave(DropTargetEvent event) {
			dropAdapter.dragLeave(event);
		}
		public void dragOperationChanged(DropTargetEvent event) {
			dropAdapter.dragOperationChanged(event);
		}
		public void dragOver(DropTargetEvent event) {
			dropAdapter.dragOver(event);
		}
		public void drop(DropTargetEvent event) {
			dropAdapter.drop(event);
		}
		public void dropAccept(DropTargetEvent event) {
			dropAdapter.dropAccept(event);
		}
		public Transfer getTransfer() {
			return transfer;
		}
		public boolean isEnabled(DropTargetEvent event) {
			return getTransfer().isSupportedType(event.currentDataType);
		}
	}
}


class Action implements ModelQueryAction {
  public int kind;
  public int startIndex;
  public int endIndex;
  public Node parent;
  public CMNode cmNode;
  public Object userData;

  public Action(int kind, Node parent, CMNode cmNode) {
    this.kind = kind;
    this.parent = parent;
    this.cmNode = cmNode;
  }

  public Action(int kind, Node parent, CMNode cmNode, int startIndex, int endIndex) {
    this.kind = kind;
    this.parent = parent;
    this.cmNode = cmNode;
    this.startIndex = startIndex;
    this.endIndex = endIndex;
  }

  public int getKind() {
    return kind;
  }

  public int getStartIndex() {
    return startIndex;
  }

  public int getEndIndex() {
    return endIndex;
  }

  public Node getParent() {
    return parent;
  }

  public CMNode getCMNode() {
    return cmNode;
  }

  public Object getUserData() {
    return userData;
  }

  public void setUserData(Object object) {
    userData = object;
  }

  public void performAction() {
  }

}

abstract class CMNodeImpl implements CMNode {

	private java.lang.String name = null;

	public CMNodeImpl(String nm) {
		super();
		name = nm;
	}

	public String getNodeName() {
		return name;
	}

	public Object getProperty(String propertyName) {
		if (propertyName.equals(HTMLCMProperties.IS_XHTML))
			return Boolean.FALSE;
		return null;
	}

	public boolean supports(String propertyName) {
		if (propertyName.equals(HTMLCMProperties.IS_XHTML))
			return true;
		return false;
	}
}

class HTMLAttrDeclImpl extends CMNodeImpl implements HTMLAttributeDeclaration {

	private HTMLCMDataTypeImpl type = null;
	private int usage = 0;

	public HTMLAttrDeclImpl(String attrName, HTMLCMDataTypeImpl valueType, int valueUsage) {
		super(attrName);
		this.type = valueType;

		switch (valueUsage) {
			case OPTIONAL :
			case REQUIRED :
			case FIXED :
			case PROHIBITED :
				this.usage = valueUsage;
				break;
			default :
				// should warn...
				this.usage = OPTIONAL; // fall back
				break;
		}
	}

	public String getAttrName() {
		return getNodeName();
	}

	public CMDataType getAttrType() {
		return type;
	}

	public String getDefaultValue() {
		if (type.getImpliedValueKind() != CMDataType.IMPLIED_VALUE_DEFAULT)
			return null;
		return type.getImpliedValue();
	}

	public Enumeration getEnumAttr() {
		Vector v = new Vector(Arrays.asList(type.getEnumeratedValues()));
		return v.elements();
	}

	public int getNodeType() {
		return CMNode.ATTRIBUTE_DECLARATION;
	}

	public int getUsage() {
		return usage;
	}

	public boolean supports(String propertyName) {
		if (propertyName.equals(HTMLCMProperties.SHOULD_IGNORE_CASE))
			return true;
		else if (propertyName.equals(HTMLCMProperties.IS_SCRIPTABLE))
			return true;
		return super.supports(propertyName);
	}

	public Object getProperty(String propertyName) {
		if (propertyName.equals(HTMLCMProperties.SHOULD_IGNORE_CASE))
			return Boolean.TRUE;
		else if (propertyName.equals(HTMLCMProperties.IS_SCRIPTABLE)) {
			return getAttrType().getDataTypeName() == HTMLCMDataType.SCRIPT ? Boolean.TRUE : Boolean.FALSE;
		}
		return super.getProperty(propertyName);
	}
}

class HTMLCMDataTypeImpl extends CMNodeImpl implements HTMLCMDataType {
	private int impliedValueKind = IMPLIED_VALUE_NONE;
	private String impliedValue = null;
	private final static String[] emptyArray = new String[0];
	private String[] enumValues = emptyArray;
	private String instanceValue = null;

	public HTMLCMDataTypeImpl(String typeName) {
		super(typeName);
	}

	public HTMLCMDataTypeImpl(String typeName, String instanceValue) {
		super(typeName);
		this.instanceValue = instanceValue;
	}

	public String generateInstanceValue() {
		return instanceValue;
	}

	public String getDataTypeName() {
		return getNodeName();
	}

	public String[] getEnumeratedValues() {
		return enumValues;
	}

	public String getImpliedValue() {
		return impliedValue;
	}

	public int getImpliedValueKind() {
		return impliedValueKind;
	}

	public int getNodeType() {
		return CMNode.DATA_TYPE;
	}

	void setEnumValues(String[] values) {
		enumValues = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			enumValues[i] = values[i];
		}
	}

	void setImpliedValue(int kind, String value) {
		switch (kind) {
			case IMPLIED_VALUE_FIXED :
			case IMPLIED_VALUE_DEFAULT :
				impliedValueKind = kind;
				impliedValue = value;
				break;
			case IMPLIED_VALUE_NONE :
			default :
				impliedValueKind = IMPLIED_VALUE_NONE;
				impliedValue = null; // maybe a null string?
				break;
		}
	}
}
