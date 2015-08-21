/******************************************************************************* 
 * Copyright (c) 2011-2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.internal.resources.ICoreConstants;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jdt.core.IJarEntryResource;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.internal.ui.text.FastJavaPartitionScanner;
import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jst.jsp.core.internal.contentmodel.TaglibController;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.TLDCMDocumentManager;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.TaglibTracker;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.DocumentProviderRegistry;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.common.componentcore.internal.ComponentResource;
import org.eclipse.wst.common.componentcore.internal.StructureEdit;
import org.eclipse.wst.common.componentcore.internal.WorkbenchComponent;
import org.eclipse.wst.css.core.internal.provisional.adapters.IModelProvideAdapter;
import org.eclipse.wst.css.core.internal.provisional.adapters.IStyleSheetAdapter;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSModel;
import org.eclipse.wst.html.core.internal.htmlcss.LinkElementAdapter;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionList;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.jboss.tools.common.el.core.ELReference;
import org.jboss.tools.common.el.core.GlobalELReferenceList;
import org.jboss.tools.common.el.core.parser.ELParserUtil;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.el.core.resolver.ELContextImpl;
import org.jboss.tools.common.el.core.resolver.ELResolverFactoryManager;
import org.jboss.tools.common.el.core.resolver.ElVarSearcher;
import org.jboss.tools.common.el.core.resolver.SimpleELContext;
import org.jboss.tools.common.el.core.resolver.Var;
import org.jboss.tools.common.resref.core.ResourceReference;
import org.jboss.tools.common.text.ext.util.Utils;
import org.jboss.tools.common.util.EclipseUIUtil;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.common.validation.ValidationELReference;
import org.jboss.tools.jst.web.WebUtils;
import org.jboss.tools.jst.web.kb.include.IncludeContextBuilder;
import org.jboss.tools.jst.web.kb.include.PageInclude;
import org.jboss.tools.jst.web.kb.internal.FaceletPageContextImpl;
import org.jboss.tools.jst.web.kb.internal.JspContextImpl;
import org.jboss.tools.jst.web.kb.internal.RemoteFileManager;
import org.jboss.tools.jst.web.kb.internal.ResourceBundle;
import org.jboss.tools.jst.web.kb.internal.XmlContextImpl;
import org.jboss.tools.jst.web.kb.internal.taglib.NameSpace;
import org.jboss.tools.jst.web.kb.taglib.CustomTagLibManager;
import org.jboss.tools.jst.web.kb.taglib.INameSpace;
import org.jboss.tools.jst.web.kb.taglib.TagLibraryManager;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.css.CSSStyleSheet;

/**
 * 
 * @author Alexey Kazakov
 */
@SuppressWarnings("restriction")
public class PageContextFactory implements IResourceChangeListener {
	private static PageContextFactory fInstance = new PageContextFactory();
	private static final String XHTML_TAG_LIB_URI = "http://www.w3.org/1999/xhtml"; //$NON-NLS-1$
	public static final String XML_PAGE_CONTEXT_TYPE = "XML_PAGE_CONTEXT_TYPE"; //$NON-NLS-1$
	public static final String JSP_PAGE_CONTEXT_TYPE = "JSP_PAGE_CONTEXT_TYPE"; //$NON-NLS-1$
	public static final String FACELETS_PAGE_CONTEXT_TYPE = "FACELETS_PAGE_CONTEXT_TYPE"; //$NON-NLS-1$
	private static final String JAVA_PROPERTIES_CONTENT_TYPE = "org.eclipse.jdt.core.javaProperties"; //$NON-NLS-1$

	public static final String EL_START_1 = "#{"; //$NON-NLS-1$
	public static final String EL_START_2 = "${"; //$NON-NLS-1$

	public static final PageContextFactory getInstance() {
		return fInstance;
	}

	/**
	 * Returns true if the file is XHTML or JSP page.
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isPage(IFile file) {
		IContentType type = IDE.getContentType(file);
		String typeId = (type == null ? null : type.getId());
		typeId = IncludeContextBuilder.getContextType(typeId);
		return JSP_PAGE_CONTEXT_TYPE.equals(typeId) || FACELETS_PAGE_CONTEXT_TYPE.equals(typeId);
	}

	/**
	 * For each file opened in an editor a document listener is created.
	 * If context for that file is already cached, the listener is added to the document.
	 * When editor is closed, listener is removed.
	 * For a file not opened in an editor, no listener is added to the document.
	 */
	Map<IFile, DocListener> listeners = new HashMap<IFile, DocListener>();

	private PageContextFactory() {
		initDocumentListeners();
	}

	private void initDocumentListeners() {
		IWorkbenchWindow[] ws = WebKbPlugin.getDefault().getWorkbench().getWorkbenchWindows();
		for (IWorkbenchWindow window: ws) {
			for (IEditorReference ref: window.getActivePage().getEditorReferences()) {
				IEditorPart editor = ref.getEditor(false);
				if(editor != null) {
					addListenerToPart(editor);
				}
			}
			window.getActivePage().addPartListener(new IPartListener() {

				@Override
				public void partOpened(IWorkbenchPart part) {
					if(part instanceof IEditorPart) {
						addListenerToPart((IEditorPart)part);
					}				
				}

				@Override
				public void partActivated(IWorkbenchPart part) {
				}

				@Override
				public void partBroughtToTop(IWorkbenchPart part) {
				}

				@Override
				public void partClosed(IWorkbenchPart part) {
					if(part instanceof IEditorPart) {
						IEditorPart editor = (IEditorPart)part;
						IEditorInput input = editor.getEditorInput();
						if(input instanceof IFileEditorInput) {
							IFileEditorInput i = (IFileEditorInput)input;
							IFile file = i.getFile();
							if(file != null) {
								DocListener listener = listeners.get(file);
								if(listener != null) {
									listener.remove();
								}
							}
						}
					}
				}

				@Override
				public void partDeactivated(IWorkbenchPart part) {
				}
			});
		}
	}
	
	class DocListener implements IDocumentListener {
		IFile file;
		IDocument document;

		public DocListener(IFile file, IDocument document) {
			this.file = file;
			this.document = document;
			listeners.put(file, this);
			document.addDocumentListener(this);
		}
		@Override
		public void documentChanged(DocumentEvent event) {
			cleanUp(file);
		}						
		@Override
		public void documentAboutToBeChanged(DocumentEvent event) {
		}

		public void remove() {
			if(document == null) return;
			document.removeDocumentListener(this);
			document = null;
			listeners.remove(file);
		}
	}

	private void addListenerToPart(IEditorPart editor) {
		IEditorInput input = editor.getEditorInput();
		if(input instanceof IFileEditorInput) {
			IFileEditorInput i = (IFileEditorInput)input;
			IFile file = i.getFile();
			if(file != null) {
				DocListener listener = listeners.get(file);
				if(listener == null) {
					IDocument doc = getConnectedDocument(input);
					if(doc != null) {
						listener = new DocListener(file, doc);
					}
				}
			}
		}
	}

