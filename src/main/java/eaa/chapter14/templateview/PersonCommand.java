package eaa.chapter14.templateview;

import java.io.IOException;

import javax.servlet.ServletException;

import eaa.chapter14.pagecontroller.domain.Person;

public class PersonCommand extends FrontCommand {

	public static final String SUCCESS = "/WEB-INF/ch_14_tv/person.jsp";
	public static final String FAILURE = "/WEB-INF/ch_14_tv/MissingPerson.jsp";
	
	@Override
	public void process() throws ServletException, IOException {
		Person person = Person.find( Integer.parseInt( request.getParameter("id") ) );
		if (person == null)
			forward( FAILURE, request, response);
		else {
			request.setAttribute("helper", new PersonHelper( person ) );
			request.setAttribute("person", person);
			forward( SUCCESS, request, response);
		}
	}

}
