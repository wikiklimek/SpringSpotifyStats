<%@page import = "models.*" %>
<%@page import = "java.util.List" %>
<%@page import = "java.util.ArrayList" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    	List<Episode> episodes = (ArrayList<Episode>) session.getAttribute("episodes");
    	request.setAttribute("list", episodes);
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>"${title}"</title>
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<div class="logo-container">
    <img src="images/spotify.png" alt="Spotify Logo" class="logo">
</div>
<h2>${title}</h2>
<c:forEach var="episode" items = "${list}">
	<tr>
		<td><p><c:out value="${episode.name}"/></p></td>
		<td><p><c:out value="${show.releaseDate}"/></p></td>
		
		<td><p><c:out value="${episode.show.name}"/></p></td>
		<td><p><c:out value="${episode.show.publisher}"/></p></td>
	</tr>
</c:forEach>
<a href="index.jsp">
        <button class="redirect-button">Check other statistics</button>
    </a>
</body>
</html> 