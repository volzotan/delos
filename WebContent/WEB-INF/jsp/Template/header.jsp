<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sb" uri="/struts-bootstrap-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<div id="header" class="<s:if test="%{#session.istVertreter}">extended</s:if>">
	<div class="navbar navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">
				<s:if test="0 < hatNachrichten">
					<div class="popover bottom benutzer-info">
						<div class="arrow"></div>
						<div class="popover-inner">
							<div class="popover-content">
								<p><s:text name="Template.header.navbar.benutzer-info.nachrichten"><s:param value="hatNachrichten" /></s:text></p>
							</div>
						</div>
					</div>
				</s:if>
				<noscript>
					<div class="popover bottom noscript-alert">
						<div class="arrow"></div>
						<div class="popover-inner">
							<div class="popover-title">
								<h3><s:text name="System.noscript.popover.titel"/></h3>
								<a class="close" href="<s:url includeParams="get"/>">×</a>
							</div>
							<div class="popover-content">
								<p><s:text name="System.noscript.popover.text"/></p>
							</div>
						</div>
					</div>
				</noscript>
				<ul class="nav pull-left">
					<li><a href="<s:url action="Seite_index"/>"><i class="nav-home-logo"></i></a></li>
					<li><a href="<s:url action="Seite_index"/>"><s:text name="Template.header.navbar.start"/></a></li>
					<li><a href="<s:url action="Ausschreibung_index"/>"><s:text name="Template.header.navbar.stellen"/></a></li>
					<s:if test="%{#session.loggedIn && #session.angemeldeterBenutzer.gruppeId == 1 || #session.angemeldeterBenutzer.gruppeId == 4}">
					<li class="dropdown">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown"><s:text name="Template.header.navbar.verwaltung"/>&nbsp;<b class="caret"></b></a>
						<ul class="dropdown-menu">
							<s:if test="#session.angemeldeterBenutzer.gruppeId == 1"><li><a href="<s:url action="Benutzer_index"/>"><s:text name="Template.header.navbar.verwaltung.benutzerkonten"/></a></li></s:if>
							<li><a href="<s:url action="Dokument_index"/>"><s:text name="Template.header.navbar.verwaltung.dokumente"/></a></li>
						</ul>
					</li>
					</s:if>
					<li class="divider-vertical"></li>
				</ul>
				<form action="/delos/Ausschreibung_suchen.action" accept-charset="utf-8" method="get" name="navbar-search" class="navbar-search pull-left">
					<input type="text" class="search-field" placeholder="<s:text name="Template.header.navbar.suche"/>" name="ausschreibung.name" />
					<button type="submit" class="icon-search icon-white" title="<s:text name="Template.header.navbar.suche.icon"/>"></button>
				</form>
				<ul class="nav pull-left">
					<li class="divider-vertical"></li>
				</ul>
				<ul class="nav pull-right">
					<s:if test="%{#session.loggedIn}">
						<li class="dropdown">
						    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><s:if test="#session.istVertreter"><s:text name="Template.header.navbar.user.vertretermodus.name"><s:param value="#session.angemeldeterBenutzer.name" /></s:text></s:if><s:else><s:property value="#session.angemeldeterBenutzer.name" /></s:else>&nbsp;<b class="caret"></b></a>
						    <ul class="dropdown-menu">
						    	<li><a href="<s:url action="%{'Benutzer_' + (#session.istVertreter ? 'anzeigen' : 'aktualisieren')}"><s:param name="benutzerId" value="%{#session.angemeldeterBenutzer.id}" /></s:url>"><s:text name="Template.header.navbar.user.profil"/></a></li>
						    	<li><a href="<s:url action="Nachricht_posteingang"/>"><s:text name="Template.header.navbar.user.nachrichten"/></a></li>
						    	<li class="divider"></li>
						    	<li><a href="<s:url action="Seite_hilfe"/>"><s:text name="Template.header.navbar.user.hilfe"/></a></li>
						    	<li><a href="<s:url action="Seite_impressum"/>"><s:text name="Template.header.navbar.user.impressum"/></a></li>
						    	<li class="divider"></li>
						    	<s:if test="!#session.istVertreter">
						    		<s:if test="#session.angemeldeterBenutzer.gruppeId in {3, 4} && !vertreterListe.isEmpty()">
								    	<li class="dropdown offset-right">
								    		<a href="javascript:void(0)" class="dropdown-toggle"><s:text name="Template.header.navbar.user.vertretermodus" /><b class="caret"></b></a>
								    		<ul class="dropdown-menu">
								    			<li class="nav-header"><s:text name="Template.header.navbar.user.vertretermodus.header"/></li>
								    			<s:iterator value="vertreterListe" status="status">				    			
								    				<li><a href="#logonVertreter<s:property value="id" />" data-toggle="modal"><s:text name="Template.header.navbar.user.vertretermodus.namenListe"><s:param value="%{vorname.substring(0, 1)}" /><s:param value="nachname" /></s:text></a></li>
								    			</s:iterator>
								    		</ul>
								    	</li>
								    </s:if>
						    	</s:if>
						    	<s:else>
						    		<li><a href="<s:url action="Benutzer_vertreterAnmelden" />"><s:text name="Template.header.navbar.user.vertretermodus.beenden"/></a></li>
						    	</s:else>
						    	<li><a href="<s:url action="Benutzer_abmelden"/>"><s:text name="Template.header.navbar.user.abmelden"/></a></li>
						    </ul>
						</li>						
					</s:if>
					<s:else>
					    <li><a href="<s:url action="Benutzer_anmelden"/>"><s:text name="Template.header.navbar.anmelden"/></a></li>
					</s:else>					
					<li class="divider-vertical"></li>
						<s:if test="%{#session.loggedIn && 0 < hatNachrichten}">
							<li><a id="benutzer-info" class="benutzer-info popover-toggle" href="javascript:void(0)"><i class="icon-comment icon-white"></i></a></li>
						</s:if>
					<li class="dropdown">
					    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="icon-lang-<s:property value="%{locale == null ? 'de_DE' : locale}"/>"></i><b class="caret"></b></a>
					    <ul class="dropdown-menu">
					    	<li><a href="<s:url includeParams="get"><s:param name="request_locale" value="%{'de_DE'}"/></s:url>"><i class="icon-lang-de_DE"></i><s:text name="sprache.deutsch" /></a></li>
					    	<li><a href="<s:url includeParams="get"><s:param name="request_locale" value="%{'de_SW'}"/></s:url>"><i class="icon-lang-de_SW"></i><s:text name="sprache.schwaebisch" /></a></li>
					    	<li><a href="<s:url includeParams="get"><s:param name="request_locale" value="%{'en_US'}"/></s:url>"><i class="icon-lang-en_US"></i><s:text name="sprache.englisch" /></a></li>
					    </ul>
					</li>
				</ul>
			</div>
		</div>
	</div>
	<s:if test="%{#session.istVertreter}">
		<div class="vertreter-info">
			<div class="container">
				<a class="hinweis" href="<s:url action="Benutzer_vertreterAnmelden" />"><s:text name="Vertretungsmodus.hinweis"><s:param value="#session.angemeldeterBenutzer.name" /></s:text></a>
				<a class="beenden" href="<s:url action="Benutzer_vertreterAnmelden" />"><s:text name="Vertretungsmodus.beenden.titel" /></a>
			</div>
		</div>
	</s:if>
</div>

<s:if test="#session.angemeldeterBenutzer.gruppeId in {3, 4}">
	<s:if test="!#session.istVertreter && !vertreterListe.isEmpty()">
		<s:iterator value="vertreterListe" status="status">
			<div id="logonVertreter<s:property value="id" />" class="modal" style="display:none">
				<div class="modal-header">
					<h3><s:text name="Template.header.navbar.user.vertretermodus.sicherheit.titel" /></h3>
				</div>
				<div class="modal-body">
					<s:text name="Template.header.navbar.user.vertretermodus.sicherheit"><s:param value="vorname" /><s:param value="nachname" /></s:text>
				</div>
				<div class="modal-footer">
					<a href="#" class="btn" style="min-width:50px" data-dismiss="modal"><s:text name="button.nein" /></a>
					<a href="<s:url action="Benutzer_vertreterAnmelden"><s:param name="benutzerId" value="id" /></s:url>" class="btn btn-success" style="min-width:50px"><s:text name="button.ja" /></a>
				</div>
			</div>
		</s:iterator>
	</s:if>
</s:if>