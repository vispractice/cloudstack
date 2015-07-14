
package com.cloud.adapi;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * SNAT��ַ����Ϣ����
 * 
 * <p>SnatInfoType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="SnatInfoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="snat_name" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType"/&gt;
 *         &lt;element name="ip_addr" type="{urn:sangfor:ad:api:soap:v1:base}AddrElementType" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SnatInfoType", propOrder = {
    "snatName",
    "ipAddr"
})
public class SnatInfoType {

    @XmlElement(name = "snat_name", required = true)
    protected byte[] snatName;
    @XmlElement(name = "ip_addr", required = true)
    protected List<AddrElementType> ipAddr;

    /**
     * ��ȡsnatName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getSnatName() {
        return snatName;
    }

    /**
     * ����snatName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setSnatName(byte[] value) {
        this.snatName = value;
    }

    /**
     * Gets the value of the ipAddr property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ipAddr property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIpAddr().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AddrElementType }
     * 
     * 
     */
    public List<AddrElementType> getIpAddr() {
        if (ipAddr == null) {
            ipAddr = new ArrayList<AddrElementType>();
        }
        return this.ipAddr;
    }

}
