<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<div class="btn-toolbar head floatable">
	<div class="btn-group">
		<a class="btn" href="<s:url includeParams="get"></s:url>" title="<s:text name="button.aktualisieren" />">&nbsp;<i class="icon-refresh"></i>&nbsp;</a>
	</div>
	<div class="btn-group">
		<a class="btn" href="<s:url action="Benutzer_registrieren"></s:url>"><s:text name="Benutzer.index.button.neuesKonto" /></a>
	</div>
	<div class="btn-group">
		<div class="input-append">
			<form action="/delos/Benutzer_suche.action" accept-charset="utf-8" method="get" name="benutzer-suche">
				<input name="suche" class="span2 hlResult" size="16" type="text" value="<s:property value="suche" />" /><button class="btn" type="submit"><s:text name="%{#activeTab == 'suche' ? 'button.erneutSuchen' : 'button.suchen'}" /></button>
			</form>
		</div>
	</div>
	<!-- Paginierung -->
	<jsp:include page="../Template/paginator.jsp" flush="false" />
</div>