	private IDocument getConnectedDocument(IEditorInput input) {
		IDocumentProvider provider= DocumentProviderRegistry.getDefault().getDocumentProvider(input);
		IDocument result = null;
		try {
			provider.connect(input);
			result = provider.getDocument(input);
			provider.disconnect(input);
		} catch (CoreException e) {
			WebKbPlugin.getDefault().logError(e);
		}
		return result;
	}

	/*
	 * The cache to store the created contexts
	 * The key is IFile.getFullPath().toString() of the resource of the context 
	 */
	private Map<IFile, SimpleELContext> cache = new HashMap<IFile, SimpleELContext>();

	private SimpleELContext getSavedContext(IFile resource) {
		synchronized (cache) {
			return cache.get(resource);
		}
	}

	private void saveConvext(SimpleELContext context) {
		if (context.getResource() != null) {
			synchronized (cache) {
				cache.put(context.getResource(), context);
			}
		}
	}

	/**
	 * Creates a page context for the specified context document
	 *
	 * @param file
	 * @param contentType
	 * @return
	 */
	public static ELContext createPageContext(IDocument document) {
		return createPageContext(document, null, false);
	}

	public static ELContext createPageContext(IDocument document, boolean dontUseCache) {
		return createPageContext(document, null, dontUseCache);
	}

	/**
	 * Creates a page context for the specified context document
	 *
	 * @param file
	 * @param contentType
	 * @return
	 */
	public static ELContext createPageContext(IDocument document, String contextType) {
		return createPageContext(document, null, contextType, false);
	}

	public static ELContext createPageContext(IDocument document, String contextType, boolean dontUseCache) {
		return createPageContext(document, null, contextType, dontUseCache);
	}

	/**
	 * Creates a page context for the specified context file
	 *
	 * @param file
	 * @param contentType
	 * @return
	 */
	public static ELContext createPageContext(IFile file) {
		return createPageContext(file, null);
	}

	/**
	 * Creates a page context for the specified context file
	 *
	 * @param file
	 * @param contentType
	 * @return
	 */
	public static ELContext createPageContext(IFile file, String contextType) {
		return createPageContext(null, file, null);
	}

	/**
	 * Creates a page context for the specified context type
	 *
	 * @param file
	 * @param contentType
	 * @return
	 */
	public static ELContext createPageContext(IDocument document, IFile file, String contextType) {
		return getInstance().createPageContext(document, file, new ArrayList<String>(), contextType, false);
	}

	public static ELContext createPageContext(IDocument document, IFile file, String contextType, boolean dontUseCache) {
		return getInstance().createPageContext(document, file, new ArrayList<String>(), contextType, dontUseCache);
	}

	/**
	 * Cleans up the context for the file specified
	 * 
	 * @param file
	 */
	public void cleanUp(IFile file) {
		synchronized (cache) {
			ELContext removedContext = cache.remove(file);
			if (removedContext == null || removedContext.getResource() == null)
				return;
			ELContext[] contexts = null;
			// Remove all the contexts that are parent to the removed context
			contexts = cache.values().toArray(new ELContext[cache.values().size()]);
			if (contexts != null) {
				for (ELContext context : contexts) {
					if (isDependencyContext(context, file)) {
						cache.remove(file);
					}
				}
			}
		}
	}

	/**
	 * Cleans up the contexts for the project specified
	 * 
	 * @param file
	 */
	public void cleanUp(IProject project) {
		synchronized (cache) {
			// Remove all the contexts that are parent to the removed context
			IFile[] files = cache.keySet().toArray(new IFile[cache.size()]);
			for (IFile file : files) {
				if (project.equals(file.getProject())) {
					cleanUp(file);
				}
			}
		}
	}

	/**
	 * Cleans up the contexts for the resource change delta
	 * 
	 * @param file
	 */
	public void cleanUp(IResourceDelta delta) {
		synchronized (cache) {
			if(!cache.isEmpty() && checkDelta(delta)) {
				processDelta(delta);
			}
		}
	}

	private SimpleELContext createPropertiesContext(IFile file, IDocument document, boolean useLastSavedStateOfFile) {
		ELContextImpl context = new ELContextImpl();
		context.setResource(file);
		context.setElResolvers(ELResolverFactoryManager.getInstance().getResolvers(file));
		String content = null;
		if(document==null || useLastSavedStateOfFile) {
			content = FileUtil.getContentFromEditorOrFile(file);
		} else {
			content = document.get();
		}
		if(content.indexOf('{')>-1 && content.indexOf(EL_START_1) >-1 || content.indexOf(EL_START_2)>-1 ) {
			ELReference elReference = new ValidationELReference();
			elReference.setResource(file);
			elReference.setLength(content.length());
			elReference.setStartPosition(0);
			context.addELReference(elReference);
		}
		return context;
	}

	private static SimpleELContext createJavaContext(IFile file, IDocument document, boolean useLastSavedStateOfFile) {
		ELContextImpl context = new ELContextImpl();
		context.setResource(file);
		context.setElResolvers(ELResolverFactoryManager.getInstance().getResolvers(file));
		FastJavaPartitionScanner scaner = new FastJavaPartitionScanner();
		if(document==null || useLastSavedStateOfFile) {
			String content = FileUtil.getContentFromEditorOrFile(file);
			document = new Document(content);
		}
		scaner.setRange(document, 0, document.getLength());
		IToken token = scaner.nextToken();
		while(token!=null && token!=Token.EOF) {
			if(IJavaPartitions.JAVA_STRING.equals(token.getData())) {
				int length = scaner.getTokenLength();
				int offset = scaner.getTokenOffset();
				String value = null;
				try {
					//Value will be kept in EL model, so it has to be separated from long char[] in document.
					value = "" + document.get(offset, length);
				} catch (BadLocationException e) {
					WebKbPlugin.getDefault().logError(e);
					return null;
				}
				if(value.indexOf('{')>-1) {
					int startEl = value.indexOf(EL_START_1);
					if(startEl==-1) {
						startEl = value.indexOf(EL_START_2);
					}
					if(startEl>-1) {
						ELReference elReference = new ValidationELReference();
						elReference.setResource(file);
						elReference.setLength(value.length());
						elReference.setStartPosition(offset);

						try {
							elReference.setLineNumber(document.getLineOfOffset(startEl));
						} catch (BadLocationException e) {
							WebKbPlugin.getDefault().logError(e);
						}
						context.addELReference(elReference);
					}
				}
			}
			token = scaner.nextToken();
		}

		return context;
	}

//	long ctm = 0;

