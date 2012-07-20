<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<div class="row">
	<div class="span12">
		<div class="hero-unit">
			<h1><s:text name="seite.index.hero" /></h1>
			<p><s:text name="seite.index.hero.sub" /></p>
			<p><a href="<s:url action="Benutzer_registrieren" />" class="btn btn-primary btn-large" ><s:text name="button.jetztRegistrieren" /></a></p>
		</div>
	</div>
</div>
<div class="row">
	<div class="span4">
		<h3><s:text name="seite.index.spalte.1" /></h3>
		<p><s:text name="seite.index.spalte.1.inhalt" /></p>
	</div>
	<s:if test="ausschreibungsListe != null || !ausschreibungsListe.isEmpty">
		<div class="span4">
			<h3><s:text name="seite.index.spalte.2" /></h3>
			<table class="table table-condensed">
				<tbody>
					<s:iterator value="ausschreibungsListe" status="status">
					
						<tr class="hyperlink trigger-tooltip" data-toggle="<s:url action="Ausschreibung_anzeigen"><s:param name="ausschreibungId" value="id" /></s:url>">
							<td><a href="javascript:void()"><s:property value="name" /></a></td>	
							<td width="8"><span class="show-tooltip square<s:property value="%{frei > 0 ? ' green' : ' red'}" />" title="<s:text name="Ausschreibung.tabelle.plaetze.tooltip"><s:param value="frei" /><s:param value="stellenzahl" /></s:text>"></span></td>	
						</tr>
					
					</s:iterator>
					<tr class="hyperlink" data-toggle="<s:url action="Ausschreibung_index" />">
						<td colspan="2" style="text-align: right"><a href="javascript:void()"><s:text name="seite.index.spalte.2.inhalt" /></a></td>				
					</tr>
				</tbody>
			</table>
		</div>
	</s:if>
</div>