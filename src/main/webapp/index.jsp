<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Spotify Stats</title>
<link rel="stylesheet" type="text/css" href="styles.css">
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
</head>
<body>
    <!-- Logo na górze po lewej stronie -->
    <div class="logo-container">
        <a href="https://www.spotify.com" target="_blank">
            <img src="images/spotify.png" alt="Spotify Icon" class="logo">
        </a>
    </div>

    <!-- Nagłówek -->
    <div class="header">
        <h1>Check your Spotify Statistics</h1>
    </div>

    <!-- Formularz z przyciskami -->
    <div class="form-container">
        <form action="callback" method="POST">
            <button type="submit" name="key-info" value="my-email">Personal data</button>
            <button type="submit" name="key-info" value="top-1-track">Top 1 Track</button>
            <button type="submit" name="key-info" value="top-10-tracks">Top 10 Tracks</button>
            <button type="submit" name="key-info" value="recently-played-1-track">Recently Played 1 Track</button>
            <button type="submit" name="key-info" value="recently-played-10-tracks">Recently Played 10 Tracks</button>
            <button type="submit" name="key-info" value="top-1-artist">Top 1 Artist</button>
            <button type="submit" name="key-info" value="top-10-artists">Top 10 Artists</button>
            <button type="submit" name="key-info" value="top-10-genres">Top 10 Genres</button>
            <button type="submit" name="key-info" value="top-25-genres">Top 25 Genres</button>
  <!--           <button type="submit" name="key-info" value="saved-shows">Your Saved Shows</button>
            <button type="submit" name="key-info" value="saved-episodes">Your Saved Episodes</button>
            <button type="submit" name="key-info" value="currently-playing">Currently Playing Track</button> -->
        </form>
    </div>
</body>
</html>