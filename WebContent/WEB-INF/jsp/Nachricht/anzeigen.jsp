<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<s:if test="nachrichtTyp == 0"><s:set var="activeTab" value="'posteingang'" /></s:if>
<s:elseif test="nachrichtTyp == 1"><s:set var="activeTab" value="'postausgang'" /></s:elseif>
<s:else><s:set var="activeTab" value="'papierkorb'" /></s:else>

<jsp:include page="head.jsp" flush="false" />

<div class="btn-toolbar head floatable">
	<div class="btn-group">
		<a class="btn" href="<s:url action="%{'Nachricht_' + #activeTab}" />" title="<s:text name="button.zurueck"/>">&nbsp;<i class="icon-arrow-left"></i>&nbsp;</a>
	</div>
	<div class="btn-group">
		<a class="btn" href="javascript:window.print()" title="<s:text name="Nachricht.button.drucken"/>">&nbsp;<i class="icon-print"></i>&nbsp;</a>
	</div>
	<div class="btn-group">
		<a class="btn" href="<s:if test="nachrichtTyp == 0"><s:url action="Nachricht_pe_loeschen"><s:param name="nachrichtId" value="nachricht.id" /></s:url></s:if><s:elseif test="nachrichtTyp == 1"><s:url action="Nachricht_pa_loeschen"><s:param name="nachrichtId" value="nachricht.nachrichtId" /></s:url></s:elseif><s:else><s:url action="Nachricht_pk_loeschen"><s:param name="nachrichtId" value="nachricht.nachrichtId" /></s:url></s:else>" title="<s:text name="button.papierkorb.titel"/>">&nbsp;<i class="icon-trash"></i>&nbsp;</a>
	</div>
	<s:if test="nachrichtTyp == 2">
		<div class="btn-group">
			<a class="btn" href="<s:url action="Nachricht_wiederherstellen"><s:param name="nachrichtId" value="nachricht.id" /></s:url>"><s:text name="button.wiederherstellen"/></a>
		</div>
	</s:if>
	<s:if test="nachricht.absenderId > 0">
		<div class="btn-group pull-right">
			<a class="btn" href="<s:url action="Nachricht_antworten"><s:param name="nachrichtId" value="nachricht.id" /></s:url>"><i class="icon-share-alt"></i>&nbsp;<s:text name="button.antworten"/></a>
		</div>
	</s:if>
	<div class="btn-group pull-right">
		<p><s:text name="nachricht.versanddatum.komplett"><s:param value="nachricht.versanddatum" /></s:text></p>
	</div>
</div>

<h3 style="margin-top: 30px"><s:property value="nachricht.betreff" /><small><s:if test="nachrichtTyp == 0 || (nachrichtTyp == 2 && #session.angemeldeterBenutzer.id == empfaengerId)"><s:text name="Nachricht.anzeigen.titel.posteingang"><s:param><s:if test="absenderId > 0"><a href="<s:url action="Benutzer_anzeigen"><s:param name="benutzerId" value="nachricht.absenderId" /></s:url>"><s:property value="nachricht.absenderName" /></a></s:if><s:else><s:property value="nachricht.absenderName" /></s:else></s:param></s:text></s:if><s:else><s:text name="Nachricht.anzeigen.titel.postausgang"><s:param><a href="<s:url action="Benutzer_anzeigen"><s:param name="benutzerId" value="nachricht.empfaengerId" /></s:url>"><s:property value="nachricht.empfaengerName" /></a></s:param></s:text></s:else></small></h3>

<div class="well"><blockquote><s:property value="nachricht.text" escape="false" /></blockquote></div>