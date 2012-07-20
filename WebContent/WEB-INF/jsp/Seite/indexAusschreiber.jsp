<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<div class="row startseite startseite-ausschreiber">
	<div class="span6 bewerbungen">
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
	
	<div class="span6">
		<h3><s:text name="seite.index.eigeneAusschreibungen" /></h3>
		<s:iterator value="ausschreibungsListe" status="status">
			<a href="<s:url action="Ausschreibung_anzeigen"><s:param name="ausschreibungId" value="id" /></s:url>" class="well-link">
				
				<div class="well span2">
					<s:property value="name" /><br />
					<div class="institut">
						<s:property value="%{getText('Institut.' + institut)}" />
					</div>
				</div>			
		</s:iterator>
	</div>
	</div>
</div>