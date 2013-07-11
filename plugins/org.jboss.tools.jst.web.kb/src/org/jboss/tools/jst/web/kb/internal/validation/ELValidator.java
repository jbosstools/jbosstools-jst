 /*******************************************************************************
  * Copyright (c) 2007-2012 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.jst.web.kb.internal.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jface.text.IRegion;
import org.eclipse.wst.validation.internal.core.ValidationException;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;
import org.eclipse.wst.validation.internal.provisional.core.IValidationContext;
import org.eclipse.wst.validation.internal.provisional.core.IValidator;
import org.jboss.tools.common.EclipseUtil;
import org.jboss.tools.common.el.core.ELReference;
import org.jboss.tools.common.el.core.model.ELExpression;
import org.jboss.tools.common.el.core.model.ELInstance;
import org.jboss.tools.common.el.core.model.ELInvocationExpression;
import org.jboss.tools.common.el.core.model.ELPropertyInvocation;
import org.jboss.tools.common.el.core.parser.ELParserFactory;
import org.jboss.tools.common.el.core.parser.ELParserUtil;
import org.jboss.tools.common.el.core.parser.LexicalToken;
import org.jboss.tools.common.el.core.parser.SyntaxError;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.el.core.resolver.ELContextImpl;
import org.jboss.tools.common.el.core.resolver.ELResolution;
import org.jboss.tools.common.el.core.resolver.ELResolver;
import org.jboss.tools.common.el.core.resolver.ELResolverFactoryManager;
import org.jboss.tools.common.el.core.resolver.ELSegment;
import org.jboss.tools.common.el.core.resolver.IVariable;
import org.jboss.tools.common.el.core.resolver.JavaMemberELSegmentImpl;
import org.jboss.tools.common.el.core.resolver.SimpleELContext;
import org.jboss.tools.common.el.core.resolver.TypeInfoCollector;
import org.jboss.tools.common.el.core.resolver.Var;
import org.jboss.tools.common.java.IJavaSourceReference;
import org.jboss.tools.common.validation.ContextValidationHelper;
import org.jboss.tools.common.validation.EditorValidationContext;
import org.jboss.tools.common.validation.IELValidationDelegate;
import org.jboss.tools.common.validation.IPreferenceInfo;
import org.jboss.tools.common.validation.IProjectValidationContext;
import org.jboss.tools.common.validation.IStringValidator;
import org.jboss.tools.common.validation.ITypedReporter;
import org.jboss.tools.common.validation.IValidatingProjectTree;
import org.jboss.tools.common.validation.PreferenceInfoManager;
import org.jboss.tools.common.validation.ValidatorManager;
import org.jboss.tools.jst.web.kb.PageContextFactory;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.internal.KbBuilder;
import org.jboss.tools.jst.web.kb.preferences.ELSeverityPreferences;

/**
 * EL Validator
 * @author Alexey Kazakov
 */
public class ELValidator extends WebValidator implements IStringValidator {

	public static final String ID = "org.jboss.tools.jst.web.kb.ELValidator"; //$NON-NLS-1$
	public static final String PREFERENCE_PAGE_ID = "org.jboss.tools.jst.web.ui.preferences.ELValidatorPreferencePage"; //$NON-NLS-1$
	public static final String PROPERTY_PAGE_ID = "org.jboss.tools.jst.web.ui.properties.ELValidatorPreferencePage"; //$NON-NLS-1$
	

	private static final String EXTENSION_POINT_ID = "org.jboss.tools.jst.web.kb.elValidationDelegate"; //$NON-NLS-1$

	private static Set<IELValidationDelegate> DELEGATES;
	static {
		DELEGATES = new HashSet<IELValidationDelegate>();
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry.getExtensionPoint(EXTENSION_POINT_ID);
		if (extensionPoint != null) {
			IExtension[] extensions = extensionPoint.getExtensions();
			for (int i = 0; i < extensions.length; i++) {
				IExtension extension = extensions[i];
				IConfigurationElement[] elements = extension.getConfigurationElements();
				for (int j = 0; j < elements.length; j++) {
					try {
						IELValidationDelegate delegate = (IELValidationDelegate) elements[j]
								.createExecutableExtension("class"); //$NON-NLS-1$
						DELEGATES.add(delegate);
					} catch (CoreException e) {
						WebKbPlugin.getDefault().logError(e);
					}
				}
			}
		}
	}

	private ELResolver[] resolvers;
	protected ELParserFactory mainFactory;

