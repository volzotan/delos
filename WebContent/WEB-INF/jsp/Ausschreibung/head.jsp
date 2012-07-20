<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<s:if test="%{listeDaten == null || listeDaten.isEmpty}">
	<s:set var="emptyList" value="true" />
</s:if>
<s:else>
	<s:set var="emptyList" value="false" />
</s:else>
<s:set var="richtung" value="#parameters['richtung'][0]" />
<s:set var="spalte" value="#parameters['spalte'][0]" />

<div class="row">
	<div class="span12">
		<div id="subnav">
			<ul class="nav nav-tabs">
				<li><h1><s:text name="Ausschreibung.navigation.jobs" /></h1></li>
				<li class="<s:if test="#activeTab.equals('index')">active</s:if>"><a href="<s:url action="Ausschreibung_index" />"><s:text name="Ausschreibung.navigation.uebersicht" /></a></li>
			 	<s:if test="#session.angemeldeterBenutzer.gruppeId in {3}">
			 		<li class="<s:if test="#activeTab.equals('meine')">active</s:if>"><a href="<s:url action="Ausschreibung_meine" />"><s:text name="Ausschreibung.navigation.meineAusschreibungen" /></a></li>
			 	</s:if>
			 	<s:if test="#session.angemeldeterBenutzer.gruppeId in {2, 3, 4}">
			 		<li class="<s:if test="#activeTab.equals('bewerbungen')">active</s:if>"><a href="<s:url action="Bewerbungen_index" />"><s:property value="%{#session.angemeldeterBenutzer.gruppeId in {3, 4} ? getText('Ausschreibung.navigation.bewerbungen') : getText('Ausschreibung.navigation.meineBewerbungen')}" /></a></li>
			 	</s:if>
			 	<li class="<s:if test="#activeTab.equals('suchen')">active</s:if>"><a href="<s:url action="Ausschreibung_suchen" />"><s:text name="Ausschreibung.navigation.erweiterteSuche" /></a></li>						
			</ul>			
		</div>
	</div>
</div>