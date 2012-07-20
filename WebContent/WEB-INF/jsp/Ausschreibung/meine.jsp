<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<s:set var="activeTab" value="'meine'" />
<s:set var="activeFilter" value="null" />

<jsp:include page="head.jsp" flush="false" />
<jsp:include page="meine_toolbar.jsp" flush="false" />

<s:if test="#emptyList">
	<div class="alert alert-info centerTxt"><s:text name="Ausschreibung.listen.meineStellen" /></div>
</s:if>
<s:else>
	<table class="table table-striped table-condensed">
		<thead>
			<tr>
				<th>
					<a href="<s:url includeParams="get"><s:param name="spalte" value="'name'" /><s:param name="richtung" value="%{#richtung == 'asc' && #spalte == 'name' ? 'desc' : 'asc'}" /></s:url>"><s:text name="Ausschreibung.tabelle.stelle" /><s:if test="#spalte.equals('name')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a>
				</th>
				<th class="text-right">
					<s:text name="Ausschreibung.tabelle.belegung" />
				</th>
				<th>
					<a href="<s:url includeParams="get"><s:param name="spalte" value="'status'" /><s:param name="richtung" value="%{#richtung == 'asc' && #spalte == 'status' ? 'desc' : 'asc'}" /></s:url>"><s:text name="Ausschreibung.tabelle.status" /><s:if test="#spalte.equals('status')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a>
				</th>
				<th>
					<a href="<s:url includeParams="get"><s:param name="spalte" value="'hin'" /><s:param name="richtung" value="%{#spalte == null || #richtung == 'desc' && #spalte == 'hin' ? 'asc' : 'desc'}" /></s:url>"><s:text name="Ausschreibung.tabelle.hinzugefuegt" /><s:if test="#spalte == null || #spalte.equals('hin')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a>
				</th>
				<th>
					<a href="<s:url includeParams="get"><s:param name="spalte" value="'beginn'" /><s:param name="richtung" value="%{#richtung == 'asc' && #spalte == 'beginn' ? 'desc' : 'asc'}" /></s:url>"><s:text name="Ausschreibung.tabelle.beginn" /><s:if test="#spalte.equals('beginn')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a>
				</th>
				<th>
					<a href="<s:url includeParams="get"><s:param name="spalte" value="'ende'" /><s:param name="richtung" value="%{#richtung == 'asc' && #spalte == 'ende' ? 'desc' : 'asc'}" /></s:url>"><s:text name="Ausschreibung.tabelle.ende" /><s:if test="#spalte.equals('ende')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a>
				</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="listeDaten" status="status">
			
				<tr class="hyperlink" data-toggle="<s:url action="Ausschreibung_anzeigen"><s:param name="ausschreibungId" value="id" /></s:url>">
					<td><a href="javascript:void()"><s:property value="%{name.length() > 70 ? name.substring(0, 70) + getText('global.text.ellipse') : name}" /></a></td>
					<td class="text-right"><span class="label label-<s:property value="%{belegt <= 0 ? 'important' : (frei == 0 ? 'success' : 'warning')}" />"><s:text name="Ausschreibung.tabelle.belegung.anzeige"><s:param value="belegt" /><s:param value="stellenzahl" /></s:text></span></td>
					<td><s:property value="%{getText('ausschreibung.status.' + status)}" /></td>
					<td><s:property value="hinzugefuegt" /></td>
					<td><s:property value="startet" /></td>
					<td><s:property value="endet" /></td>
				</tr>	
				
			</s:iterator>
		</tbody>
	</table>
	<jsp:include page="../Template/paginatorBottom.jsp" flush="false" />
</s:else>		