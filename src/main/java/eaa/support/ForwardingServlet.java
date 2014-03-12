package eaa.support;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ForwardingServlet extends HttpServlet {

	@Override
	protected abstract void doGet(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException;

	protected final void forward(String target, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException {
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(target);
		dispatcher.forward(request, response);
	}
	
}
