package spotifyTest;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface WebFunctionCalls {
	public void myFunc(HttpServletRequest request, HttpServletResponse response, String str) throws ServletException, IOException ;
}
