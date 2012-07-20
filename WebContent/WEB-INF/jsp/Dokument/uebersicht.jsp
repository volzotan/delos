<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<s:set var="activeTab" value="'uebersicht'" />

<jsp:include page="head.jsp" flush="false" />

<s:form action="Dokument_multi_checkbox" method="post" acceptcharset="utf-8">
	<div class="btn-toolbar head floatable">
		<s:if test="%{#session.angemeldeterBenutzer.gruppeId == 1}">
			<div class="btn-group">
				<s:if test="#emptyList">
					<button class="btn dropdown-toggle" disabled="disabled">&nbsp;<i class="icon-check"></i>&nbsp;<span class="caret"></span>&nbsp;</button>
				</s:if>
				<s:else>
					<a class="btn dropdown-toggle" data-toggle="dropdown" href="#" title="<s:text name="button.markieren"/>">&nbsp;<i class="icon-check"></i>&nbsp;<span class="caret"></span>&nbsp;</a>
					<ul class="dropdown-menu">
						<li><a href="javascript:void(0)" onclick="$(this).selectAllBoxes()"><s:text name="button.markieren.alle"/></a></li>
						<li><a href="javascript:void(0)" onclick="$(this).selectNoneBoxes()"><s:text name="button.markieren.keine"/></a></li>
						<li><a href="javascript:void(0)" onclick="$(this).invertSelection()"><s:text name="button.markieren.invertieren"/></a></li>
					</ul>
				</s:else>
			</div>
		</s:if>
		<div class="btn-group">
			<a class="btn " href="<s:url includeParams="get" />" title="<s:text name="button.aktualisieren"/>">&nbsp;<i class="icon-refresh"></i>&nbsp;</a>
		</div>
		<s:if test="%{#session.angemeldeterBenutzer.gruppeId == 1}">
			<div class="btn-group">
				<s:if test="#emptyList">
					<button class="btn" disabled="disabled"><s:text name="button.loeschen"/></button>
				</s:if>
				<s:else>
					<button class="btn" name="multiSubmitButton" value="0"><s:text name="button.loeschen"/></button>
				</s:else>
			</div>
			<div class="btn-group">
				<a class="btn" href="<s:url action="Dokument_erstellen" />"><s:text name="Dokument.index.neuesDokument"/></a>
			</div>
		</s:if>
		<!-- Paginierung -->
		<jsp:include page="../Template/paginator.jsp" flush="false" />
	</div>
	
	<s:if test="#emptyList">
		<div class="alert alert-info centerTxt">
			<s:text name="Dokument.uebersicht.leer"/>
		</div>
	</s:if>
	<s:else>
		<table class="table table-striped table-condensed">
			<thead>
				<tr>
					<s:if test="%{#session.angemeldeterBenutzer.gruppeId == 1}">
						<th></th>
					</s:if>
					<th><a href="<s:url includeParams="get"><s:param name="spalte" value="'name'" /><s:param name="richtung" value="%{#spalte == null || #richtung == 'asc' && #spalte == 'name' ? 'desc' : 'asc'}" /></s:url>"><s:text name="dokument.name" /><s:if test="#spalte == null || #spalte.equals('name')">&nbsp;<b class="caret <s:if test="#richtung == null || #richtung.equals('asc')">asc</s:if>"></b></s:if></a></th>
					<th><a href="<s:url includeParams="get"><s:param name="spalte" value="'standard'" /><s:param name="richtung" value="%{#richtung == 'asc' && #spalte == 'standard' ? 'desc' : 'asc'}" /></s:url>"><s:text name="dokument.standard" /><s:if test="#spalte.equals('standard')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a></th>					
				</tr>
			</thead>
			<tbody>
				<s:iterator value="listeDaten" status="status">
				
				<tr class="hyperlink" data-toggle="<s:url action="Dokument_anzeigen"><s:param name="dokumentId" value="id" /></s:url>">
					<s:if test="%{#session.angemeldeterBenutzer.gruppeId == 1}">
						<td class="filter" width="20"><input type="checkbox" name="multiDokumentId" value="<s:property value="id" />"></td>
					</s:if>
					<td><a href="javascript:void()"><s:text name="name" /></a></td>
					<td><s:text name="%{'dokument.standard.'+standard+'.kurz'}" /></td>		
				</tr>
				
				</s:iterator>
			</tbody>
		</table>
		<jsp:include page="../Template/paginatorBottom.jsp" flush="false" />
	</s:else>
</s:form>