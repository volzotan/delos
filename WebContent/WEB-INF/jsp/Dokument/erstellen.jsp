<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<s:set var="activeTab" value="null" />

<jsp:include page="head.jsp" flush="false" />

<s:form action="%{formularZiel}" method="post" acceptcharset="utf-8" theme="bootstrap" cssClass="form-horizontal">
	<jsp:include page="form.jsp" flush="false" />
	<hr />
	<div class="btn-toolbar floatable">
		<div class="btn-group">
			<a href="javascript:history.back()" class="btn"><s:text name="button.zurueck" /></a>
		</div>
		<div class="btn-group">
			<s:submit value="%{getText('button.speichern')}" cssClass="btn btn-primary" />
		</div>
	</div>
</s:form>