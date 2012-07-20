<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<div class="row">
	<div class="span12 grey-to-white">
			<div class="row">
				<div class="span8 offset2">
					<h2><s:text name="Benutzer.anmelden.titel" /></h2>
				</div>
			</div>
			<div class="row">
				<div class="span8 offset2">
					<div class="well">
						<s:form action="Benutzer_anmelden" method="post" acceptcharset="utf-8" theme="bootstrap">
							<div class="row">
								<div class="span4">
									<s:textfield key="benutzer.email" cssClass="span4 autofocus" tabindex="1" />
								</div>
								<div class="span4">
									<s:password key="benutzer.passwort" cssClass="span4" tabindex="2" />
								</div>
							</div>
							<div class="row">
								<div class="span8">
									<a href="<s:url action="Benutzer_passwortAnfordern" />" class="btn pull-right"><s:text name="button.passwortVergessen" /></a>
									<s:submit value="%{getText('button.anmelden')}" cssClass="btn btn-primary" />
								</div>
							</div>
						</s:form>
					</div>
					<p style="text-align:right;">
						<a href="<s:url action="Benutzer_registrieren" />"><s:text name="Benutzer.anmelden.nochKeinKonto" /></a>
					</p>
				</div>
			</div>
		</div>
	</div>