
package com.cloud.adapi;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * ǰ�õ��Ȳ����б�Ӧ������
 * 
 * <p>VsSchedListType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="VsSchedListType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="schedlist" type="{urn:sangfor:ad:api:soap:v1:base}VsSchedType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VsSchedListType", propOrder = {
    "schedlist"
})
public class VsSchedListType {

    protected List<VsSchedType> schedlist;

    /**
     * Gets the value of the schedlist property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the schedlist property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSchedlist().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VsSchedType }
     * 
     * 
     */
    public List<VsSchedType> getSchedlist() {
        if (schedlist == null) {
            schedlist = new ArrayList<VsSchedType>();
        }
        return this.schedlist;
    }

}
