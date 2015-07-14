
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * �Ự������Ϣ����(����ʱʹ��)
 * 
 * <p>PersistUpdateInfoType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="PersistUpdateInfoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="persist_name" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType" minOccurs="0"/&gt;
 *         &lt;element name="prior_to_connect" type="{urn:sangfor:ad:api:soap:v1:base}BoolType" minOccurs="0"/&gt;
 *         &lt;element name="sourceIp" type="{urn:sangfor:ad:api:soap:v1:base}PersistSourceIpType" minOccurs="0"/&gt;
 *         &lt;element name="cookie" type="{urn:sangfor:ad:api:soap:v1:base}PersistCookieType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersistUpdateInfoType", propOrder = {
    "persistName",
    "priorToConnect",
    "sourceIp",
    "cookie"
})
public class PersistUpdateInfoType {

    @XmlElement(name = "persist_name")
    protected byte[] persistName;
    @XmlElement(name = "prior_to_connect")
    protected Boolean priorToConnect;
    protected PersistSourceIpType sourceIp;
    protected PersistCookieType cookie;

    /**
     * ��ȡpersistName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getPersistName() {
        return persistName;
    }

    /**
     * ����persistName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setPersistName(byte[] value) {
        this.persistName = value;
    }

    /**
     * ��ȡpriorToConnect���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPriorToConnect() {
        return priorToConnect;
    }

    /**
     * ����priorToConnect���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPriorToConnect(Boolean value) {
        this.priorToConnect = value;
    }

    /**
     * ��ȡsourceIp���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link PersistSourceIpType }
     *     
     */
    public PersistSourceIpType getSourceIp() {
        return sourceIp;
    }

    /**
     * ����sourceIp���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link PersistSourceIpType }
     *     
     */
    public void setSourceIp(PersistSourceIpType value) {
        this.sourceIp = value;
    }

    /**
     * ��ȡcookie���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link PersistCookieType }
     *     
     */
    public PersistCookieType getCookie() {
        return cookie;
    }

    /**
     * ����cookie���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link PersistCookieType }
     *     
     */
    public void setCookie(PersistCookieType value) {
        this.cookie = value;
    }

}
