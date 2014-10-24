<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ page session="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>B&W Garden</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<script src="<c:url value="/resources/js/jquery-1.11.1.min.js" />"></script>
	<script src="<c:url value="/resources/js/jquery-ui.min.js" />"></script>
	<script src="<c:url value="/resources/js/jquery.autocomplete.min.js" />"></script>
	<link href="<c:url value="/resources/css/jquery-ui.css" />" rel="stylesheet">
	<link href="<c:url value="/resources/css/photos.css" />" rel="stylesheet">
	<link href="<c:url value="/resources/css/tooltip.css" />" rel="stylesheet">
	<link href="<c:url value="/resources/css/autocomplete.css" />" rel="stylesheet">
</head>
<body>
	<div class="wrapper">
		<div class="main">
			<header>
				<div class="title">
					<h1><abbr title="Black and White">B&W</abbr> Garden</h1>
					<h2>the beauty of black and white photography</h2>
				</div>
			</header>
			
			<div class="results">
			<table>
				<c:set var="count" value="0" scope="page" />
				<c:forEach var="result" items="${results}" >
					<c:if test="${(count mod 3) == 0}"><tr></c:if>
					<td>
						<a href="<c:url value="/photo/${result.id}"/>">
					  		<img title="<c:out value="${result.title}"/>" src="<c:url value="/jpg/${result.id}" />"  />
					  	</a>
					<c:set var="count" value="${count + 1}" scope="page"/>
					</td>
					<c:if test="${(count mod 3) == 0}"></tr></c:if>
				</c:forEach>
			</table>
			</div>
		</div>
		<aside>
			<div class="search">
				<form method="POST" action="/photos/search/terms">
					<div class="search-input">
						<input  name="terms" id="input-query" value="${terms}"/>
					</div>
					<div class="search-submitt">
						<input type="submit" name="submit" value="">
					</div>
				</form>
			</div>
			<div class="facets">
				<c:forEach var="facet" items="${facets}" >
					<h3><c:out value="${facet.name}"/></h3>
					<c:if test="${filters.containsKey(facet.name)}">
						<a href="<c:url value="/search/reset/${facet.name}"/>">[Reset filter]</a>
					</c:if>
					<c:forEach var="value" items="${facet.facetValues}" begin="0" end="100" >
						<nav id="facet">
							<a href="<c:url value="/search/filter/${facet.name}:${value.label}" />"><c:out value="${value.name}  (${value.count})"/></a>
						</nav>
					</c:forEach>
				</c:forEach>
			</div>
		</aside>
	</div>
	<footer class="site-footer">
		<a href="http://dbpedia.org/"><img src="<c:url value="/resources/img/dbpedia.png" />" /></a>
		<a href="http://www.opencalais.com/"><img src="<c:url value="/resources/img/opencalais.png" />" /></a>
		<a href="http://www.marklogic.com/"><img src="<c:url value="/resources/img/marklogic.png" />" /></a>
		<div>Built with </div> 
	</footer>
			
  <script>
  
  $(document).ready(function() {
 
	$('#input-query').autocomplete({
		serviceUrl: '${pageContext.request.contextPath}/suggest/titles',
		paramName: "terms",
		delimiter: ",",
	   	transformResult: function(response) {
 
		return {      	
		  //must convert json to javascript object before process
		  suggestions: $.map($.parseJSON(response), function(item) {
 
		      return { value: item };
		   })
 
		 };
 
            }
 
	 });
 
  });
  </script>
  <script>
  $(function() {
	var du = 1200;  
    $( document ).tooltip({
      open: function (event, ui) {
          setTimeout(function () {
              $(ui.tooltip).hide("puff");
          }, du);
      },
      position: {
        my: "center bottom+50",
        at: "center top",
        using: function( position, feedback ) {
          $( this ).css( position );
          $( "<div>" )
            .addClass( "arrow" )
            .addClass( feedback.vertical )
            .addClass( feedback.horizontal )
            .appendTo( this );
        }
      }
    });
  });
  </script>
  

</body>
</html>