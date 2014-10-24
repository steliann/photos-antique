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
	<script src="<c:url value="/resources/js/jquery.autocomplete.min.js" />"></script>
	<script src="<c:url value="/resources/jquery.webRating.min.js" />"></script>
	<link href="<c:url value="/resources/css/photos.css" />" rel="stylesheet">
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
			<div class="details">
				<div data-webRating="2.5" data-webRatingN="5" data-webRatingArg='{"type":"book","uid":12}' class="summary" role="article">
					<h3>${photo.title}</h3>
					<p>${photo.description}</p>
				</div>
		
				<div class="pdf" >
				  	<a href="<c:url value="/pdf/${photo.id}"/>"><img src="<c:url value="/resources/img/pdf.png" />" /></a>
				</div>
				
				<c:choose>
      			<c:when test="${photo.explicit}">
				<div class="image-explicit">
      			</c:when>

      			<c:otherwise>
				<div class="image">
      			</c:otherwise>
				</c:choose>
				  	<img src="<c:url value="/jpg/${photo.id}" />"  />
				</div>
			</div>
		</div>
		<aside>
			<div class="facets">
				<c:if test="${photo.tags.size() > 0}"></tr>
					<h3>Tags</h3>
					<c:forEach var="tag" items="${photo.tags}" >
						<nav><c:out value="${tag}"/></nav>
					</c:forEach>
				</c:if>
				<h3>Author</h3>
				<nav><a href="<c:url value="/search/filter/author:${photo.author}" />"><c:out value="${photo.author}"/></a></nav>
				<h3>Country</h3>
				<nav><a href="<c:url value="/search/filter/country:${photo.country}" />"><c:out value="${photo.country}"/></a></nav>
				<h3>Year</h3>
				<nav><a href="<c:url value="/search/filter/year:${photo.year}" />"><c:out value="${photo.year}"/></a></nav>
			</div>

			<div class="tag-input">
				<form method="POST" action="<c:url value="/photo/addtag/${photo.id}"/>">
					<div class="addtag-text">
						<input name="tagName" id="input-tag-name"/>
					</div>
					<div class="addtag-submitt">
						<input type="submit" name="addtag" value="">
					</div>
				</form>
				 
				<form method="POST" action="<c:url value="/photo/semtag/${photo.id}"/>">
					<div class="semtag-submitt">
						<input type="submit" name="semtag" value="semantic">
					</div>
				</form>
			
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
 
	$('#input-tag-name').autocomplete({
		serviceUrl: '${pageContext.request.contextPath}/suggest/tags',
		paramName: "tagName",
		delimiter: ",",
	   transformResult: function(response) {
 
		return {      	
		  //must convert json to javascript object before process
		  suggestions: $.map($.parseJSON(response), function(item) {
 
		      return { value: item.tagName };
		   })
 
		 };
 
            }
 
	 });
 
  });
  </script>
  
  <script>
  jQuery("div").webRating({     
      // count
      ratingCount     : 5,

      // image & color
      imgSrc          : "generalIcons.png",
      xLocation       : 53, //in px
      yLocation         : 49, //in px
      width               : 15, //in px
      height          : 15, //in px

      //CSS
      onClass         : 'onClass',
      offClass        : 'offClass',
      onClassHover    : 'onClassHover', //Optional
      offClassHover   : 'offClassHover' //Optional

      //on click funcitons
      cookieEnable        : false,
      cookiePrefix        : "myRating_",
      maxClick                : 1,
      onClick                 : function(clickScore, data){
          //Your function & post action
      },

      //Tooltip
      tp_showAverage  : true,
      prefixAverage   : "Avg",
      tp_eachStar     : {'1':'Very Bad','2':'Bad','3':'Ok','4':'Good','5':'Very Good'} //Rating guide
}); 
  </script>

</body>
</html>