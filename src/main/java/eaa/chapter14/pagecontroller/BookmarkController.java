package eaa.chapter14.pagecontroller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eaa.chapter14.pagecontroller.domain.Bookmark;
import eaa.chapter14.pagecontroller.domain.Person;
import eaa.support.ForwardingServlet;

public class BookmarkController extends ForwardingServlet {
	
	private static final long serialVersionUID = 1L;
	
	public static final String SUCCESS = "/WEB-INF/chapter14/bookmark.jsp";
	public static final String FAILURE = "/WEB-INF/chapter14/MissingBookmark.jsp";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		try { 
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
		} catch ( Exception ex ) {
			request.setAttribute("exception", ex );
			forward( FAILURE, request, response);
		}
	}
	
}
