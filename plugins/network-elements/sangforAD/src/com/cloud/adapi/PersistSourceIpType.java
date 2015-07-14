
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * SourceIp�Ự������Ϣ����
 * 
 * <p>PersistSourceIpType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="PersistSourceIpType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="mask_v4" type="{urn:sangfor:ad:api:soap:v1:base}IpType" minOccurs="0"/&gt;
 *         &lt;element name="mask_v6" type="{urn:sangfor:ad:api:soap:v1:base}IpType" minOccurs="0"/&gt;
 *         &lt;element name="sourceip_timeout" type="{urn:sangfor:ad:api:soap:v1:base}STimeOutType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersistSourceIpType", propOrder = {
    "maskV4",
    "maskV6",
    "sourceipTimeout"
})
public class PersistSourceIpType {

    @XmlElement(name = "mask_v4")
    protected String maskV4;
    @XmlElement(name = "mask_v6")
    protected String maskV6;
    @XmlElement(name = "sourceip_timeout")
    @XmlSchemaType(name = "integer")
    protected Integer sourceipTimeout;

    /**
     * ��ȡmaskV4���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaskV4() {
        return maskV4;
    }

    /**
     * ����maskV4���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaskV4(String value) {
        this.maskV4 = value;
    }

    /**
     * ��ȡmaskV6���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaskV6() {
        return maskV6;
    }

    /**
     * ����maskV6���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaskV6(String value) {
        this.maskV6 = value;
    }

    /**
     * ��ȡsourceipTimeout���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSourceipTimeout() {
        return sourceipTimeout;
    }

    /**
     * ����sourceipTimeout���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSourceipTimeout(Integer value) {
        this.sourceipTimeout = value;
    }

}
