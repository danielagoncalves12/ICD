package bean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Check {

	public static String username(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Obter o username guardado na sessao
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("username");

		// Se nao houver username guardado na sessao,
		// é procurado pelo username guardado em cookie
		if (username == null) {
			Cookie[] cookies = request.getCookies();
			
			if (cookies != null) 
				for (Cookie cookie : cookies) 
				   if (cookie.getName().equals("username")) 
					   username = cookie.getValue();
		}
		
		if (username != null) return username;
		else {
			session.invalidate();
			request.getRequestDispatcher("/login.html").forward(request, response);
			return "";
		}
	}
	
	public static String name(HttpServletRequest request) throws ServletException, IOException {
		
		HttpSession session = request.getSession(true);
		
		if (session != null) {
			
			String name = (String) session.getAttribute("name");
			if (name != null) return name;		
		}
		return "";
	}
	
	public static String picture(HttpServletRequest request) throws ServletException, IOException {
		
		HttpSession session = request.getSession(true);
		
		if (session != null) {
			
			String name = (String) session.getAttribute("picture");
			if (name != null) return name;		
		}
		return "";
	}
}
