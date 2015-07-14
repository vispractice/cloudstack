
package com.cloud.adapi;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * �ڵ�״̬��Ϣ����
 * 
 * <p>NodeStatusInfoType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="NodeStatusInfoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="conf_status" type="{urn:sangfor:ad:api:soap:v1:base}NodeConfStatusType"/&gt;
 *         &lt;element name="current_status" type="{urn:sangfor:ad:api:soap:v1:base}NodeCurrentStatusType"/&gt;
 *         &lt;element name="connection" type="{urn:sangfor:ad:api:soap:v1:base}NodeConnectionType"/&gt;
 *         &lt;element name="addr" type="{urn:sangfor:ad:api:soap:v1:base}IpType"/&gt;
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
@XmlType(name = "NodeStatusInfoType", propOrder = {
    "confStatus",
    "currentStatus",
    "connection",
    "addr",
    "port"
})
public class NodeStatusInfoType {

    @XmlElement(name = "conf_status", required = true)
    @XmlSchemaType(name = "string")
    protected NodeConfStatusType confStatus;
    @XmlElement(name = "current_status", required = true)
    @XmlSchemaType(name = "string")
    protected NodeCurrentStatusType currentStatus;
    @XmlElement(required = true)
    protected BigInteger connection;
    @XmlElement(required = true)
    protected String addr;
    @XmlSchemaType(name = "integer")
    protected Integer port;

    /**
     * ��ȡconfStatus���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link NodeConfStatusType }
     *     
     */
    public NodeConfStatusType getConfStatus() {
        return confStatus;
    }

    /**
     * ����confStatus���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link NodeConfStatusType }
     *     
     */
    public void setConfStatus(NodeConfStatusType value) {
        this.confStatus = value;
    }

    /**
     * ��ȡcurrentStatus���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link NodeCurrentStatusType }
     *     
     */
    public NodeCurrentStatusType getCurrentStatus() {
        return currentStatus;
    }

    /**
     * ����currentStatus���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link NodeCurrentStatusType }
     *     
     */
    public void setCurrentStatus(NodeCurrentStatusType value) {
        this.currentStatus = value;
    }

    /**
     * ��ȡconnection���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getConnection() {
        return connection;
    }

    /**
     * ����connection���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setConnection(BigInteger value) {
        this.connection = value;
    }

    /**
     * ��ȡaddr���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddr() {
        return addr;
    }

    /**
     * ����addr���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddr(String value) {
        this.addr = value;
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
