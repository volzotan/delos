<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<hr />

<div class="btn-toolbar head floatable">
	<jsp:include page="../Template/paginator.jsp" flush="false" />
</div>

<table class="table table-striped table-condensed">
	<thead>
		<tr>
			<th>
				<a href="<s:url includeParams="get"><s:param name="spalte" value="'institut'" /><s:param name="richtung" value="%{#richtung == 'asc' && #spalte == 'institut' ? 'desc' : 'asc'}" /></s:url>"><s:text name="Ausschreibung.tabelle.institut" /><s:if test="#spalte.equals('institut')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a>
			</th>
			<th>
				<a href="<s:url includeParams="get"><s:param name="spalte" value="'name'" /><s:param name="richtung" value="%{#richtung == 'asc' && #spalte == 'name' ? 'desc' : 'asc'}" /></s:url>"><s:text name="Ausschreibung.tabelle.stelle" /><s:if test="#spalte.equals('name')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a>
			</th>
			<th></th>
			<th class="text-right">
				<a href="<s:url includeParams="get"><s:param name="spalte" value="'stunden'" /><s:param name="richtung" value="%{#richtung == 'asc' && #spalte == 'stunden' ? 'desc' : 'asc'}" /></s:url>"><s:text name="Ausschreibung.tabelle.stunden" /><s:if test="#spalte.equals('stunden')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a>
			</th>
			<th>
				<a href="<s:url includeParams="get"><s:param name="spalte" value="'beginn'" /><s:param name="richtung" value="%{#richtung == 'asc' && #spalte == 'beginn' ? 'desc' : 'asc'}" /></s:url>"><s:text name="Ausschreibung.tabelle.beginn" /><s:if test="#spalte.equals('beginn')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a>
			</th>
			<th>
				<a href="<s:url includeParams="get"><s:param name="spalte" value="'ende'" /><s:param name="richtung" value="%{#richtung == 'asc' && #spalte == 'ende' ? 'desc' : 'asc'}" /></s:url>"><s:text name="Ausschreibung.tabelle.ende" /><s:if test="#spalte.equals('ende')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a>
			</th>
			<th>
				<a href="<s:url includeParams="get"><s:param name="spalte" value="'frist'" /><s:param name="richtung" value="%{#richtung == 'asc' && #spalte == 'frist' ? 'desc' : 'asc'}" /></s:url>"><s:text name="Ausschreibung.tabelle.bewerbungsfrist" /><s:if test="#spalte.equals('frist')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a>
			</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="listeDaten" status="status">
		
			<tr class="hyperlink trigger-tooltip" data-toggle="<s:url action="Ausschreibung_anzeigen"><s:param name="ausschreibungId" value="id" /></s:url>">
				<td title="<s:property value="%{getText('Institut.' + institut)}" />"><s:property value="%{getText('Institut.' + institut).length() > 20 ? getText('Institut.' + institut).substring(0, 20) + getText('global.text.ellipse') : getText('Institut.' + institut)}" /></td>
				<td class="hlTarget"><a href="javascript:void()"><s:property value="%{name.length() > 60 ? name.substring(0, 60) + getText('global.text.ellipse') : name}" /></a></td>
				<td><span class="show-tooltip square<s:property value="%{frei > 0 ? ' green' : ' red'}" />" title="<s:text name="Ausschreibung.tabelle.plaetze.tooltip"><s:param value="frei" /><s:param value="stellenzahl" /></s:text>"></span></td>
				<td class="text-right"><s:property value="stundenzahl" /></td>
				<td><s:property value="startet" /></td>
				<td><s:property value="endet" /></td>
				<td><s:property value="bewerbungsfrist" /></td>		
			</tr>	
			
		</s:iterator>
	</tbody>
</table>
<jsp:include page="../Template/paginatorBottom.jsp" flush="false" />