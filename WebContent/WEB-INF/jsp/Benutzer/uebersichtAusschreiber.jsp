<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<s:set var="activeTab" value="'ausschreiber'" />

<jsp:include page="head.jsp" flush="false" />
<jsp:include page="toolbar.jsp" flush="false" />

<s:if test="#emptyList">
	<div class="alert alert-info centerTxt"><s:text name="Benutzer.index.liste.ausschreiber.leer" /></div>
</s:if>
<s:else>
	<table class="table table-striped table-condensed">
		<thead>
			<tr>
				<th>
					<a href="<s:url includeParams="get"><s:param name="spalte" value="'name'" /><s:param name="richtung" value="%{#spalte == null || #richtung == 'asc' && #spalte == 'name' ? 'desc' : 'asc'}" /></s:url>"><s:text name="benutzer.name" /><s:if test="#spalte == null || #spalte.equals('name')">&nbsp;<b class="caret <s:if test="#richtung == null || #richtung.equals('asc')">asc</s:if>"></b></s:if></a>
				</th >
				<th>
					<a href="<s:url includeParams="get"><s:param name="spalte" value="'email'" /><s:param name="richtung" value="%{#richtung == 'asc' && #spalte == 'email' ? 'desc' : 'asc'}" /></s:url>"><s:text name="benutzer.email" /><s:if test="#spalte.equals('email')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a>
				</th>
				<th>
					<s:text name="benutzer.vertreterName" />
				</th>
				<th>
					<a href="<s:url includeParams="get"><s:param name="spalte" value="'hin'" /><s:param name="richtung" value="%{#richtung == 'asc' && #spalte == 'hin' ? 'desc' : 'asc'}" /></s:url>"><s:text name="benutzer.hinzugefuegt" /><s:if test="#spalte.equals('hin')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a>
				</th>
				<th>
					<a href="<s:url includeParams="get"><s:param name="spalte" value="'mod'" /><s:param name="richtung" value="%{#richtung == 'asc' && #spalte == 'mod' ? 'desc' : 'asc'}" /></s:url>"><s:text name="benutzer.bearbeitet" /><s:if test="#spalte.equals('mod')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a>
				</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="listeDaten" status="status">
			
				<tr class="hyperlink" data-toggle="<s:url action="Benutzer_aktualisieren"><s:param name="benutzerId" value="id" /></s:url>">
					<td><a href="javascript:void()"><s:property value="name" /></a></td>		
					<td><s:property value="email" /></td>
					<s:if test="vertreterId > 0">
						<td class="filter"><a href="<s:url action="Benutzer_aktualisieren"><s:param name="benutzerId" value="vertreterId" /></s:url>"><s:property value="vertreterName" /></a></td>
					</s:if>
					<s:else>
						<td></td>
					</s:else>
					<td><s:property value="hinzugefuegt" /></td>
					<td><s:property value="bearbeitet" /></td>		
				</tr>
			
			</s:iterator>
		</tbody>
	</table>
	<jsp:include page="../Template/paginatorBottom.jsp" flush="false" />
</s:else>