<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<s:set var="activeTab" value="'impressum'" />

<div class="row">
	<div class="span12">
		<div class="row">
			<div class="span9 offset3">
				<h1><s:text name="Seite.impressum.titel" /></h1>
				<hr />
			</div>
		</div>
		<div class="row">
			<div class="span3">
				<jsp:include page="navlist.jsp" flush="false" />
			</div>
			<div class="span9">
				<div class="alert alert-info">
					<s:text name="Seite.impressum.untertitel" />
				</div>
				<div class="row">
					<div class="span" style="width: 340px">
						<h3><s:text name="Seite.impressum.anbieter.titel" /></h3>
						<div class="well">
							<s:text name="Seite.impressum.anbieter" />
						</div>
					</div>
					<div class="span" style="width: 340px">
						<h3><s:text name="Seite.impressum.ansprechpartner.titel" /></h3>
						<div class="well">
							<s:text name="Seite.impressum.ansprechpartner" />
						</div>
					</div>
				</div>
				<h3 style="margin-bottom: 8px;"><s:text name="Seite.impressum.team.titel" /></h3>
				<div class="row">
					<div class="span" style="width:100px">
						<div class="thumbnail popover-top" data-original-title="<s:text name="Seite.impressum.team.bohner" />" data-content="<s:text name="Seite.impressum.team.bohner.text" />">
			  				<img src="img/team-thumbs/bohner.jpg" alt="">
						</div>
					</div>
					<div class="span" style="width:100px">
						<div class="thumbnail popover-top" data-original-title="<s:text name="Seite.impressum.team.getschmann" />" data-content="<s:text name="Seite.impressum.team.getschmann.text" />">
			  				<img src="img/team-thumbs/getschmann.jpg" alt="">
						</div>
					</div>
					<div class="span" style="width:100px">
						<div class="thumbnail popover-top" data-original-title="<s:text name="Seite.impressum.team.grottendieck" />" data-content="<s:text name="Seite.impressum.team.grottendieck.text" />">
			  				<img src="img/team-thumbs/grottendieck.jpg" alt="">
						</div>
					</div>
					<div class="span" style="width:100px">
						<div class="thumbnail popover-top" data-original-title="<s:text name="Seite.impressum.team.hofer" />" data-content="<s:text name="Seite.impressum.team.hofer.text" />">
			  				<img src="img/team-thumbs/hofer.jpg" alt="">
						</div>
					</div>
					<div class="span" style="width:100px">
						<div class="thumbnail popover-top" data-original-title="<s:text name="Seite.impressum.team.musmann" />" data-content="<s:text name="Seite.impressum.team.musmann.text" />">
			  				<img src="img/team-thumbs/musmann.jpg" alt="">
						</div>
					</div>
					<div class="span" style="width:100px">
						<div class="thumbnail popover-top" data-original-title="<s:text name="Seite.impressum.team.osswald" />" data-content="<s:text name="Seite.impressum.team.osswald.text" />">
			  				<img src="img/team-thumbs/osswald.jpg" alt="">
						</div>
					</div>
				</div>
				<hr />
				<h3><s:text name="Seite.impressum.externeLinks.titel" /></h3>
				<div class="well">
					<s:text name="Seite.impressum.externeLinks" />
				</div>
				<h3><s:text name="Seite.impressum.informationen.titel" /></h3>
				<div class="well">
					<s:text name="Seite.impressum.informationen" />
				</div>
			</div>
		</div>
	</div>
</div>