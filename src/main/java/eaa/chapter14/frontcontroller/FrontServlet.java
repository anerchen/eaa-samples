package eaa.chapter14.frontcontroller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eaa.support.ForwardingServlet;

public class FrontServlet extends ForwardingServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String command = ((String) request.getParameter( "command" ));
			String clazz = "eaa.chapter14.frontcontroller." + command + "Command";
			System.out.println(" class =" + clazz );
			
			FrontCommand fc = (FrontCommand) Class.forName( clazz ).newInstance();
			fc.init( getServletContext(), request, response );
			fc.process();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException( e );
		} 

	}

}
