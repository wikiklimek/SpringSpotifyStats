package spotify;
import spotifyTest.*;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@WebServlet("/callback")
public class SpotifyCallbackServletEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;
	SpotifyAuthDictionaries dictionary;
	private SpotifyAuthModel model;
	String scope;
	String info_to_get;
	
	@Override
	public void init() throws ServletException {
		model = new SpotifyAuthModel();
		SpotifyCallbackRedirectFunctions redirectFunctions = new SpotifyCallbackRedirectFunctions();
		dictionary = new SpotifyAuthDictionaries(model, redirectFunctions);
    	scope = "user-read-email user-top-read user-read-recently-played user-library-read user-read-currently-playing";
    	info_to_get = "my-email";
   
	}
	
  @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String authorizationCode = request.getParameter("code");
        if (authorizationCode != null) 
        {
        	//response.getWriter().println(dictionary.getInfo(authorizationCode, info_to_get));
        	//redirrectToView(request, response, dictionary.getInfo(authorizationCode, info_to_get));
        	dictionary.getInfo(authorizationCode, info_to_get, request, response);
        } 
        else 
        {
            response.getWriter().println("Error: " + request.getParameter("error"));
        }
    }
    

	
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String key = request.getParameter("key-info");
    	
    	info_to_get = key;
    	scope = dictionary.getScope(key);
    	
        String authorizationUrl = SpotifyPaths.getAuthorizationUrl(scope);
        response.sendRedirect(authorizationUrl); 
    }
    
    
    
    }
