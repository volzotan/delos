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

<s:form action="Dokument_benoetigteDokumenteBearbeiter_multi" method="post" acceptcharset="utf-8" theme="bootstrap">
	<s:hidden name="bewerbungsVorgangId" />
	
	<h1><s:text name="BewerbungsVorgang.anzeigen.titel.bennoetigteDoc" /></h1>
	<hr />
	<div class="row">
		<div class="span12">
			<h3><s:property value="bewerbungsVorgang.ausschreibungName" /></h3>
		</div>
	</div>
	<div class="row">
		<div class="span6">
			<div class="well well-table">
				<table class="table table-condensed">
					<tbody>
						<tr>
							<th><s:text name="bewerbungsVorgang.bewerberId" /></th>
							<td><a href="<s:url action="Benutzer_anzeigen"><s:param name="benutzerId" value="bewerbungsVorgang.bewerberId" /></s:url>"><s:property value="bewerbungsVorgang.bewerberName" /></a></td>
						</tr>
						<tr>
							<th><s:text name="bewerbungsVorgang.ausschreiberId" /></th>
							<td><a href="<s:url action="Benutzer_anzeigen"><s:param name="benutzerId" value="bewerbungsVorgang.ausschreiberId" /></s:url>"><s:property value="bewerbungsVorgang.ausschreiberName" /></a></td>
						</tr>
						<tr>
							<th><s:text name="bewerbungsVorgang.bearbeiterId" /></th>
							<td><a href="<s:url action="Benutzer_anzeigen"><s:param name="benutzerId" value="bewerbungsVorgang.bearbeiterId" /></s:url>"><s:property value="bewerbungsVorgang.bearbeiterName" /></a></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="span6">
			<div class="well well-table">
				<table class="table table-condensed">
					<tbody>	
						<tr>
							<th><s:text name="bewerbungsVorgang.stundenzahl" /></th>
							<td><s:property value="bewerbungsVorgang.stundenzahl" /></td>
						</tr>
						<tr>
							<th><s:text name="bewerbungsVorgang.startet" /></th>
							<td><s:property value="bewerbungsVorgang.startet" /></td>
						</tr>
						<tr>
							<th><s:text name="bewerbungsVorgang.endet" /></th>
							<td><s:property value="bewerbungsVorgang.endet" /></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="span6">
			<h3><small><s:text name="BewerbungsVorgang.anzeigen.bewerbungseingang"><s:param value="bewerbungsVorgang.hinzugefuegt" /></s:text></small></h3>
		</div>
		<div class="span6">
			<h3><small><s:text name="BewerbungsVorgang.anzeigen.letzteAenderung"><s:param value="bewerbungsVorgang.bearbeitet" /></s:text></small></h3>
		</div>
	</div>
	<hr />
	<div class="btn-toolbar head floatable">
		<div class="btn-group">
			<a class="btn" href="<s:url action="BewerbungsVorgang_anzeigen"><s:param name="bewerbungsVorgangId" value="bewerbungsVorgang.id" /></s:url>"><s:text name="button.zurueck" /></a>
		</div>
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
		<div class="btn-group">
			<s:if test="#emptyList">
				<button class="btn btn-primary" disabled="disabled"><s:text name="button.aenderungenSpeichern" /></button>
			</s:if>
			<s:else>
				<s:submit value="%{getText('button.auswahlAlsVorliegendSpeichern')}" cssClass="btn btn-primary" />
			</s:else>
		</div>
		<div class="btn-group pull-right">
			<a class="btn" href="<s:url action="Dokument_unbenoetigteDokumenteBearbeiter"><s:param name="bewerbungsVorgangId" value="bewerbungsVorgang.id" /></s:url>"><s:text name="button.dokument.weiteresHinzufuegen" /></a>
		</div>
	</div>
	
	<s:if test="#emptyList">
		<div class="alert alert-info centerTxt">
			<s:text name="Bewerbungsvorgang.anzeigen.dokument.benoetigt.leer" />
		</div>
	</s:if>
	<s:else>
		<table class="table table-striped table-condensed">
			<thead>
				<tr>
					<th></th>
					<th><s:text name="Bewerbungsvorgang.anzeigen.dokument.benoetigt"/></th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="listeDaten" status="status">
				
					<tr class="hyperlink" data-toggle="<s:url action="Dokument_anzeigen"><s:param name="dokumentId" value="dokumentId" /></s:url>">
						<td class="filter" width="20">
							<s:if test="status == 0">
								<input type="checkbox" name="multiBewerbungsVorgangDokumentId" value="<s:property value="id" />">
							</s:if>
							<s:else>
								<input type="checkbox" checked="checked" name="multiBewerbungsVorgangDokumentId" value="<s:property value="id" />">
							</s:else>
						</td>
						<td>
							<a href="<s:url action="Dokument_anzeigen"><s:param name="dokumentId" value="dokumentId" /></s:url>"><s:property value="dokumentName" /></a>
						</td>
						<td class="filter show-tooltip" width="14" title="<s:text name="BewerbungsVorgang.anzeigen.dokument.entfernen.tooltip" />">
							<a href="<s:url action="Dokument_bewerbungsVorgangDokumentLoeschen"><s:param name="bewerbungsVorgangDokumentId" value="id" /><s:param name="bewerbungsVorgangId" value="bewerbungsVorgangId" /></s:url>"><span class="icon-trash"></span></a>
						</td>
					</tr>
				
				</s:iterator>
			</tbody>
		</table>
	</s:else>
</s:form>