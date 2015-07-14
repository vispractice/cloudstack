
package com.cloud.adapi;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * �ڵ���Ϣ���ͣ�����ʱʹ�ã�
 * 
 * <p>NodeInfoExType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="NodeInfoExType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="status" type="{urn:sangfor:ad:api:soap:v1:base}NodeConfStatusType" minOccurs="0"/&gt;
 *         &lt;element name="ratio" type="{urn:sangfor:ad:api:soap:v1:base}NodeRatioType" minOccurs="0"/&gt;
 *         &lt;element name="max_connects" type="{urn:sangfor:ad:api:soap:v1:base}NodeMaxConnectsType" minOccurs="0"/&gt;
 *         &lt;element name="new_connects" type="{urn:sangfor:ad:api:soap:v1:base}NodeNewConnectsType" minOccurs="0"/&gt;
 *         &lt;element name="max_request" type="{urn:sangfor:ad:api:soap:v1:base}NodeMaxRequestType" minOccurs="0"/&gt;
 *         &lt;element name="rs_val1" type="{urn:sangfor:ad:api:soap:v1:base}NodeRsVal1Type" minOccurs="0"/&gt;
 *         &lt;element name="port" type="{urn:sangfor:ad:api:soap:v1:base}SPortType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NodeInfoExType", propOrder = {
    "status",
    "ratio",
    "maxConnects",
    "newConnects",
    "maxRequest",
    "rsVal1",
    "port"
})
public class NodeInfoExType {

    @XmlSchemaType(name = "string")
    protected NodeConfStatusType status;
    @XmlSchemaType(name = "integer")
    protected Integer ratio;
    @XmlElement(name = "max_connects")
    protected BigInteger maxConnects;
    @XmlElement(name = "new_connects")
    protected BigInteger newConnects;
    @XmlElement(name = "max_request")
    protected BigInteger maxRequest;
    @XmlElement(name = "rs_val1")
    protected byte[] rsVal1;
    @XmlSchemaType(name = "integer")
    protected Integer port;

    /**
     * ��ȡstatus���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link NodeConfStatusType }
     *     
     */
    public NodeConfStatusType getStatus() {
        return status;
    }

    /**
     * ����status���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link NodeConfStatusType }
     *     
     */
    public void setStatus(NodeConfStatusType value) {
        this.status = value;
    }

    /**
     * ��ȡratio���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRatio() {
        return ratio;
    }

    /**
     * ����ratio���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRatio(Integer value) {
        this.ratio = value;
    }

    /**
     * ��ȡmaxConnects���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMaxConnects() {
        return maxConnects;
    }

    /**
     * ����maxConnects���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMaxConnects(BigInteger value) {
        this.maxConnects = value;
    }

    /**
     * ��ȡnewConnects���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNewConnects() {
        return newConnects;
    }

    /**
     * ����newConnects���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNewConnects(BigInteger value) {
        this.newConnects = value;
    }

    /**
     * ��ȡmaxRequest���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMaxRequest() {
        return maxRequest;
    }

    /**
     * ����maxRequest���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMaxRequest(BigInteger value) {
        this.maxRequest = value;
    }

    /**
     * ��ȡrsVal1���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getRsVal1() {
        return rsVal1;
    }

    /**
     * ����rsVal1���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setRsVal1(byte[] value) {
        this.rsVal1 = value;
    }

    /**
     * ��ȡport���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPort() {
        return port;
    }

    /**
     * ����port���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPort(Integer value) {
        this.port = value;
    }

}
