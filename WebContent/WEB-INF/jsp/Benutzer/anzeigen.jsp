<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<div class="row">
	<div class="span12">
		<div class="row">
			<div class="span9 offset3">
				<h1><s:text name="Benutzer.anzeigen.titel"><s:param value="benutzer.name" /></s:text></h1>
				<hr />
			</div>
		</div>
		<div class="row">
			<div class="span3">
				<div class="thumbnail" style="margin-top: -20px;">
	  				<img src="img/<s:property value="%{(benutzer.geschlecht == 1 ? 'male' : 'female')}" />-placeholder.png"  style="background-color: #f5f5f5;" alt="">
	  				<table class="table table-striped table-condensed">
						<tbody>
							<tr>
								<th colspan="2"><s:property value="%{getText('Benutzer.gruppeId.'+benutzer.gruppeId)}" /></th>
							</tr>
							<tr>
								<th><s:text name="benutzer.anzeigen.registriertSeit"/></th>
								<td><s:property value="benutzer.hinzugefuegt" />
							</tr>
							<s:if test="%{benutzer.id == #session.angemeldeterBenutzer.id || #session.angemeldeterBenutzer.gruppeId == 1}">
								<tr>
									<th><s:text name="benutzer.anzeigen.letzteAenderung"/></th>
									<td><s:property value="benutzer.bearbeitet" />
								</tr>
							</s:if>
						</tbody>
					</table>
				</div>
				<s:if test="%{benutzer.id != #session.angemeldeterBenutzer.id}">
					<a class="btn" style="width:198px; margin-top:8px;" href="<s:url action="Nachricht_erstellen"><s:param name="empfaengerId" value="benutzer.id" /></s:url>"><s:text name="button.nachricht.senden" /></a>
				</s:if>
			</div>
			<div class="span9">
				<div class="row">
					<div class="span" style="width: 340px">
						<h3><s:text name="Benutzer.anzeigen.zurPerson" /></h3>
						<div class="well well-table">
							<table class="table table-condensed">
								<tbody>
									<tr>
										<th><s:text name="benutzer.vorname"/></th>
										<td><s:property value="benutzer.vorname" />
									</tr>
									<tr>
										<th><s:text name="benutzer.nachname"/></th>
										<td><s:property value="benutzer.nachname" />
									</tr>
									<s:if test="%{benutzer.gruppeId == 2}">
										<tr>
											<th><s:text name="benutzer.matrikelnummer"/></th>
											<td><s:property value="%{benutzer.matrikelnummer == 0 ? '' : benutzer.matrikelnummer}" />
										</tr>
										<tr>
											<th><s:text name="benutzer.studiengang"/></th>
											<td><s:property value="%{benutzer.studiengang == 0 ? '' : getText('Studiengang.' + benutzer.studiengang)}" />
										</tr>
										<tr>
											<th><s:text name="benutzer.geburtstag"/></th>
											<td><s:property value="benutzer.geburtstag" />
										</tr>
										<tr>
											<th><s:text name="benutzer.nationalitaet"/></th>
											<td><s:property value="%{getText('Benutzer.nationalitaet.'+benutzer.nationalitaet)}" />
										</tr>
									</s:if>
									<s:if test="%{benutzer.gruppeId != 2}">
										<tr>
											<th><s:text name="benutzer.institut"/></th>
											<td><s:property value="%{getText('Institut.'+benutzer.institut)}" />
										</tr>
									</s:if>
								</tbody>
							</table>
						</div>
					</div>
					<div class="span" style="width: 340px">
						<h3><s:text name="Benutzer.anzeigen.kontakt" /></h3>
						<div class="well well-table">
							<table class="table table-condensed">
								<tbody>
									<tr>
										<th><s:text name="benutzer.email"/></th>
										<td><s:property value="benutzer.email" /></td>
									</tr>
									<tr>
										<th><s:text name="benutzer.telefon"/></th>
										<td><s:property value="benutzer.telefon" /></td>
									</tr>
									<s:if test="%{benutzer.gruppeId != 2}">
										<tr>
											<th><s:text name="benutzer.raum.short"/></th>
											<td><s:property value="benutzer.raum" /></td>
										</tr>
										<s:if test="%{benutzer.gruppeId > 2 && benutzer.vertreterId > 0}">
											<tr>
												<th><s:text name="benutzer.vertreterName"/></th>
												<td><a title="<s:text name="Benutzer.anzeigen.zumProfil"/>" href="<s:url action="Benutzer_anzeigen"><s:param name="benutzerId" value="benutzer.vertreterId" /></s:url>"><s:property value="benutzer.vertreterName" /></a>
											</tr>
										</s:if>
									</s:if>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<s:if test="%{benutzer.gruppeId == 2}">
					<div class="row">
						<div class="span" style="width: 340px">
							<h3><s:text name="Benutzer.formular.semesteradresse" /></h3>
							<div class="well well-table">
								<table class="table table-condensed">
									<tbody>
										<tr>
											<th><s:text name="benutzer.strasse"/></th>
											<td><s:property value="benutzer.strasse" /></td>
										</tr>
										<tr>
											<th><s:text name="benutzer.hausnummer"/></th>
											<td><s:property value="benutzer.hausnummer" /></td>
										</tr>
										<tr>
											<th><s:text name="benutzer.postleitzahl"/></th>
											<td><s:property value="benutzer.postleitzahl" /></td>
										</tr>
										<tr>
											<th><s:text name="benutzer.stadt"/></th>
											<td><s:property value="benutzer.stadt" />
										</tr>
										<tr>
											<th><s:text name="benutzer.land"/></th>
											<td><s:property value="%{getText('Benutzer.land.'+benutzer.land)}" /></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div class="span" style="width: 340px">
							<h3><s:text name="Benutzer.formular.heimatadresse" /></h3>
							<div class="well well-table">
								<table class="table table-condensed">
									<tbody>
										<tr>
											<th><s:text name="benutzer.strasseHeimat"/></th>
											<td><s:property value="benutzer.strasseHeimat" /></td>
										</tr>
										<tr>
											<th><s:text name="benutzer.hausnummerHeimat"/></th>
											<td><s:property value="benutzer.hausnummerHeimat" /></td>
										</tr>
										<tr>
											<th><s:text name="benutzer.postleitzahlHeimat"/></th>
											<td><s:property value="benutzer.postleitzahlHeimat" /></td>
										</tr>
										<tr>
											<th><s:text name="benutzer.stadtHeimat"/></th>
											<td><s:property value="benutzer.stadtHeimat" /></td>
										</tr>
										<tr>
											<th><s:text name="benutzer.landHeimat"/></th>
											<td><s:property value="%{getText('Benutzer.land.'+benutzer.landHeimat)}" /></td>
										</tr>
									</tbody>
								</table>										
							</div>
						</div>
					</div>
				</s:if>
				<hr />
				<a href="javascript:history.back()" class="btn"><s:text name="button.zurueck" /></a>
				<s:if test="#session.angemeldeterBenutzer.gruppeId == 1 || #session.angemeldeterBenutzer.id == benutzer.id">
					<a class="btn btn-primary" href="<s:url action="Benutzer_aktualisieren"><s:param name="benutzerId" value="benutzer.id" /></s:url>"><s:text name="button.profilBearbeiten" /></a>
				</s:if>
			</div>
		</div>
	</div>
</div>