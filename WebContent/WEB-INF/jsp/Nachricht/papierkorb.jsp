<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<s:set var="activeTab" value="'papierkorb'" />

<jsp:include page="head.jsp" flush="false" />

<form id="Nachricht_pk_multi_checkbox" name="Nachricht_pk_multi_checkbox" action="/delos/Nachricht_pk_multi_checkbox.action" method="post" accept-charset="utf-8">
	<div class="btn-toolbar head floatable">
		<div class="btn-group">
			<s:if test="#emptyList">
				<button class="btn dropdown-toggle" disabled="disabled">&nbsp;<i class="icon-check"></i>&nbsp;<span class="caret"></span>&nbsp;</button>
			</s:if>
			<s:else>
				<a class="btn dropdown-toggle" data-toggle="dropdown" href="#" title="<s:text name="button.markieren"/>">&nbsp;<i class="icon-check"></i>&nbsp;<span class="caret"></span>&nbsp;</a>
				<ul class="dropdown-menu">
					<li><a href="javascript:void(0)" onclick="$(this).selectAllBoxes()"><s:text name="button.markieren.alle"/></a></li>
					<li><a href="javascript:void(0)" onclick="$(this).selectNoneBoxes()"><s:text name="button.markieren.keine"/></a></li>
					<li><a href="javascript:void(0)" onclick="$(this).selectReadBoxes()"><s:text name="button.markieren.gelesene"/></a></li>
					<li><a href="javascript:void(0)" onclick="$(this).selectUnreadBoxes()"><s:text name="button.markieren.ungelesene"/></a></li>
					<li><a href="javascript:void(0)" onclick="$(this).selectAnsweredBoxes()"><s:text name="button.markieren.beantwortete"/></a></li>
					<li><a href="javascript:void(0)" onclick="$(this).invertSelection()"><s:text name="button.markieren.invertieren"/></a></li>
				</ul>
			</s:else>
		</div>
		<div class="btn-group">
			<a class="btn " href="<s:url includeParams="get" />" title="<s:text name="button.aktualisieren"/>">&nbsp;<i class="icon-refresh"></i>&nbsp;</a>
		</div>
		<div class="btn-group">
			<s:if test="#emptyList">
				<button class="btn disabled" disabled="disabled"><s:text name="button.loeschen"/></button>
			</s:if>
			<s:else>
				<button class="btn" name="multiSubmitButton" value="0"><s:text name="button.loeschen"/></button>
			</s:else>
		</div>
		<div class="btn-group">
			<s:if test="#emptyList">
				<button class="btn disabled" disabled="disabled"><s:text name="button.wiederherstellen"/></button>
			</s:if>
			<s:else>
				<button class="btn" name="multiSubmitButton" value="1"><s:text name="button.wiederherstellen"/></button>
			</s:else>
		</div>
		<jsp:include page="../Template/paginator.jsp" flush="false" />
	</div>
	
	<s:if test="#emptyList">
		<div class="alert alert-info centerTxt">
			<s:text name="Nachricht.info.papierkorbLeer"/>
		</div>
	</s:if>
	<s:else>
		<table class="table table-striped table-condensed">
			<thead>
				<tr>
					<th></th>
					<th><s:text name="Nachricht.tabelle.kopf.von"/></th>
					<th></th>
					<th><a href="<s:url includeParams="get"><s:param name="spalte" value="'titel'" /><s:param name="richtung" value="%{#richtung == 'asc' && #spalte == 'titel' ? 'desc' : 'asc'}" /></s:url>"><s:text name="Nachricht.tabelle.kopf.betreff" /><s:if test="#spalte.equals('titel')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a></th>
					<th><a href="<s:url includeParams="get"><s:param name="spalte" value="'datum'" /><s:param name="richtung" value="%{#spalte == null || #richtung == 'desc' && #spalte == 'datum' ? 'asc' : 'desc'}" /></s:url>"><s:text name="Nachricht.tabelle.kopf.datum"/><s:if test="#spalte == null || #spalte.equals('datum')">&nbsp;<b class="caret <s:if test="#richtung.equals('asc')">asc</s:if>"></b></s:if></a></th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="listeDaten" status="status">
					
					<tr class="hyperlink<s:if test="#session.angemeldeterBenutzer.id == empfaengerId && (3 == status || 1 == status)"> status-highlight</s:if>" data-toggle="<s:url action="Nachricht_anzeigen"><s:param name="nachrichtId" value="id" /></s:url>">
						<td class="filter" width="20"><input type="checkbox" name="multiNachrichtId" value="<s:property value="id" />"></td>
						<td><s:property value="absenderName" /></td>
						<td class="option-stack"><s:if test="#session.angemeldeterBenutzer.id == empfaengerId && 2 > status"><i class="icon-share-alt"></i></s:if><i class="icon-trash icon-fade"></i></td>
						<td><a href="javascript:void()"><s:property value="%{betreff.length() > 70 ? betreff.substring(0, 70) + getText('global.text.ellipse') : betreff}" /></a></td>
						<td>
						<s:if test="%{currentDay.equals(versanddatum.toString().substring(0, 10))}"><s:text name="nachricht.versanddatum.uhrzeit"><s:param value="versanddatum" /></s:text></s:if>
						<s:else><s:text name="nachricht.versanddatum.komplett"><s:param value="versanddatum" /></s:text></s:else>
						</td>
					</tr>
				
				</s:iterator>	
			</tbody>
		</table>
		<jsp:include page="../Template/paginatorBottom.jsp" flush="false" />
	</s:else>
</form>