package eaa.chapter14.frontcontroller;

import java.io.IOException;

import javax.servlet.ServletException;

import eaa.chapter14.pagecontroller.domain.Bookmark;
import eaa.chapter14.pagecontroller.domain.Person;

public class BookmarkCommand extends FrontCommand {

	public static final String SUCCESS = "/WEB-INF/ch_14_fc/bookmark.jsp";
	public static final String FAILURE = "/WEB-INF/ch_14_fc/MissingBookmark.jsp";
	
	@Override
	public void process() throws ServletException, IOException {
		Person person = Person.find( Integer.parseInt( request.getParameter("person_id" ) ) );
		Bookmark bookmark = null;
		for( Bookmark bm : person.getBookmarks() ) {
			if ( bm.getId() == Integer.parseInt( request.getParameter("id") ) ) {
				bookmark = bm; 
				break;
			}
		}
		if (bookmark == null)
			forward( FAILURE, request, response);
		else {
			request.setAttribute("bookmark", bookmark);
			forward( SUCCESS, request, response);
		}
	}

}
