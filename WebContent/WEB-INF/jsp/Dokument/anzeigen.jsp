<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<s:set var="activeTab" value="'null'" />

<jsp:include page="head.jsp" flush="false" />

<h1><s:property value="dokument.name" /></h1>
<hr />
<h3><s:text name="Dokument.anzeigen.beschreibung"/></h3>
<div class="row">
	<div class="span12">
		<div class="well">
			<s:property value="dokument.beschreibung" escape="false" />
		</div>
		<h3><small><s:text name="%{'dokument.standard.'+dokument.standard}" /></small></h3>
		<hr />
		<div class="btn-toolbar floatable">
			<div class="btn-group">
				<a href="javascript:history.back()" class="btn"><s:text name="button.zurueck" /></a>
			</div>
			<s:if test="#session.angemeldeterBenutzer.gruppeId == 1">
				<div class="btn-group">
					<a class="btn btn-primary" href="<s:url action="Dokument_aktualisieren"><s:param name="dokumentId" value="dokument.id" /></s:url>"><s:text name="button.bearbeiten" /></a>
				</div>		
			</s:if>
		</div>
	</div>
</div>