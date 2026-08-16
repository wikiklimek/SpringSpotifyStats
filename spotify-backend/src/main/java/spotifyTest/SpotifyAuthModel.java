package spotifyTest;

import helpers.*;
import models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SpotifyAuthModel extends SpotifyPaths {
 
private String getAccessToken(String body) throws IOException
{
	
	 URL url = new URL(TOKEN_URL);
	 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	 connection.setRequestMethod("POST");
	 connection.setDoOutput(true);
	   
	 try (OutputStream os = connection.getOutputStream()) {
         byte[] input = body.getBytes(StandardCharsets.UTF_8);
         os.write(input, 0, input.length);
     }
	 
     
         String jsonResponse = readResponseStringFromConnection(connection);
         TokenResponse token_response = new TokenResponse();
         ObjectMapper mapper = new ObjectMapper();
         
         try 
	     {
        	 token_response = mapper.readValue(jsonResponse, TokenResponse.class);
	     }
	     catch(IOException e)
	     {
	         System.out.println(e.getMessage());
	     }
       
         
         return token_response.getAccessToken();
	    
}
 public String getAccessTokenAuthorisation(String authorizationCode) throws IOException {
     return getAccessToken(getAccessTokenAuthorisationBody(authorizationCode));   
 }
 
 public String getAccessTokenNoAuthorisation() throws IOException {
	    return getAccessToken(getAccessTokenNoAuthorisationBody());
	}

 
 private String readResponseStringFromConnection(HttpURLConnection connection) throws IOException
 {
	 try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
	        StringBuilder response = new StringBuilder();
	        String line;
	        while ((line = br.readLine()) != null) {
	            response.append(line);
	        }
	        
	        return response.toString();
	    }
 }
 
private String getResponseString(String stringURL, String accessToken) throws IOException
{
	URL url = new URL(stringURL);
	HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    connection.setRequestProperty("Authorization", "Bearer " + accessToken);

    return readResponseStringFromConnection(connection);
}
 
 public UserProfile getUserEmail(String accessToken) throws IOException {

	 try {
	    	
	     String jsonResponse = getResponseString(USER_INFO_URL, accessToken);
	     
	     ObjectMapper mapper = new ObjectMapper();
	     UserProfile user_profile = new UserProfile();
	     
	     try 
	     {
	    	 user_profile = mapper.readValue(jsonResponse, UserProfile.class);
	     }
	     catch(IOException e)
	     {
	         System.out.println(e.getMessage());
	     }
	     
	     return user_profile;
     }
	 catch(IOException e)
	 {
		 System.out.println(e.getMessage());
		 //return e.getMessage();
		 return new UserProfile();
	 }
 }
 
 public List<Track> getTop1PlayedTrack(String accessToken) throws IOException {
	    return getTopPlayedTracks(accessToken, "?limit=1");
	}
 
 public List<Track> getTop10PlayedTracks(String accessToken) throws IOException {
	    return getTopPlayedTracks(accessToken, "?limit=10");
	}
 
 private List<Track> getTopPlayedTracks(String accessToken, String limit) throws IOException {
	 try
	 {
	    List<Track> tracks = getTopPlayedTracksList(accessToken, limit);
	    
	    int i = 0;
	    for(Track track:tracks)
	        track.setTopNumber(++i);
	        
	    return tracks;
	   }
	   catch(IOException e)
	   {
		   System.out.println(e.getMessage());
		  	//return e.getMessage();
		   return new ArrayList<Track>();
		}
	}
 
 public List<Track> get1RecentlyPlayedTrack(String accessToken) throws IOException {
	 return getRecentlyPlayedTracks(accessToken, "?limit=1");
 }
 
 public List<Track> get10RecentlyPlayedTracks(String accessToken) throws IOException {
	 return getRecentlyPlayedTracks(accessToken, "?limit=10");
 }
 
