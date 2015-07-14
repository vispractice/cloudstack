
package com.cloud.adapi;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * �߲��������SSL����
 * 
 * <p>SSLAttrType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="SSLAttrType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ssl_profile" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType" minOccurs="0"/&gt;
 *         &lt;element name="http_redir_enable" type="{urn:sangfor:ad:api:soap:v1:base}BoolType" minOccurs="0"/&gt;
 *         &lt;element name="port" type="{urn:sangfor:ad:api:soap:v1:base}IntegerType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SSLAttrType", propOrder = {
    "sslProfile",
    "httpRedirEnable",
    "port"
})
public class SSLAttrType {

    @XmlElement(name = "ssl_profile")
    protected byte[] sslProfile;
    @XmlElement(name = "http_redir_enable")
    protected Boolean httpRedirEnable;
    protected BigInteger port;

    /**
     * ��ȡsslProfile���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getSslProfile() {
        return sslProfile;
    }

    /**
     * ����sslProfile���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setSslProfile(byte[] value) {
        this.sslProfile = value;
    }

    /**
     * ��ȡhttpRedirEnable���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isHttpRedirEnable() {
        return httpRedirEnable;
    }

    /**
     * ����httpRedirEnable���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHttpRedirEnable(Boolean value) {
        this.httpRedirEnable = value;
    }

    /**
     * ��ȡport���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPort() {
        return port;
    }

    /**
     * ����port���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPort(BigInteger value) {
        this.port = value;
    }

}
