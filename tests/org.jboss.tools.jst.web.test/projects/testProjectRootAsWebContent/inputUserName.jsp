<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<f:loadBundle var="Message" basename="demo.Messages"/>

<html>
    <head>
    	<title>Input User Name Page</title>
    </head>
    <body>

	<f:view>
		<h1><h:outputText value="#{Message.header}" id="q"/></h1>

		<h:messages style="color: red"/>

    	<h:form id="greetingForm">
    		<h:outputText value="#{Message.header}"/>
    		<h:inputText value="#{user.name}" required="true">
	    		<f:validateLength maximum="30" minimum="3"/>
    		</h:inputText>

		 	<h:commandButton action="hello" value="Say Hello!" />

    	</h:form>
	</f:view>
    </body>
</html>