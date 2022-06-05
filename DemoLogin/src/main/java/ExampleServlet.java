

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class dump
 */
@WebServlet("/ExampleServlet")
public class ExampleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExampleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		   // check valid for login
		   bean.Check.login(request, response); 
			// Set the MIME type for the response message
	       response.setContentType("text/html");
	       // Write to network
	       PrintWriter out = response.getWriter();
	       out.println("<h3>");
	       out.println("Sobre os métodos do 'request':<br><br>");
	       out.println("getContextPath()---------> "+request.getContextPath()+"<br>");
	       out.println("getLocalAddr()-----------> "+request.getLocalAddr()+"<br>");
	       out.println("getLocalName()-----------> "+request.getLocalName()+"<br>");
	       out.println("getLocalPort()-----------> "+request.getLocalPort()+"<br>");
	       out.println("getMethod()--------------> "+request.getMethod()+"<br>");
	       out.println("getPathInfo()------------> "+request.getPathInfo()+"<br>");
	       out.println("getProtocol()------------> "+request.getProtocol()+"<br>");
	       out.println("getQueryString()---------> "+request.getQueryString()+"<br>");
	       out.println("getRequestedSessionId()--> "+request.getRequestedSessionId()+"<br>");
	       out.println("getRequestURI()----------> "+request.getRequestURI()+"<br>");
	       out.println("getRequestURL()----------> "+request.getRequestURL()+"<br>");
	       out.println("getScheme()--------------> "+request.getScheme()+"<br>"); 
	       out.println("getServerName()----------> "+request.getServerName()+"<br>"); 
	       out.println("getServerPort()----------> "+request.getServerPort()+"<br>"); 
	       out.println("getServletPath()---------> "+request.getServletPath()+"<br>"); 
	       out.println("getParameterNames()------> "+request.getParameterNames()+"<br>");
	       out.println("</h3>");

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
