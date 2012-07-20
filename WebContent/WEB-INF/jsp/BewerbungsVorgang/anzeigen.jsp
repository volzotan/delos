<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<h1><s:text name="BewerbungsVorgang.anzeigen.titel" /></h1>
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
	<div class="span12">
		<div class="alert alert-<s:property value="%{bewerbungsVorgang.status in {0, 4} ? 'danger' : bewerbungsVorgang.status in {1, 2} ? 'info' : bewerbungsVorgang.status in {-2, -1} ? 'warning' : 'success'}" /> centerTxt">
			<strong><s:property value="%{getText('bewerbungsVorgang.status.' + bewerbungsVorgang.status)}" /></strong>
			<s:if test="bewerbungsVorgang.status in {-1, -2}">
				<s:if test="#session.angemeldeterBenutzer.gruppeId == 2">
					<br />
					<s:text name="BewerbungsVorgang.anzeigen.detailAenderung" />
				</s:if>
				<s:if test="#session.angemeldeterBenutzer.gruppeId == 4">
					<br />
					<s:text name="BewerbungsVorgang.anzeigen.detailAenderung.bearbeiter" />
				</s:if>
			</s:if>
		</div>
	</div>
</div>

<div class="row">
	<div class="span6">
		<h2><small><s:text name="BewerbungsVorgang.anzeigen.bewerbungseingang"><s:param value="bewerbungsVorgang.hinzugefuegt" /></s:text></small></h2>
	</div>
	<div class="span6">
		<h2><small><s:text name="BewerbungsVorgang.anzeigen.letzteAenderung"><s:param value="bewerbungsVorgang.bearbeitet" /></s:text><s:if test="#session.angemeldeterBenutzer.gruppeId == 2 && bewerbungsVorgang.status in {-1, -2}">&nbsp;<i class="icon-exclamation-sign show-tooltip" title="<s:text name="BewerbungsVorgang.anzeigen.detailAenderung.tooltip" />"></i></s:if></small></h2>
	</div>
</div>

<s:if test="%{!(bewerbungsVorgang.kommentar == null || bewerbungsVorgang.kommentar.equals(''))}">
	<hr />
	<h3><s:text name="BewerbungsVorgang.anzeigen.kommentar" /></h3>
	<div class="well">
		<s:property value="bewerbungsVorgang.kommentar" escape="false" />
	</div>
</s:if>

<s:if test="bewerbungsVorgang.status in {-2, 2, 3}">
	<hr />
	<s:if test="bewerbungsVorgangDokumenteListen['alle'].empty">
		<div class="well centerTxt">
			<s:text name ="Dokument.liste.alle.leer" />
		</div>
	</s:if>
	<s:else>
		<div class="row">
			<div class="span6">
				<h3><s:text name="BewerbungsVorgang.anzeigen.abgegebeneDokumente"><s:param value="bewerbungsVorgangDokumenteListen['abgegebene'].size" /></s:text></h3>
				<s:if test="bewerbungsVorgangDokumenteListen['abgegebene'].empty">
					<div class="well well-table centerTxt">
						<s:text name ="Dokument.liste.abgegebene.leer" />
					</div>
				</s:if>
				<s:else>
					<div class="well well-table">
						<table class="table table-condensed">
							<tbody>							
								<s:iterator value="bewerbungsVorgangDokumenteListen['abgegebene']" status="status">
								
									<tr>
										<td>
											<a href="<s:url action="Dokument_anzeigen"><s:param name="dokumentId" value="dokumentId" /></s:url>"><s:property value="dokumentName" /></a>
										</td>
										<td width="20">
											<i class="icon-check icon-fade"></i>
										</td>
									</tr>
																	
								</s:iterator>
							</tbody>
						</table>
					</div>
				</s:else>
			</div>
			<div class="span6">
				<h3><s:text name="BewerbungsVorgang.anzeigen.abzugebeneDokumente"><s:param value="bewerbungsVorgangDokumenteListen['abzugebene'].size" /></s:text></h3>
				<s:if test="bewerbungsVorgangDokumenteListen['abzugebene'].empty">
					<div class="well well-table centerTxt">
						<s:text name ="Dokument.liste.abzugebene.leer" />
					</div>
				</s:if>
				<s:else>
					<div class="well well-table">
						<table class="table table-condensed">
							<tbody>
								<s:iterator value="bewerbungsVorgangDokumenteListen['abzugebene']" status="status">
								
									<tr>
										<td>
											<a href="<s:url action="Dokument_anzeigen"><s:param name="dokumentId" value="dokumentId" /></s:url>"><s:property value="dokumentName" /></a>
										</td>
									</tr>
									
								</s:iterator>
							</tbody>
						</table>
					</div>
				</s:else>
			</div>
		</div>
	</s:else>
</s:if>

