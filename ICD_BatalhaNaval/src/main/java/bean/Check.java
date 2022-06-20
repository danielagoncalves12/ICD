package bean;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import socket.User;

public class Check {
	
	public static String username(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Obter o username guardado na sessao
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("username");

		// Se nao houver username guardado na sessao,
		// � procurado pelo username guardado em cookie

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

	public static HashMap<String, String> profile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParserConfigurationException {

		HashMap<String, String> profile = new HashMap<>();
		String username = username(request, response);
		
		if (!username.equals("")) {
			
			String profileInfo = new User().sendRequestProfileInfo(username);
			
			profile.put("Name", profileInfo.split(",")[0]);
			profile.put("Color", profileInfo.split(",")[1]);
			profile.put("Picture", profileInfo.split(",")[2]);
			profile.put("WinsNum", profileInfo.split(",")[3]);
			profile.put("Date", profileInfo.split(",")[4]);
			
			return profile;
		}
		return null;
	}
}
