<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<h1><s:property value="ausschreibung.name" /></h1>
<hr />
<div class="row">
	<div class="span6">
		<div class="well well-table">
			<table class="table table-condensed">
				<tbody>
					<tr>
						<th><s:text name="Ausschreibung.tabelle.institut"/></th>
						<td><s:property value="%{getText('Institut.'+ausschreibung.institut)}" /></td>
					</tr>
					<tr>
						<th><s:text name="Ausschreibung.tabelle.ausschreiber"/></th>
						<td><a href="<s:url action="Benutzer_anzeigen"><s:param name="benutzerId" value="ausschreibung.ausschreiberId" /></s:url>"><s:property value="ausschreibung.ausschreiberName" /></a></td>
					</tr>
					<tr>
						<th><s:text name="Ausschreibung.tabelle.bearbeiter"/></th>
						<td><a href="<s:url action="Benutzer_anzeigen"><s:param name="benutzerId" value="ausschreibung.bearbeiterId" /></s:url>"><s:property value="ausschreibung.bearbeiterName" /></a></td>
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
						<th><s:text name="Ausschreibung.tabelle.bewerbungsfrist"/></th>
						<td style="text-align:right;"><s:property value="ausschreibung.bewerbungsfrist" /></td>
					</tr>
					<tr>
						<th><s:text name="Ausschreibung.tabelle.beginn"/></th>
						<td style="text-align:right;"><s:property value="ausschreibung.startet" /></td>
					</tr>
					<tr>
						<th><s:text name="Ausschreibung.tabelle.ende"/></th>
						<td style="text-align:right;"><s:property value="ausschreibung.endet" /></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
<s:if test="#session.angemeldeterBenutzer.gruppeId == 3">
	<div class="row">
		<div class="span12">
			<div class="alert alert-<s:property value="%{ausschreibung.status == 0 ? 'success' : 'danger'}" /> centerTxt">
				<strong><s:property value="%{getText('ausschreibung.status.' + ausschreibung.status)}" /></strong>
			</div>
		</div>
	</div>
</s:if>
<div class="row">
	<div class="span6">
		<h2><small><s:text name="Ausschreibung.sonstiges.stundenWoche"><s:param value="ausschreibung.stundenzahl" /></s:text></small></h2>
	</div>
	<div class="span6">
		<h2><small><s:if test="ausschreibung.frei == 0"><s:text name="Ausschreibung.sonstiges.freieStellen.keine" /></s:if><s:else><s:text name="Ausschreibung.sonstiges.freieStellen"><s:param value="ausschreibung.frei" /><s:param value="ausschreibung.stellenzahl" /></s:text></s:else></small></h2>
	</div>
</div>
<hr />
<div class="row">
	<s:if test="ausschreibung.voraussetzungen == null || ausschreibung.voraussetzungen.equals('')">
		<div class="span12">
			<h3><s:text name="Ausschreibung.tabelle.beschreibung"/></h3>
			<div class="well">
				<s:property value="ausschreibung.beschreibung" escape="false" />
			</div>
		</div>
	</s:if>
	<s:else>
		<div class="span6">
			<h3><s:text name="Ausschreibung.tabelle.vorraussetzung"/></h3>
			<div class="well">
				<s:property value="ausschreibung.voraussetzungen" escape="false" />
			</div>
		</div>
		<div class="span6">
			<h3><s:text name="Ausschreibung.tabelle.beschreibung"/></h3>
			<div class="well">
				<s:property value="ausschreibung.beschreibung" escape="false" />
			</div>
		</div>
	</s:else>
</div>
<s:if test="!(listeDaten == null || listeDaten.isEmpty)">
	<hr />
	<div class="row">
		<div class="span12">
			<h3><s:text name="Ausschreibung.sonstiges.angenommeneBenutzer" /></h3>
			<div class="well well-table-head">
				<table class="table table-condensed">
					<thead>
						<tr>
							<th><s:text name="benutzer.name" /></th>
							<th><s:text name="bewerbungsVorgang.bearbeiterName" /></th>
							<th><s:text name="bewerbungsVorgang.stundenzahl" /></th>
							<th><s:text name="bewerbungsVorgang.startet" /></th>
							<th><s:text name="bewerbungsVorgang.endet" /></th>
						</tr>
					</thead>	
					<tbody>
						<s:iterator value="listeDaten" status="status">
							<tr class="hyperlink" data-toggle="<s:url action="BewerbungsVorgang_anzeigen"><s:param name="bewerbungsVorgangId" value="id" /></s:url>">
								<td><s:property value="bewerberName" /></td>
								<td><s:property value="bearbeiterName" /></td>
								<td><s:property value="stundenzahl" /></td>
								<td><s:property value="startet" /></td>
								<td><s:property value="endet" /></td>
							</tr>
						</s:iterator>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</s:if>
<hr />
<div class="btn-toolbar floatable">
	<div class="btn-group">
		<a href="javascript:history.back()" tabindex="5" class="btn"><s:text name="button.zurueck" /></a>
	</div>
	<s:if test="#session.angemeldeterBenutzer.gruppeId == 2 && bewerbungID == 0 && ausschreibung.stellenzahl > ausschreibung.belegt">
		<div class="btn-group">
			<a href="<s:url action="BewerbungsVorgang_erstellen"><s:param name="ausschreibungId" value="ausschreibung.id" /></s:url>" class="btn btn-primary"><s:text name="Ausschreibung.sonstiges.bewerben" /></a>
		</div>
	</s:if>
	<s:if test="#session.angemeldeterBenutzer.gruppeId == 1 || #session.angemeldeterBenutzer.id == ausschreibung.ausschreiberId">
		<s:if test="beenden==true">
			<div class="btn-group">
				<a href="<s:url action="Ausschreibung_aktualisieren"><s:param name="ausschreibungId" value="ausschreibung.id" /></s:url>" class="btn btn-primary"><s:text name="button.bearbeiten" /></a>
			</div>
		</s:if>
		<s:if test="loeschen==true">
			<div class="btn-group pull-right">
				<a href="#ausschreibungLoeschen" data-toggle="modal" class="btn btn-danger"><s:text name="button.loeschen" /></a>
			</div>
		</s:if>
		<s:if test="beenden==true">
			<div class="btn-group pull-right">
				<a href="<s:url action="Ausschreibung_vorzeitigBeenden"><s:param name="ausschreibungId" value="ausschreibung.id" /></s:url>" class="btn"><s:text name="button.vorzeitigBeenden" /></a>
			</div>
		</s:if>
	</s:if>
</div>

<s:if test="#session.angemeldeterBenutzer.gruppeId == 1 || #session.angemeldeterBenutzer.id == ausschreibung.ausschreiberId">
	<div class="modal" id="ausschreibungLoeschen" style="display:none">
		<div class="modal-header">
			<button class="close" data-dismiss="modal">×</button>
			<h3><s:text name="Ausschreibung.loeschen.titel" /></h3>
		</div>
		<div class="modal-body">
			<s:text name="Ausschreibung.loeschen.text"><s:param value="ausschreibung.name" /></s:text>
		</div>
		<div class="modal-footer">
			<a href="#" class="btn" data-dismiss="modal"><s:text name="button.abbrechen" /></a>
			<a href="<s:url action="Ausschreibung_loeschen"><s:param name="ausschreibungId" value="ausschreibung.id" /></s:url>" class="btn btn-danger"><s:text name="button.loeschen" /></a>
		</div>
	</div>
</s:if>