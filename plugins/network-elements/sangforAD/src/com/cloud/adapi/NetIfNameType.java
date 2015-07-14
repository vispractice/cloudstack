
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * �������
 * 
 * <p>NetIfNameType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="NetIfNameType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ifname" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType"/&gt;
 *         &lt;element name="enable" type="{urn:sangfor:ad:api:soap:v1:base}BoolType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NetIfNameType", propOrder = {
    "ifname",
    "enable"
})
public class NetIfNameType {

    @XmlElement(required = true)
    protected byte[] ifname;
    protected boolean enable;

    /**
     * ��ȡifname���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getIfname() {
        return ifname;
    }

    /**
     * ����ifname���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setIfname(byte[] value) {
        this.ifname = value;
    }

    /**
     * ��ȡenable���Ե�ֵ��
     * 
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * ����enable���Ե�ֵ��
     * 
     */
    public void setEnable(boolean value) {
        this.enable = value;
    }

}
