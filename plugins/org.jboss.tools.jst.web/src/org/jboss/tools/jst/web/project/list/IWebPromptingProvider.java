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
package org.jboss.tools.jst.web.project.list;

import java.util.*;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.project.IPromptingProvider;

public interface IWebPromptingProvider extends IPromptingProvider {
	static String ERROR = "error";
	static List<Object> EMPTY_LIST = Collections.unmodifiableList(new ArrayList<Object>());

	static String JSF_BUNDLES = "jsf.bundles";
	static String JSF_REGISTERED_BUNDLES = "jsf.registered.bundles";
	static String JSF_BUNDLE_PROPERTIES = "jsf.bundle.properties";
	static String JSF_MANAGED_BEANS = "jsf.beans";
	static String JSF_BEAN_PROPERTIES = "jsf.bean.properties";
	static String JSF_BEAN_METHODS = "jsf.bean.methods";
	static String JSF_BEAN_ADD_PROPERTY = "jsf.bean.add.property";
	static String JSF_VIEW_ACTIONS = "jsf.view.action";
	static String JSF_BEAN_OPEN = "jsf.bean.open";
	static String JSF_GET_PATH = "jsf.get.path";
	static String JSF_OPEN_ACTION = "jsf.open.action";
	static String JSF_OPEN_CONVERTOR = "jsf.open.convertor";
	static String JSF_OPEN_RENDER_KIT = "jsf.open.render-kit";
	static String JSF_OPEN_VALIDATOR = "jsf.open.validator";
	static String JSF_OPEN_CLASS_PROPERTY = "jsf.open.property";
	static String JSF_OPEN_TAG_LIBRARY = "jsf.open.taglibrary";
	static String JSF_OPEN_KEY = "jsf.open.key";
	static String JSF_OPEN_BUNDLE = "jsf.open.bundle";
	static String JSF_GET_URL = "jsf.get.url";
	static String JSF_GET_TAGLIBS = "jsf.get.taglibs";

	static String JSF_CONVERT_URL_TO_PATH = "jsf.url.to.path";

	static String STRUTS_OPEN_PARAMETER = "struts.open.parameter";
	static String STRUTS_OPEN_BUNDLE = "struts.open.bundle";
	static String STRUTS_OPEN_KEY = "struts.open.key";
	static String STRUTS_OPEN_LINK_FORWARD = "struts.open.link.forward";
	static String STRUTS_OPEN_LINK_PAGE = "struts.open.link.page";
	static String STRUTS_OPEN_LINK_ACTION = "struts.open.link.action";
	static String STRUTS_OPEN_PROPERTY = "struts.open.property";
	static String STRUTS_OPEN_ACTION_MAPPING = "struts.open.action.mapping";
	static String STRUTS_OPEN_FORM_BEAN = "struts.open.form.bean";
	static String STRUTS_OPEN_FORWARD_PATH = "struts.open.forward.path";
	static String STRUTS_OPEN_OBJECT_BY_PATH = "struts.open.object.by.path";
	static String STRUTS_OPEN_FILE_IN_WEB_ROOT = "struts.open.file.in.web.root";
	static String STRUTS_OPEN_VALIDATOR = "struts.open.validator";
	static String STRUTS_OPEN_TAG_LIBRARY = "struts.open.taglibrary";
	static String STRUTS_OPEN_METHOD = "struts.open.method";

	static String SHALE_OPEN_COMPONENT = "shale.open.component";
	static String SHALE_COMPONENTS = "shale.components";
	static String SHALE_OPEN_DIALOG = "shale.open.dialog";
	static String SHALE_VIEW_DIALOGS = "shale.view.dialogs";

	static String PROPERTY_TYPE = "propertyType";
	static String PROPERTY_BEAN_ONLY = "bean-only";
	static String PARAMETER_TYPES = "parameterTypes";  // String[]
	static String RETURN_TYPE = "returnType";
	static String VIEW_PATH = "viewPath";
	static String FILE = "file";
	static String BUNDLE = "bundle";
	static String KEY = "key";
	static String MODULE = "module";
	static String ACTION = "action";
	static String TYPE = "type";
	static String PROPERTY = "property";
	static String MODEL_OBJECT_PATH = "model-path";
	static String NAME = "name";
	static String ATTRIBUTE = "attribute";
	static String LOCALE = "locale";
	
	public boolean isSupporting(String id);
	public List<Object> getList(XModel model, String id, String prefix, Properties properties);
}
