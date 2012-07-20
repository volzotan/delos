<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<h1><s:property value="bewerbungsVorgang.ausschreibungName" /></h1>
<hr />
<div class="row">
	<div class="span6">
		<div class="well well-table">
			<table class="table table-condensed">
				<tbody>
					<tr>
						<th><s:text name="Ausschreibung.tabelle.institut"/></th>
						<td><s:property value="%{getText('Institut.'+bewerbungsVorgang.institut)}" /></td>
					</tr>
					<tr>
						<th><s:text name="Ausschreibung.tabelle.ausschreiber"/></th>
						<td><a href="<s:url action="Benutzer_anzeigen"><s:param name="benutzerId" value="bewerbungsVorgang.ausschreiberId" /></s:url>"><s:property value="bewerbungsVorgang.ausschreiberName" /></a></td>
					</tr>
					<tr>
						<th><s:text name="Ausschreibung.tabelle.bearbeiter"/></th>
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
						<th><s:text name="Ausschreibung.tabelle.stunden"/></th>
						<td style="text-align:right;"><s:property value="bewerbungsVorgang.stundenzahl" /></td>
					</tr>
					<tr>
						<th><s:text name="Ausschreibung.tabelle.beginn"/></th>
						<td style="text-align:right;"><s:property value="bewerbungsVorgang.startet" /></td>
					</tr>
					<tr>
						<th><s:text name="Ausschreibung.tabelle.ende"/></th>
						<td style="text-align:right;"><s:property value="bewerbungsVorgang.endet" /></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
<hr />
<s:form action="BewerbungsVorgang_erstellen" method="post" acceptcharset="utf-8" theme="bootstrap">
	<s:hidden name="bewerbungsVorgang.id" />
	<s:hidden name="bewerbungsVorgang.bewerberId" value="%{#session.angemeldeterBenutzer.getId()}" />
	<s:hidden name="bewerbungsVorgang.ausschreibungId" />
	<s:hidden name="bewerbungsVorgang.institut" />
	<s:hidden name="bewerbungsVorgang.ausschreiberId" />
	<s:hidden name="bewerbungsVorgang.bearbeiterId" />
	<s:hidden name="bewerbungsVorgang.startet" />
	<s:hidden name="bewerbungsVorgang.endet" />
	<s:hidden name="bewerbungsVorgang.stundenzahl" />
	<div class="row">
		<div class="span8">
			<s:textarea name="bewerbungsVorgang.kommentar" escape="false" rows="18" cssClass="span8" required="true" />
		</div>
		<div class="span4">
			<div class="alert alert-info" style="margin-top:38px;">
				<s:text name="BewerbungsVorgang.erstellen.kommentar.beschreibung" />
			</div>
		</div>
	</div>
	<hr />
	<div class="btn-toolbar floatable">
		<div class="btn-group">
			<a href="javascript:history.back()" tabindex="5" class="btn"><s:text name="button.zurueck" /></a>
		</div>
		<div class="btn-group">
			<s:submit value="%{getText('button.abschicken')}" cssClass="btn btn-primary" />
		</div>
		<div class="btn-group pull-right">
			<s:reset value="%{getText('button.zuruecksetzen')}" cssClass="btn" />
		</div>
	</div>
</s:form>