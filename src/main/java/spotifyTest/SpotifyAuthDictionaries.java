package spotifyTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import models.*;
import spotify.SpotifyCallbackRedirectFunctions;

//Controller: Coordinates between the model and view
public class SpotifyAuthDictionaries {
private final SpotifyAuthModel model;
private final Map<String, WebFunctionCalls> dictionaryFunctions;
private final Map<String, String> dictionaryScopes;

public SpotifyAuthDictionaries(SpotifyAuthModel model, SpotifyCallbackRedirectFunctions redirectFunctions) {
   this.model = model;
   dictionaryFunctions = new HashMap<String, WebFunctionCalls>();
   dictionaryFunctions.put("my-email", new WebFunctionCalls() 
   {
	   public void myFunc (HttpServletRequest request, HttpServletResponse response, String str) throws ServletException, IOException 
	   {
		   UserProfile user = model.getUserEmail(str);
		   List<Episode> episodes = model.get50SavedEpisodes(str); 
           System.out.println("Saved Episodes: " + episodes.size()); 
	       List<Show> shows = model.get50SavedShows(str); 
	       Track track = model.getCurrentlyPlaying(str); 
	       System.out.println("currently playing: " + track.getName());
	       redirectFunctions.redirrectToViewUserInfo(request, response,"User Info" , user,episodes, shows, track);
	   }
   });
   dictionaryFunctions.put("top-1-track", new WebFunctionCalls() 
   {
	   public void myFunc (HttpServletRequest request, HttpServletResponse response, String str) throws ServletException, IOException  
	   {
		  List<Track> tracks = model.getTop1PlayedTrack(str);
		  redirectFunctions.redirrectToViewTracks(request, response,"Top 1 Track" , tracks);
	   }
   });
   dictionaryFunctions.put("recently-played-1-track", new WebFunctionCalls() 
   {
	   public void myFunc (HttpServletRequest request, HttpServletResponse response, String str) throws ServletException, IOException 
	   {
		   List<Track> tracks = model.get1RecentlyPlayedTrack(str);
		   redirectFunctions.redirrectToViewTracks(request, response, "Recently Played Track", tracks);
	   }
   });
   dictionaryFunctions.put("top-1-artist", new WebFunctionCalls() 
   {
	   public void myFunc (HttpServletRequest request, HttpServletResponse response, String str) throws ServletException, IOException  
	   {
		   List<Artist> artists = model.getTop1Artist(str);
		   redirectFunctions.redirrectToViewArtists(request, response,"Top 1 Artist", artists);
	   }
   });
   dictionaryFunctions.put("top-10-tracks", new WebFunctionCalls() 
   {
	   public void myFunc (HttpServletRequest request, HttpServletResponse response, String str) throws ServletException, IOException  
	   {
		   List<Track> tracks = model.getTop10PlayedTracks(str);
		   redirectFunctions.redirrectToViewTracks(request, response,"Top 10 Track" ,  tracks);
	   }
   });
   dictionaryFunctions.put("top-10-artists", new WebFunctionCalls() 
   {
	   public void myFunc (HttpServletRequest request, HttpServletResponse response, String str) throws ServletException, IOException  
	   {
		   List<Artist> artists = model.getTop10Artists(str);
		   redirectFunctions.redirrectToViewArtists(request, response, "Top 10 Artist", artists);
	   }
   });
   dictionaryFunctions.put("recently-played-10-tracks", new WebFunctionCalls() 
   {
	   public void myFunc (HttpServletRequest request, HttpServletResponse response, String str) throws ServletException, IOException  
	   {
		   List<Track> tracks = model.get10RecentlyPlayedTracks(str);
		   redirectFunctions.redirrectToViewTracks(request, response, "10 Recently Played Tracks", tracks);
	   }
   });
   dictionaryFunctions.put("top-10-genres", new WebFunctionCalls() 
   {
	   public void myFunc (HttpServletRequest request, HttpServletResponse response, String str) throws ServletException, IOException  
	   {
		   List<Genre> genres = model.getTop10Genres(str);
		   redirectFunctions.redirrectToViewGenres(request, response, "Top 10 Genres", genres);
	   }
   });
   dictionaryFunctions.put("top-25-genres", new WebFunctionCalls() 
   {
	   public void myFunc (HttpServletRequest request, HttpServletResponse response, String str) throws ServletException, IOException  
	   {
		   List<Genre> genres = model.getTop25Genres(str);
		   redirectFunctions.redirrectToViewGenres(request, response,"Top 25 Genres", genres);
	   }
   });
//   dictionaryFunctions.put("saved-shows", new WebFunctionCalls() 
//   {
//	   public void myFunc (HttpServletRequest request, HttpServletResponse response, String str) throws ServletException, IOException  
//	   {
//		   List<Show> shows = model.get50SavedShows(str);
//		   redirectFunctions.redirrectToViewShows(request, response,"Saved Shows", shows);
//	   }
//   });
//   dictionaryFunctions.put("saved-episodes", new WebFunctionCalls() 
//   {
//	   public void myFunc (HttpServletRequest request, HttpServletResponse response, String str) throws ServletException, IOException  
//	   {
//		   List<Episode> episodes = model.get50SavedEpisodes(str);
//		   redirectFunctions.redirrectToViewEpisodes(request, response,"Saved Episodes", episodes);
//	   }
//   });
//   dictionaryFunctions.put("currently-playing", new WebFunctionCalls() 
//   {
//	   public void myFunc (HttpServletRequest request, HttpServletResponse response, String str) throws ServletException, IOException  
//	   {
//		   Track track = model.getCurrentlyPlaying(str);
//		   List<Track> tracks = new ArrayList<Track>();
//		   tracks.add(track);
//		   redirectFunctions.redirrectToViewTracks(request, response,"Currently Playing", tracks);
//	   }
//   });
   
   
   //currently-playing
   //user-read-currently-playing
   
   dictionaryScopes = new HashMap<String, String>();
   dictionaryScopes.put("my-email", "user-read-email user-library-read user-read-currently-playing");
   dictionaryScopes.put("top-1-track", "user-top-read");
   dictionaryScopes.put("recently-played-1-track", "user-read-recently-played");
   dictionaryScopes.put("top-1-artist", "user-top-read");
   dictionaryScopes.put("top-10-tracks", "user-top-read");
   dictionaryScopes.put("top-10-artists", "user-top-read");
   dictionaryScopes.put("recently-played-10-tracks", "user-read-recently-played");
   dictionaryScopes.put("top-10-genres", "user-top-read");
   dictionaryScopes.put("top-25-genres", "user-top-read");
	/*
	 * dictionaryScopes.put("saved-shows", "user-library-read");
	 * dictionaryScopes.put("saved-episodes", "user-library-read");
	 * dictionaryScopes.put("currently-playing", "user-read-currently-playing");
	 */
}

public String getScope(String info_to_get)
{
	return dictionaryScopes.get(info_to_get);
}

public void getInfo(String authorizationCode, String info_to_get, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
{
	try
	{
		String accessToken = model.getAccessTokenAuthorisation(authorizationCode);
		dictionaryFunctions.get(info_to_get).myFunc(request, response, accessToken);
	}
	catch(IOException exc){}
}



}