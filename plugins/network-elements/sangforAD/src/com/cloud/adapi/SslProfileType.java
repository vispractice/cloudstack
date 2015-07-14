
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * SSL������Ϣ����
 * 
 * <p>SslProfileType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="SslProfileType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ssl_profile_name" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType"/&gt;
 *         &lt;element name="ssl_cert_name" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SslProfileType", propOrder = {
    "sslProfileName",
    "sslCertName"
})
public class SslProfileType {

    @XmlElement(name = "ssl_profile_name", required = true)
    protected byte[] sslProfileName;
    @XmlElement(name = "ssl_cert_name", required = true)
    protected byte[] sslCertName;

    /**
     * ��ȡsslProfileName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getSslProfileName() {
        return sslProfileName;
    }

    /**
     * ����sslProfileName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setSslProfileName(byte[] value) {
        this.sslProfileName = value;
    }

    /**
     * ��ȡsslCertName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getSslCertName() {
        return sslCertName;
    }

    /**
     * ����sslCertName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setSslCertName(byte[] value) {
        this.sslCertName = value;
    }

}
