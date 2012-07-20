 <%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<div class="row">
	<div class="span12 grey-to-white">
		<div class="row">
			<div class="span8 offset2 delos-404">
				<h3><s:text name="404.titel" /></h3>
			</div>
		</div>
		<div class="row">
			<div class="span8 offset2">
				<div class="well">
					<s:text name="404.beschreibung" /><br />
					<a href="javascript:history.back()" title=""><s:text name="404.verweis" /></a>
				</div>
			</div>
		</div>
	</div>
</div>