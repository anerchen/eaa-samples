
package eaa.chapter15.server.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "createPersonResponse", namespace = "http://service.server.chapter15.eaa/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createPersonResponse", namespace = "http://service.server.chapter15.eaa/")
public class CreatePersonResponse {

    @XmlElement(name = "return", namespace = "")
    private long _return;

    /**
     * 
     * @return
     *     returns long
     */
    public long get_return() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void set_return(long _return) {
        this._return = _return;
    }

}
