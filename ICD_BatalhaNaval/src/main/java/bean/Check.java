package bean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Check {

	public static String username(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(true);
		String nickname = (String) session.getAttribute("username");

		if(session != null) {
	
			if (nickname != null) return nickname;
			else session.invalidate();
		}
		request.getRequestDispatcher("/login.html").forward(request, response);
		return "????";
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
