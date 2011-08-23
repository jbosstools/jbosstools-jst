package org.jboss.tools.jst.web.kb.internal.validation;

import org.eclipse.osgi.util.NLS;

public class ELValidationMessages {
	private static final String BUNDLE_NAME = "org.jboss.tools.jst.web.kb.internal.validation.messages"; //$NON-NLS-1$
	
	public static String UNKNOWN_EL_VARIABLE_NAME;
	public static String UNKNOWN_EL_VARIABLE_PROPERTY_NAME;
	public static String UNPAIRED_GETTER_OR_SETTER;
	public static String EL_SYNTAX_ERROR;

	public static String VALIDATING_EL_FILE;

	public static String EL_VALIDATOR_ERROR_VALIDATING;
	public static String EL_VALIDATOR_SETTER;
	public static String EL_VALIDATOR_GETTER;

	static {
		NLS.initializeMessages(BUNDLE_NAME, ELValidationMessages.class);
	}
}