<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<div class="row">
	<div class="span12">
		<h1>Exception <small><s:property value="exception"/></small></h1>
	    <h2>Stack trace:</h2>
	    <pre>
	        <s:property value="exceptionStack"/>
	    </pre>					
	</div>
</div>