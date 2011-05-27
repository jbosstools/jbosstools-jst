<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
                                                          
<f:loadBundle var="Message" basename="demo.Messages"/>

<html>
 <body>
  <h1><h:outputText value="#{Message.header}"/></h1>
 </body>
</html>
