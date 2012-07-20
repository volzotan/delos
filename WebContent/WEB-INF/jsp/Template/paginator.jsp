<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sb" uri="/struts-bootstrap-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<div class="btn-group pull-right">
	<s:if test="%{seite - 1 >= 0}">
		<a class="btn" href="<s:url includeParams="get"><s:param name="seite" value="seite - 1" /></s:url>" title="<s:text name="button.vorherigeSeite.title" />">&nbsp;<i class="icon-chevron-left"></i>&nbsp;</a>
	</s:if>
	<s:else>
		<button class="btn" disabled="disabled">&nbsp;<i class="icon-chevron-left"></i>&nbsp;</button>
	</s:else>
	<button class="btn" disabled="disabled"><s:text name="global.paginator.anzeige"><s:param value="%{seitenlaenge * seite + (#emptyList ? 0 : 1)}" /><s:param value="%{listeDaten.size + seitenlaenge * seite}" /><s:param value="listeAnzahlElemente" /></s:text></button>					
	<s:if test="%{listeAnzahlElemente > seitenlaenge * (seite + 1)}">
		<a class="btn" href="<s:url includeParams="get"><s:param name="seite" value="seite +1 " /></s:url>" title="<s:text name="button.naechsteSeite.title" />">&nbsp;<i class="icon-chevron-right"></i>&nbsp;</a>    
	</s:if>
	<s:else>
		<button class="btn" disabled="disabled">&nbsp;<i class="icon-chevron-right"></i>&nbsp;</button>
	</s:else>						
</div>