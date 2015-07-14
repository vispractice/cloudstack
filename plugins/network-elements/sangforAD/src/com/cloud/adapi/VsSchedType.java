
package com.cloud.adapi;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * ǰ�õ��Ȳ�������
 * 
 * <p>VsSchedType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="VsSchedType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="schedname" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="service" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VsSchedType", propOrder = {
    "schedname",
    "service"
})
public class VsSchedType {

    protected List<byte[]> schedname;
    @XmlElement(required = true)
    protected byte[] service;

    /**
     * Gets the value of the schedname property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the schedname property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSchedname().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * byte[]
     * 
     */
    public List<byte[]> getSchedname() {
        if (schedname == null) {
            schedname = new ArrayList<byte[]>();
        }
        return this.schedname;
    }

    /**
     * ��ȡservice���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getService() {
        return service;
    }

    /**
     * ����service���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setService(byte[] value) {
        this.service = value;
    }

}
