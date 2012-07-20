<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<div class="well" style="padding: 8px 0; margin-top: -20px;">
	<ul class="nav nav-list">
		<li class="nav-header"><s:text name="Seite.nav.header.delos" /></li>
		<li class="<s:if test="#activeTab == 'impressum'">active</s:if>"><a href="<s:url action="Seite_impressum" />"><s:text name="Template.footer.menu.rechts.impressum" /></a></li>
		<li class="nav-header"><s:text name="Seite.nav.header.rechtliches" /></li>
		<li class="<s:if test="#activeTab == 'datenschutz'">active</s:if>"><a href="<s:url action="Seite_datenschutz" />"><s:text name="Template.footer.menu.rechts.datenschutz" /></a></li>
		<li class="<s:if test="#activeTab == 'nutzungsbed'">active</s:if>"><a href="<s:url action="Seite_nutzungsbedingungen" />"><s:text name="Template.footer.menu.rechts.nutzungsbedingungen" /></a></li>
	</ul>
</div>