
package eaa.chapter15.server.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "findPersonResponse", namespace = "http://service.server.chapter15.eaa/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findPersonResponse", namespace = "http://service.server.chapter15.eaa/")
public class FindPersonResponse {

    @XmlElement(name = "return", namespace = "")
    private eaa.chapter15.dto.PersonDTO _return;

    /**
     * 
     * @return
     *     returns PersonDTO
     */
    public eaa.chapter15.dto.PersonDTO get_return() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void set_return(eaa.chapter15.dto.PersonDTO _return) {
        this._return = _return;
    }

}
