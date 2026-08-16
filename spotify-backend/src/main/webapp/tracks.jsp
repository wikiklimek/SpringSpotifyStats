<%@page import = "models.*" %>
<%@page import = "java.util.List" %>
<%@page import = "java.util.ArrayList" %>
<%@page import = "spotifyTest.*" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    	List<Track> tracks = (ArrayList<Track>) session.getAttribute("tracks");
    	request.setAttribute("list", tracks);
    	String title = (tracks.size() == 1) ? "You can stop listening to this song!" : "You can't stop listening to these songs!";
    	request.setAttribute("title", title);
   
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${title}</title>
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<div class="logo-container">
    <img src="images/spotify.png" alt="Spotify Logo" class="logo">
</div>
<div class="title">
    <h2>${title}</h2>
</div>

     <!-- Kontener dla utworów z możliwością przewijania -->
    <div class="track-list-container">
        <c:forEach var="track" items="${list}">
            <div class="track">
                <%-- <p><c:out value="${track.topNumber}"/>. <c:out value="${track.name}"/></p> --%>
                <p>
    				<c:choose>
        				<c:when test="${track.topNumber != null && track.topNumber != 0}">
            				<span class='track-number'><c:out value="${track.topNumber}"/>. </span>
        				</c:when>
    				</c:choose>
    				<span class='track-name'><c:out value="${track.name}"/></span>
				</p>
                <p>by</p>
                <c:forEach var="artist" items="${track.artists}">
                    <div class="artist">
                        <!-- Sprawdzanie, czy artysta ma zdjęcie -->
                        <c:choose>
                            <c:when test="${not empty artist.imageUrl}">
                                <div class="artist-photo">
                                    <a href ="${artist.href}?access_token=${sessionScope.accessToken}" target ="_blank">
                                    	<img src="${artist.imageUrl}" alt="${artist.name}" class="artist-img"/>
                                    </a>	
                                </div>
                            </c:when>
                            <c:otherwise>
                                <!-- Domyślny obrazek lub puste miejsce -->
                                <div class="artist-photo">
                                    <img src="images/user.png" alt="Default Image" class="artist-img"/>
                                </div>
                            </c:otherwise>
                        </c:choose>
						
                        <p class="artist-name">${artist.name}</p>
                    </div>
                </c:forEach>
            </div>
        </c:forEach>
    </div>

<a href="index.jsp">
        <button class="redirect-button">Check other statistics</button>
    </a>
</body>
</html>

