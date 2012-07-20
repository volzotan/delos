<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<s:form action="%{formularZiel}" method="post" acceptcharset="utf-8" theme="bootstrap">
	<s:hidden name="ausschreibung.ausschreiberId" />
	<s:hidden name="ausschreibung.id" />
	
	<div class="row">
		<div class="span12">
			<div class="well">
				<s:textfield key="ausschreibung.name" required="true" cssClass="span12" />
			</div>
		</div>
	</div>
	<div class="row">
		<div class="span6">
			<h3><s:text name="Ausschreibung.sonstiges.verwaltung" /></h3>
			<div class="well">
				<s:if test="#session.angemeldeterBenutzer.gruppeId != 1">
					<s:textfield label="%{getText('ausschreibung.institut')}" value="%{getText('Institut.'+#session.angemeldeterBenutzer.getInstitut())}" disabled="true" cssClass="span6" />
					<s:hidden name="ausschreibung.institut" value="%{#session.angemeldeterBenutzer.getInstitut()}" />
				</s:if>
				<s:else>
					<s:select 
						headerKey="-1" headerValue="%{getText('select.bitteauswaehlen')}"
						key="ausschreibung.institut"
						list="instituteListe"
						required="true" 
					/>
				</s:else>
				<s:textfield key="ausschreibung.ausschreiberName" disabled="true" cssClass="span6" />
				<s:select 
					headerKey="-1" headerValue="%{getText('select.bitteauswaehlen')}"
					key="ausschreibung.bearbeiterId"
					list="bearbeiterSelect"
					listKey="id"
					listValue="name"
					required="true" 
				/>
			</div>
		</div>
		<div class="span6">
			<h3><s:text name="Ausschreibung.sonstiges.termine" /></h3>
			<div class="well">
				<sj:datepicker key="ausschreibung.bewerbungsfrist" displayFormat="%{getText('validierung.dateFormat')}" buttonImageOnly="true" required="true" cssClass="span6" />
				<sj:datepicker key="ausschreibung.startet" displayFormat="%{getText('validierung.dateFormat')}" buttonImageOnly="true" required="true" cssClass="span6" />
				<sj:datepicker key="ausschreibung.endet" displayFormat="%{getText('validierung.dateFormat')}" buttonImageOnly="true" required="true" cssClass="span6" cssStyle="margin-bottom:0px;" />
			</div>
		</div>
	</div>
	<div class="row">
		<div class="span6">
			<div class="well">
				<s:textfield key="ausschreibung.stundenzahl" required="true" cssClass="span6" />
			</div>
		</div>
		<div class="span6">
			<div class="well">
				<s:textfield key="ausschreibung.stellenzahl" required="true" cssClass="span6" />
			</div>
		</div>
	</div>
	<div class="row">
		<div class="span6">
			<h3><s:text name="ausschreibung.beschreibung" /></h3>
			<div class="well">
				<s:textarea name="ausschreibung.beschreibung" rows="15" required="true" cssClass="span6" />
			</div>
		</div>
		<div class="span6">
			<h3><s:text name="ausschreibung.voraussetzungen" /></h3>
			<div class="well">
				<s:textarea name="ausschreibung.voraussetzungen" rows="15" cssClass="span6" />
			</div>
		</div>
	</div>
	<hr />
	<s:submit value="%{formularZiel == 'Ausschreibung_erstellen' ? getText('button.ausschreibungErstellen') : getText('button.aenderungenSpeichern')}" cssClass="btn btn-primary" />
	<a class="btn" href="javascript:history.back()"><s:text name="button.abbrechen" /></a>
	
	<s:if test="0 < ausschreibung.id">
		<s:if test="#session.angemeldeterBenutzer.gruppeId == 1 || #session.angemeldeterBenutzer.id == ausschreibung.ausschreiberId">
			<a class="btn btn-danger pull-right" href="#ausschreibungLoeschen" data-toggle="modal"><s:text name="button.loeschen" /></a>
		</s:if>		
	</s:if>		
</s:form>

<s:if test="0 < ausschreibung.id">
	<s:if test="#session.angemeldeterBenutzer.gruppeId == 1 || #session.angemeldeterBenutzer.id == ausschreibung.ausschreiberId">
		<div class="modal" id="ausschreibungLoeschen" style="display:none">
			<div class="modal-header">
				<h3><s:text name="Ausschreibung.loeschen.titel" /></h3>
			</div>
			<div class="modal-body">
				<s:text name="Ausschreibung.loeschen.text"><s:param value="ausschreibung.name" /></s:text>
			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal"><s:text name="button.abbrechen" /></button>
				<a href="<s:url action="Ausschreibung_loeschen"><s:param name="ausschreibungId" value="ausschreibung.id" /></s:url>" class="btn btn-danger"><s:text name="button.loeschen" /></a>
			</div>
		</div>
	</s:if>
</s:if>