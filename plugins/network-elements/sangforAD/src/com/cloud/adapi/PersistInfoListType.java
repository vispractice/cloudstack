
package com.cloud.adapi;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * �Ự�����б���Ϣ����
 * 
 * <p>PersistInfoListType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="PersistInfoListType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="persist_info" type="{urn:sangfor:ad:api:soap:v1:base}PersistBaseInfoType" maxOccurs="50" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersistInfoListType", propOrder = {
    "persistInfo"
})
public class PersistInfoListType {

    @XmlElement(name = "persist_info")
    protected List<PersistBaseInfoType> persistInfo;

    /**
     * Gets the value of the persistInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the persistInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPersistInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PersistBaseInfoType }
     * 
     * 
     */
    public List<PersistBaseInfoType> getPersistInfo() {
        if (persistInfo == null) {
            persistInfo = new ArrayList<PersistBaseInfoType>();
        }
        return this.persistInfo;
    }

}
