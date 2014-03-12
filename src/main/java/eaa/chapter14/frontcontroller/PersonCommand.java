package eaa.chapter14.frontcontroller;

import java.io.IOException;

import javax.servlet.ServletException;

import eaa.chapter14.pagecontroller.domain.Person;

public class PersonCommand extends FrontCommand {

	public static final String SUCCESS = "/WEB-INF/ch_14_fc/person.jsp";
	public static final String FAILURE = "/WEB-INF/ch_14_fc/MissingPerson.jsp";
	
	@Override
	public void process() throws ServletException, IOException {
		Person person = Person.find( Integer.parseInt( request.getParameter("id") ) );
		if (person == null)
			forward( FAILURE, request, response);
		else {
			request.setAttribute("person", person);
			forward( SUCCESS, request, response);
		}
	}

}
