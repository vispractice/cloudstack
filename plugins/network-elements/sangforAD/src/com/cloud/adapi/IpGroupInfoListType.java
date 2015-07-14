
package com.cloud.adapi;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * IP����Ϣ�б�����
 * 
 * <p>IpGroupInfoListType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="IpGroupInfoListType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ip_group_list" type="{urn:sangfor:ad:api:soap:v1:base}IpGroupBaseType" maxOccurs="100" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IpGroupInfoListType", propOrder = {
    "ipGroupList"
})
public class IpGroupInfoListType {

    @XmlElement(name = "ip_group_list")
    protected List<IpGroupBaseType> ipGroupList;

    /**
     * Gets the value of the ipGroupList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ipGroupList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIpGroupList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IpGroupBaseType }
     * 
     * 
     */
    public List<IpGroupBaseType> getIpGroupList() {
        if (ipGroupList == null) {
            ipGroupList = new ArrayList<IpGroupBaseType>();
        }
        return this.ipGroupList;
    }

}
