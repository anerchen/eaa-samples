package eaa.chapter15.server.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

import eaa.chapter15.dto.PersonDTO;
import eaa.chapter15.server.domain.Bookmark;
import eaa.chapter15.server.domain.Person;
import eaa.chapter15.server.dtomapper.PersonDTOMapper;
import eaa.support.ApplicationException;
import eaa.support.DB;

@WebService
public class PersonService {

	@WebMethod
	public PersonDTO findPerson( long id ) throws ApplicationException {
		
		try { 
			Person person = Person.find( id );
			if (person == null) {
				throw new ApplicationException("Person id " + id + " not found." );
			}
			PersonDTO personDTO = PersonDTOMapper.mapPerson( person );
			DB.rollback();
			return personDTO;
		} catch ( ApplicationException ex ) {
			throw ex;
		} catch ( Exception ex ) {
			throw new ApplicationException( ex );
		}
		
	}
	
	@WebMethod
	public long createPerson( String name, String[] bookmarkUrls ) throws ApplicationException {
		try {
			Person person = new Person( name );
			long id = person.insert();
			
			for( String bm : bookmarkUrls ) {
				Bookmark bookmark = new Bookmark();
				bookmark.setPerson( person );
				bookmark.setUrl( bm );
				bookmark.insert();
			}
			
			DB.commit();
			return id;
		} catch ( Exception ex ) {
			throw new ApplicationException( ex );
		}
	}
	
}