// private List<Track> getRecentlyPlayedTracks(String accessToken, String limit) throws IOException {
//
//	    String recentlyPlayedURL = RECENTLY_PLAYED_URL + limit;
//
//	    try {
//	    	
//	    	String jsonResponse = getResponseString(recentlyPlayedURL, accessToken);
//	    	//StringBuilder my_info = new StringBuilder();
//	    	
//	    	ObjectMapper mapper = new ObjectMapper();
//	        SpotifyRecentlyResponse spotify_response = new SpotifyRecentlyResponse();
//	        
//	        try 
//	        {
//	        	spotify_response = mapper.readValue(jsonResponse, SpotifyRecentlyResponse.class);
//	        }
//	        catch(IOException e)
//	        {
//	        	System.out.println(e.getMessage());
//	        }
//	        
//	        
//	        List<Track> tracks = new ArrayList<Track>();
//	        for(RecentlyItem item : spotify_response.getItems())
//	        	tracks.add(item.getTrack());
//	        
//	        int i = 0;
//		    for(Track track:tracks)
//		        track.setTopNumber(++i);
//	        
//	        return tracks;
//	    }
//	    catch(IOException e)
//	    {
//	    	System.out.println(e.getMessage());
//	    	//return e.getMessage();
//	    	return new ArrayList<Track>();
//	    }
//	}
// 
 
 private List<Track> getRecentlyPlayedTracks(String accessToken, String limit) throws IOException {

	    String recentlyPlayedURL = RECENTLY_PLAYED_URL + limit;
	    List<Track> trackList = new ArrayList<>();

	    try {
	        String jsonResponse = getResponseString(recentlyPlayedURL, accessToken);
	        
	        ObjectMapper mapper = new ObjectMapper();
	        
	        
	        //SpotifyRecentlyResponse spotifyResponse = mapper.readValue(jsonResponse, SpotifyRecentlyResponse.class);
	        SpotifyRecentlyResponse spotifyResponse = new SpotifyRecentlyResponse();
	        try 
	        {
	        	spotifyResponse = mapper.readValue(jsonResponse, SpotifyRecentlyResponse.class);
	        }
	        catch(IOException e)
	        {
	        	System.out.println(e.getMessage());
	        }
	        
	        
	        // Przekształć listę RecentlyItem w listę Track
	        for (RecentlyItem item : spotifyResponse.getItems()) {
	            trackList.add(item.getTrack());
	        }

	        // Dla każdego utworu z listy
	        String accessTokenArtist = getAccessTokenNoAuthorisation();  // Nowy token, jeśli jest wymagany dla artystów
	        for (Track track : trackList) {
	            List<Artist> completeArtists = new ArrayList<>();
	            
	            // Dla każdego artysty w utworze
	            for (Artist incompleteArtist : track.getArtists()) {
	                Artist completeArtist = getArtistByID(accessTokenArtist, incompleteArtist.getId());
	                completeArtists.add(completeArtist);  // Dodaj pełną wersję artysty
	            }
	            
	            // Zaktualizuj listę artystów w utworze
	            track.setArtists(completeArtists);
	        }

	    } catch (IOException e) {
	        System.out.println(e.getMessage());
	    }

	    return trackList;
	}


 public List<Artist> getTop10Artists(String accessToken) throws IOException {
	 return getTopArtists(accessToken, "?limit=10");
 }
 
 public List<Artist> getTop1Artist(String accessToken) throws IOException {
	 return getTopArtists(accessToken, "?limit=1");
	}
 
 private List<Artist> getTopArtists(String accessToken, String limit) throws IOException {
	    try {
	    	List<Artist> topArtistList = getTopArtistsList(accessToken, limit);
	    	
	    	int i = 0;
		    for(Artist artist:topArtistList)
		        artist.setTopNumber(++i);
		    
	    	return topArtistList;
	    }
	    catch(IOException e)
	    {
	    	System.out.println(e.getMessage());
	    	//return e.getMessage();
	    	return new ArrayList<Artist>();
	    }
	}
 
 private Map<String, Integer> getTopGenresMap(String accessToken) throws IOException {
	    try {
	    	List<Artist> topArtistList = getTopArtistsList(accessToken, "?limit=50");
	    	List<Track> topTrackList = getTopPlayedTracksList(accessToken, "?limit=50");
	    	Map<String, Integer> genres = new HashMap<String, Integer>();
	    	
	        //tutaj dla kazdego tracks dla kazdego artist trzeba zbrac info o gatunkach
	        String accessTokenArtist = getAccessTokenNoAuthorisation();
	        for(Track track : topTrackList)
	        	for(Artist artist_incomplete : track.getArtists())
	        	{
	        		Artist artist_complete = getArtistByID(accessTokenArtist, artist_incomplete.getId());
	        		
	        		//every artist once
	        		/*
	        		if(!topArtistList.stream().anyMatch(a -> a.getId().equals(artist_complete.getId())))
	        		{
	        			topArtistList.add(artist_complete);
	        		}
	        		*/
	        		
	        		//duplicate artists
	        		topArtistList.add(artist_complete);
	        	}
	        
	        for(Artist artist : topArtistList)
	        	for(String genre : artist.getGenres())
		        {
			        if(genres.containsKey(genre))
			        {
			        	genres.put(genre, genres.get(genre) + 1);
			        }
			        else
			        {
			        	genres.put(genre, 1);
			        }
	        		
		        }
	        
	        
	        return genres;
	        
	    }
	    catch(IOException e)
	    {
	    	System.out.println(e.getMessage());
	    	return new HashMap<String, Integer>();
	    }
	}
 
 public List<Genre> getTop10Genres(String accessToken) throws IOException {
	 return getTopGenres(accessToken, 10);
 }
 
 public List<Genre> getTop25Genres(String accessToken) throws IOException {
	 return getTopGenres(accessToken, 25);
 }
 
 private List<Genre> getTopGenres(String accessToken, int limit) throws IOException {
	 Map<String, Integer> map_genres_unsorted = getTopGenresMap(accessToken);
	 Map<String, Integer> map_genres = Sort.sortByValue(map_genres_unsorted);
	 List<String> list_genres = new ArrayList<String>(map_genres.keySet());
	 
	 for (var entry : map_genres.entrySet()) {
		    System.out.println(entry.getKey() + "/" + entry.getValue());
	}
	 
	 
	 List<Genre> genres_return = new ArrayList<Genre>();
	 for(int i = 0; i< Math.min(limit, list_genres.size()); i++)
		 genres_return.add(new Genre(list_genres.get(i), i));
	 
	 return genres_return;
 }
 
 private List<Artist> getTopArtistsList(String accessToken, String limit) throws IOException {
	    String topArtistsURL = TOP_ARTISTS_URL + limit;

	    try {
	    	
	    	String jsonResponse = getResponseString(topArtistsURL, accessToken);
	    	
	        ObjectMapper mapper = new ObjectMapper();
	        SpotifyTopResponseArtist spotify_response = new SpotifyTopResponseArtist();
	        
	        try 
	        {
	        	spotify_response = mapper.readValue(jsonResponse, SpotifyTopResponseArtist.class);
	        }
	        catch(IOException e)
	        {
	        	System.out.println(e.getMessage());
	        }
	        
	        
	        return spotify_response.getItems();
	        
	        
	    }
	    catch(IOException e)
	    {
	    	System.out.println(e.getMessage());
	    	return new ArrayList<Artist>();
	    }
	}
 
 private List<Track> getTopPlayedTracksList(String accessToken, String limit) throws IOException {

	    String topTracksURL = TOP_TRACKS_URL + limit;
	    List<Track> trackList = new ArrayList<>();
	    
	    try {
	    	
	    	String jsonResponse = getResponseString(topTracksURL, accessToken);
	        ObjectMapper mapper = new ObjectMapper();
	        SpotifyTopResponseTrack spotify_response = new SpotifyTopResponseTrack();
	        
	        try 
	        {
	        	spotify_response = mapper.readValue(jsonResponse, SpotifyTopResponseTrack.class);
	        }
	        catch(IOException e)
	        {
	        	System.out.println(e.getMessage());
	        }
	        
	        trackList = spotify_response.getItems();
	        
	        String accessTokenArtist = getAccessTokenNoAuthorisation();  // Nowy token, jeśli jest wymagany dla artystów
	        for (Track track : trackList) {
	            List<Artist> completeArtists = new ArrayList<>();
	            
	            // Dla każdego artysty w utworze
	            for (Artist incompleteArtist : track.getArtists()) {
	                Artist completeArtist = getArtistByID(accessTokenArtist, incompleteArtist.getId());
	                completeArtists.add(completeArtist);  // Dodaj pełną wersję artysty
	            }
	            
	            // Zaktualizuj listę artystów w utworze
	            track.setArtists(completeArtists);
	        }
	        
	        
	    }
	    catch(IOException e)
		{
	    	System.out.println(e.getMessage());
		  	return new ArrayList<Track>();
		}
	    
	    return trackList;
	}
 
 public Artist getArtistByID(String accessToken, String id) throws IOException {
	    String artistsURL = ARTISTS_URL + id;

	    try {
	    	
	    	String jsonResponse = getResponseString(artistsURL, accessToken);
	    	//System.out.println("Odpowiedź JSON przed mapowaniem: " + jsonResponse);

	    	ObjectMapper mapper = new ObjectMapper();
	        Artist artist = new Artist();
	        
	        try 
	        {
	        	artist = mapper.readValue(jsonResponse, Artist.class);
	        }
	        catch(IOException e)
	        {
	        	System.out.println(e.getMessage());
	        }
	        
	        
	        if (artist.getImages() != null && !artist.getImages().isEmpty()) {
	        	String imageUrl = artist.getImages().get(0).getUrl();  // Pobieranie URL pierwszego zdjęcia
	        	artist.setImageUrl(imageUrl);
	        }  
	        
	        return artist;
	        
	        
	    }
	    catch(IOException e)
	    {
	    	System.out.println(e.getMessage());
	    	return new Artist();
	    }
	}
 
 public Track getTrackByID(String accessToken, String id) throws IOException {
	    String tracksURL = TRACKS_URL + id;

	    try {
	    	
	    	String jsonResponse = getResponseString(tracksURL, accessToken);
	    	
	        ObjectMapper mapper = new ObjectMapper();
	        Track track = new Track();
	        
	        try 
	        {
	        	track = mapper.readValue(jsonResponse, Track.class);
	        }
	        catch(IOException e)
	        {
	        	System.out.println(e.getMessage());
	        }
	        
	        
	        return track;
	        
	        
	    }
	    catch(IOException e)
	    {
	    	System.out.println(e.getMessage());
	    	return new Track();
	    }
	}
 
 public List<Show> get50SavedShows(String accessToken) throws IOException {
	    return getUserSavedShowsList(accessToken, "?limit=50");
	}

