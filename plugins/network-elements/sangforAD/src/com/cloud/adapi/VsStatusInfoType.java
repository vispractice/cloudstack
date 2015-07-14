
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * �������״̬��Ϣ����
 * 
 * <p>VsStatusInfoType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="VsStatusInfoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="name" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType"/&gt;
 *         &lt;element name="inbytes" type="{urn:sangfor:ad:api:soap:v1:base}StringType"/&gt;
 *         &lt;element name="outbytes" type="{urn:sangfor:ad:api:soap:v1:base}StringType"/&gt;
 *         &lt;element name="inpackets" type="{urn:sangfor:ad:api:soap:v1:base}StringType"/&gt;
 *         &lt;element name="outpackets" type="{urn:sangfor:ad:api:soap:v1:base}StringType"/&gt;
 *         &lt;element name="curconns" type="{urn:sangfor:ad:api:soap:v1:base}StringType"/&gt;
 *         &lt;element name="newconns" type="{urn:sangfor:ad:api:soap:v1:base}StringType"/&gt;
 *         &lt;element name="maxconns" type="{urn:sangfor:ad:api:soap:v1:base}StringType"/&gt;
 *         &lt;element name="totalconns" type="{urn:sangfor:ad:api:soap:v1:base}StringType"/&gt;
 *         &lt;element name="requests" type="{urn:sangfor:ad:api:soap:v1:base}StringType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VsStatusInfoType", propOrder = {
    "name",
    "inbytes",
    "outbytes",
    "inpackets",
    "outpackets",
    "curconns",
    "newconns",
    "maxconns",
    "totalconns",
    "requests"
})
public class VsStatusInfoType {

    @XmlElement(required = true)
    protected byte[] name;
    @XmlElement(required = true)
    protected String inbytes;
    @XmlElement(required = true)
    protected String outbytes;
    @XmlElement(required = true)
    protected String inpackets;
    @XmlElement(required = true)
    protected String outpackets;
    @XmlElement(required = true)
    protected String curconns;
    @XmlElement(required = true)
    protected String newconns;
    @XmlElement(required = true)
    protected String maxconns;
    @XmlElement(required = true)
    protected String totalconns;
    @XmlElement(required = true)
    protected String requests;

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
     * ��ȡinbytes���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInbytes() {
        return inbytes;
    }

    /**
     * ����inbytes���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInbytes(String value) {
        this.inbytes = value;
    }

    /**
     * ��ȡoutbytes���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutbytes() {
        return outbytes;
    }

    /**
     * ����outbytes���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutbytes(String value) {
        this.outbytes = value;
    }

    /**
     * ��ȡinpackets���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInpackets() {
        return inpackets;
    }

    /**
     * ����inpackets���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInpackets(String value) {
        this.inpackets = value;
    }

    /**
     * ��ȡoutpackets���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutpackets() {
        return outpackets;
    }

    /**
     * ����outpackets���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutpackets(String value) {
        this.outpackets = value;
    }

    /**
     * ��ȡcurconns���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurconns() {
        return curconns;
    }

    /**
     * ����curconns���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurconns(String value) {
        this.curconns = value;
    }

    /**
     * ��ȡnewconns���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewconns() {
        return newconns;
    }

    /**
     * ����newconns���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewconns(String value) {
        this.newconns = value;
    }

    /**
     * ��ȡmaxconns���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxconns() {
        return maxconns;
    }

    /**
     * ����maxconns���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxconns(String value) {
        this.maxconns = value;
    }

    /**
     * ��ȡtotalconns���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalconns() {
        return totalconns;
    }

    /**
     * ����totalconns���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalconns(String value) {
        this.totalconns = value;
    }

    /**
     * ��ȡrequests���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequests() {
        return requests;
    }

    /**
     * ����requests���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequests(String value) {
        this.requests = value;
    }

}
