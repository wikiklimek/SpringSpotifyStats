<%@page import = "models.Genre" %>
<%@page import = "java.util.List" %>
<%@page import = "java.util.ArrayList" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    List<Genre> genres = (ArrayList<Genre>) session.getAttribute("genres");
    request.setAttribute("list", genres);
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

    <div class="genre-container">
        <ul class="genre-list">
            <c:forEach var="genre" items="${list}">
                <li class="genre-item">
                    <p><c:out value="${genre.topNumber + 1}"/>.<c:out value="${genre.name}"/></p>
                </li>
            </c:forEach>
        </ul>
    </div>

    <a href="index.jsp">
        <button class="redirect-button">Check other statistics</button>
    </a>
</body>
</html>
