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
				<li><h1><s:text name="Benutzer.index.titel" /></h1></li>
				<li class="<s:if test="#activeTab.equals('alle')">active</s:if>"><a href="<s:url action="Benutzer_index" />"><s:text name="Benutzer.index.tab.alle" /></a></li>
				<li class="<s:if test="#activeTab.equals('bewerber')">active</s:if>"><a href="<s:url action="Benutzer_index_bewerber" />"><s:text name="Benutzer.index.tab.bewerber" /></a></li>
				<li class="<s:if test="#activeTab.equals('ausschreiber')">active</s:if>"><a href="<s:url action="Benutzer_index_ausschreiber" />"><s:text name="Benutzer.index.tab.ausschreiber" /></a></li>
				<li class="<s:if test="#activeTab.equals('bearbeiter')">active</s:if>"><a href="<s:url action="Benutzer_index_bearbeiter" />"><s:text name="Benutzer.index.tab.bearbeiter" /></a></li>
				<li class="<s:if test="#activeTab.equals('administratoren')">active</s:if>"><a href="<s:url action="Benutzer_index_administratoren" />"><s:text name="Benutzer.index.tab.administratoren" /></a></li>
			</ul>
		</div>
	</div>
</div>