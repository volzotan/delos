<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<s:form action="%{formularZiel}" method="post" acceptcharset="utf-8" theme="bootstrap">
	<s:hidden name="nachricht.id" value="%{nachricht.id}"/>
	<s:hidden name="nachricht.empfaengerId"/>
	<s:hidden name="nachricht.absenderId"/>
	<s:hidden name="nachricht.status"/>
	
    <s:textfield key="nachricht.empfaengerName" disabled="true" tabindex="1" cssClass="span12"/>
    <s:textfield key="nachricht.betreff" maxlength="128" tabindex="2" cssClass="span12"/>
    <s:textarea name="nachricht.text" escape="false" rows="18" tabindex="3" cssClass="span12"/>
    
    <div class="btn-toolbar floatable">
		<div class="btn-group">
			<s:submit value="%{getText('button.abschicken')}" tabindex="4" cssClass="btn btn-primary" />
		</div>
		<div class="btn-group">
			<a href="javascript:history.back()" tabindex="5" class="btn"><s:text name="button.abbrechen" /></a>
		</div>
	</div>
</s:form>