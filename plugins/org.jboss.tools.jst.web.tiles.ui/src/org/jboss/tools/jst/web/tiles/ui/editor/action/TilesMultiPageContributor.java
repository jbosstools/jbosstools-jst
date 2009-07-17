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
package org.jboss.tools.jst.web.tiles.ui.editor.action;

import java.util.Iterator;

import org.jboss.tools.common.model.ui.texteditors.AbstractMultiPageContributor;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.jboss.tools.common.gef.action.ActionRegistrySupport;
import org.jboss.tools.common.gef.action.PrintRetargetAction;
import org.jboss.tools.jst.web.tiles.ui.editor.TilesGuiEditor;

/**
 * @author Jeremy
 *
 */
public class TilesMultiPageContributor extends AbstractMultiPageContributor {
	ActionRegistrySupport registrySupport = new TilesRegistrySupport();

	public TilesMultiPageContributor() {
	}
	
	public void dispose() {
		registrySupport.dispose();
		super.dispose();
	}
	
	public void init(IActionBars bars) {
		registrySupport.setPage(getPage());
		super.init(bars);
		registrySupport.buildGEFActions();
		registrySupport.addRetargetAction(new TilesCutRetargetAction());
		registrySupport.declareGlobalActionKeys();
		registrySupport.contributeGEFToToolBar(bars.getToolBarManager());
		initEditMenu(bars);
	}

	public void setActivePage(IEditorPart part) {
		if (fActiveEditorPart == part) return;
		cleanStatusLine();

		fActiveEditorPart = part;	
		IActionBars actionBars = getActionBars();		
		if (actionBars != null) {

			ActionRegistry registry = null;
			if(fActiveEditorPart instanceof TilesGuiEditor)
				registry = (ActionRegistry)part.getAdapter(ActionRegistry.class);
			Iterator globalActionKeys = registrySupport.getGlobalActionKeys();
			while(globalActionKeys.hasNext()) {
				String id = (String)globalActionKeys.next();				
				actionBars.setGlobalActionHandler(id, (registry == null ? null : registry.getAction(id)));
			}
			IToolBarManager tbm = actionBars.getToolBarManager();
			IContributionItem [] items = tbm.getItems();
			if(items != null){
				for(int i = 0; i < items.length; i++){
					String id = items[i].getId();
					if(id == null) continue; //Separator
					actionBars.setGlobalActionHandler(id, (registry==null ? null:registry.getAction(id)));
				}
			}
	
			ITextEditor editor = (part instanceof ITextEditor) ? (ITextEditor) part : null;
			
			if (editor!=null) {
	
				actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), (registry != null) ? registry.getAction(ITextEditorActionConstants.DELETE) : (editor != null) ? getAction(editor, ITextEditorActionConstants.DELETE) : null);
				actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), getAction(editor, ITextEditorActionConstants.UNDO));
				actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), getAction(editor, ITextEditorActionConstants.REDO));
				actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(), (registry != null) ? registry.getAction(ITextEditorActionConstants.CUT) : (editor != null) ? getAction(editor, ITextEditorActionConstants.CUT) : null);
				actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), (registry != null) ? registry.getAction(ITextEditorActionConstants.COPY) : (editor != null) ? getAction(editor, ITextEditorActionConstants.COPY) : null);
				actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), (registry != null) ? registry.getAction(ITextEditorActionConstants.PASTE) : (editor != null) ? getAction(editor, ITextEditorActionConstants.PASTE) : null);
				actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), getAction(editor, ITextEditorActionConstants.SELECT_ALL));
				actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(), getAction(editor, ITextEditorActionConstants.FIND));
				actionBars.setGlobalActionHandler(IDEActionFactory.BOOKMARK.getId(), getAction(editor, IDEActionFactory.BOOKMARK.getId()));
				actionBars.setGlobalActionHandler(IDEActionFactory.ADD_TASK.getId(), getAction(editor, IDEActionFactory.ADD_TASK.getId()));
				actionBars.setGlobalActionHandler(ActionFactory.PRINT.getId(), (registry != null) ? registry.getAction("Print_Diagram") : (editor != null) ? getAction(editor, ActionFactory.PRINT.getId()) : null); //$NON-NLS-1$
				actionBars.setGlobalActionHandler(ActionFactory.REVERT.getId(), getAction(editor, ITextEditorActionConstants.REVERT));
				actionBars.setGlobalActionHandler(ActionFactory.SAVE.getId(), getAction(editor, ITextEditorActionConstants.SAVE));
			} else {
				actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), null);
				actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), null);
			}
			// re-register action on key binding service
			IEditorPart localPart = (part!=null)?part:mainPart;
			IHandlerService handler = (IHandlerService)localPart.getEditorSite().getService(IHandlerService.class);
			if(registry != null) {
				registerKeyBindings(handler, ACTIONS_1, registry);
			} else if (editor != null) {
				// editor
				registerKeyBindings(handler, ACTIONS_2, editor);
			} else {
				//fakeTextEditor
//				registerKeyBindings(handler, ACTIONS_1, fakeTextEditor);
			}

			cleanActionBarStatus();
			actionBars.updateActionBars(); 
		}
	
		updateStatus();
	}

	public void registerKeyBindings(IHandlerService handler, String[] actions, ActionRegistry registry) {
		for (int i = 0; i < actions.length; i++) {
			IAction action = registry.getAction(actions[i]);
			if(action == null) continue;
			registerKeyBinding(handler, actions[i], action);
		}
	}

	class TilesRegistrySupport extends ActionRegistrySupport{
		
		public void buildGEFActions() {
			addRetargetAction(new PrintRetargetAction());
			addRetargetAction(new TilesPreferencesRetargetAction());

			addRetargetAction(ActionFactory.DELETE.create(getPage().getWorkbenchWindow())); // new DeleteRetargetAction());
			addRetargetAction(ActionFactory.COPY.create(getPage().getWorkbenchWindow())); // new CopyRetargetAction());
			addRetargetAction(ActionFactory.PASTE.create(getPage().getWorkbenchWindow())); // new PasteRetargetAction());
		
			addRetargetAction(new ZoomInRetargetAction());
			addRetargetAction(new ZoomOutRetargetAction());
		}
		public void contributeGEFToToolBar(IToolBarManager tbm) {
			tbm.add(getAction("Print_Diagram")); //$NON-NLS-1$
			tbm.add(getAction("Preferences")); //$NON-NLS-1$

			tbm.add(new Separator());
			tbm.add(new ZoomComboContributionItem(getPage()));
		}
		
	}

}
