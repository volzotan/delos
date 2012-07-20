<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<s:hidden name="dokument.id" />
<s:textfield key="dokument.name" size="128" required="true" cssClass="span10" />
<s:select
	headerKey="-1" headerValue="%{getText('select.bitteauswaehlen')}"
	list="#{'0':getText('dokument.standard.0'), '1':getText('dokument.standard.1'), '2':getText('dokument.standard.2'), '3':getText('dokument.standard.3')}" 
	key="dokument.standard"
	required="true" 
	cssClass="span10"
/>
<s:textarea name="dokument.beschreibung" rows="15" required="true" cssClass="span10" />