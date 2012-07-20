<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<s:if test="#session.angemeldeterBenutzer.gruppeId == 3">
	<s:set name="noAccess" value="false" />
</s:if>
<s:else>
	<s:set name="noAccess" value="true" />
</s:else>

<h1><s:if test="%{bewerbungsVorgang.ausschreibungName.length() > 55}"><s:text name="BewerbungsVorgang.aktualisieren.titel" ><s:param value="%{bewerbungsVorgang.ausschreibungName.substring(0, 55)}" /></s:text></s:if><s:else><s:property value="bewerbungsVorgang.ausschreibungName" /></s:else></h1>
<s:if test="#session.angemeldeterBenutzer.gruppeId != 2">
	<h1><small><s:property value="bewerbungsVorgang.bewerberName" /></small></h1>
</s:if>

<hr />
<s:form action="BewerbungsVorgang_aktualisieren" method="post" acceptcharset="utf-8" theme="bootstrap" cssClass="">
	<s:hidden name="bewerbungsVorgang.id" />
	
	<div class="row">
		<div class="span6">
			<s:textfield key="bewerbungsVorgang.ausschreiberName" cssClass="span6" required="true" readonly="true" />
		</div>
		<div class="span6">
			<s:if test="#session.angemeldeterBenutzer.gruppeId == 3">
				<s:select 
					headerKey="-1" headerValue="%{getText('select.bitteauswaehlen')}"
					key="bewerbungsVorgang.bearbeiterId"
					list="bearbeiterSelect"
					listKey="id"
					listValue="name"
					required="true" 
					cssClass="span6"
				/>	
			</s:if>
			<s:else>
				<s:textfield key="bewerbungsVorgang.bearbeiterName" cssClass="span6" required="true" readonly="true" />
			</s:else>
		</div>
	</div>
	<div class="row">
		<div class="span4">
			<s:textfield key="bewerbungsVorgang.stundenzahl" required="true" cssClass="span4" readonly="%{#noAccess}" />
		</div>
		<div class="span4">
			<sj:datepicker key="bewerbungsVorgang.startet" displayFormat="%{getText('validierung.dateFormat')}" buttonImageOnly="true" required="true" cssClass="span4" readonly="%{#noAccess}" />
		</div>
		<div class="span4">
			<sj:datepicker key="bewerbungsVorgang.endet" displayFormat="%{getText('validierung.dateFormat')}" buttonImageOnly="true" required="true" cssClass="span4" readonly="%{#noAccess}" />
		</div>
	</div>	

	<s:if test="#session.angemeldeterBenutzer.gruppeId == 2">
	<hr />
	<div class="row">
		<div class="span8">
			<s:textarea name="bewerbungsVorgang.kommentar" escape="false" rows="15" cssClass="span8" required="true" />
		</div>
		<div class="span4">
			<div class="alert alert-info" style="margin-top: 38px;">
				<s:text name="BewerbungsVorgang.erstellen.kommentar.beschreibung" />
			</div>
		</div>
	</div>
	</s:if>
	<hr />
	<div class="btn-toolbar floatable">
		<div class="btn-group">
			<a href="javascript:history.back()" class="btn"><s:text name="button.zurueck" /></a>
		</div>
		<div class="btn-group">
			<s:submit value="%{getText('button.abschicken')}" cssClass="btn btn-primary" />
		</div>
		<div class="btn-group pull-right">
			<s:reset value="%{getText('button.zuruecksetzen')}" cssClass="btn" />
		</div>
	</div>
</s:form>