	private boolean revalidateUnresolvedELs = false;
	private boolean validateVars = true;

	public ELValidator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidator#isEnabled(org.eclipse.core.resources.IProject)
	 */
	public boolean isEnabled(IProject project) {
		return ELSeverityPreferences.isValidationEnabled(project) && ELSeverityPreferences.shouldValidateEL(project);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.validation.ValidationErrorManager#getMaxNumberOfMarkersPerFile(org.eclipse.core.resources.IProject)
	 */
	@Override
	public int getMaxNumberOfMarkersPerFile(IProject project) {
		return ELSeverityPreferences.getMaxNumberOfProblemMarkersPerFile(project);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.validation.ValidationErrorManager#init(org.eclipse.core.resources.IProject, org.jboss.tools.jst.web.kb.internal.validation.ContextValidationHelper, org.jboss.tools.jst.web.kb.validation.IProjectValidationContext, org.eclipse.wst.validation.internal.provisional.core.IValidator, org.eclipse.wst.validation.internal.provisional.core.IReporter)
	 */
	@Override
	public void init(IProject project, ContextValidationHelper validationHelper, IProjectValidationContext context, org.eclipse.wst.validation.internal.provisional.core.IValidator manager, IReporter reporter) {
		super.init(project, validationHelper, context, manager, reporter);
		setAsYouTypeValidation(false);
		resolvers = ELResolverFactoryManager.getInstance().getResolvers(project);
		mainFactory = ELParserUtil.getJbossFactory();
		validateVars = ELSeverityPreferences.ENABLE.equals(ELSeverityPreferences.getInstance().getProjectPreference(validatingProject, ELSeverityPreferences.CHECK_VARS));
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidator#validate(java.util.Set, org.eclipse.core.resources.IProject, org.jboss.tools.jst.web.kb.internal.validation.ContextValidationHelper, org.jboss.tools.jst.web.kb.validation.IProjectValidationContext, org.jboss.tools.jst.web.kb.internal.validation.ValidatorManager, org.eclipse.wst.validation.internal.provisional.core.IReporter)
	 */
	public IStatus validate(Set<IFile> changedFiles, IProject project, ContextValidationHelper validationHelper, IProjectValidationContext context, ValidatorManager manager, IReporter reporter) throws ValidationException {
		init(project, validationHelper, context, manager, reporter);
		initRevalidationFlag();
		IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();

		Set<IFile> filesToValidate = new HashSet<IFile>();
		boolean containsJavaOrComponentsXml = false;
		for (IFile file : changedFiles) {
			if(shouldBeValidated(file) && notValidatedYet(file)) {
				filesToValidate.add(file);
				if(!containsJavaOrComponentsXml) {
					String fileName = file.getName().toLowerCase();
					containsJavaOrComponentsXml = fileName.endsWith(".java") || fileName.endsWith(".properties") || fileName.equals("components.xml"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
			}
		}

		if(containsJavaOrComponentsXml) {
			if(revalidateUnresolvedELs) {
				Set<IPath> unnamedResources = validationContext.getUnnamedElResources();
				for (IPath path : unnamedResources) {
					IFile file = wsRoot.getFile(path);
					if(shouldBeValidated(file) && notValidatedYet(file)) {
						filesToValidate.add(file);
					}
				}
			}
		}

		Set<ELReference> els = validationContext.getElsForValidation(changedFiles, false);
		validationContext.removeLinkedEls(filesToValidate);
		Set<ELReference> elsToValidate = new HashSet<ELReference>();
		if(revalidateUnresolvedELs) {
			int i=0;
			for (ELReference el : els) {
				IResource resource = el.getResource();
				if(resource instanceof IFile && shouldBeValidated((IFile)resource) && !filesToValidate.contains(resource) && notValidatedYet(resource)) {
					// Don't re-validate more than 1000 ELs.
					if(i++>1000) {
						break;
					}
					validationContext.removeLinkedEl(el);
					elsToValidate.add(el);
				}
			}
		}
		for (IFile file : filesToValidate) {
			validateFile(file);
		}
		for (ELReference el : elsToValidate) {
			validateEL(el, false, null);
			coreHelper.getValidationContextManager().addValidatedProject(this, el.getResource().getProject());
		}

		validationContext.clearOldVariableNameForElValidation();
		return OK_STATUS;
	}

	private void initRevalidationFlag() {
		String revalidateUnresolvedELsString = ELSeverityPreferences.getInstance().getProjectPreference(validatingProject, ELSeverityPreferences.RE_VALIDATE_UNRESOLVED_EL);
		revalidateUnresolvedELs = ELSeverityPreferences.ENABLE.equals(revalidateUnresolvedELsString);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidator#validateAll(org.eclipse.core.resources.IProject, org.jboss.tools.jst.web.kb.internal.validation.ContextValidationHelper, org.jboss.tools.jst.web.kb.validation.IProjectValidationContext, org.jboss.tools.jst.web.kb.internal.validation.ValidatorManager, org.eclipse.wst.validation.internal.provisional.core.IReporter)
	 */
	public IStatus validateAll(IProject project, ContextValidationHelper validationHelper, IProjectValidationContext context, ValidatorManager manager, IReporter reporter) throws ValidationException {
		init(project, validationHelper, context, manager, reporter);
		initRevalidationFlag();
		Set<IFile> files = validationHelper.getProjectSetRegisteredFiles();
		Set<IFile> filesToValidate = new HashSet<IFile>();
		for (IFile file : files) {
			if(shouldBeValidated(file)) {
				if(notValidatedYet(file)) {
					filesToValidate.add(file);
				}
			} else {
				validationContext.removeUnnamedElResource(file.getFullPath());
			}
		}
		for (IFile file : filesToValidate) {
			validateFile(file);
		}
		return OK_STATUS;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.validation.IAsYouTypeValidator#validate(org.eclipse.wst.validation.internal.provisional.core.IValidator, org.eclipse.core.resources.IProject, java.util.Collection, org.eclipse.wst.validation.internal.provisional.core.IValidationContext, org.eclipse.wst.validation.internal.provisional.core.IReporter, org.jboss.tools.common.validation.EditorValidationContext, org.jboss.tools.common.validation.IProjectValidationContext, org.eclipse.core.resources.IFile)
	 */
	@Override
	public void validate(IValidator validatorManager, IProject rootProject, Collection<IRegion> dirtyRegions, IValidationContext helper, IReporter reporter, EditorValidationContext validationContext, IProjectValidationContext projectContext, IFile file) {
		init(rootProject, null, projectContext, validatorManager, reporter);
		setAsYouTypeValidation(true);
		this.document = validationContext.getDocument();
		ELContext elContext = PageContextFactory.createPageContext(this.document, true);
		if(elContext!=null) {
			elContext.setDirty(true);
			for (IRegion region : dirtyRegions) {
				Collection<ELReference> regionReferences = elContext.getELReferences(region);
				for (ELReference elReference : regionReferences) {
					validateEL(elReference, true, elContext);
				}
				disableProblemAnnotations(region, reporter);
			}

			if(reporter instanceof ITypedReporter) {
				((ITypedReporter)reporter).addTypeForRegion(getProblemType());
			}
		}
	}

	private int markers;

	private void validateFile(IFile file) {
		if(reporter.isCancelled() || !shouldFileBeValidated(file)) {
			return;
		}
		displaySubtask(ELValidationMessages.VALIDATING_EL_FILE, new String[]{file.getProject().getName(), file.getName()});
		coreHelper.getValidationContextManager().addValidatedProject(this, file.getProject());
		removeAllMessagesFromResource(file);
		markers = 0;
		ELContext context = PageContextFactory.createPageContext(file);
		if(context!=null) {
			ELReference[] references = context.getELReferences();
			for (int i = 0; i < references.length; i++) {
				if(markers<getMaxNumberOfMarkersPerFile(file.getProject())) {
					validateEL(references[i], false, context);
				}
			}
		}
	}

	private void validateEL(ELReference el, boolean asYouType, ELContext context) {
		if(asYouType || (!reporter.isCancelled() && shouldBeValidated(el.getResource()))) {
			displaySubtask(ELValidationMessages.VALIDATING_EL_FILE, new String[]{el.getResource().getProject().getName(), el.getResource().getName()});
			if(!asYouType) {
				el.deleteMarkers();
			}

			if(context!=null && !el.getSyntaxErrors().isEmpty() && !isDollarExpressionInXML(el)) {
				for (SyntaxError error: el.getSyntaxErrors()) {
					markers++;
					IJavaSourceReference reference = getJavaReference(el.getResource(), el.getStartPosition() + error.getPosition(), 1);
					if(reference == null) {
						if(asYouType) {
							addMessage(el.getResource(), el.getStartPosition() + error.getPosition(), 1, ELSeverityPreferences.EL_SYNTAX_ERROR, ELValidationMessages.EL_SYNTAX_ERROR, new String[]{error.getProblem()});
						} else {
							addError(ELValidationMessages.EL_SYNTAX_ERROR, ELSeverityPreferences.EL_SYNTAX_ERROR, new String[]{error.getProblem()}, el.getLineNumber(), 1, el.getStartPosition() + error.getPosition(), context.getResource());
						}
					} else {
						if(asYouType) {
							addMessage(el.getResource(), reference, ELSeverityPreferences.EL_SYNTAX_ERROR, ELValidationMessages.EL_SYNTAX_ERROR, new String[]{error.getProblem()});
						} else {
							addError(ELValidationMessages.EL_SYNTAX_ERROR, ELSeverityPreferences.EL_SYNTAX_ERROR, new String[]{error.getProblem()}, reference, context.getResource());
						}
					}
				}
			}
			for (ELExpression expresion : el.getEl()) {
				validateELExpression(el, expresion, asYouType, context);
			}
		}
	}

	private boolean isDollarExpressionInXML(ELReference el) {
		String ext = el.getResource().getFileExtension();
		return ("xml".equals(ext) && isDollarExpression(el)); 
	}
	private boolean isDollarExpression(ELReference el) {
		List<ELInstance> is = el.getELModel().getInstances();
		for (ELInstance i:is) {
			if(i.getFirstToken().getText().equals("${")) {
				return true;
			}
		}
		return false;
	}

	private void validateELExpression(ELReference elReference, ELExpression el, boolean asYouType, ELContext context) {
		List<ELInvocationExpression> es = el.getInvocations();
		for (ELInvocationExpression token: es) {
			validateElOperand(elReference, token, asYouType, context);
		}
	}

	private void validateElOperand(ELReference elReference, ELInvocationExpression operandToken, boolean asYouType, ELContext context) {
		IFile file = elReference.getResource();
		int documnetOffset = elReference.getStartPosition();
		String operand = operandToken.getText();
		if(operand.trim().length()==0) {
			return;
		}
		String varName = operand;
		int offsetOfVarName = documnetOffset + operandToken.getFirstToken().getStart();
		int lengthOfVarName = varName.length();
		boolean unresolvedTokenIsVariable = false;
		ELResolution resolution = null;
		if(context==null) {
			context = PageContextFactory.createPageContext(file);
		}
		if(context==null) {
			context = new SimpleELContext();
			context.setResource(file);
			context.setElResolvers(resolvers);
		}
		int maxNumberOfResolvedSegments = -1;
		int maxNumberOfDetectedSegments = -1;
		List<Var> vars = null;
		ELContextImpl c = null;
		if(!validateVars && context instanceof ELContextImpl) {
			c = (ELContextImpl)context;
			vars = c.getAllVars();
			c.setAllVars(new ArrayList<Var>());
		}

		for (int i = 0; i < resolvers.length; i++) {
			ELResolution elResolution = resolvers[i].resolve(context, operandToken, documnetOffset);
			if(elResolution==null) {
				continue;
			}
			if(!asYouType) {
				for (ELSegment segment : elResolution.getSegments()) {
					IResource resource = segment.getResource();
					if(resource instanceof IFile) {
						validationContext.addLinkedEl(resource.getFullPath().toString(), elReference);
					}
				}
			}
			if(elResolution.isResolved()) {
				resolution = elResolution;
				break;
			}
			int number = elResolution.getNumberOfResolvedSegments();
			if(number > maxNumberOfResolvedSegments) {
				maxNumberOfResolvedSegments = number;
				maxNumberOfDetectedSegments = elResolution.getSegments().size();
				resolution = elResolution;
			} else if(number == maxNumberOfResolvedSegments) {
				int segmentsNumber = elResolution.getSegments().size();
				if(segmentsNumber > maxNumberOfDetectedSegments) {
					maxNumberOfDetectedSegments = segmentsNumber;
					resolution = elResolution;
				}
			}
		}

		if(c!=null) {
			c.setAllVars(vars);
		}

		if(resolution==null) {
			return;
		}
		if(!asYouType) {
			if(!resolution.isResolved()) {
				Set<String> names = findVariableNames(operandToken);
				for (String name : names) {
					validationContext.addLinkedEl(name, elReference);
				}
			}
		}

		List<ELSegment> segments = resolution.getSegments();
		List<IVariable> usedVariables = new ArrayList<IVariable>();
		for (ELSegment segment : segments) {
			if(!segment.getVariables().isEmpty()) {
				usedVariables.addAll(segment.getVariables());
			}
			// Check pair for getter/setter
			if(segment instanceof JavaMemberELSegmentImpl) {
				JavaMemberELSegmentImpl javaSegment = (JavaMemberELSegmentImpl)segment;
				if(!javaSegment.getUnpairedGettersOrSetters().isEmpty()) {
					TypeInfoCollector.MethodInfo unpairedMethod = javaSegment.getUnpairedGettersOrSetters().values().iterator().next();
					String methodName = unpairedMethod.getName();
					String propertyName = javaSegment.getUnpairedGettersOrSetters().keySet().iterator().next();
					String missingMethodName = ELValidationMessages.EL_VALIDATOR_SETTER;
					String existedMethodName = ELValidationMessages.EL_VALIDATOR_GETTER;
					if(methodName.startsWith("s")) { //$NON-NLS-1$
						missingMethodName = existedMethodName;
						existedMethodName = ELValidationMessages.EL_VALIDATOR_SETTER;
					}
					int startPosition = documnetOffset + operandToken.getStartPosition();
					int length = operandToken.getLength();
					int startPr = operand.indexOf(propertyName);
					if(startPr>-1) {
						startPosition = startPosition + startPr;
						length = propertyName.length();
					}

					IJavaSourceReference reference = getJavaReference(file, startPosition, length);
					IMarker marker = null;
					if(reference != null) {
						if(asYouType) {
							addMessage(file, reference, ELSeverityPreferences.UNPAIRED_GETTER_OR_SETTER, ELValidationMessages.UNPAIRED_GETTER_OR_SETTER, new String[]{propertyName, existedMethodName, missingMethodName});
						} else {
							marker = addError(ELValidationMessages.UNPAIRED_GETTER_OR_SETTER, ELSeverityPreferences.UNPAIRED_GETTER_OR_SETTER, new String[]{propertyName, existedMethodName, missingMethodName}, reference, file);
							elReference.addMarker(marker);
						}
					} else {
						if(asYouType) {
							addMessage(file, startPosition, length, ELSeverityPreferences.UNPAIRED_GETTER_OR_SETTER, ELValidationMessages.UNPAIRED_GETTER_OR_SETTER, new String[]{propertyName, existedMethodName, missingMethodName});
						} else {
							marker = addError(ELValidationMessages.UNPAIRED_GETTER_OR_SETTER, ELSeverityPreferences.UNPAIRED_GETTER_OR_SETTER, new String[]{propertyName, existedMethodName, missingMethodName}, elReference.getLineNumber(), length, startPosition, file);
							elReference.addMarker(marker);
						}
					}
					if(marker!=null) {
						markers++;
					}
				}
			}
		}
		if(!asYouType) {
			// Save links between resource and used variables names
			for(IVariable variable: usedVariables) {
				validationContext.addLinkedEl(variable.getName(), elReference);
			}
		}

		if (resolution.isResolved() || !resolution.isValidatable()) {
			// It's valid EL or we should ignore it.
			return;
		}

		ELSegment segment = resolution.getUnresolvedSegment();
		if(segment==null) {
			return;
		}
		LexicalToken token = segment.getToken();
		if(token==null) {
			WebKbPlugin.getDefault().logError("The token from unresolved segment is null. EL: [" + operand + "]");
		}
		varName = token.getText();
		if(varName == null) {
			//This is syntax error case. Reported by parser.
			return;						
		}
		offsetOfVarName = documnetOffset + token.getStart();
		lengthOfVarName = varName == null ? 0 : varName.length();
		if(usedVariables.isEmpty()) {
			unresolvedTokenIsVariable = true;
		}
		IJavaSourceReference javaReference = getJavaReference(file, offsetOfVarName, lengthOfVarName);

		IMarker marker = null;
		// Mark invalid EL

		String message = ELValidationMessages.UNKNOWN_EL_VARIABLE_PROPERTY_NAME;
		String preference = ELSeverityPreferences.UNKNOWN_EL_VARIABLE_PROPERTY_NAME;
		if(unresolvedTokenIsVariable) {
			message = ELValidationMessages.UNKNOWN_EL_VARIABLE_NAME;
			preference = ELSeverityPreferences.UNKNOWN_EL_VARIABLE_NAME;
		}
		if(javaReference == null) {
			if(asYouType) {
				addMessage(file, offsetOfVarName, lengthOfVarName, preference, message, new String[]{varName});
			} else {
				marker = addError(message, preference, new String[]{varName}, elReference.getLineNumber(), lengthOfVarName, offsetOfVarName, file);
			}
		} else {
			if(asYouType) {
				addMessage(file, javaReference, preference, message, new String[]{varName});
			} else {
				marker = addError(message, preference, new String[]{varName}, javaReference, file);
			}
		}

		if(marker != null) {
			if(!asYouType) {
				elReference.addMarker(marker);
			}
			markers++;
		}
	}

	IJavaSourceReference getJavaReference(final IFile file, final int startPosition, final int length) {
		IJavaElement el = null;
		
		if(file.getName().endsWith(".java")) {
			ICompilationUnit u = EclipseUtil.getCompilationUnit(file);
			if(u != null) {
				try {
					el = u.getElementAt(startPosition);
				} catch (CoreException e) {
					WebKbPlugin.getDefault().logError(e);
				}
			}
		}

		if(el instanceof IMember) {
			final IMember member = (IMember)el;
			return new IJavaSourceReference() {
				public int getStartPosition() {
					return startPosition;
				}
				public int getLength() {
					return length;
				}
				public IResource getResource() {
					return file;
				}
				public IMember getSourceMember() {
					return member;
				}
				public IJavaElement getSourceElement() {
					return member;
				}				
			};
		}
		return null;
	}

	private Set<String> findVariableNames(ELInvocationExpression invocationExpression){
		Set<String> names = new HashSet<String>();
		while(invocationExpression != null) {
			if(invocationExpression instanceof ELPropertyInvocation) {
				String name = ((ELPropertyInvocation)invocationExpression).getQualifiedName();
				if(name != null) {
					names.add(name);
				}
			}
			invocationExpression = invocationExpression.getLeft();
		}
		return names;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.validation.ValidationErrorManager#getPreference(org.eclipse.core.resources.IProject, java.lang.String)
	 */
	@Override
	protected String getPreference(IProject project, String preferenceKey) {
		return ELSeverityPreferences.getInstance().getProjectPreference(project, preferenceKey);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidator#getId()
	 */
	public String getId() {
		return ID;
	}

	public String getBuilderId() {
		return KbBuilder.BUILDER_ID;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidator#getValidatingProjects(org.eclipse.core.resources.IProject)
	 */
	public IValidatingProjectTree getValidatingProjects(IProject project) {
		int max = 0;
		IValidatingProjectTree result = null;
		for (IELValidationDelegate delegate : DELEGATES) {
			if(delegate.shouldValidate(project)) {
				IValidatingProjectTree tree = delegate.getValidatingProjects(project);
				if(tree.getAllProjects().size()>max) {
					max = tree.getAllProjects().size();
					result = tree;
				}
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.jst.web.kb.validation.IValidator#shouldValidate(org.eclipse
	 * .core.resources.IProject)
	 */
	public boolean shouldValidate(IProject project) {
		return shouldValidate(project, false);
	}

	@Override
	public boolean shouldValidateAsYouType(IProject project) {
		return shouldValidate(project, true);
	}

	public boolean shouldValidate(IProject project, boolean asYouType) {
		boolean result = false;
		try {
			if(project.isAccessible() && isEnabled(project) && (asYouType || validateBuilderOrder(project))) {
				for (IELValidationDelegate delegate : DELEGATES) {
					if(delegate.shouldValidate(project)) {
						result = true;
					}
				}
			}
		} catch (CoreException e) {
			WebKbPlugin.getDefault().logError(e);
		}
		return result;
	}

	private boolean validateBuilderOrder(IProject project) throws CoreException {
		return KBValidator.validateBuilderOrder(project, getBuilderId(), getId(), ELSeverityPreferences.getInstance());
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.validation.WebValidator#shouldValidateJavaSources()
	 */
	@Override
	protected boolean shouldValidateJavaSources() {
		return true;
	}

	private static final String BUNDLE_NAME = "org.jboss.tools.jst.web.kb.internal.validation.messages";

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.validation.TempMarkerManager#getMessageBundleName()
	 */
	@Override
	protected String getMessageBundleName() {
		return BUNDLE_NAME;
	}

	@Override
	public void registerPreferenceInfo() {
		PreferenceInfoManager.register(getProblemType(), new ELPreferenceInfo());
	}
	
	class ELPreferenceInfo implements IPreferenceInfo{

		@Override
		public String getPreferencePageId() {
			return PREFERENCE_PAGE_ID;
		}

		@Override
		public String getPropertyPageId() {
			return PROPERTY_PAGE_ID;
		}

		@Override
		public String getPluginId() {
			return WebKbPlugin.PLUGIN_ID;
		}
		
	}
}