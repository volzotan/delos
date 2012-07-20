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
				<h2><s:text name="Benutzer.passwortAnfordern.titel" /></h2>
			</div>
		</div>
		<div class="row">
			<div class="span8 offset2">
				<div class="well">
					<s:form action="Benutzer_passwortAnfordern" method="post" acceptcharset="utf-8" validate="true" theme="bootstrap">
						<s:textfield name="benutzer.email" label="%{getText('Benutzer.passwortAnfordern.beschreibung')}" cssClass="span8 autofocus" tabindex="1" />
						<div class="row">
							<div class="span8">
								<a href="<s:url action="Benutzer_anmelden" />" class="btn pull-right"><s:text name="button.zurueck" /></a>
								<s:submit value="%{getText('button.anfordern')}" cssClass="btn btn-primary" />
							</div>
						</div>
					</s:form>
				</div>
			</div>
		</div>
	</div>
</div>