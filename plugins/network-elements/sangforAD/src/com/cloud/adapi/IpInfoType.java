
package com.cloud.adapi;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * ��·��������
 * 
 * <p>IpInfoType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="IpInfoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="netif_name" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType"/&gt;
 *         &lt;element name="ip_addr" type="{urn:sangfor:ad:api:soap:v1:base}IpType" maxOccurs="32"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IpInfoType", propOrder = {
    "netifName",
    "ipAddr"
})
public class IpInfoType {
	//IP组关联的网络接口名称
    @XmlElement(name = "netif_name", required = true)
    protected byte[] netifName;
    //网络接口上的IP
    @XmlElement(name = "ip_addr", required = true)
    protected List<String> ipAddr;

    /**
     * ��ȡnetifName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getNetifName() {
        return netifName;
    }

    /**
     * ����netifName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setNetifName(byte[] value) {
        this.netifName = value;
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
     * {@link String }
     * 
     * 
     */
    public List<String> getIpAddr() {
        if (ipAddr == null) {
            ipAddr = new ArrayList<String>();
        }
        return this.ipAddr;
    }

	public void setIpAddr(List<String> ipAddr) {
		this.ipAddr = ipAddr;
	}

}
