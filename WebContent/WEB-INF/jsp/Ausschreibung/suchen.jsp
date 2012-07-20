<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<s:set var="activeTab" value="'suchen'" />

<jsp:include page="head.jsp" flush="false" />

<s:form action="Ausschreibung_suchen" method="get" acceptcharset="utf-8" theme="bootstrap">
	<div class="row">
		<div class="span3">
			<s:if test="ausschreibung">
				<div class="well jobsearch centerTxt" style="padding: 31px 8px 8px 8px">
					<h1><s:property value="listeAnzahlElemente" /><small>&nbsp;<s:text name="Ausschreibung.listen.suche.anzahl.treffer" /></small></h1>
				</div>
			</s:if>
			<s:else>
				<div class="well jobsearch img"></div>
			</s:else>
		</div>
		<div class="span8 offset1">
			<div class="row">
				<div class="span4">
					<s:select 
						headerKey="-1" headerValue="%{getText('select.bitteauswaehlen')}"
						key="ausschreibung.institut"
						list="instituteListe" 
						cssClass="span4"
						tabindex="2"
					/>
				</div>	
				<div class="span4">
					<s:select
						headerKey="-1" headerValue="%{getText('select.bitteauswaehlen')}"
						list="#{'1':getText('Ausschreibung.suchen.k.10'), 
								'2':getText('Ausschreibung.suchen.k.20'), 
								'3':getText('Ausschreibung.suchen.k.30'), 
								'4':getText('Ausschreibung.suchen.k.40'), 
								'5':getText('Ausschreibung.suchen.g.40')}" 
						key="ausschreibung.stundenzahl"
						cssClass="span4"
						tabindex="3"
					/>
				</div>		
			</div>
			<div class="row">
				<div class="span7" style="position: relative">
					<s:textfield name="ausschreibung.name" label="%{getText('ausschreibung.suche')}" cssStyle="width:490px;" cssClass="autofocus hlResult" tabindex="1" />
					<i class="suche-hinweis popover-bottom icon-info-sign icon-fade" data-content="<s:text name="Ausschreibung.suchen.hilfe" />" data-title="<s:text name="Ausschreibung.suchen.hilfe.titel" />"></i>
				</div>
				<div class="span1">
					<button type="submit" class="btn btn-primary" style="margin-top: 23px; width: 100%;" title="<s:text name="button.suchen.title" />"><i class="icon-search icon-white"></i></button>
				</div>
			</div>
		</div>	
	</div>
</s:form>

<s:if test="ausschreibung && !#emptyList">
	<jsp:include page="suchergebnis.jsp" flush="false" />
</s:if>