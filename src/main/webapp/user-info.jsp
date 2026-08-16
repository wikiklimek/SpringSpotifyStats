<%@page import = "models.*" %>
<%@page import = "java.util.List" %>
<%@page import = "java.util.ArrayList" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    	UserProfile user = (UserProfile) session.getAttribute("user");
		List<Episode> episodes = (ArrayList<Episode>) session.getAttribute("episodes");
		List<Show> shows = (ArrayList<Show>) session.getAttribute("shows");
		Track currentlyPlaing = (Track) session.getAttribute("currentlyPlaying");
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
	<div class="container">
		<!-- Profile Box -->
		<div class="profile-box">
			<div class="profile-photo">
				<img src="${user.imageUrl}" alt="Profile Image" class="profile-img">
			</div>
			<div class="profile-info">
				<h2>Your email:</h2>
				<p class="info"><c:out value="${user.email}"/></p>
				<h2>Your name:</h2>
				<p class="info"><c:out value="${user.displayName}"/></p>
			</div>
		</div>

		<!-- Currently Playing Box -->
		<div class="currently-playing">
<%-- 			<h3>Currently Playing</h3>
			<p><c:out value="${currentlyPlaying.name}"/></p> --%>
			    <h3>Currently Playing</h3>
			    <div class="song-info">
			        <p class="song-name"><c:out value="${currentlyPlaying.name}"/></p>
			        <p> by </p>
			        <p class="artists">
			            <c:forEach var="artist" items="${currentlyPlaying.artists}">
			                <c:out value="${artist.name}"/><c:if test="${not empty artist.name}">, </c:if>
			            </c:forEach>
			        </p>
			    </div> 
		</div>
		<div class="saved-sections">
			<div class="saved-box episodes-box">
				<h3>Your Saved Episodes</h3>
				<div class="scroll-box">
					<table>
						<thead>
							<tr>
								<th>Episode Name</th>
								<th>Release Date</th>
								<th>Show Name</th>
								<th>Publisher</th>
							</tr>
						</thead>
						<tbody>
						<c:forEach var="episode" items="${episodes}">
							<tr>
								<td><c:out value="${episode.name}"/></td>
								<td><c:out value="${episode.releaseDate}"/></td>
								<td><c:out value="${episode.show.name}"/></td>
								<td><c:out value="${episode.show.publisher}"/></td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
			</div>

			<!-- Saved Shows -->
			<div class="saved-box shows-box">
				<h3>Your Saved Shows</h3>
				<div class="scroll-box">
					<table>
						<thead>
							<tr>
								<th>Show Name</th>
								<th>Publisher</th>
								
							</tr>
						</thead>
						<tbody>
						<c:forEach var="show" items="${shows}">
							<tr>
								<td><c:out value="${show.name}"/></td>
								<td><c:out value="${show.publisher}"/></td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<a href="index.jsp">
			<button class="redirect-button">Check other statistics</button>
		</a>
	</div>
</body>
</html>
