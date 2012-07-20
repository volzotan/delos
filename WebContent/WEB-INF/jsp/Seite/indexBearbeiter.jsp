<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<div class="row startseite startseite-bearbeiter">
	<div class="span12">
		<h3><s:text name="seite.index.aktuelleBewerbungen" /></h3>
		<s:iterator value="bewerbungsListe" status="status">
			<a href="<s:url action="BewerbungsVorgang_anzeigen"><s:param name="bewerbungsVorgangId" value="id" /></s:url>" class="well-link">
				<div class="well">
					<s:property value="ausschreibungName" />
					<span class="status">
						<s:property value="%{getText('bewerbungsVorgang.status.' + status)}" />
					</span>
				</div>			
			</a>
		</s:iterator>
	</div>

</div>