private List<Show> getUserSavedShowsList(String accessToken, String limit) throws IOException {

    String savedShowsURL = USER_SAVED_SHOWS + limit;
    List<Show> showList = new ArrayList<>();
    
    try {
    	
    	String jsonResponse = getResponseString(savedShowsURL, accessToken);
        ObjectMapper mapper = new ObjectMapper();
        SpotifyShowsResponse spotify_response = new SpotifyShowsResponse();
        
        try 
        {
        	spotify_response = mapper.readValue(jsonResponse, SpotifyShowsResponse.class);
        }
        catch(IOException e)
        {
        	System.out.println(e.getMessage());
        }
        
        for(ShowItem item : spotify_response.getItems())
        	showList.add(item.getShow());
        
        
        
        
    }
    catch(IOException e)
	{
    	System.out.println(e.getMessage());
	  	return new ArrayList<Show>();
	}
    
    return showList;
}

public List<Episode> get50SavedEpisodes(String accessToken) throws IOException {
    return getUserSavedEpisodesList(accessToken, "?limit=50");
}

private List<Episode> getUserSavedEpisodesList(String accessToken, String limit) throws IOException {

    String savedEpisodesURL = USER_SAVED_EPISODES + limit;
    List<Episode> episodeList = new ArrayList<>();
    
    try {
    	
    	String jsonResponse = getResponseString(savedEpisodesURL, accessToken);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        
        SpotifyEpisodesResponse spotify_response = new SpotifyEpisodesResponse();
        
        try 
        {
        	spotify_response = mapper.readValue(jsonResponse, SpotifyEpisodesResponse.class);
        }
        catch(IOException e)
        {
        	System.out.println(e.getMessage());
        }
        
        for(EpisodeItem item : spotify_response.getItems())
        	episodeList.add(item.getEpisode());
        
        
        
        
    }
    catch(IOException e)
	{
    	System.out.println(e.getMessage());
	  	return new ArrayList<Episode>();
	}
    
    return episodeList;
}

public Track getCurrentlyPlaying(String accessToken) throws IOException {

    String currentlyURL = CURRENTLY_PLAYING;
    
    try {
    	
    	String jsonResponse = getResponseString(currentlyURL, accessToken);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        
        SpotifyCurrentlyPlaying spotify_response = new SpotifyCurrentlyPlaying();
        
        try 
        {
        	spotify_response = mapper.readValue(jsonResponse, SpotifyCurrentlyPlaying.class);
        }
        catch(IOException e)
        {
        	System.out.println(e.getMessage());
        }
        
        return spotify_response.getItem();
        
    }
    catch(IOException e)
	{
    	System.out.println(e.getMessage());
	  	return new Track();
	}
    
}
}