	/**
	 * Creates a page context for the specified context type.
	 * Either file or document can be null. File is always null for documents from jar files.
	 *
	 * @param file
	 * @param contentType
	 * @param parents List of parent contexts
	 * @return
	 */
	private ELContext createPageContext(IDocument document, IFile file, List<String> parents, String defaultContextType, boolean dontUseCache) {
		if (file == null) {
			file = getResource(document);
		}

		boolean modified = EclipseUIUtil.isOpenInActiveEditor(file);
		boolean isContextCachingAllowed = !dontUseCache && !modified; 
		SimpleELContext context = isContextCachingAllowed ? getSavedContext(file) : null;
		if (context == null) {
			String typeId = getContentTypeIdentifier(file == null ? document : file);

			if(JavaCore.JAVA_SOURCE_CONTENT_TYPE.equalsIgnoreCase(typeId)) {
				context = createJavaContext(file, document, !dontUseCache);
			} else if(JAVA_PROPERTIES_CONTENT_TYPE.equalsIgnoreCase(typeId)) {
				context = createPropertiesContext(file, document, !dontUseCache);
			} else if(file != null && isXMLWithoutEL(file)) {
				IProject project = file != null ? file.getProject() : getActiveProject();
				context = new SimpleELContext();
				context.setResource(file);
				context.setElResolvers(ELResolverFactoryManager.getInstance().getResolvers(project));
			} else {
				IModelManager manager = StructuredModelManager.getModelManager();
				// manager==null if plug-in org.eclipse.wst.sse.core 
				// is stopping or un-installed, that is Eclipse is shutting down.
				// there is no need to report it, just stop validation.
				if(manager != null) {
					IStructuredModel model = null;
					try {
						model = file != null ? 
								manager.getModelForRead(file) : 
									manager.getExistingModelForRead(document);
						if (model instanceof IDOMModel) {
							IDOMModel domModel = (IDOMModel) model;
							context = defaultContextType == null ? 
									createPageContextInstance(domModel.getContentTypeIdentifier()) :
												createContextInstanceOfType(defaultContextType);
							if (context != null) {
								IDOMDocument domDocument = domModel.getDocument();
								context.setResource(file);
								if (document == null && context instanceof XmlContextImpl) {
									document = model.getStructuredDocument();
								}
								
								IProject project = file != null ? file.getProject() : getActiveProject();
								
								context.setElResolvers(ELResolverFactoryManager.getInstance().getResolvers(project));
								if (document!=null && context instanceof JspContextImpl && !(context instanceof FaceletPageContextImpl)) {
									// Fill JSP namespaces defined in TLDCMDocumentManager 
									fillJSPNameSpaces((JspContextImpl)context, document, dontUseCache);
								}
								
								if(file != null) {
									IKbProject kbProject = KbProjectFactory.getKbProject(project, true);
									if(kbProject != null) {
										kbProject.getIncludeModel().clean(file.getFullPath());
									}
								}
								// The subsequently called functions may use the file and document
								// already stored in context for their needs
								fillContextForChildNodes(model.getStructuredDocument(), domDocument, context, parents, dontUseCache);
							}
						}
					} catch (CoreException e) {
						WebKbPlugin.getDefault().logError(e);
					} catch (IOException e) {
						WebKbPlugin.getDefault().logError(e);
					} finally {
						if (model != null) {
							model.releaseFromRead();
						}
					}
				}
			}

			if (context != null) { // && isContextCachingAllowed) {  <- Save context even for modified files to prevent multiple initialization when invoked from NON-UI thread.
				context.setDirty(modified);
				saveConvext(context);
			}
		}
		return context;
	}

	/**
	 * Method performs on-demand loading for CSS StyleSheet Descriptors for the 
	 * specified context.
	 * 
	 * @param context
	 */
	public static void updateContextWithCSSInfo(IPageContext context) {
		if (!(context instanceof ICSSContainerSupport))
			return;

		IFile file = context.getResource();
		if (file == null)
			return;
		
		IModelManager manager = StructuredModelManager.getModelManager();
		if(manager == null) 
			return;
		
		IStructuredModel model = null;
		try {
			model = manager.getModelForRead(file);
			if (!(model instanceof IDOMModel))
				return;

			fillCSSStyleSheetDescriptorsForChildNodes(((IDOMModel)model).getDocument(), context);
		} catch (CoreException e) {
			WebKbPlugin.getDefault().logError(e);
		} catch (IOException e) {
			WebKbPlugin.getDefault().logError(e);
		} finally {
			if (model != null) {
				model.releaseFromRead();
			}
		}
	}

