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
	<s:if test="#session.angemeldeterBenutzer.gruppeId == 3">
		<div class="btn-group">
			<a class="btn" href="<s:url action="Ausschreibung_erstellen"></s:url>"><s:text name="button.ausschreibungErstellen" /></a>
		</div>
	</s:if>
	<s:if test="#session.angemeldeterBenutzer.gruppeId == 4 && #activeFilter == null">
		<s:if test="#emptyList">
			<div class="btn-group">								
				<button class="btn" disabled="disabled"><s:text name="Ausschreibung.sonstiges.exportieren" /></button>							
			</div>
		</s:if>
		<s:else>
			<div class="btn-group">								
				<a href="<s:url action="BewerbungsVorgang_exportieren" />" class="btn"><s:text name="Ausschreibung.sonstiges.exportieren" /></a>							
			</div>
		</s:else>
	</s:if>
	<div class="btn-group">
		<s:if test="#activeFilter == null && #emptyList">
			<button class="btn" disabled="disabled"><s:text name="Ausschreibung.navigation.filter"><s:param value="%{getText('Ausschreibung.navigation.filter.deaktiviert')}" /></s:text></button>
			<button class="btn dropdown-toggle" disabled="disabled"><span class="caret"></span></button>
		</s:if>
		<s:else>
			<button class="btn dropdown-label" disabled="disabled"><s:text name="Ausschreibung.navigation.filter"><s:param value="%{#activeFilter == null ? getText('Ausschreibung.navigation.filter.deaktiviert') : getText('Ausschreibung.navigation.bewerbungen.' + #activeFilter)}" /></s:text></button>
			<button class="btn dropdown-toggle" data-toggle="dropdown"><span class="caret"></span></button>
			<ul class="dropdown-menu">
				<s:if test="#activeFilter != null">
					<li><a href="<s:url action="Bewerbungen_index" />"><s:text name="Ausschreibung.navigation.filter.deaktiviert" /></a></li>
				</s:if>
				<s:if test="#session.angemeldeterBenutzer.gruppeId == 4 && #activeFilter != 'institut'">
					<li><a href="<s:url action="Bewerbungen_institut" />"><s:text name="Ausschreibung.navigation.bewerbungen.institut" /></a></li>
				</s:if>
				<li class="divider"></li>
				<s:if test="#session.angemeldeterBenutzer.gruppeId in {2, 3} && #activeFilter != 'neu'">
					<li><a href="<s:url action="Bewerbungen_neu" />"><s:text name="Ausschreibung.navigation.bewerbungen.neu" /></a></li>
				</s:if>
				<s:if test="#activeFilter != 'bearbeitung'">
					<li><a href="<s:url action="Bewerbungen_aktiv" />"><s:text name="Ausschreibung.navigation.bewerbungen.bearbeitung" /></a></li>
				</s:if>
				<s:if test="#activeFilter != 'abgeschlossen'">
					<li><a href="<s:url action="Bewerbungen_abgeschlossen" />"><s:text name="Ausschreibung.navigation.bewerbungen.abgeschlossen" /></a></li>
				</s:if>
				<s:if test="#activeFilter != 'zurueckgezogen'">
					<li><a href="<s:url action="Bewerbungen_zurueckgezogen" />"><s:text name="Ausschreibung.navigation.bewerbungen.zurueckgezogen" /></a></li>
				</s:if>
				<s:if test="#activeFilter != 'abgelehnt'">
					<li><a href="<s:url action="Bewerbungen_abgelehnt" />"><s:text name="Ausschreibung.navigation.bewerbungen.abgelehnt" /></a></li>
				</s:if>
			</ul>
		</s:else>
	</div>
	<!-- Paginator -->
	<jsp:include page="../Template/paginator.jsp" flush="false" />
</div>