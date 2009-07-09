/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Exadel, Inc.
 *     Red Hat, Inc. 
 *******************************************************************************/
package org.jboss.tools.jst.web.messages.xpl;

import org.eclipse.osgi.util.NLS;


public class WebUIMessages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.jst.web.messages.messages";//$NON-NLS-1$
	
	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, WebUIMessages.class);		
	}
	public static String ATTRIBUTES;
	public static String CANNOT_RUN_SELECTION_WITHOUT_AVAILABLE_SERVER;
	public static String PLEASE_CREATE_A_SERVER_AND_SELECT_IT_ON_TOOLBAR;
	public static String ERROR;
	public static String INCORRECT_URL;
	public static String OK;
	public static String APPLICATION_ISNOT_REGISTERED_IN_SELECTED_SERVER;
	public static String CLOSE;
	public static String FILE_DOESNOT_EXIST;
	public static String FILE_ISNOT_RECOGNIZED_AS_WEBDESCRIPTOR_FILE;
	public static String WAR_LOCATION_MUST_BE_SET;
	public static String FILE_DOESNOT_EXIST_P;
	public static String FILE_ISNOT_CORRECT;
	public static String FILE_DOESNOT_CONTAIN_WEBXML;
	public static String CANNOT_READ_WEBXML;
	public static String NAME_MUST_BE_SET;
	public static String PROJECT_ALREADY_EXISTS_IN_THE_WORKSPACE;
	public static String FILE_ISNOT_RECOGNIZED;
	public static String WEBDESCRIPTOR_FILE_IS_CORRUPTED;
	public static String SERVLET_VERSION_WARNING;
	public static String SERVLET_VERSION_ISNOT_CONSISTENT_WITH_WEBXML_VERSION;
	public static String SAVE_OLD_SERVLET;
	public static String RUNTIME_IS_REQUIRED;
	public static String SPECIFIED_RUNTIME_DOESNOT_EXIST;
	public static String APPLICATION_IS_ALREADY_REGISTERED;
	public static String ITEM_ISNOT_REFERENCED;
	public static String CANCEL;
	public static String YOU_WANT_TO_REARRANGE_THE_DIAGRAM_ELEMENTS;
	public static String DELETE_REFERENCE_FROM_WEBDESCRIPTOR;
	public static String DELETE_FILENAME;
	public static String APPLICATION_HAS_BEEN_UNREGISTERED_FROM;
	public static String MESSAGE;
	public static String LIBRARY_SET_IS_ALREADY_ADDED;
	public static String LIBRARY_SET_ISNOT_FOUND;
	public static String PROJECT_TEMPLATE_ALREADY_EXISTS;
	public static String LIBRARY_SET_ALREADY_EXISTS;
	public static String LIBRARY_SET_NAME_MUST_BE_A_VALID_FOLDERNAME;
	public static String LIBRARY_SET_NAME_MUST_BE_A_VALID_FOLDER_NAME;
	public static String MUST_NOT_CONTAIN_CHARACTER;
	public static String TEMPLATE_PAGE_ALREADY_EXISTS;
	public static String PAGE_PATH_ISNOT_CORRECT;
	public static String IMPLEMENTATION_ALREADY_EXISTS;
	public static String DEFINE_COMMON_TEMPLATE_PROPERTIES;
	public static String SELECT_FOLDERS_AND_FILES;
	public static String SELECT_FILES_THAT_ARE_VELOCITY_TEMPLATES;
	public static String SET_VELOCITY_PROPERTIES;
	public static String CANNOT_CREATE_TEMPLATE_FOR_PROJECT;
	public static String YOU_WANT_TO_DELETE_TEMPLATE;
	public static String CONFIRMATION;
	public static String YOU_WANT_TO_DELETE_PAGE_TEMPLATE;
	public static String LIBRARYSET_CONTAINS;
	public static String YOU_WANT_TO_DELETE_FROM_LIBRARYSET;
	public static String YOU_WANT_TO_DELETE_LIBRARYSET;
	public static String CANNOT_FIND_PROJECT_VERSIONS_DESCRIPTORFILE;
	public static String CANNOT_PARSE_PROJECT_VERSIONS_DESCRIPTORFILE;
	public static String CANNOT_READ_PROJECT_VERSIONS_DESCRIPTORFILE;
	public static String YOU_WANT_TO_DELETE_IMPLEMENTATION;
	public static String CORE_LIBRARY_LOCATION_ISNOT_SET_FOR_VERSION;
	public static String CORE_LIBRARY_LOCATION_FOR_VERSION_ISNOT_CORRECT;
	public static String COMMON_LIBRARY_LOCATION_FOR_VERSION_ISNOT_CORRECT;
	public static String TEMPLATES_LOCATION_ISNOT_SET_FOR_VERSION;
	public static String TEMPLATES_LOCATION_FOR_VERSION_ISNOT_CORRECT;
	public static String YOU_WANT_TO_DELETE_LIBRARY;
	public static String WEBXML_CHANGES;
	public static String JSP_REFACTORING;
	public static String SERVER_ISNOT_SELECTED;
	public static String CANNOT_FIND_MODULE_FOR_THE_PROJECT;
	public static String CANNOT_REGISTER_IN_THIS_SERVER;
	public static String DELETE_VALIDATOR_NODE_FROM_XML;
	public static String PARENT_GROUP_MUST_BE_SPECIFIED;
	public static String PALETTE_ALREADY_CONTAINS_TAB;
	public static String PALETTE_ALREADY_CONTAINS_TAB_2P;
	public static String PATH_TO_TLD_ISNOT_CORRECT;
	public static String START_TOMCAT_SERVER;
	public static String EITHER_OR_MUST_BE_SET;
	public static String YOU_MAY_SET_ONLY_ONE;
	public static String ADD_JSP_BREAKPOINT;
	public static String REMOVE_JSP_BREAKPOINT;
	public static String NOT_RESOLVED;
	public static String VALUE_NOT_RESOLVED;
	public static String STRUTS;
	public static String EDIT_FILTER;
	public static String EditProjectTemplateSupport_Save;
	public static String ENTER_NEW_FILTER;
	public static String DELETE_REFERENCE_FROM_STRUTS_CONFIGURATION_FILE;
	public static String YOU_WANT_TO_DELETE_LINK_TO;
	public static String TILES_EDITOR;
	public static String DIAGRAM;
	public static String PRINT_DIAGRAM;
	public static String TILES_CONFIG_DESCRIPTION;
	public static String DEFINITIONS;
	public static String BASIC;
	public static String ADVANCED;
	public static String DEPRECATED;
	public static String PUT;
	public static String PUTLIST;
	public static String ADD;
	public static String ITEM;
	public static String BEAN;
	public static String SET_PROPERTIES;
	public static String SET_PROPERTY;
	public static String STRUTS_MODEL;
	public static String PRINT;
	public static String ZOOM;
	public static String ALL;
	public static String SELECTED_PAGES;
	public static String SELECT_ALL;
	public static String UNSELECT_ALL;
	public static String PRINT_PREVIEW;
	public static String REDHAT_TAG_LIBRARY_EDITOR;
	public static String SECURITY_ROLES;
	public static String SERVLET;
	public static String AUTH_CONSTRAINT;
	public static String TAGLIBS;
	public static String PROPERTY_GROUPS;
	public static String PROPERTY_GROUP;
	public static String INITPARAMS;
	public static String FILTER;
	public static String RESOURCE_COLLECTIONS;
	public static String SECURITY_CONSTRAINT;
	public static String LOCALE_ENCODING_MAPPINGS;
	public static String WELCOME_FILES;
	public static String YES;
	public static String NO;
	public static String ADD_TO_EXISTING_GROUP;
	public static String CREATE_NEW_GROUP;
	public static String WARNING;
	public static String PROJECT_EXISTS_IN_WORKSPACE;
	public static String JAVA_PROJECT_EXISTS;
	public static String PROJECT_EXISTS;
	public static String PATH_TO_JVM_IS_EMPTY;
	public static String SPECIFIED_FOLDER_DOESNOT_EXIST;
	public static String SPECIFIED_FOLDER_ISNOT_JVMHOME;
	public static String CANNOT_FIND_TOOLSJAR;
	public static String CHECK_JVM;
	public static String CONTEXT_ROOT_CANNOT_CONTAIN_CHARACTER;
	public static String WEB_RESOURCES;
	public static String INCORRECT_URI;
	public static String INCORRECT_PREFIX;
}
