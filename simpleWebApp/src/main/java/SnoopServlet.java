/* $Id: SnoopServlet.java,v 1.5 2004/02/22 22:57:59 billbarker Exp $
 *
 */
/*   
 *  Copyright 1999-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 *
 * @author James Duncan Davidson 
 * @author Jason Hunter 
 */
@WebServlet("/SnoopServlet")
public class SnoopServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain");

        out.println("Snoop Servlet");
	out.println();
	out.println("Servlet init parameters:");
	Enumeration<String> e = getInitParameterNames();
	while (e.hasMoreElements()) {
	    String key = (String)e.nextElement();
	    String value = getInitParameter(key);
	    out.println("   " + key + " = " + value); 
	}
	out.println();

	out.println("Context init parameters:");
	ServletContext context = getServletContext();
	Enumeration<String> enum1 = context.getInitParameterNames();
	while (enum1.hasMoreElements()) {
	    String key = (String)enum1.nextElement();
            Object value = context.getInitParameter(key);
            out.println("   " + key + " = " + value);
	}
	out.println();

	out.println("Context attributes:");
	enum1 = context.getAttributeNames();
	while (enum1.hasMoreElements()) {
	    String key = (String)enum1.nextElement();
            Object value = context.getAttribute(key);
            out.println("   " + key + " = " + value);
	}
	out.println();
	
        out.println("Request attributes:");
        e = request.getAttributeNames();
        while (e.hasMoreElements()) {
            String key = (String)e.nextElement();
            Object value = request.getAttribute(key);
            out.println("   " + HtmlFilter.filter(key) + " = " + value);
        }
        out.println();
        out.println("Servlet Name: " + getServletName());
        out.println("Protocol: " + request.getProtocol());
        out.println("Scheme: " + request.getScheme());
        out.println("Server Name: " + HtmlFilter.filter(request.getServerName()));
        out.println("Server Port: " + request.getServerPort());
        out.println("Server Info: " + context.getServerInfo());
        out.println("Remote Addr: " + request.getRemoteAddr());
        out.println("Remote Host: " + request.getRemoteHost());
        out.println("Character Encoding: " + HtmlFilter.filter(request.getCharacterEncoding()));
        out.println("Content Length: " + request.getContentLength());
        out.println("Content Type: "+ HtmlFilter.filter(request.getContentType()));
        out.println("Locale: "+ HtmlFilter.filter(request.getLocale().toString()));
        out.println("Default Response Buffer: "+ response.getBufferSize());
        out.println();
        out.println("Parameter names in this request:");
        e = request.getParameterNames();
        while (e.hasMoreElements()) {
            String key = (String)e.nextElement();
            String[] values = request.getParameterValues(key);
            out.print("   " + HtmlFilter.filter(key) + " = ");
            for(int i = 0; i < values.length; i++) {
                out.print(HtmlFilter.filter(values[i]) + " ");
            }
            out.println();
        }
        out.println();
        out.println("Headers in this request:");
        e = request.getHeaderNames();
        while (e.hasMoreElements()) {
            String key = (String)e.nextElement();
            String value = request.getHeader(key);
            out.println(HtmlFilter.filter("   " + key + ": " + value));
        }
        out.println();  
        out.println("Cookies in this request:");
        Cookie[] cookies = request.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            out.println(HtmlFilter.filter("   " + cookie.getName() + " = " + cookie.getValue()));
        }
        out.println();

        out.println("Request Is Secure: " + request.isSecure());
        out.println("Auth Type: " + request.getAuthType());
        out.println("HTTP Method: " + request.getMethod());
        out.println("Remote User: " + request.getRemoteUser());
        out.println("Request URI: " + request.getRequestURI());
        out.println("Context Path: " + request.getContextPath());
        out.println("Servlet Path: " + request.getServletPath());
        out.println("Path Info: " + HtmlFilter.filter(request.getPathInfo()));
	out.println("Path Trans: " + request.getPathTranslated());
        out.println("Query String: " + HtmlFilter.filter(request.getQueryString()));

        out.println();
        HttpSession session = request.getSession();
        out.println("Requested Session Id: " +
        		HtmlFilter.filter(request.getRequestedSessionId()));
        out.println("Current Session Id: " + session.getId());
	out.println("Session Created Time: " + session.getCreationTime());
        out.println("Session Last Accessed Time: " +
                    session.getLastAccessedTime());
        out.println("Session Max Inactive Interval Seconds: " +
                    session.getMaxInactiveInterval());
        out.println();
        out.println("Session values: ");
        Enumeration<String> names = session.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            out.println(HtmlFilter.filter("   " + name + " = " + session.getAttribute(name)));
        }
    }
}
