package eaa.chapter15.server.dtomapper;

import java.util.ArrayList;

import eaa.chapter15.dto.PersonDTO;
import eaa.chapter15.server.domain.Bookmark;
import eaa.chapter15.server.domain.Person;

public class PersonDTOMapper {

	public static PersonDTO mapPerson( Person person ) {
		PersonDTO personDTO = new PersonDTO();
		
		personDTO.setId( person.getId() );
		personDTO.setName( person.getName() );
		
		ArrayList<String> bookmarks = new ArrayList<String>();
		for( Bookmark bookmark : person.getBookmarks() ) {
			bookmarks.add( bookmark.getUrl() );
		}
		
		return personDTO;
	}
	
}
