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
package org.jboss.tools.jst.web.tld.model;

import org.jboss.tools.common.model.loaders.*;
import org.jboss.tools.common.xml.XMLEntityResolver;
import org.jboss.tools.jst.web.WebModelPlugin;

public class FileTLDRecognizer implements EntityRecognizer, TLDConstants {
    static {
        try {
            XMLEntityResolver.registerPublicEntity(TLD_DOC_PUBLICID_1_1, FileTLDRecognizer.class, "/meta/web-jsptaglibrary_1_1.dtd");
            XMLEntityResolver.registerPublicEntity(TLD_DOC_PUBLICID_1_2, FileTLDRecognizer.class, "/meta/web-jsptaglibrary_1_2.dtd");
        } catch (Exception e) {
			WebModelPlugin.getPluginLog().logError(e);
        }
    }
    
    static String VERSION_2_0 = "version=\"2.0\"";
    static String VERSION_2_1 = "version=\"2.1\"";
    static String XMLNS_2_0 = "\"http://java.sun.com/xml/ns/j2ee\"";
    static String XMLNS_2_1 = "\"http://java.sun.com/xml/ns/javaee\"";
    	
    public FileTLDRecognizer() {}

    public String getEntityName(String ext, String body) {
    	if((body == null || !"tld".equals(ext))) return null;
        int i = body.indexOf("<!DOCTYPE");
        if(i >= 0) {
        	int j = body.indexOf(">", i);
        	if(j < 0) return null;
        	String dt = body.substring(i, j);
        	if(dt.indexOf("taglib") < 0) return null;
        	if(dt.indexOf(TLD_DOC_PUBLICID_1_1) > 0) return "FileTLD_PRO";
        	if(dt.indexOf(TLD_DOC_PUBLICID_1_2) > 0) return "FileTLD_1_2";
        	if(dt.indexOf("SYSTEM") > 0 && dt.indexOf("web-jsptaglibrary_1_1.dtd") > 0) return "FileTLD_PRO";
        	if(dt.indexOf("SYSTEM") > 0 && dt.indexOf("web-jsptaglibrary_1_2.dtd") > 0) return "FileTLD_1_2";
        }
        return (body.indexOf("<taglib") >= 0 
		        && body.indexOf(VERSION_2_0) > 0
		        && body.indexOf("xmlns=" + XMLNS_2_0) > 0) ? "FileTLD_2_0" :
		       (isTLD20WithNamespace(body, VERSION_2_0, XMLNS_2_0)) ? "FileTLD_2_0" :
		       (body.indexOf("<taglib") >= 0 
		        && body.indexOf(VERSION_2_1) > 0
		        && body.indexOf("xmlns=" + XMLNS_2_1) > 0) ? "FileTLD_2_1" :
		       (isTLD20WithNamespace(body, VERSION_2_1, XMLNS_2_1)) ? "FileTLD_2_1" :
               "FileTLD";
    }
    
    private boolean isTLD20WithNamespace(String body, String version, String xmlns) {
    	if(body.indexOf(version) < 0) return false;
    	int q = body.indexOf(xmlns);
    	if(q < 0) return false;
    	String s = body.substring(0, q);
    	int b = s.lastIndexOf('<');
    	if(b < 0) return false;
    	int ti = s.indexOf(":taglib", b);
    	if(ti < 0) return false;
    	String namespace = s.substring(b + 1, ti);
    	int e = s.lastIndexOf("xmlns:" + namespace);
    	if(e < 0) return false;
    	return true;
    }

}
