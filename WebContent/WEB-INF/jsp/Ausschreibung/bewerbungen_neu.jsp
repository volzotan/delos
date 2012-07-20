<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<s:set var="activeTab" value="'bewerbungen'" />
<s:set var="activeFilter" value="'neu'" />

<jsp:include page="head.jsp" flush="false" />
<jsp:include page="bewerbungen_toolbar.jsp" flush="false" />

<s:if test="#emptyList">
	<div class="alert alert-info centerTxt">
		<s:if test="#session.angemeldeterBenutzer.gruppeId == 2"><s:text name ="Ausschreibung.listen.bewerbungen.neu.bewerber" /></s:if>
		<s:elseif test="#session.angemeldeterBenutzer.gruppeId == 3"><s:text name ="Ausschreibung.listen.bewerbungen.neu.ausschreiber" /></s:elseif>
		<s:elseif test="#session.angemeldeterBenutzer.gruppeId == 4"><s:text name ="Ausschreibung.listen.bewerbungen.neu.bearbeiter" /></s:elseif>
	</div>
</s:if>
<s:else>
	<s:if test="#session.angemeldeterBenutzer.gruppeId == 2">
		<!-- UI Bewerber -->
		<table class="table table-striped table-condensed">
			<thead>
				<tr>
					<th>
						<a href="<s:url includeParams="get"><s:param name="spalte" value="'institut'" /><s:param name="richtung" value="%{#richtung == 'asc' && #spalte == 'institut' ? 'desc' : 'asc'}" /></s:url>"><s:text name="Ausschreibung.tabelle.institut" /><s:if test="#spalte.equals('institut')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a>
					</th>
					<th>
						<s:text name="bewerbungsVorgang.ausschreiberName" />
					</th>
					<th>
						<a href="<s:url includeParams="get"><s:param name="spalte" value="'name'" /><s:param name="richtung" value="%{#richtung == 'asc' && #spalte == 'name' ? 'desc' : 'asc'}" /></s:url>"><s:text name="Ausschreibung.tabelle.stelle" /><s:if test="#spalte.equals('name')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a>
					</th>
					<th>
						<a href="<s:url includeParams="get"><s:param name="spalte" value="'hin'" /><s:param name="richtung" value="%{#spalte == null || #richtung == 'desc' && #spalte == 'hin' ? 'asc' : 'desc'}" /></s:url>"><s:text name="bewerbungsVorgang.tabelle.datum" /><s:if test="#spalte == null || #spalte.equals('hin')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a>
					</th>
					<th>
						<a href="<s:url includeParams="get"><s:param name="spalte" value="'mod'" /><s:param name="richtung" value="%{#richtung == 'asc' && #spalte == 'mod' ? 'desc' : 'asc'}" /></s:url>"><s:text name="bewerbungsVorgang.bearbeitet" /><s:if test="#spalte.equals('mod')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a>
					</th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="listeDaten" status="status">
				
					<tr class="hyperlink" data-toggle="<s:url action="BewerbungsVorgang_anzeigen"><s:param name="bewerbungsVorgangId" value="id" /></s:url>">
						<td><s:property value="%{getText('Institut.' + institut).length() > 20 ? getText('Institut.' + institut).substring(0, 20) + getText('global.text.ellipse') : getText('Institut.' + institut)}" /></td>
						<td><s:property value="ausschreiberName" /></td>
						<td><s:property value="%{ausschreibungName.length() > 60 ? ausschreibungName.substring(0, 60) + getText('global.text.ellipse') : ausschreibungName}" /></td>
						<td><s:property value="hinzugefuegt" /></td>
						<td><s:property value="bearbeitet" /></td>
					</tr>
				
				</s:iterator>
			</tbody>
		</table>
		<jsp:include page="../Template/paginatorBottom.jsp" flush="false" />
	</s:if>
	<s:elseif test="#session.angemeldeterBenutzer.gruppeId == 3">
		<!-- UI Ausschreiber -->
		<table class="table table-striped table-condensed">
			<thead>
				<tr>
					<th>
						<s:text name="bewerbungsVorgang.tabelle.von" />
					</th>
					<th>
						<a href="<s:url includeParams="get"><s:param name="spalte" value="'name'" /><s:param name="richtung" value="%{#richtung == 'asc' && #spalte == 'name' ? 'desc' : 'asc'}" /></s:url>"><s:text name="Ausschreibung.tabelle.stelle" /><s:if test="#spalte.equals('name')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a>
					</th>
					<th>
						<a href="<s:url includeParams="get"><s:param name="spalte" value="'hin'" /><s:param name="richtung" value="%{#spalte == null || #richtung == 'desc' && #spalte == 'hin' ? 'asc' : 'desc'}" /></s:url>"><s:text name="bewerbungsVorgang.tabelle.datum" /><s:if test="#spalte == null || #spalte.equals('hin')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a>
					</th>
					<th>
						<a href="<s:url includeParams="get"><s:param name="spalte" value="'mod'" /><s:param name="richtung" value="%{#richtung == 'asc' && #spalte == 'mod' ? 'desc' : 'asc'}" /></s:url>"><s:text name="bewerbungsVorgang.bearbeitet" /><s:if test="#spalte.equals('mod')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a>
					</th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="listeDaten" status="status">
				
					<tr class="hyperlink" data-toggle="<s:url action="BewerbungsVorgang_anzeigen"><s:param name="bewerbungsVorgangId" value="id" /></s:url>">
						<td><s:property value="bewerberName" /></td>
						<td><s:property value="%{ausschreibungName.length() > 60 ? ausschreibungName.substring(0, 60) + getText('global.text.ellipse') : ausschreibungName}" /></td>
						<td><s:property value="hinzugefuegt" /></td>
						<td><s:property value="bearbeitet" /></td>
					</tr>
				
				</s:iterator>
			</tbody>
		</table>
		<jsp:include page="../Template/paginatorBottom.jsp" flush="false" />
	</s:elseif>
	<s:elseif test="#session.angemeldeterBenutzer.gruppeId == 4">
		<!-- UI Bearbeiter -->
		<table class="table table-striped table-condensed">
			<thead>
				<tr>
					<th>
						<s:text name="bewerbungsVorgang.tabelle.von" />
					</th>
					<th>
						<s:text name="bewerbungsVorgang.tabelle.an" />
					</th>
					<th>
						<a href="<s:url includeParams="get"><s:param name="spalte" value="'name'" /><s:param name="richtung" value="%{#richtung == 'asc' && #spalte == 'name' ? 'desc' : 'asc'}" /></s:url>"><s:text name="Ausschreibung.tabelle.stelle" /><s:if test="#spalte.equals('name')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a>
					</th>
					<th>
						<a href="<s:url includeParams="get"><s:param name="spalte" value="'hin'" /><s:param name="richtung" value="%{#spalte == null || #richtung == 'desc' && #spalte == 'hin' ? 'asc' : 'desc'}" /></s:url>"><s:text name="bewerbungsVorgang.tabelle.datum" /><s:if test="#spalte == null || #spalte.equals('hin')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a>
					</th>
					<th>
						<a href="<s:url includeParams="get"><s:param name="spalte" value="'mod'" /><s:param name="richtung" value="%{#richtung == 'asc' && #spalte == 'mod' ? 'desc' : 'asc'}" /></s:url>"><s:text name="bewerbungsVorgang.bearbeitet" /><s:if test="#spalte.equals('mod')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a>
					</th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="listeDaten" status="status">
				
					<tr class="hyperlink" data-toggle="<s:url action="BewerbungsVorgang_anzeigen"><s:param name="bewerbungsVorgangId" value="id" /></s:url>">
						<td><s:property value="bewerberName" /></td>
						<td><s:property value="ausschreiberName" /></td>
						<td><s:property value="%{ausschreibungName.length() > 60 ? ausschreibungName.substring(0, 60) + getText('global.text.ellipse') : ausschreibungName}" /></td>
						<td><s:property value="hinzugefuegt" /></td>
						<td><s:property value="bearbeitet" /></td>
					</tr>
				
				</s:iterator>
			</tbody>
		</table>
		<jsp:include page="../Template/paginatorBottom.jsp" flush="false" />
	</s:elseif>
</s:else>