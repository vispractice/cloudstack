
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Cookie�Ự������Ϣ����
 * 
 * <p>PersistCookieType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="PersistCookieType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="cookie_type" type="{urn:sangfor:ad:api:soap:v1:base}CookieType" minOccurs="0"/&gt;
 *         &lt;element name="cookie_name" type="{urn:sangfor:ad:api:soap:v1:base}CookieNameType" minOccurs="0"/&gt;
 *         &lt;element name="cookie_domain" type="{urn:sangfor:ad:api:soap:v1:base}CookieDomainType" minOccurs="0"/&gt;
 *         &lt;element name="cookie_path" type="{urn:sangfor:ad:api:soap:v1:base}CookiePathType" minOccurs="0"/&gt;
 *         &lt;element name="cookie_timeout" type="{urn:sangfor:ad:api:soap:v1:base}CTimeOutType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersistCookieType", propOrder = {
    "cookieType",
    "cookieName",
    "cookieDomain",
    "cookiePath",
    "cookieTimeout"
})
public class PersistCookieType {

    @XmlElement(name = "cookie_type")
    @XmlSchemaType(name = "string")
    protected CookieType cookieType;
    @XmlElement(name = "cookie_name")
    protected byte[] cookieName;
    @XmlElement(name = "cookie_domain")
    protected String cookieDomain;
    @XmlElement(name = "cookie_path")
    protected String cookiePath;
    @XmlElement(name = "cookie_timeout")
    @XmlSchemaType(name = "integer")
    protected Integer cookieTimeout;

    /**
     * ��ȡcookieType���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link CookieType }
     *     
     */
    public CookieType getCookieType() {
        return cookieType;
    }

    /**
     * ����cookieType���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link CookieType }
     *     
     */
    public void setCookieType(CookieType value) {
        this.cookieType = value;
    }

    /**
     * ��ȡcookieName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getCookieName() {
        return cookieName;
    }

    /**
     * ����cookieName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setCookieName(byte[] value) {
        this.cookieName = value;
    }

    /**
     * ��ȡcookieDomain���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCookieDomain() {
        return cookieDomain;
    }

    /**
     * ����cookieDomain���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCookieDomain(String value) {
        this.cookieDomain = value;
    }

    /**
     * ��ȡcookiePath���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCookiePath() {
        return cookiePath;
    }

    /**
     * ����cookiePath���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCookiePath(String value) {
        this.cookiePath = value;
    }

    /**
     * ��ȡcookieTimeout���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCookieTimeout() {
        return cookieTimeout;
    }

    /**
     * ����cookieTimeout���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCookieTimeout(Integer value) {
        this.cookieTimeout = value;
    }

}
