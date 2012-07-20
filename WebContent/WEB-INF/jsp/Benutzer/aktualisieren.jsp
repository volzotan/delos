<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<s:if test="#session.angemeldeterBenutzer.gruppeId == 1 && benutzer.id != #session.angemeldeterBenutzer.id">
	<s:set name="noAccess" value="true" />
</s:if>
<s:else>
	<s:set name="noAccess" value="false" />
</s:else>

<s:if test="#noAccess">
	<s:if test="benutzer.gruppeId == 1"><s:set var="activeTab" value="'administratoren'" /></s:if>
	<s:if test="benutzer.gruppeId == 2"><s:set var="activeTab" value="'bewerber'" /></s:if>
	<s:if test="benutzer.gruppeId == 3"><s:set var="activeTab" value="'ausschreiber'" /></s:if>
	<s:if test="benutzer.gruppeId == 4"><s:set var="activeTab" value="'bearbeiter'" /></s:if>
	<jsp:include page="head.jsp" flush="false" />
</s:if>
<s:else>
<h1><s:text name="Benutzer.aktualisieren.titel"><s:param value="benutzer.bearbeitet" /></s:text></h1>
<hr />
</s:else>

<s:if test="#session.angemeldeterBenutzer.id == benutzer.id || #session.angemeldeterBenutzer.gruppeId == 1">
	<s:form action="Benutzer_aktualisieren" method="post" acceptcharset="utf-8" validate="true" theme="bootstrap"> 	
	<s:hidden name="benutzer.bearbeitet" />		
		<div class="row">
			<div class="span6">
				<h3><s:if test="noAccess"><s:text name="Benutzer.formular.ueberBenutzer"><s:param value="benutzer.name"/></s:text></s:if><s:else><s:text name="Benutzer.formular.ueberDich" /></s:else></h3>
				<div class="well">
					<s:select
						headerKey="-1" headerValue="%{getText('select.bitteauswaehlen')}"
						list="anredenListe" 
						key="benutzer.geschlecht"
						required="true"
						disabled="%{#noAccess}"
					/>
					<s:textfield key="benutzer.vorname" required="true" cssClass="span6" disabled="%{#noAccess}" />
					<s:textfield key="benutzer.nachname" required="true" cssClass="span6" disabled="%{#noAccess}" />
					<s:if test="#session.angemeldeterBenutzer.gruppeId == 2 || (#session.angemeldeterBenutzer.gruppeId == 1 && benutzer.gruppeId == 2)">
						<sj:datepicker key="benutzer.geburtstag" displayFormat="%{getText('validierung.dateFormat')}" buttonImageOnly="true" cssClass="span6" disabled="%{#noAccess}" />
						<s:select label="%{getText('benutzer.nationalitaet')}"
							headerKey="-1" headerValue="%{getText('select.bitteauswaehlen')}"
							list="nationalitaetenMap" 
							key="benutzer.nationalitaet" 
							required="true"	
							disabled="%{#noAccess}"		 
						/>
					</s:if>
					<s:if test="benutzer.gruppeId != 2">
						<s:textfield key="benutzer.raum" cssClass="span6" disabled="%{#noAccess}" />
					</s:if>
				</div>
			</div>
			<div class="span6">
				<h3><s:text name="Benutzer.formular.kontoeinstellungen" /></h3>
				<div class="well">
					<s:if test="#session.angemeldeterBenutzer.gruppeId != 1">
						<s:hidden name="benutzer.gruppeId" />
					</s:if>
					<s:else>
						<s:select
							headerKey="-1" headerValue="%{getText('select.bitteauswaehlen')}"
							list="benutzergruppenListe" 
							key="benutzer.gruppeId"
							required="true" 
						/>
					</s:else>
					<s:if test="benutzer.gruppeId != 2">
						<s:select label="%{getText('benutzer.institut')}"
							headerKey="-1" headerValue="%{getText('select.bitteauswaehlen')}"
							list="instituteListe" 
							name="benutzer.institut"
							required="true" 
							disabled="%{#noAccess}"
						/>
					</s:if>
					<s:else>
						<s:textfield key="benutzer.matrikelnummer" cssClass="span6 input-matrikel" disabled="%{#noAccess}" />
						<s:select label="%{getText('benutzer.studiengang')}"
							headerKey="-1" headerValue="%{getText('select.bitteauswaehlen')}"
							list="studiengaengeMap" 
							key="benutzer.studiengang" 
							required="true"	
							disabled="%{#noAccess}"		 
						/>
					</s:else>
					<s:hidden name="benutzer.id" />				
					<s:if test="benutzer.gruppeId in {3, 4}">
						<s:textfield name="vertreterMail" label="%{getText('Benutzer.aktualisieren.vertreter.mail')}" cssClass="span6" disabled="%{#noAccess}" />
					</s:if>
					<s:textfield key="benutzer.telefon" cssClass="span6" disabled="%{#noAccess}" />
					<s:if test="#session.angemeldeterBenutzer.id == benutzer.id">
						<hr />
					</s:if>
					<s:textfield key="benutzer.email" required="true" cssClass="span6" disabled="%{#noAccess}" />
					<s:if test="#session.angemeldeterBenutzer.id == benutzer.id">
						<s:password name="benutzer.passwort" label="%{getText('Benutzer.aktualisieren.passwort.neu')}" cssClass="span6 secure-check" disabled="%{#noAccess}" />
						<s:password name="passwortBestaetigen" label="%{getText('Benutzer.aktualisieren.passwort.neu.bestaetigen')}" cssClass="span6 equal-check" disabled="%{#noAccess}" />
					</s:if>
				</div>
			</div>
		</div>
		<s:if test="#session.angemeldeterBenutzer.gruppeId == 2 || (#session.angemeldeterBenutzer.gruppeId == 1 && benutzer.gruppeId == 2)">
			<div class="row">
				<div class="span6">
					<h3><s:text name="Benutzer.formular.semesteradresse" /></h3>
					<div class="well">
						<s:textfield key="benutzer.strasse" required="true" cssClass="span6" disabled="%{#noAccess}" /> 
						<s:textfield key="benutzer.hausnummer" required="true" cssClass="span6" disabled="%{#noAccess}" />
						<s:textfield key="benutzer.postleitzahl" required="true" cssClass="span6" disabled="%{#noAccess}" /> 
						<s:textfield key="benutzer.stadt" required="true" cssClass="span6" disabled="%{#noAccess}" />
						<s:select label="%{getText('benutzer.land')}" 
							headerKey="-1" headerValue="%{getText('select.bitteauswaehlen')}"
							list="laenderMap"  
							name="benutzer.land" 
							required="true" 
							disabled="%{#noAccess}"				
						/>
					</div>
				</div>
				<div class="span6">
					<h3><s:text name="Benutzer.formular.heimatadresse" /></h3>
					<div class="well">
						<s:textfield key="benutzer.strasseHeimat" required="true" cssClass="span6" disabled="%{#noAccess}" /> 
						<s:textfield key="benutzer.hausnummerHeimat" required="true" cssClass="span6" disabled="%{#noAccess}" />
						<s:textfield key="benutzer.postleitzahlHeimat" required="true" cssClass="span6" disabled="%{#noAccess}" /> 
						<s:textfield key="benutzer.stadtHeimat" required="true" cssClass="span6" disabled="%{#noAccess}" />
						<s:select label="%{getText('benutzer.landHeimat')}"
							headerKey="-1" headerValue="%{getText('select.bitteauswaehlen')}"
							list="laenderMap"  
							name="benutzer.landHeimat" 
							required="true"
							disabled="%{#noAccess}"
						/>
					</div>
				</div>
			</div>
		</s:if>
		<hr />
		<a href="javascript:history.back()" class="btn"><s:text name="button.abbrechen" /></a>
		<s:submit value="%{getText('button.aenderungenSpeichern')}" cssClass="btn btn-primary" />		
		<span class="pull-right">
			<a href="<s:url action="Benutzer_anzeigen"><s:param name="benutzerId" value="benutzer.id"/></s:url>" class="btn popover-top" data-title="<s:text name="Benutzer.aktualisieren.sichtbarkeitHinweis.titel" />" data-content="<s:text name="Benutzer.aktualisieren.sichtbarkeitHinweis" />">
				<s:property value="%{#session.angemeldeterBenutzer.id == benutzer.id ? getText('button.benutzer.profilAnsehen.modus') : getText('button.benutzer.profilAnsehen.admin')}" />
			</a>
			<s:if test="#session.angemeldeterBenutzer.gruppeId == 1 || #session.angemeldeterBenutzer.id == benutzer.id">
				<a href="#benutzerLoeschen" data-toggle="modal" class="btn btn-danger"><s:text name="button.loeschen" /></a>
			</s:if>		
		</span>
	</s:form>
</s:if>

<div id="benutzerLoeschen" class="modal" style="display:none">
	<div class="modal-header">
		<button class="close" data-dismiss="modal">×</button>
		<h3><s:text name="Benutzer.loeschen.titel" /></h3>
	</div>
	<div class="modal-body">
		<s:if test="#session.angemeldeterBenutzer.id == benutzer.id">
			<s:text name="Benutzer.loeschen.text"><s:param value="benutzer.name" /></s:text>
		</s:if>
		<s:else>
			<s:text name="Benutzer.loeschen.text.admin"><s:param value="benutzer.name" /></s:text>
		</s:else>
	</div>
	<div class="modal-footer">
		<a href="#" class="btn" data-dismiss="modal"><s:text name="button.abbrechen" /></a>
		<a href="<s:url action="Benutzer_loeschen"><s:param name="benutzerId" value="benutzer.id" /></s:url>" class="btn btn-danger"><s:text name="button.loeschen" /></a>
	</div>
</div>