<hr />
<div class="btn-toolbar floatable">
	<div class="btn-group">
		<a href="javascript:history.back()" class="btn"><s:text name="button.zurueck" /></a>
	</div>
	
	<s:if test="!(bewerbungsVorgang.status in {0, 3, 4})">
		<s:if test="#session.angemeldeterBenutzer.gruppeId == 2">
			<div class="btn-group">
				<a class="btn" href="<s:url action="BewerbungsVorgang_aktualisieren"><s:param name="bewerbungsVorgangId" value="bewerbungsVorgang.id" /></s:url>"><s:text name="%{'button.kommentar' + (bewerbungsVorgang.kommentar == null || bewerbungsVorgang.kommentar.equals('') ? 'Hinzufuegen' : 'Aendern')}" /></a>
			</div>
			<div class="btn-group pull-right">
				<a class="btn btn-danger" href="#bewerbungZurueckziehen" data-toggle="modal"><s:text name="button.zurueckziehen" /></a>
			</div>
			<s:if test="bewerbungsVorgang.status in {-1, -2}">
				<div class="btn-group pull-right">
					<a class="btn btn-warning" href="<s:url action="BewerbungsVorgang_aenderungenAkzeptieren"><s:param name="bewerbungsVorgangId" value="bewerbungsVorgang.id" /></s:url>"><s:text name="button.aenderungenAkzeptieren" /></a>
				</div>
			</s:if>
		</s:if>
		
		<s:if test="#session.angemeldeterBenutzer.gruppeId == 3">
			<div class="btn-group">
				<a class="btn" href="<s:url action="BewerbungsVorgang_aktualisieren"><s:param name="bewerbungsVorgangId" value="bewerbungsVorgang.id" /></s:url>"><s:text name="button.bearbeiten.bewerbung" /></a>
			</div>
			<div class="btn-group pull-right">
				<a class="btn btn-danger" href="#bewerbungAblehnen" data-toggle="modal"><s:text name="button.ablehnen" /></a>
			</div>
			<s:if test="bewerbungsVorgang.status == 1">
				<div class="btn-group pull-right">
					<a class="btn btn-success" href="#bewerbungAkzeptieren" data-toggle="modal"><s:text name="button.akzeptieren" /></a>
				</div>
			</s:if>
		</s:if>
		
		<s:if test="#session.angemeldeterBenutzer.gruppeId == 4 && bewerbungsVorgang.status == 2">
			<div class="btn-group">
				<a class="btn btn-primary" href="<s:url action="%{bewerbungsVorgangDokumenteListen['abzugebene'].empty && bewerbungsVorgangDokumenteListen['abgegebene'].empty ? 'Dokument_unbenoetigteDokumenteBearbeiter' : 'Dokument_benoetigteDokumenteBearbeiter'}"><s:param name="bewerbungsVorgangId" value="bewerbungsVorgang.id" /></s:url>"><s:text name="button.dokumentAbgabeVermerken" /></a>
			</div>
			<s:if test="bewerbungsVorgangDokumenteListen['abzugebene'].empty">
				<div class="btn-group pull-right">
					<a class="btn btn-success" href="#bewerbungAbschliessen" data-toggle="modal"><s:text name="button.abschliessen" /></a>
				</div>
			</s:if>
		</s:if>
	</s:if>
</div>

<div class="modal" id="bewerbungZurueckziehen" style="display:none">
	<div class="modal-header">
		<button class="close" data-dismiss="modal">×</button>
		<h3><s:text name="BewerbungsVorgang.zurueckziehen.titel" /></h3>
	</div>
	<div class="modal-body">
		<p><s:text name="BewerbungsVorgang.zurueckziehen.text" /></p>
	</div>
	<div class="modal-footer">
		<a href="#" class="btn" data-dismiss="modal"><s:text name="button.abbrechen" /></a>
		<a href="<s:url action="BewerbungsVorgang_zurueckziehen"><s:param name="bewerbungsVorgangId" value="bewerbungsVorgang.id" /></s:url>" class="btn btn-danger"><s:text name="button.zurueckziehen" /></a>
	</div>
</div>

<div class="modal" id="bewerbungAkzeptieren" style="display:none">
	<div class="modal-header">
		<button class="close" data-dismiss="modal">×</button>
		<h3><s:text name="BewerbungsVorgang.akzeptieren.titel" /></h3>
	</div>
	<div class="modal-body">
		<p><s:text name="BewerbungsVorgang.akzeptieren.text" /></p>
	</div>
	<div class="modal-footer">
		<a href="#" class="btn" data-dismiss="modal"><s:text name="button.abbrechen" /></a>
		<a href="<s:url action="BewerbungsVorgang_akzeptieren"><s:param name="bewerbungsVorgangId" value="bewerbungsVorgang.id" /></s:url>" class="btn btn-danger"><s:text name="button.akzeptieren" /></a>
	</div>
</div>

<div class="modal" id="bewerbungAblehnen" style="display:none">
	<div class="modal-header">
		<button class="close" data-dismiss="modal">×</button>
		<h3><s:text name="BewerbungsVorgang.ablehnen.titel" /></h3>
	</div>
	<div class="modal-body">
		<p><s:text name="BewerbungsVorgang.ablehnen.text" /></p>
	</div>
	<div class="modal-footer">
		<a href="#" class="btn" data-dismiss="modal"><s:text name="button.abbrechen" /></a>
		<a href="<s:url action="BewerbungsVorgang_ablehnen"><s:param name="bewerbungsVorgangId" value="bewerbungsVorgang.id" /></s:url>" class="btn btn-danger"><s:text name="button.ablehnen" /></a>
	</div>
</div>

<div class="modal" id="bewerbungAbschliessen" style="display:none">
	<div class="modal-header">
		<button class="close" data-dismiss="modal">×</button>
		<h3><s:text name="BewerbungsVorgang.abschliessen.titel" /></h3>
	</div>
	<div class="modal-body">
		<p><s:text name="BewerbungsVorgang.abschliessen.text" /></p>
	</div>
	<div class="modal-footer">
		<a href="#" class="btn" data-dismiss="modal"><s:text name="button.abbrechen" /></a>
		<a href="<s:url action="BewerbungsVorgang_abschliessen"><s:param name="bewerbungsVorgangId" value="bewerbungsVorgang.id" /></s:url>" class="btn btn-danger"><s:text name="button.abschliessen" /></a>
	</div>
</div>