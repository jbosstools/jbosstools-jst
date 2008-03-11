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
package org.jboss.tools.jst.web.tiles.ui.editor.model.commands;

import org.eclipse.gef.commands.Command;

import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.jst.web.tiles.ui.editor.edit.TilesEditPart;
import org.jboss.tools.jst.web.tiles.ui.editor.model.IDefinition;
import org.jboss.tools.jst.web.tiles.ui.editor.model.ILink;

public class ConnectionCommand extends Command {

	protected TilesEditPart oldSource;

	protected String oldSourceTerminal;

	protected TilesEditPart oldTarget;

	protected String oldTargetTerminal;

	protected TilesEditPart source;

	protected String sourceTerminal;

	protected TilesEditPart target;

	protected String targetTerminal;

	protected ILink link;

	public ConnectionCommand() {
		super("connection command");
	}

	public boolean canExecute() {
		if (target == null)
			return false;
		if (target.getModel() == null)
			return false;
		return ((IDefinition) source.getModel()).getTilesModel().getHelper()
				.canMakeLink(
						(XModelObject) ((IDefinition) source.getModel())
								.getSource(),
						(XModelObject) ((IDefinition) target.getModel())
								.getSource());
	}

	public void execute() {
		if (((IDefinition) target.getModel()).isCollapsed())
			((IDefinition) target.getModel()).expand();
		((IDefinition) source.getModel()).getTilesModel().getHelper().makeLink(
				(XModelObject) ((IDefinition) source.getModel()).getSource(),
				(XModelObject) ((IDefinition) target.getModel()).getSource());
	}

	public String getLabel() {
		return "connection command";
	}

	public TilesEditPart getSource() {
		return source;
	}

	public java.lang.String getSourceTerminal() {
		return sourceTerminal;
	}

	public TilesEditPart getTarget() {
		return target;
	}

	public String getTargetTerminal() {
		return targetTerminal;
	}

	public ILink getLink() {
		return link;
	}

	public void setSource(TilesEditPart newSource) {
		source = newSource;
	}

	public void setSourceTerminal(String newSourceTerminal) {
		sourceTerminal = newSourceTerminal;
	}

	public void setTarget(TilesEditPart newTarget) {
		target = newTarget;
	}

	public void setTargetTerminal(String newTargetTerminal) {
		targetTerminal = newTargetTerminal;
	}

	public void setLink(ILink l) {
		link = l;
	}

	public boolean canUndo() {
		return false;
	}
}
