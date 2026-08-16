<%@page import="models.*" %>
<%@page import="java.util.List" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    List<Artist> artists = (List<Artist>) session.getAttribute("artists");
    request.setAttribute("artistList", artists);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Artist List</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<div class="title">
    <h2>Wow, you really like these artists</h2>
</div>
    <div class="logo-container">
        <img src="images/spotify.png" alt="Spotify Logo" class="logo">
    </div>

    <div class="artist-slider">
        <button class="arrow left" onclick="prevArtist()">&#9664;</button>
        <div class="artist-list">
            <c:forEach var="artist" items="${artistList}" varStatus="status">
                <div class="artist-card" data-index="${status.index}">
                    <a href="${artist.external_urls.spotify}" target="_blank">
                        <img src="${artist.imageUrl}" alt="${artist.name}" class="artist-image">
                        <h3>${artist.topNumber}.${artist.name}</h3>
                        <p>Popularity: ${artist.popularity}%</p>
                        <p>Followers: ${artist.followers.total}</p>
                    </a>
                </div>
            </c:forEach>
        </div>
        <button class="arrow right" onclick="nextArtist()">&#9654;</button>
    </div>

    <a href="index.jsp">
        <button class="redirect-button">Check other statistics</button>
    </a>

    <script>
        let currentIndex = 0;
        const artistList = document.querySelector('.artist-list');
        const artistCards = document.querySelectorAll('.artist-card');
        const leftArrow = document.querySelector('.arrow.left');
        const rightArrow = document.querySelector('.arrow.right');
        
        // Pokazuje tylko jeden artysta
        function showArtist(index) {
            artistCards.forEach((card, i) => {
                card.style.display = (i === index) ? 'block' : 'none';
            });
        }

        // Przesuń do poprzedniego artysty
        function prevArtist() {
            if (currentIndex > 0) {
                currentIndex--;
                showArtist(currentIndex);
            }
        }

        // Przesuń do następnego artysty
        function nextArtist() {
            if (currentIndex < artistCards.length - 1) {
                currentIndex++;
                showArtist(currentIndex);
            }
        }

   
        showArtist(currentIndex);
        function toggleArrows() {
            if (artistCards.length > 1) {
                leftArrow.style.display = 'block';
                rightArrow.style.display = 'block';
            } else {
                leftArrow.style.display = 'none';
                rightArrow.style.display = 'none';
            }
        }

        toggleArrows();
    </script>
</body>
</html>
 