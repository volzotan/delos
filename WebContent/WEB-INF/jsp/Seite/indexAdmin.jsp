<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>


<div class="row">
	<div class="span4">
		<a href="<s:url action="Benutzer_index" />" class="btn">Benutzerliste</a>
	</div>
	<div class="span4">
		<a href="<s:url action="Benutzer_registrieren" />" class="btn">Neuen Benutzer anlegen</a>
	</div>
	<div class="span4">
		<a href="<s:url action="Dokument_erstellen" />" class="btn">Neues Vertragsdokument anlegen</a>
	</div>
</div>