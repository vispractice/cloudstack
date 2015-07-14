
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * WAN����Ϣ����
 * 
 * <p>WanInfoType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="WanInfoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="wan_name" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType"/&gt;
 *         &lt;element name="ifname" type="{urn:sangfor:ad:api:soap:v1:base}NetIfNameType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WanInfoType", propOrder = {
    "wanName",
    "ifname"
})
public class WanInfoType {

    @XmlElement(name = "wan_name", required = true)
    protected byte[] wanName;
    @XmlElement(required = true)
    protected NetIfNameType ifname;

    /**
     * ��ȡwanName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getWanName() {
        return wanName;
    }

    /**
     * ����wanName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setWanName(byte[] value) {
        this.wanName = value;
    }

    /**
     * ��ȡifname���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link NetIfNameType }
     *     
     */
    public NetIfNameType getIfname() {
        return ifname;
    }

    /**
     * ����ifname���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link NetIfNameType }
     *     
     */
    public void setIfname(NetIfNameType value) {
        this.ifname = value;
    }

}
