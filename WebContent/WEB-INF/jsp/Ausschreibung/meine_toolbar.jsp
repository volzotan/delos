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
		<a class="btn" href="<s:url action="Ausschreibung_erstellen"></s:url>"><s:text name="button.ausschreibungErstellen" /></a>
	</div>
	<div class="btn-group">
		<s:if test="#activeFilter == null && #emptyList">
			<button class="btn" disabled="disabled"><s:text name="Ausschreibung.navigation.filter"><s:param value="%{getText('Ausschreibung.navigation.filter.deaktiviert')}" /></s:text></button>
			<button class="btn dropdown-toggle" disabled="disabled"><span class="caret"></span></button>
		</s:if>
		<s:else>
			<button class="btn dropdown-label" disabled="disabled"><s:text name="Ausschreibung.navigation.filter"><s:param value="%{#activeFilter == null ? getText('Ausschreibung.navigation.filter.deaktiviert') : getText('Ausschreibung.navigation.meine.' + #activeFilter)}" /></s:text></button>
			<button class="btn dropdown-toggle" data-toggle="dropdown"><span class="caret"></span></button>
			<ul class="dropdown-menu">
				<s:if test="#activeFilter != null">
					<li><a href="<s:url action="Ausschreibung_meine" />"><s:text name="Ausschreibung.navigation.filter.deaktiviert" /></a></li>
				</s:if>
				<s:if test="#activeFilter != 'aktiv'">
					<li><a href="<s:url action="Ausschreibung_meine_aktiv" />"><s:text name="Ausschreibung.navigation.meine.aktiv" /></a></li>
				</s:if>
				<s:if test="#activeFilter != 'beendet'">
					<li><a href="<s:url action="Ausschreibung_meine_beendet" />"><s:text name="Ausschreibung.navigation.meine.beendet" /></a></li>
				</s:if>
			</ul>
		</s:else>
	</div>
	<!-- Paginator -->
	<jsp:include page="../Template/paginator.jsp" flush="false" />
</div>