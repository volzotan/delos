<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<s:set var="activeTab" value="'null'" />

<jsp:include page="head.jsp" flush="false" />

<s:form action="%{formularZiel}" method="post" acceptcharset="utf-8" theme="bootstrap" cssClass="form-horizontal">
    <jsp:include page="form.jsp" flush="false" />
    <hr />
	<div class="btn-toolbar floatable">
		<div class="btn-group">
			<a href="javascript:history.back()" class="btn"><s:text name="button.zurueck" /></a>
		</div>
		<div class="btn-group">
			<s:submit value="%{getText('button.aenderungenSpeichern')}" cssClass="btn btn-primary" />
		</div>
		<div class="btn-group pull-right">
			<a class="btn btn-danger" href="#dokumentLoeschen" data-toggle="modal"><s:text name="button.loeschen" /></a>
		</div>
	</div>
</s:form>

<div class="modal" id="dokumentLoeschen" style="display:none">
	<div class="modal-header">
		<h3><s:text name="Dokument.loeschen.titel" /></h3>
	</div>
	<div class="modal-body">
		<s:text name="Dokument.loeschen.text"><s:param value="dokument.name" /></s:text>
	</div>
	<div class="modal-footer">
		<a href="#" class="btn" data-dismiss="modal"><s:text name="button.abbrechen" /></a>
		<a href="<s:url action="Dokument_loeschen"><s:param name="dokumentId" value="dokument.id" /></s:url>" class="btn btn-danger"><s:text name="button.loeschen" /></a>
	</div>
</div>