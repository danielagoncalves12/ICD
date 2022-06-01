import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Example servlet showing request headers
 *
 * @author James Duncan Davidson <duncan@eng.sun.com>
 * Modificado por: Porfírio Filipe
 */
@WebServlet("/CookieEx")
public class CookieEx extends HttpServlet {

	private static final long serialVersionUID = 1L;
	// ResourceBundle rb = ResourceBundle.getBundle("LocalStrings");
	// Use a ResourceBundle for localized string in "LocalStrings_xx.properties" for
	// i18n.
	// The request.getLocale() sets the locale based on the "Accept-Language"
	// request header.
	// ResourceBundle rb = ResourceBundle.getBundle("LocalStrings",
	// request.getLocale());
	// To test other locales.
	// ResourceBundle rb = ResourceBundle.getBundle("LocalStrings", new
	// Locale("fr"));

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		ResourceBundle rb = ResourceBundle.getBundle("LocalStrings", request.getLocale());
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<body bgcolor=\"white\">");
		out.println("<head>");

		String title = rb.getString("cookies.title");
		out.println("<title>" + title + "</title>");
		out.println("</head>");
		out.println("<body>");

		// relative links

		// XXX
		// making these absolute till we work out the
		// addition of a PathInfo issue

		out.println("<a href=\"/examples/servlets/cookies.html\">");
		out.println("<img src=\"/examples/images/code.gif\" height=24 "
				+ "width=24 align=right border=0 alt=\"view code\"></a>");
		out.println("<a href=\"/examples/servlets/index.html\">");
		out.println("<img src=\"/examples/images/return.gif\" height=24 "
				+ "width=24 align=right border=0 alt=\"return\"></a>");

		out.println("<h3>" + title + "</h3>");

		Cookie[] cookies = request.getCookies();
		if ((cookies != null) && (cookies.length > 0)) {
			out.println(rb.getString("cookies.cookies") + "<br>");
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				out.print("Cookie Name: " + HtmlFilter.filter(cookie.getName()) + "<br>");
				out.println("Cookie Value: " + HtmlFilter.filter(cookie.getValue()) + "<br><br>");
				out.println("Cookie Version: " + cookie.getVersion() + "<br>");
				out.println("Cookie Domain: " + HtmlFilter.filter(cookie.getDomain()) + "<br>");
				out.println("Cookie Path: " + HtmlFilter.filter(cookie.getPath()) + "<br>");
				out.println("<br>");
			}
		} else {
			out.println(rb.getString("cookies.no-cookies"));
		}

		String cookieName = request.getParameter("cookiename");
		String cookieValue = request.getParameter("cookievalue");
		String path = request.getParameter("cookiepath");
		String domain = request.getParameter("cookiedomain");
		String secure = request.getParameter("cookiesecure");
		String version = request.getParameter("cookieversion");
		String comment = request.getParameter("cookiecomment");
		String maxage = request.getParameter("cookiemaxage");
		if (cookieName != null && !"".equals(cookieName)) {
			// cookie without value is valid !
			Cookie cookie = new Cookie(cookieName, cookieValue);
			if (!"".equals(path))
				cookie.setPath(path);
			if (!"".equals(domain))
				cookie.setDomain(domain);
			if (!"".equals(secure))
				cookie.setSecure(true);
			if ("1".equals(version))
				cookie.setVersion(1);
			if (!"".equals(comment))
				cookie.setComment(comment);
			if (!"".equals(maxage)) {
				try {
					cookie.setMaxAge(Integer.parseInt(maxage));
				} catch (Exception ex) {
				}
			}

			response.addCookie(cookie);
			out.println("<P>");
			out.println(rb.getString("cookies.set") + "<br>");
			out.print(rb.getString("cookies.name") + "  " + HtmlFilter.filter(cookieName) + "<br>");
			out.print(rb.getString("cookies.value") + "  " + HtmlFilter.filter(cookieValue));
		}

		out.println("<P>");
		out.println(rb.getString("cookies.make-cookie") + "<br>");
		out.print("<form action=\"");
		out.println("CookieEx\" method=POST>");
		out.print(rb.getString("cookies.name") + "  ");
		out.println("<input type=text name=cookiename placeholder=myCookieName><br>");
		out.print(rb.getString("cookies.value") + "  ");
		out.println("<input type=text name=cookievalue placeholder=theCookieValue><br>");
		out.print("Path (The path to the directory or web page that set the cookie. This may be blank if you want to retrieve the cookie from any directory or page)<br>");
		out.println("<input type=text name=cookiepath placeholder=/welcomeUser><br>");
		out.print("Domain (The domain name of your site) <br>");
		out.println("<input type=text name=cookiedomain placeholder=www.example.com><br>");
		out.print("Secure (If this field contains the word \"secure\", then the cookie may only be retrieved with a secure server. If this field is blank, no such restriction exists.)<br>");
		out.println("<input type=text name=cookiesecure placeholder=secure><br>");
		out.print("Version ");
		out.println("<input type=text name=cookieversion placeholder=myCookieVersion><br>");
		out.print("Comment ");
		out.println("<input type=text name=cookiecomment placeholder=myCookieComment><br>");
		out.print("MaxAge (Defines how many seconds the cookie should be valid for. After this time, the cookie cannot be used by a client (browser) when sending a request and it also should be removed from the browser cache)<br> ");
		out.println("<input type=text name=cookiemaxage placeholder=3600><br>");
		out.println("<input type=submit></form>");

		out.println("</body>");
		out.println("</html>");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doGet(request, response);
	}

}
