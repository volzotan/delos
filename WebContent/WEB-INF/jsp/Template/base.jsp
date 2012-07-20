<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sb" uri="/struts-bootstrap-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<sj:head/><sb:head/>
		<link href="<s:url value='/css/bootstrap-wysihtml5.css'/>" rel="stylesheet" type="text/css" media="screen" />
		<link href="<s:url value='/css/global.css'/>" rel="stylesheet" type="text/css" media="screen" />
		<link href="<s:url value='/css/print.css'/>" rel="stylesheet" type="text/css" media="print" />			
		<!--[if lt IE 9]><script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
		<link rel="icon" href="<s:url value='/img/favicon.ico'/>" type="image/png">
		<title><tiles:insertAttribute name="title" ignore="true" /></title>
	</head>
	<body>
  	<div id="super" class="container">
		<tiles:insertAttribute name="header" />
  		<div id="container">
  			<div class="row">
				<div class="span12">
					<s:if test="#session.benachrichtigungenError != null">
						<div class="alert-error system">
							<s:iterator value="#session.benachrichtigungenError" status="status">
								<p><strong><s:property value="titel" /></strong> <s:property value="nachricht" /></p>
							</s:iterator>
						</div>
						<c:remove var="benachrichtigungenError"/>
					</s:if>
					<s:if test="#session.benachrichtigungenDanger != null">
						<div class="alert-danger system">
							<s:iterator value="#session.benachrichtigungenDanger" status="status">
								<p><strong><s:property value="titel" /></strong> <s:property value="nachricht" /></p>
							</s:iterator>
						</div>
						<c:remove var="benachrichtigungenDanger"/>
					</s:if>
					<s:if test="#session.benachrichtigungenSuccess != null">
						<div class="alert-success system">
							<s:iterator value="#session.benachrichtigungenSuccess" status="status">
								<p><strong><s:property value="titel" /></strong> <s:property value="nachricht" /></p>
							</s:iterator>
						</div>
						<c:remove var="benachrichtigungenSuccess"/>
					</s:if>
					<s:if test="#session.benachrichtigungenInfo != null">
						<div class="alert-info system">
							<s:iterator value="#session.benachrichtigungenInfo" status="status">
								<p><strong><s:property value="titel" /></strong> <s:property value="nachricht" /></p>
							</s:iterator>							
						</div>
						<c:remove var="benachrichtigungenInfo"/>
					</s:if>
				</div>
			</div>
  			<div id="content">
				<tiles:insertAttribute name="body" />
  			</div>
  		</div>
  		<div id="whitespace"></div>
  	</div>
  	
  	<tiles:insertAttribute name="footer" />
  	<!-- execute some script -->
  	<script type="text/javascript" src="<s:url value='/js/wysihtml5-0.3.0.min.js'/>"></script>
  	<script type="text/javascript" src="<s:url value='/js/bootstrap-wysihtml5.js'/>"></script>
  	<script type="text/javascript" src="<s:url value='/js/jquery-hl.js'/>"></script>
	<script type="text/javascript" src="<s:url value='/js/jquery-exec.js'/>"></script>
  </body>
</html>