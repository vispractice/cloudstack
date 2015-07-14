
package com.cloud.adapi;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * IP����Ϣ����
 * 
 * <p>IpGroupInfoType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="IpGroupInfoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="name" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType"/&gt;
 *         &lt;element name="ip_info" type="{urn:sangfor:ad:api:soap:v1:base}IpInfoType" maxOccurs="32"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IpGroupInfoType", propOrder = {
    "name",
    "ipInfo"
})
public class IpGroupInfoType {

    @XmlElement(required = true)
    protected byte[] name;
    @XmlElement(name = "ip_info", required = true)
    protected List<IpInfoType> ipInfo;
    
    /**
     * ��ȡname���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getName() {
        return name;
    }

    /**
     * ����name���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setName(byte[] value) {
        this.name = value;
    }

    /**
     * Gets the value of the ipInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ipInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIpInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IpInfoType }
     * 
     * 
     */
    public List<IpInfoType> getIpInfo() {
        if (ipInfo == null) {
            ipInfo = new ArrayList<IpInfoType>();
        }
        return this.ipInfo;
    }

	public void setIpInfo(List<IpInfoType> ipInfo) {
		this.ipInfo = ipInfo;
	}

}
