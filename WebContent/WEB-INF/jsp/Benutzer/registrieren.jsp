<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<s:if test="#session.loggedIn">
	<jsp:include page="head.jsp" flush="false" />
</s:if>

<s:form action="Benutzer_registrieren" method="post" acceptcharset="utf-8" validate="true" theme="bootstrap">
	<div id="controlledSlide" class="carousel slide">
		<s:hidden name="benutzer.id" />
		<s:if test="#session.angemeldeterBenutzer.gruppeId != 1">
			<s:hidden name="benutzer.gruppeId" />
		</s:if>
		<div class="row">
			<div class="span12">
				<h2><s:text name="Benutzer.registrieren.titel" /></h2>
			</div>
		</div>
		<div class="carousel-inner">
			<div class="active item">
				<div class="row">
					<div class="span6">
						<div class="well">
							<s:select
								headerKey="-1" headerValue="%{getText('select.bitteauswaehlen')}"
								list="anredenListe" 
								key="benutzer.geschlecht"
								required="true" 
							/>
							<s:textfield key="benutzer.vorname" required="true" cssClass="span6" />
							<s:textfield key="benutzer.nachname" required="true" cssClass="span6" />
							<s:if test="!#session.loggedIn">
								<s:textfield key="benutzer.geburtstag" displayFormat="%{getText('validierung.dateFormat')}" cssClass="span6" />
							</s:if>
						</div>
					</div>
					<div class="span6">
						<div class="well">
							<s:if test="!#session.loggedIn">
								<s:textfield key="benutzer.telefon" cssClass="span6" />
							</s:if>
							<s:textfield key="benutzer.email" required="true" cssClass="span6" />
							<s:if test="!#session.loggedIn">
								<s:password key="benutzer.passwort" required="true" cssClass="span6 secure-check" />
								<s:password name="passwortBestaetigen" label="%{getText('Benutzer.registrieren.passwortWiederholen')}" required="true" cssClass="span6 equal-check" />
							</s:if>
							<s:else>
								<s:select
									headerKey="-1" headerValue="%{getText('select.bitteauswaehlen')}"
									list="#{'1':getText('Benutzer.gruppeId.1'), '3':getText('Benutzer.gruppeId.3'), '4':getText('Benutzer.gruppeId.4')}" 
									key="benutzer.gruppeId"
									required="true"
								/>
								<s:select label="%{getText('benutzer.institut')}"
									headerKey="-1" headerValue="%{getText('select.bitteauswaehlen')}"
									list="instituteListe" 
									name="benutzer.institut"
									required="true"
								/>
							</s:else>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="span12">
						<p style="text-align:right"><s:text name="Benutzer.registrieren.pflichtfeldHinweis" /></p>
					</div>
				</div>
				<hr />
				<s:if test="!#session.loggedIn">
					<a href="javascript:void(0)" class="btn btn-primary slideToNextItem"><s:text name="button.weiter" /></a>
				</s:if>
				<s:else>
					<button type="submit" class="btn btn-primary"><s:text name="button.kontoErstellen" /></button>
				</s:else>
				<a href="<s:property value="getActionHistoryStack()[2]" />" class="btn"><s:text name="button.abbrechen" /></a>
			</div>
			<s:if test="!#session.loggedIn">
				<div class="item">
					<div class="row">
						<div class="span6">
							<div class="well">
								<s:select label="%{getText('benutzer.nationalitaet')}"
									headerKey="-1" headerValue="%{getText('select.bitteauswaehlen')}"
									list="nationalitaetenMap"  
									key="benutzer.nationalitaet" 
									required="true"
								/>
								<s:select label="%{getText('benutzer.studiengang')}"
									headerKey="-1" headerValue="%{getText('select.bitteauswaehlen')}"
									list="studiengaengeMap"  
									key="benutzer.studiengang" 
									required="true"
								/>
								<s:textfield key="benutzer.matrikelnummer" required="true" cssClass="span6 input-matrikel" /> 
							</div>
						</div>
						<div class="span6">
							<div class="alert alert-info">
								<s:text name="Benutzer.registrieren.info.daten" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="span6">
							<h3><s:text name="Benutzer.formular.semesteradresse" /></h3>
							<div class="well">
								<s:textfield key="benutzer.strasse" required="true" cssClass="span6" /> 
								<s:textfield key="benutzer.hausnummer" required="true" cssClass="span6" />
								<s:textfield key="benutzer.postleitzahl" required="true" cssClass="span6" /> 
								<s:textfield key="benutzer.stadt" required="true" cssClass="span6" />
								<s:select label="%{getText('benutzer.land')}" 
									headerKey="-1" headerValue="%{getText('select.bitteauswaehlen')}"
									list="laenderMap"   
									name="benutzer.land" 
									required="true" 			
								/>
							</div>
						</div>
						<div class="span6">
							<h3><s:text name="Benutzer.formular.heimatadresse" /><button type="button" class="btn btn-mini adresseIdentischToggle" title="<s:text name="Benutzer.registrieren.adressenIdentisch.titel" />" style="float:right" data-toggle="button"><s:text name="Benutzer.registrieren.adressenIdentisch" /></button></h3>
							<div class="well">
								<s:textfield key="benutzer.strasseHeimat" required="true" cssClass="span6" /> 
								<s:textfield key="benutzer.hausnummerHeimat" required="true" cssClass="span6" />
								<s:textfield key="benutzer.postleitzahlHeimat" required="true" cssClass="span6" /> 
								<s:textfield key="benutzer.stadtHeimat" required="true" cssClass="span6" />
								<s:select label="%{getText('benutzer.landHeimat')}"
									headerKey="-1" headerValue="%{getText('select.bitteauswaehlen')}"
									list="laenderMap"   
									name="benutzer.landHeimat" 
									required="true"
								/>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="span6 agb-check">
							<s:checkbox name="agbCheckbox" label="%{getText('Benutzer.registrieren.agb.dsb.check')}" />
						</div>
						<div class="span6">
							<p style="text-align:right"><s:text name="Benutzer.registrieren.pflichtfeldHinweis" /></p>
						</div>
					</div>
					<hr />
					<button type="submit" class="btn btn-primary"><s:text name="button.kontoErstellen" /></button>
					<a href="<s:property value="getActionHistoryStack()[2]" />" class="btn"><s:text name="button.abbrechen" /></a>
					<a href="javascript:void(0)" class="btn pull-right slideToPrevItem"><s:text name="button.zurueck" /></a>
				</div>
			</s:if>
		</div>
	</div>
</s:form>