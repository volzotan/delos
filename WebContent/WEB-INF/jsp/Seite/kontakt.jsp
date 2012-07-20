<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<s:set var="activeTab" value="'kontakt'" />

<div class="row">
	<div class="span12">
		<div class="row">
			<div class="span8 offset4">
				<h1><s:text name="Seite.kontakt.titel" /></h1>
				<hr />
			</div>
		</div>
		<div class="row">
			<div class="span4">
				<jsp:include page="navlist.jsp" flush="false" />
			</div>
			<div class="span8">
				<div class="row">
					<div class="span8">
						<div class="well">
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>