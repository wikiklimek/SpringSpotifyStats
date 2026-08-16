package spotify;
import spotifyTest.*;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.*;

public class SpotifyCallbackRedirectFunctions {
	public void redirrectToViewGenres(HttpServletRequest request, HttpServletResponse response, String title, List<Genre> genres) throws ServletException, IOException 
	{
    	HttpSession hs = request.getSession();
    	hs.setAttribute("title", title);
    	hs.setAttribute("genres", genres);
    	response.sendRedirect("genres.jsp");
    }
    
    public void redirrectToViewTracks(HttpServletRequest request, HttpServletResponse response, String title, List<Track> tracks) throws ServletException, IOException 
    {
    	HttpSession hs = request.getSession();
    	hs.setAttribute("title", title);
    	hs.setAttribute("tracks", tracks);
    	response.sendRedirect("tracks.jsp");
    }
    
    public void redirrectToViewArtists(HttpServletRequest request, HttpServletResponse response,String title, List<Artist> artists) throws ServletException, IOException 
    {
    	HttpSession hs = request.getSession();
    	hs.setAttribute("title", title);
    	hs.setAttribute("artists", artists);
    	response.sendRedirect("artists.jsp");
    }

    public void redirrectToViewUserInfo(HttpServletRequest request, HttpServletResponse response,String title, UserProfile user, List<Episode> episodes, List<Show> shows, Track track) throws ServletException, IOException 
    {
    	HttpSession hs = request.getSession();
    	hs.setAttribute("title", title);
    	hs.setAttribute("user", user);
    	hs.setAttribute("episodes", episodes);
    	hs.setAttribute("shows", shows);
    	hs.setAttribute("currentlyPlaying", track);
    	response.sendRedirect("user-info.jsp");
    }
    

}
