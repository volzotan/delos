<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sb" uri="/struts-bootstrap-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<div id="footer">
	<div class="span">
		<a href="http://www.uni-ulm.de/" target="_blank" title="Universität Ulm"><img class="bildmarke" src="<s:url value='/img/foot-bildmarke-small.png'/>" alt="" /></a>
	</div>
	<div class="divider-vertical"></div>
	<div class="span quad-bl">
		<strong><s:text name="Template.footer.menu.links.titel" /></strong>
		<ul class="pull-down unstyled">
			<li><a href="<s:url action="Ausschreibung_index"/>" title=""><s:text name="Template.footer.menu.links.jobs" /></a></li>
			<s:if test="#session.loggedIn">
				<s:if test="#session.angemeldeterBenutzer.gruppeId == 1">
					<li><a href="<s:url action="Benutzer_index"/>"><s:text name="Template.footer.menu.links.benutzer" /></a></li>
					<li><a href="<s:url action="Dokument_index"/>"><s:text name="Template.footer.menu.links.dokumente" /></a></li>
				</s:if>
				<s:elseif test="#session.angemeldeterBenutzer.gruppeId == 2">
					<li><a href="<s:url action="Bewerbungen_index"/>"><s:text name="Template.footer.menu.links.meineBewerbungen" /></a></li>
				</s:elseif>
				<s:elseif test="#session.angemeldeterBenutzer.gruppeId == 3">
					<li><a href="<s:url action="Ausschreibung_meine"/>"><s:text name="Template.footer.menu.links.meineAusschreibungen" /></a></li>
					<li><a href="<s:url action="Bewerbungen_index"/>"><s:text name="Template.footer.menu.links.bewerbungen" /></a></li>
				</s:elseif>
				<s:elseif test="#session.angemeldeterBenutzer.gruppeId == 4">
					<li><a href="<s:url action="Bewerbungen_index"/>"><s:text name="Template.footer.menu.links.meineBewerbungen" /></a></li>
					<li><a href="<s:url action="Dokument_index"/>"><s:text name="Template.footer.menu.links.dokumente" /></a></li>
				</s:elseif>
				<li><br /></li>
				<li><a href="<s:url action="Nachricht_posteingang"/>"><s:text name="Template.footer.menu.links.nachrichten" /></a></li>
			</s:if>
			<s:else>
				<li><br /></li>
				<li><a href="<s:url action="Benutzer_anmelden"/>"><s:text name="Template.footer.menu.links.anmelden" /></a></li>
				<li><a href="<s:url action="Benutzer_registrieren"/>"><s:text name="Template.footer.menu.links.registrieren" /></a></li>
			</s:else>
		</ul>
	</div>
	<div class="divider-vertical"></div>
	<div class="span quad-br">
		<strong><s:text name="Template.footer.menu.rechts.titel" /></strong>
		<ul class="pull-down unstyled">
			<li><a href="<s:url action="Seite_hilfe"/>" title=""><s:text name="Template.footer.menu.links.hilfe" /></a></li>
			<li><a href="<s:url action="Seite_impressum"/>"><s:text name="Template.footer.menu.rechts.impressum" /></a></li>
			<li><br /></li>
			<li><a href="<s:url action="Seite_datenschutz"/>" title=""><s:text name="Template.footer.menu.rechts.datenschutz" /></a></li>
			<li><a href="<s:url action="Seite_nutzungsbedingungen"/>" title=""><s:text name="Template.footer.menu.rechts.nutzungsbedingungen" /></a></li>
		</ul>
	</div>
	<div class="divider-vertical"></div>
	<div class="span">					
		<div class="pull-down">
			<a style="display:block; margin-bottom:5px;" href="<s:url action="Seite_index"/>"><img src="<s:url value='/img/footer-delos.png'/>" alt=""/></a>
			<s:text name="Template.footer.copyright" />
		</div>
	</div>
</div>