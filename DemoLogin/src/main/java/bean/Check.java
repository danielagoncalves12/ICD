package bean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Check {

public static String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	HttpSession session=request.getSession(false);
	if(session!=null) {
		String name=(String)session.getAttribute("name");
		if(name!=null)
		  return name;
		else
		  session.invalidate();
	}
	request.getRequestDispatcher("/login.html").forward(request, response);
	return "????";
}
}
