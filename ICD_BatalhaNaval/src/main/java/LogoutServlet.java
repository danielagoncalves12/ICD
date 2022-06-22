import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null)
	        for (Cookie cookie : cookies) {
	        	if (cookie.getName().equals("username")) {
	        		cookie.setValue(null);
	        		cookie.setMaxAge(0);
		            response.addCookie(cookie);
	        	}            
	        }
	    request.removeAttribute("username");
		
		request.getSession().invalidate();
		request.getRequestDispatcher("/login.jsp").forward(request, response);
	}
}