	private static void fillCSSStyleSheetDescriptorsForChildNodes(IDOMNode parent, IPageContext context) {
		NodeList children = parent.getChildNodes();
		for(int i = 0; children != null && i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof IDOMNode) {
				fillCSSStyleSheetDescriptorForNode((IDOMNode)child, context);
				fillCSSStyleSheetDescriptorsForChildNodes((IDOMNode)child, context);
			}
		}
	}
	
	private static void fillCSSStyleSheetDescriptorForNode(IDOMNode node, IPageContext context) {
		if (!(context instanceof ICSSContainerSupport) || !(node instanceof IDOMElement))
			return;
		
		String prefix = node.getPrefix() == null ? "" : node.getPrefix(); //$NON-NLS-1$
		String tagName = node.getLocalName();
		Map<String, List<INameSpace>> nsMap = context.getNameSpaces(node.getStartOffset());
		String[] uris = getUrisByPrefix(nsMap, prefix);

		for (String uri : uris) {
			if(IncludeContextBuilder.isCSSStyleSheetContainer(uri, tagName)) {
				fillCSSStyleSheetFromElement(((IDOMElement)node), (ICSSContainerSupport)context, false);
			} else if(IncludeContextBuilder.isJSF2CSSStyleSheetContainer(uri, tagName)) {
				fillCSSStyleSheetFromElement(((IDOMElement)node), (ICSSContainerSupport)context, true);
			} else {
				String[] cssAttributes = IncludeContextBuilder.getCSSStyleSheetAttributes(uri, tagName);
				for (String attr : cssAttributes) {
					fillCSSStyleSheetFromAttribute(((IDOMElement)node), attr, (ICSSContainerSupport)context, false);
				}
				cssAttributes = IncludeContextBuilder.getJSF2CSSStyleSheetAttributes(uri, tagName);
				for (String attr : cssAttributes) {
					fillCSSStyleSheetFromAttribute(((IDOMElement)node), attr, (ICSSContainerSupport)context, true);
				}
			}
		}
	}

	boolean isXMLWithoutEL(IFile file) {
		if(!file.getName().endsWith(".xml")) { //$NON-NLS-1$
			return false;
		}
		String content = FileUtil.getContentFromEditorOrFile(file);
		return content != null && content.indexOf(EL_START_1) < 0 && content.indexOf(EL_START_2) < 0;
	}

	private static IProject getActiveProject() {
		ITextEditor editor = EclipseUIUtil.getActiveEditor();
		if (editor == null) return null;
			
		IEditorInput editorInput = editor.getEditorInput();
		if (editorInput instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput)editorInput).getFile();
			return file == null ? null : file.getProject();
		} else if (editorInput instanceof IStorageEditorInput) {
			IStorage storage;
			try {
				storage = ((IStorageEditorInput)editorInput).getStorage();
			} catch (CoreException e) {
				return null;
			}
			if (storage instanceof IJarEntryResource) {
				Object parent = ((IJarEntryResource)storage).getParent();
				while (parent instanceof IJarEntryResource && !(parent instanceof IProject)) {
					parent = ((IJarEntryResource)parent).getParent();
				}
				if (parent instanceof JarPackageFragmentRoot) {
					return ((JarPackageFragmentRoot)parent).getJavaProject().getProject();
				}
				return null;
			}
		}
		return null;
	}
	
	protected String getContentTypeIdentifier(Object source) {
		if (source instanceof IFile) {
			IContentType type = IDE.getContentType((IFile)source);
			if (type != null) return type.getId();
		} else if (source instanceof IDocument) {
			IStructuredModel sModel = StructuredModelManager.getModelManager().getExistingModelForRead((IDocument)source);
			try {
				if (sModel != null) return sModel.getContentTypeIdentifier();
			} finally {
				if (sModel != null) sModel.releaseFromRead();
			}
		}
		return null;
	}
	
	/**
	 * Returns IFile resource of the document
	 * 
	 * @return
	 */
	public static IFile getResource(IDocument document) {
		if (document == null) return null;
		IStructuredModel sModel = StructuredModelManager.getModelManager().getExistingModelForRead(document);
		try {
			if (sModel != null) {
				String baseLocation = sModel.getBaseLocation();
				IPath location = new Path(baseLocation).makeAbsolute();
				IFile result = FileBuffers.getWorkspaceFileAtLocation(location);
				if(result != null) {
					//JBIDE-13367 wtp may return wrong location.
					return result;
				}
			}
		}
		finally {
			if (sModel != null) {
				sModel.releaseFromRead();
			}
		}

		ITextFileBuffer buffer = FileBuffers.getTextFileBufferManager().getTextFileBuffer(document);
		if (buffer != null && buffer.getLocation() != null) {
			IPath path = buffer.getLocation();
			if (path.segmentCount() > 1) {
				IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
				if (file.isAccessible()) {
					return file;
				}
			}
		}

		return null;
	}
	
	private static SimpleELContext createPageContextInstance(String contentType) {
		String contextType = IncludeContextBuilder.getContextType(contentType);
		if (contextType == null && contentType != null) {
			IContentType baseContentType = Platform.getContentTypeManager().getContentType(contentType);
			baseContentType = baseContentType == null ? null : baseContentType.getBaseType();
			
			while (contextType == null && baseContentType != null) {
				contextType = IncludeContextBuilder.getContextType(baseContentType.getId());
				baseContentType = baseContentType.getBaseType();
			}
		}

		return createContextInstanceOfType(contextType);
	}

	private static SimpleELContext createContextInstanceOfType(String contextType) {
		if (JSP_PAGE_CONTEXT_TYPE.equals(contextType)) {
			return new JspContextImpl();
		} else if (FACELETS_PAGE_CONTEXT_TYPE.equals(contextType)) {
			return new FaceletPageContextImpl();
		}
		return new XmlContextImpl();
	}

	/**
	 * Sets up the context with namespaces and according libraries from the TagLibraryManager
	 * 
	 * @param node
	 * @param context
	 */
	@SuppressWarnings("rawtypes")
	private static void fillJSPNameSpaces(JspContextImpl context, IDocument document, boolean dontUseCache) {
		IProject project = context.getResource() != null ? context.getResource().getProject() : getActiveProject();
		if (project == null)
			return;
		
		TLDCMDocumentManager manager = TaglibController.getTLDCMDocumentManager(document);
		List trackers = (manager == null? null : manager.getCMDocumentTrackers(document.getLength() - 1));
		for (int i = 0; trackers != null && i < trackers.size(); i++) {
			TaglibTracker tt = (TaglibTracker)trackers.get(i);
			final String prefix = tt.getPrefix() == null ? null : tt.getPrefix().trim();
			final String uri = tt.getURI() == null ? null : tt.getURI().trim();
			if (prefix != null && prefix.length() > 0 &&
					uri != null && uri.length() > 0) {
				
				
				IRegion region = new Region(0, document.getLength());
				INameSpace nameSpace = new NameSpace(
						uri, prefix, null,
						TagLibraryManager.getLibraries(
								project, uri));
				context.addNameSpace(region, nameSpace);

				if(!dontUseCache) {
					IKbProject kbProject = KbProjectFactory.getKbProject(project, true);
					if(kbProject != null) {
						kbProject.getNameSpaceStorage().add(prefix, uri);
					}
				}
			}
		}
	}

	private void fillContextForChildNodes(IDocument document, IDOMNode parent, ELContext context, List<String> parents, boolean dontUseCache) {
		NodeList children = parent.getChildNodes();
		for(int i = 0; children != null && i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof IDOMNode) {
				fillContextForNode(document, (IDOMNode)child, context, parents, dontUseCache);
				fillContextForChildNodes(document, (IDOMNode)child, context, parents, dontUseCache);
			}
		}
	}

	private void fillContextForNode(IDocument document, IDOMNode node, ELContext context, List<String> parents, boolean dontUseCache) {
		if (context instanceof XmlContextImpl) {
			XmlContextImpl xmlContext = (XmlContextImpl)context;
			fillElReferencesForNode(document, node, xmlContext);
			if (node instanceof IDOMElement) {
				fillXMLNamespacesForNode((IDOMElement)node, xmlContext, dontUseCache);
			}
		}

		if ((context instanceof JspContextImpl || 
				context instanceof FaceletPageContextImpl) &&
				node instanceof IDOMElement) {
			fillVarsForNode((IDOMElement)node, (ELContextImpl)context);
		}

		if (context instanceof FaceletPageContextImpl) {
			if(node instanceof IDOMElement && context.getResource() != null && context.getResource().exists()) {
				fillUIParamsForNode((IDOMElement)node, (ELContextImpl)context);
			}
			// Insert here the initialization code for FaceletPage context elements which may exist in Text nodes
		}

		if (context instanceof JspContextImpl && node instanceof IDOMElement) {
			fillResourceBundlesForNode((IDOMElement)node,  (JspContextImpl)context);
		}

		// There could be some context type to be initialized somehow that is different from JSP or FaceletPage context  
		// Insert its on-node initialization code here

		// The only Elements may have include/CSS Stylesheet links and other additional info
		if (context instanceof IPageContext && node instanceof IDOMElement) {
			fillAdditionalInfoForNode((IDOMElement)node, (IPageContext)context, parents);
		}
	}

	private static void fillVarsForNode (IDOMElement node, ELContextImpl context) {
		Var var = ElVarSearcher.findVar(node, ELParserUtil.getJbossFactory());
		if (var != null) {
			int start = ((IndexedRegion) node).getStartOffset();
			int length = ((IndexedRegion) node).getLength();

			start = node.getStartOffset();
			length = node.getEndOffset() - start;

			var.setFile(context.getResource());
			context.addVar(new Region(start, length), var);
		}
	}

	static String TAG_COMPOSITION = "composition"; //$NON-NLS-1$
	static String TAG_DECORATE = "decorate"; //$NON-NLS-1$
	static String ATTR_TEMPLATE = "template"; //$NON-NLS-1$
	static String ATTR_SRC = "src"; //$NON-NLS-1$
	static String NODE_PARAM = "param"; //$NON-NLS-1$
	static String ATTR_NAME = "name"; //$NON-NLS-1$
	static String ATTR_VALUE = "value"; //$NON-NLS-1$
	static Map<String, String> PATH_ATTRIBUTES = new HashMap<String, String>();
	static {
		PATH_ATTRIBUTES.put(TAG_COMPOSITION, ATTR_TEMPLATE);
		PATH_ATTRIBUTES.put(TAG_DECORATE, ATTR_TEMPLATE);
		PATH_ATTRIBUTES.put(IncludeContextBuilder.TAG_INCLUDE, ATTR_SRC);
	}

	private static void fillUIParamsForNode(IDOMElement node, ELContextImpl context) {
		String namespaceURI = node.getNamespaceURI();
		if(!CustomTagLibManager.FACELETS_UI_TAG_LIB_URI.equals(namespaceURI)
			&& !CustomTagLibManager.FACELETS_22_UI_TAG_LIB_URI.equals(namespaceURI)) {
			return;
		}
		String pathAttr = PATH_ATTRIBUTES.get(node.getLocalName());
		if(pathAttr == null) {
			return;
		}
		String src = node.getAttribute(pathAttr);
		if(src == null || src.trim().length() == 0) {
			return;
		}
		IFile includedFile = getFile(src, context.getResource());
		if(includedFile == null) return;
		NodeList list = node.getElementsByTagNameNS (namespaceURI, NODE_PARAM);
		List<Var> vars = null;
		for (int i = 0; i < list.getLength(); i++) {
			Node n = list.item(i);
			if(n instanceof IDOMElement) {
				IDOMElement element = (IDOMElement)n;
				synchronized (element) {
					if(element.hasAttribute(ATTR_NAME)) {
						String var = element.getAttribute(ATTR_NAME);
						int declOffset = 0;
						int length = 0;
						Node varAttr = element.getAttributeNode(ATTR_NAME); 
						if (varAttr instanceof IDOMAttr) {
							int varStart = ((IDOMAttr)varAttr).getValueRegionStartOffset() + 1;
							declOffset = varStart;
							length = var.length();
						}
						var = var.trim();
						if(!"".equals(var)) { //$NON-NLS-1$					
							if(element.hasAttribute(ATTR_VALUE)) {
								String value = element.getAttribute(ATTR_VALUE);
								value = value.trim();
								Var newVar = new Var(ELParserUtil.getJbossFactory(), var, value, declOffset, length);
								if(newVar.getElToken() == null) {
									//Value can be string, so if it is not a valid EL, treat it as a string.
									newVar = new Var(ELParserUtil.getJbossFactory(), var, "#{\"" + value + "\".toString()}", declOffset, length);
								}
								if(newVar.getElToken()!=null) {
									newVar.setFile(context.getResource());
									if(vars == null) {
										vars = new ArrayList<Var>();
									}
									vars.add(newVar);
								}
							}
						}
					}
				}
			}
		}
		if(vars != null && !vars.isEmpty()) {
			IKbProject kbProject = KbProjectFactory.getKbProject(context.getResource().getProject(), true);
			if(kbProject != null) {
				PageInclude include = new PageInclude(context.getResource().getFullPath(), includedFile.getFullPath(), vars);
				kbProject.getIncludeModel().addInclude(context.getResource().getFullPath(), include);
			}
		}
	}

	public static IFile getFile(String fileName, IFile includeFile) {
		IFile file = null;
		if (fileName.startsWith("/")) { //$NON-NLS-1$
			IContainer[] webRootFolders = WebUtils.getWebRootFolders(includeFile.getProject());
			if (webRootFolders.length > 0) {
				for (IContainer webRootFolder : webRootFolders) {
					IFile handle = webRootFolder.getFile(new Path(fileName));
					if (handle.exists()) {
						file = handle;
						break;
					}
				}
			} else {
				file = resolveRelatedPath(includeFile, fileName); // ?
			}
		} else {
			file = resolveRelatedPath(includeFile, fileName);
		}
		return file;
	}	
	private static IFile resolveRelatedPath(IFile baseFile,
			String relatedFilePath) {
		IPath currentFolder = baseFile.getParent().getFullPath();
		IPath path = currentFolder.append(relatedFilePath);
		return ResourcesPlugin.getWorkspace().getRoot().getFile(path);
	}

	private static void fillElReferencesForNode(IDocument document, IDOMNode node, XmlContextImpl context) {
		if (context.getResource() == null)	// JBIDE-13060: do not deal with EL References if no resource available
			return;
		IStructuredDocumentRegion regionNode = node.getFirstStructuredDocumentRegion(); 
		if (regionNode == null) return;
		
		if(Node.ELEMENT_NODE == node.getNodeType()) {
			ITextRegionList regions = regionNode.getRegions();
			if (regions == null) return;
			
			for (ITextRegion region : regions.toArray()) {
				if (DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE  == region.getType() || DOMRegionContext.XML_CONTENT == region.getType()) {
					fillElReferencesForRegionNode(document, node, regionNode, region, context);
				}
			}
		} else if (Node.TEXT_NODE == node.getNodeType()) {
			IStructuredDocumentRegion lastRegionNode = node.getLastStructuredDocumentRegion();
			while (regionNode != null) {
				ITextRegionList regions = regionNode.getRegions();
				if (regions == null) return;
				
				for (ITextRegion region : regions.toArray()) {
					if (DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE  == region.getType() || DOMRegionContext.XML_CONTENT == region.getType()) {
						fillElReferencesForRegionNode(document, node, regionNode, region, context);
					}
				}
				if (regionNode == lastRegionNode) break;
				regionNode = regionNode.getNext();
			}
		}
	}

	private static void fillElReferencesForRegionNode(IDocument document, IDOMNode node, IStructuredDocumentRegion regionNode, ITextRegion region, XmlContextImpl context) {
		if (context.getResource() == null)	// JBIDE-13060: do not deal with EL References if no resource available
			return;
		String text = regionNode.getFullText(region);
		if(text.indexOf('{')>-1 && (text.indexOf(EL_START_1) > -1 || text.indexOf(EL_START_2) > -1)) {
			int offset = regionNode.getStartOffset() + region.getStart();

			ELReference elReference = new ValidationELReference();
			elReference.setResource(context.getResource());
			elReference.setLength(text.length());
			elReference.setStartPosition(offset);
			try {
				if(Node.TEXT_NODE == node.getNodeType()) {
					if(elReference.getEl().length == 1) {
						elReference.setLineNumber(document.getLineOfOffset(elReference.getStartPossitionOfFirstEL()) + 1);
					}
				} else {
					elReference.setLineNumber(document.getLineOfOffset(offset) + 1);
				}
			} catch (BadLocationException e) {
				WebKbPlugin.getDefault().logError(e);
			}
			context.addELReference(elReference);
		}
	}

	private void fillAdditionalInfoForNode(IDOMElement node, IPageContext context, List<String> parents) {
		if (context.getResource() == null)	// JBIDE-13060: do not deal with includes and/or CSS if no resource available
			return;

		String prefix = node.getPrefix() == null ? "" : node.getPrefix(); //$NON-NLS-1$
		String tagName = node.getLocalName();
		Map<String, List<INameSpace>> nsMap = context.getNameSpaces(node.getStartOffset());
		String[] uris = getUrisByPrefix(nsMap, prefix);

		for (String uri : uris) {
			if (context instanceof IIncludedContextSupport) {
				String[] includeAttributes = IncludeContextBuilder.getIncludeAttributes(uri, tagName);
				if (includeAttributes.length > 0) {
					List<String> newParentList = parents == null ? new ArrayList<String>() : new ArrayList<String>(parents);
					newParentList.add(context.getResource().getFullPath().toString());
					for (String attr : includeAttributes) {
						String fileName = node.getAttribute(attr);
						if (fileName != null && !fileName.trim().isEmpty()) {
							IFile file = getFileFromProject(fileName, context.getResource());
							if (file != null && checkCycling(parents, file)) { // checkCycling is to fix for JBIDE-5083
								ELContext includedContext = createPageContext(null, file, newParentList, null, false);
								if (includedContext != null)
									((IIncludedContextSupport)context).addIncludedContext(includedContext);
							}
						}
					}
				}
			}
		}
	}

	private static boolean checkCycling(List<String> parents, IFile resource) {
		String resourceId = resource.getFullPath().toString();
		for (String parentId : parents) {
			if (resourceId.equals(parentId))
				return false;
		}
		return true;
	}

	/**
	 * Sets up the context with namespaces and according libraries for the node
	 * For the Facelet Context the methods adds an additional special namespace for
	 * CustomTagLibManager.FACELETS_UI_TAG_LIB_URI when CustomTagLibManager.FACELETS_UI_TAG_LIB_URI 
	 * is found in xmlns attributes
	 * 
	 * @param node
	 * @param context
	 */
	private static void fillXMLNamespacesForNode(Element node, XmlContextImpl context, boolean dontUseCache) {
		IProject project = context.getResource() != null ? context.getResource().getProject() : getActiveProject();
		if (project == null)
			return;

		NamedNodeMap attrs = node.getAttributes();
		boolean mainNnIsRedefined = false;
		for (int j = 0; attrs != null && j < attrs.getLength(); j++) {
			Attr a = (Attr) attrs.item(j);
			String name = a.getName();

			if (!name.startsWith("xmlns:") && !name.equals("xmlns")) //$NON-NLS-1$ //$NON-NLS-2$
				continue;

			String prefix = name.startsWith("xmlns:") ? name.substring("xmlns:".length()) : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			String uri = a.getValue();

			prefix = prefix == null ? null : prefix.trim();
			uri = uri == null ? null : uri.trim();
			if (XHTML_TAG_LIB_URI.equalsIgnoreCase(uri))
				continue;

			if (prefix != null // prefix may be empty
					&& uri != null && uri.length() > 0) {

				int start = ((IndexedRegion) node).getStartOffset();
				int length = ((IndexedRegion) node).getLength();

				IDOMElement domElement = (node instanceof IDOMElement ? (IDOMElement) node
						: null);
				if (domElement != null) {
					start = domElement.getStartOffset();
					length = (domElement.hasEndTag() ? 
							domElement.getEndStructuredDocumentRegion().getEnd() : 
								((IDOMNode) node.getOwnerDocument()).getEndOffset() - start);
				}

				Region region = new Region(start, length);
				INameSpace nameSpace = new NameSpace(
						uri, prefix, node.getNodeName(),
						TagLibraryManager.getLibraries(
								project, uri));

				context.addNameSpace(region, nameSpace);

				if(!dontUseCache) {
					IKbProject kbProject = KbProjectFactory.getKbProject(project, true);
					if(kbProject != null) {
						kbProject.getNameSpaceStorage().add(prefix, uri);
					}
				}

				if (prefix.length() == 0)
					mainNnIsRedefined = true;

				if (context instanceof FaceletPageContextImpl && 
						(CustomTagLibManager.FACELETS_UI_TAG_LIB_URI.equals(uri) 
							|| CustomTagLibManager.FACELETS_22_UI_TAG_LIB_URI.equals(uri))
						&&
						!mainNnIsRedefined) {
					nameSpace = new NameSpace(
							CustomTagLibManager.FACELETS_HTML_TAG_LIB_URI, "", node.getNodeName(), //$NON-NLS-1$
							TagLibraryManager.getLibraries(
									project, 
									CustomTagLibManager.FACELETS_HTML_TAG_LIB_URI));
					context.addNameSpace(region, nameSpace);
				}
			}
		}
	}

	private static void fillResourceBundlesForNode(IDOMElement node, JspContextImpl context) {
		String name = node.getNodeName();
		if (name == null || !name.endsWith("loadBundle") || name.indexOf(':') == -1)  //$NON-NLS-1$
				return;
		String prefix = name.substring(0, name.indexOf(':'));

		Map<String, List<INameSpace>> ns = context.getNameSpaces(node.getStartOffset());
		if (!containsPrefix(ns, prefix)) return;

		NamedNodeMap attributes = node.getAttributes();
		String basename = (attributes.getNamedItem("basename") == null ? null : attributes.getNamedItem("basename").getNodeValue()); //$NON-NLS-1$ //$NON-NLS-2$
		String var = (attributes.getNamedItem("var") == null ? null : attributes.getNamedItem("var").getNodeValue()); //$NON-NLS-1$ //$NON-NLS-2$
		if (basename == null || basename.length() == 0 || var == null || var.length() == 0) 
			return;

		context.addResourceBundle(new ResourceBundle(basename, var));
	}

	private static void fillCSSStyleSheetFromAttribute(IDOMElement node,
			String attribute, ICSSContainerSupport context, boolean jsf2Source) {
		context.addCSSStyleSheetDescriptor(new CSSStyleSheetDescriptorForAttribute(node, attribute, jsf2Source));
	}

	private static void fillCSSStyleSheetFromElement(IDOMElement node,
			ICSSContainerSupport context, boolean jsf2Source) {
		context.addCSSStyleSheetDescriptor(new CSSStyleSheetDescriptor(context.getResource().getFullPath().toString(), node, jsf2Source));
	}

	private static final String JSF2_RESOURCES_FOLDER = "/resources"; //$NON-NLS-1$

	public static class CSSStyleSheetDescriptor {
		protected Node stylesheetContainer;
		protected String source;
		protected boolean jsf2Source;
		protected boolean initialized = false;

		CSSStyleSheetDescriptor (String source, Node styleheetContainer, boolean jsf2Source) {
			this.source = source;
			this.stylesheetContainer = styleheetContainer;
			this.jsf2Source = jsf2Source;
		}

		public String getFilePath() {
			if (!initialized) {
				getStylesheet();
			}
			
			if (!jsf2Source)
				return source;
			
			String jsf2Library = null;
			if (jsf2Source && stylesheetContainer instanceof Element) {
				Attr libraryAttr = ((Element)stylesheetContainer).getAttributeNode("library"); //$NON-NLS-1$
				if (libraryAttr != null && libraryAttr.getNodeValue() != null) {
					jsf2Library = libraryAttr.getNodeValue().trim();
					jsf2Library = jsf2Library.length() == 0 ? null : jsf2Library;
				}
			}		
			
			if (jsf2Library != null) {
				String library = jsf2Library.trim();
				if (library.length() != 0) {
					return JSF2_RESOURCES_FOLDER + '/' + library + '/' + source;
				}
			}
			return JSF2_RESOURCES_FOLDER + '/' + source;
		}
		
		public Node getContainerNode() {
			return stylesheetContainer;
		}
		
		public String getSource() {
			if (!initialized) {
				getStylesheet();
			}
			return source;
		}

		/**
		 * 
		 * @param stylesContainer
		 * @return
		 */
		public CSSStyleSheet getStylesheet() {
			if (stylesheetContainer == null)
				return null;
			
			INodeNotifier notifier = (INodeNotifier) stylesheetContainer;
			CSSStyleSheet sheet = null;

			synchronized (notifier) {
				IStyleSheetAdapter adapter = (IStyleSheetAdapter) notifier.getAdapterFor(IStyleSheetAdapter.class);

				if (adapter != null) {
					sheet = (CSSStyleSheet) adapter.getSheet();
				}
			}

			initialized = true;
			return sheet;
		}
	}

	public static class CSSStyleSheetDescriptorForAttribute extends CSSStyleSheetDescriptor {
		public String attribute;
		
		public CSSStyleSheetDescriptorForAttribute(Node styleheetContainer, String attribute, boolean jsf2Source) {
			super(null, styleheetContainer, jsf2Source);
			this.attribute = attribute;
		}
		
		/**
		 * 
		 * @param stylesContainer
		 * @return
		 */
		public CSSStyleSheet getStylesheet() {
			if (stylesheetContainer == null)
				return null;
			
			INodeNotifier notifier = (INodeNotifier) stylesheetContainer;
			CSSStyleSheet sheet = null;

			synchronized (notifier) {
				IStyleSheetAdapter originalAdapter = (IStyleSheetAdapter) notifier.getAdapterFor(IStyleSheetAdapter.class);
				if (originalAdapter != null)
					notifier.removeAdapter(originalAdapter);
				
				IStyleSheetAdapter tempAdapter = new ExtendedLinkElementAdapter(
						(Element) stylesheetContainer, attribute, jsf2Source);
				
				// These getSheet()/getSource() calls are here just to ensure that the CSS Stylesheet exists
				// But we can't use the sheet returned by getSheet() call because it's improperly initialized
				// unless the adapter is registered on the node 
				sheet = (CSSStyleSheet) tempAdapter.getSheet();
				this.source = ((ExtendedLinkElementAdapter)tempAdapter).getSource();
				if (sheet != null && source != null) {
					// So, do register adapter
					notifier.addAdapter(tempAdapter);
				}

				// Re-get the values
				sheet = (CSSStyleSheet) tempAdapter.getSheet();
				this.source = ((ExtendedLinkElementAdapter)tempAdapter).getSource();

				// And restore the original adapter (if so)
				notifier.removeAdapter(tempAdapter);
				if (originalAdapter != null)
					notifier.addAdapter(originalAdapter);
			}

			initialized = true;
			return (CSSStyleSheet) sheet;
		}
	}
	
	private static boolean containsPrefix(Map<String, List<INameSpace>> ns, String prefix) {
		for (List<INameSpace> n: ns.values()) {
			for (INameSpace nameSpace : n) {
				if(prefix.equals(nameSpace.getPrefix())) return true;
			}
		}
		return false;
	}

	/**
	 * Searches the namespace map and returns all the URIs for the specified prefix
	 *  
	 * @param nsMap
	 * @param prefix
	 * @return
	 */
	public static String[] getUrisByPrefix(Map<String, List<INameSpace>> nsMap, String prefix) {
		if(nsMap.isEmpty()) {
			return new String[0];
		}
		Set<String> uris = new HashSet<String>();
		for (List<INameSpace> nsList : nsMap.values()) {
			for (INameSpace ns : nsList) {
				if (prefix.equals(ns.getPrefix())) {
					uris.add(ns.getURI());
				}
			}
		}

		return uris.isEmpty() ? new String[] {prefix} : (String[])uris.toArray(new String[uris.size()]);
	}

	/**
	 * Searches the file with the name specified
	 * 
	 * @param fileName
	 * @param documentFile
	 * @return
	 */
	public static IFile getFileFromProject(String fileName, IFile documentFile) {
		if(documentFile == null || !documentFile.isAccessible()) return null;

		fileName = findAndReplaceElVariable(fileName);

		IProject project = documentFile.getProject();
		String name = Utils.trimFilePath(fileName);
		IPath currentPath = documentFile.getLocation()
				.removeLastSegments(1);
		IResource member = null;
		StructureEdit se = StructureEdit.getStructureEditForRead(project);
		if (se == null) {
			return null;
		}
		WorkbenchComponent[] modules = se.getWorkbenchModules();
		for (int i = 0; i < modules.length; i++) {
			if (name.startsWith("/")) { //$NON-NLS-1$
				member = findFileByAbsolutePath(project, modules[i], name);
			} else {
				member = findFileByRelativePath(project, modules[i],
						currentPath, name);
				if (member == null && name.length() > 0) {
					// in some cases path having no leading "/" is
					// nevertheless absolute
					member = findFileByAbsolutePath(project, modules[i],
							"/" + name); //$NON-NLS-1$
				}
			}
			if (member != null && (member instanceof IFile)) {
				if (((IFile) member).exists())
					return (IFile) member;
			}
		}
		return null;
	}

	private static IFile findFileByRelativePath(IProject project,
			WorkbenchComponent module, IPath basePath, String path) {

		if (path == null || path.trim().length() == 0)
			return null;

		path = findAndReplaceElVariable(path);

		ComponentResource[] resources = module.findResourcesBySourcePath(
				new Path("/"), 0); //$NON-NLS-1$
		IPath projectPath = project.getLocation();
		IFile member = null;

		for (int i = 0; i < resources.length; i++) {
			IPath runtimePath = resources[i].getRuntimePath();
			IPath sourcePath = resources[i].getSourcePath();

			// Look in source environment
			IPath webRootPath = projectPath.append(sourcePath);
			IPath relativePath = Utils.getRelativePath(webRootPath,
					basePath);
			IPath filePath = relativePath.append(path);
			member = project.getFile(sourcePath.append(filePath));
			if (member.exists()) {
				return member;
			}

			// Look in runtime environment
			if (runtimePath.segmentCount() >= ICoreConstants.MINIMUM_FOLDER_SEGMENT_LENGTH - 1) {
				webRootPath = projectPath.append(runtimePath);
				relativePath = Utils.getRelativePath(webRootPath, basePath);
				filePath = relativePath.append(path);
				member = project.getFile(runtimePath.append(filePath));
				if (member.exists()) {
					return member;
				}
			}
		}
		return null;
	}

	private static IFile findFileByAbsolutePath(IProject project,
		WorkbenchComponent module, String path) {
		ComponentResource[] resources = module.findResourcesBySourcePath(
				new Path("/"), 0); //$NON-NLS-1$

		path = findAndReplaceElVariable(path);

		IFile member = null;

		for (int i = 0; resources != null && i < resources.length; i++) {
			IPath runtimePath = resources[i].getRuntimePath();
			IPath sourcePath = resources[i].getSourcePath();

			// Look in source environment
			member = project.getFile(sourcePath.append(path));
			if(member.exists()) {
					return member;
			} 

			// Look in runtime environment
			if (runtimePath.segmentCount() >= ICoreConstants.MINIMUM_FOLDER_SEGMENT_LENGTH - 1) {
				member = project.getFile(runtimePath.append(path));
					if (member.exists()) {
						return member;
				}
			}
		}
		return null;
	}

	private static final String DOLLAR_PREFIX = "${"; //$NON-NLS-1$

    private static final String SUFFIX = "}"; //$NON-NLS-1$

    private static final String SHARP_PREFIX = "#{"; //$NON-NLS-1$

	public static final String CONTEXT_PATH_EXPRESSION = "^\\s*(\\#|\\$)\\{facesContext.externalContext.requestContextPath\\}"; //$NON-NLS-1$

	// partly copied from org.jboss.tools.vpe.editor.util.ElService
	private static String findAndReplaceElVariable(String fileName){
		if (fileName != null)
			fileName = fileName.replaceFirst(CONTEXT_PATH_EXPRESSION, ""); //$NON-NLS-1$

		final IPath workspacePath = Platform.getLocation();

        final ResourceReference[] gResources = GlobalELReferenceList.getInstance().getAllResources(workspacePath);
		String result = fileName;

		ResourceReference[] sortedReferences = sortReferencesByScope(gResources);

		for (ResourceReference rf : sortedReferences) {
			final String dollarEl = DOLLAR_PREFIX + rf.getLocation() + SUFFIX;
			final String sharpEl = SHARP_PREFIX + rf.getLocation() + SUFFIX;

			if (fileName.contains(dollarEl)) {
				result = result.replace(dollarEl, rf.getProperties());
			}
			if (fileName.contains(sharpEl)) {
				result = result.replace(sharpEl, rf.getProperties());
			}
		}
		return result;
	}

	// copied from org.jboss.tools.vpe.editor.util.ElService
	private static ResourceReference[] sortReferencesByScope(ResourceReference[] references) {
		ResourceReference[] sortedReferences = references.clone();

		Arrays.sort(sortedReferences, new Comparator<ResourceReference>() {
			public int compare(ResourceReference r1, ResourceReference r2) {
				return r1.getScope() - r2.getScope();
			}
		});

		return sortedReferences;
	}
	
	public static class ExtendedLinkElementAdapter extends LinkElementAdapter {

		private Element element;
		private String hrefAttrName;
		private String source = null;
		private boolean jsf2Source;
		private String prefix = null;

		public ExtendedLinkElementAdapter(Element element, String hrefAttrName, boolean jsf2Source) {
			this.element = element;
			this.hrefAttrName = hrefAttrName;
			this.jsf2Source = jsf2Source;
		}

		@Override
		public Element getElement() {
			return element;
		}

		public String getSource() {
			return source;
		}

		@Override
		protected boolean isValidAttribute() {
			boolean result = true;
			if (!super.isValidAttribute()) {
				String href = getElement().getAttribute(hrefAttrName);
				result = href != null && !href.isEmpty();
			}
			return result;
		}

		/**
		 */
		public ICSSModel getModel() {
			// Fix for JBIDE-5079 >>>
			if (super.isValidAttribute()) {
				source = getSourceFromAttribute("href"); //$NON-NLS-1$
			} else if (isValidAttribute()) {
				if (jsf2Source) {
					String library = null;
					Attr libraryAttr = element.getAttributeNode("library"); //$NON-NLS-1$
					if (libraryAttr != null && libraryAttr.getNodeValue() != null) {
						library = libraryAttr.getNodeValue().trim();
						library = library.length() == 0 ? null : library;
					}
					if (library != null) {
						prefix = JSF2_RESOURCES_FOLDER + '/' + library + '/';
					} else {
						prefix = JSF2_RESOURCES_FOLDER + '/';
					}
				}
				source = getSourceFromAttribute(hrefAttrName);
			} else {
				return null;
			}
			ICSSModel model = retrieveModel();
			setModel(model);
//			System.out.println("get CSS: " + source + " ==>> " + (model == null ? "FAILED" : " SUCCESSFULL"));
			return model;
		}

		private String getSourceFromAttribute(String hrefAttributeName) {
			String hrefExtracted = findAndReplaceElVariable(element
					.getAttribute(hrefAttrName));

			return hrefExtracted;
		}

		/**
		 */
		private ICSSModel retrieveModel() {
			if (!isValidAttribute() || source==null) {
				return null;
			}

			//If source starts with http(s):// then look at the workspace local cache. If not found then start a separate process to download the file and put it to the cache.
			if(source.startsWith("http://") || source.startsWith("https://")) {
				RemoteFileManager.Result result = RemoteFileManager.getInstance().getFile(source);
				if(result.isReady()) {
					source = result.getLocalPath();
				}
			}

			// null,attr check is done in isValidAttribute()
			IDOMModel baseModel = ((IDOMNode) element).getModel();
			if (baseModel == null)
				return null;
			Object id = baseModel.getId();
			if (!(id instanceof String))
				return null;
			// String base = (String)id;

			// get ModelProvideAdapter
			IModelProvideAdapter adapter = (IModelProvideAdapter) ((INodeNotifier) getElement())
					.getAdapterFor(IModelProvideAdapter.class);

			ProjectURLModelProvider provider = new ProjectURLModelProvider();
			try {
				IStructuredModel newModel = provider.getModelForRead(baseModel,
						prefix == null ? source : prefix + source);
				if (newModel == null)
					return null;
				if (!(newModel instanceof ICSSModel)) {
					newModel.releaseFromRead();
					return null;
				}

				// notify adapter
				if (adapter != null)
					adapter.modelProvided(newModel);

				return (ICSSModel) newModel;
			} catch (UnsupportedEncodingException e) {
				WebKbPlugin.getDefault().logError(e);
			} catch (IOException e) {
				WebKbPlugin.getDefault().logError(e);
			}

			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
	 */
	public void resourceChanged(IResourceChangeEvent event) {
		if(event == null || event.getDelta() == null) return;
		cleanUp(event.getDelta());
	}

	private static boolean checkDelta(IResourceDelta delta) {
		IResource resource = delta.getResource();
		if(resource instanceof IWorkspaceRoot) {
			IResourceDelta[] d = delta.getAffectedChildren();
			return (d.length > 0 && checkDelta(d[0]));
		}
		return true;
	}

	private void processDelta(IResourceDelta delta) {
		if(delta!= null) {
			int kind = delta.getKind();
			IResource resource = delta.getResource();
	
			if(resource instanceof IProject &&
					kind == IResourceDelta.REMOVED) {
				cleanUp((IProject)resource);
			} else if (resource instanceof IFile && (
				kind == IResourceDelta.CHANGED || 
				kind == IResourceDelta.ADDED ||
				kind == IResourceDelta.REMOVED ||
				kind == IResourceDelta.CONTENT)) {
				cleanUp((IFile)resource);
			}
			
			IResourceDelta[] cs = delta.getAffectedChildren();
			for (int i = 0; i < cs.length; i++) {
				processDelta(cs[i]);
			}
		}
	}

	private static boolean isDependencyContext(ELContext context, IFile resource) {
		if (resource.equals(context.getResource())) {
			return true;
		}

		if(context instanceof IIncludedContextSupport) {
			List<ELContext> includedContexts = ((IIncludedContextSupport)context).getIncludedContexts();
			for (ELContext includedContext : includedContexts) {
				if (isDependencyContext(includedContext, resource))
					return true;
			}
		}
		return false;
	}
}