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
package org.jboss.tools.jst.web.tld.model.handlers;

import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.files.handlers.CreateFileSupport;

public class CreateTLDFileSupport extends CreateFileSupport {

	protected XModelObject modifyCreatedObject(XModelObject o) {
/*
		String entity = getChildTagEntity(o.getModelEntity());
		XModelObject t = o.getModel().createModelObject(entity, null);
		t.setAttributeValue("name", "EnterNewTag");
		o.addChild(t);
*/
		return o;
	}
/*
	private String getChildTagEntity(XModelEntity entity) {
		XChild[] cs = entity.getChildren();
		for (int i = 0; i < cs.length; i++) {
			if(cs[i].getName().startsWith("TLDTag")) return cs[i].getName();
		}
		return null;
	}

	protected DefaultWizardDataValidator createValidator() {
		return new TLDValidator(); 
	}
	protected class TLDValidator extends Validator {
		public void validate(Properties data) {
			super.validate(data);
			if(message != null) return;
			String uri = data.getProperty("uri");
			validateURI(uri);
		}
		
		private void validateURI(String uri) {
			if(uri == null) return;
			uri = uri.trim();
			if(uri.length() == 0) {
				warning = "Attribute URI is not set.";
				return;
			}
			WebProject p = WebProject.getInstance(getTarget().getModel());
			if(p.getTaglibMapping().getTaglibObject(uri) != null) {
				warning = "A tag library with specified uri exists in the project.";
				return;
			}
		}
	}
*/

}
