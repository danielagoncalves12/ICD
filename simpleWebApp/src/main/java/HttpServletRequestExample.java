import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/HttpServletRequestExample")
public class HttpServletRequestExample extends HttpServlet {
	/**
	 * Informação genérica sobre o pedido
	 * Manutenção de estado entre os vários pedidos
	 */
	private static final long serialVersionUID = 1L;

// acesso a informação associada ao pedido
protected void doGet(HttpServletRequest request, HttpServletResponse response)
throws ServletException, IOException {
response.setContentType("text/html;charset=UTF-8");
response.setContentType("text/html");
PrintWriter out = response.getWriter();
out.println("<html><head><title>State Information</title></head><body>");
out.println("<h1>Request Information</h1>");
out.println("Remote Address : "+request.getRemoteAddr()+"<br>");
out.println("Remote Host : "+request.getRemoteHost()+"<br>");
out.println("Remote Port : "+request.getRemotePort()+"<br>");
out.println("Server Name : "+request.getServerName()+"<br>");
out.println("Server Port : "+request.getServerPort()+"<br>");
out.println("Servlet Context : "+request.getServletContext()+"<br>");
out.println("Servlet Path : "+request.getServletPath()+"<br>");
out.println("Servlet AuthType : "+request.getAuthType()+"<br>");
// out.println("Servlet isUserInRole : "+request.isUserInRole()+"<br>");
out.println("Servlet Protocol : "+request.getProtocol()+"<br>");
out.println("Servlet isSecure : "+request.isSecure()+"<br>");
//out.println("Servlet UserPrinciple : "+request.getUserPrinciple()+"<br>");
out.println("Servlet RemoteUser : "+request.getRemoteUser()+"<br>");

/* 
/*
 * https://docs.oracle.com/cd/E13222_01/wls/docs92/webapp/sessions.html#wp150309
session.getAttribute()
session.getAttributeNames() 
session.setAttribute() 
session.removeAttribute()

Using the session.invalidate() method, which is often used to log out a user, 
only invalidates the current session for a user—the user’s authentication information 
still remains valid and is stored in the context of the server or virtual host
*/
//Get the session and the counter param attribute

out.println("<h1>Session Counter Information</h1>");
	  HttpSession session = request.getSession (true);
	  Integer ival = (Integer) 
	                 session.getAttribute("simplesession.counter");
	  if (ival == null) // Initialize the counter
	    ival = 1;
	  else // Increment the counter
	    ival = ival.intValue() + 1;
	  // Set the new attribute value in the session
	  session.setAttribute("simplesession.counter", ival);
	  // Output the HTML page
	  out.println("You have hit this page "+ival + " times! <br>");
	  session.setAttribute("simplesession.lastTime",LocalDateTime.now());
	  out.println("<h1>All Session Atributtes Information</h1>");  
//Shows how to get all the existing name=value pairs
	  Enumeration<String> sessionNames = session.getAttributeNames();
	  String sessionName = null;
	  Object sessionValue = null;

	  while (sessionNames.hasMoreElements()) {
	    sessionName = (String)sessionNames.nextElement();
	    sessionValue = session.getAttribute(sessionName);
	    out.println("Session name is " + sessionName +
	                       ", value is " + sessionValue+"<br>");
	  }

out.println("</body></html>");
}
}