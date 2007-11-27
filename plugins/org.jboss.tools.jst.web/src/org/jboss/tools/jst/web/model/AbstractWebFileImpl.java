/*
 * Created on February 20, 2003, 4:02 PM
 */

package org.jboss.tools.jst.web.model;

import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.filesystems.impl.AbstractExtendedXMLFileImpl;
import org.jboss.tools.common.model.loaders.*;
import org.jboss.tools.common.model.loaders.impl.SerializingLoader;
import org.jboss.tools.common.model.util.*;

/**
 *
 * @author  valera & glory
 */
public abstract class AbstractWebFileImpl extends AbstractExtendedXMLFileImpl {

    public AbstractWebFileImpl() {}

	protected WebProcess provideWebProcess() {
		String entity = getProcessEntity();
		if(entity == null) return null;
		if(getModelEntity().getChild(entity) == null) return null;
		WebProcess process = (WebProcess)getChildByPath("process");
		if(process == null) {
			process = (WebProcess)getModel().createModelObject(entity, null);
			addChild(process); 
		}
		return process;
	}
	
	protected void mergeAll(XModelObject f, boolean update) {
		WebProcess process = provideWebProcess();
		merge(f, !update);

		if(process != null) {
			if(!process.isPrepared()/* || update*/ || isForceLoadOn()) {
				XObjectLoader loader = XModelObjectLoaderUtil.getObjectLoader(this);
				((WebProcessLoader)loader).reloadProcess(this);
			}
			if(process.isPrepared())
				process.autolayout();
		}
	}
    
	SerializingLoader loader = null;

	public String getAsText() {
		boolean isIncorrect = ("yes".equals(getAttributeValue("isIncorrect")));
		if(isIncorrect) return getAttributeValue("incorrectBody");
		String abts = get("actualBodyTimeStamp");
		if(abts != null && (abts.equals("0") || abts.equals("" + getTimeStamp()))) {
			return get("correctBody");
		}
		if(loader == null) loader = (SerializingLoader)XModelObjectLoaderUtil.getObjectLoader(this);
		String body = loader.serializeObject(this);
		if(body == null) return "";
		set("correctBody", body);
		set("actualBodyTimeStamp", "" + getTimeStamp());
		return body;
	}
	
}
