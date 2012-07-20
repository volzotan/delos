<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<s:set var="activeTab" value="'datenschutz'" />

<div class="row">
	<div class="span12">
		<div class="row">
			<div class="span9 offset3">
				<h1><s:text name="Seite.datenschutz.titel" /></h1>
				<hr />
			</div>
		</div>
		<div class="row">
			<div class="span3">
				<jsp:include page="navlist.jsp" flush="false" />
			</div>
			<div class="span9">
				<div class="row">
					<div class="span9">
						<div class="alert alert-info">
							<s:text name="Seite.datenschutz.erklaerung" />
						</div>
					</div>
				</div>
				<div class="row">
					<div class="span9">
						<h3><s:text name="Seite.datenschutz.uebermittlung.titel" /></h3>
						<div class="well">
							<s:text name="Seite.datenschutz.uebermittlung" />
						</div>
					</div>
				</div>
				<div class="row">
					<div class="span9">
						<h3><s:text name="Seite.datenschutz.bestandsdaten.titel" /></h3>
						<div class="well">
							<s:text name="Seite.datenschutz.bestandsdaten" />
						</div>
					</div>
				</div>
				<div class="row">
					<div class="span9">
						<h3><s:text name="Seite.datenschutz.nutzungsdaten.titel" /></h3>
						<div class="well">
							<s:text name="Seite.datenschutz.nutzungsdaten" />
						</div>
					</div>
				</div>
				<div class="row">
					<div class="span9">
						<h3><s:text name="Seite.datenschutz.cookies.titel" /></h3>
						<div class="well">
							<s:text name="Seite.datenschutz.cookies" />
						</div>
					</div>
				</div>
				<div class="row">
					<div class="span9">
						<h3><s:text name="Seite.datenschutz.auskunftsrecht.titel" /></h3>
						<div class="well">
							<s:text name="Seite.datenschutz.auskunftsrecht" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>