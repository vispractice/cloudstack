
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * �ڵ����������Ϣ����(����/�鿴ʱʹ��)
 * 
 * <p>NodeMonitorBaseInfoType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="NodeMonitorBaseInfoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="type" type="{urn:sangfor:ad:api:soap:v1:base}MonitorType"/&gt;
 *         &lt;element name="interval" type="{urn:sangfor:ad:api:soap:v1:base}MonitorTimeType"/&gt;
 *         &lt;element name="timeout" type="{urn:sangfor:ad:api:soap:v1:base}MonitorTimeType"/&gt;
 *         &lt;element name="tryout" type="{urn:sangfor:ad:api:soap:v1:base}MonitorTimeType"/&gt;
 *         &lt;element name="addr" type="{urn:sangfor:ad:api:soap:v1:base}IpType"/&gt;
 *         &lt;element name="port" type="{urn:sangfor:ad:api:soap:v1:base}SPortExtType" minOccurs="0"/&gt;
 *         &lt;element name="debug" type="{urn:sangfor:ad:api:soap:v1:base}BoolType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NodeMonitorBaseInfoType", propOrder = {
    "type",
    "interval",
    "timeout",
    "tryout",
    "addr",
    "port",
    "debug"
})
public class NodeMonitorBaseInfoType {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected MonitorType type;
    @XmlSchemaType(name = "integer")
    protected int interval;
    @XmlSchemaType(name = "integer")
    protected int timeout;
    @XmlSchemaType(name = "integer")
    protected int tryout;
    @XmlElement(required = true)
    protected String addr;
    protected String port;
    protected boolean debug;

    /**
     * ��ȡtype���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link MonitorType }
     *     
     */
    public MonitorType getType() {
        return type;
    }

    /**
     * ����type���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link MonitorType }
     *     
     */
    public void setType(MonitorType value) {
        this.type = value;
    }

    /**
     * ��ȡinterval���Ե�ֵ��
     * 
     */
    public int getInterval() {
        return interval;
    }

    /**
     * ����interval���Ե�ֵ��
     * 
     */
    public void setInterval(int value) {
        this.interval = value;
    }

    /**
     * ��ȡtimeout���Ե�ֵ��
     * 
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * ����timeout���Ե�ֵ��
     * 
     */
    public void setTimeout(int value) {
        this.timeout = value;
    }

    /**
     * ��ȡtryout���Ե�ֵ��
     * 
     */
    public int getTryout() {
        return tryout;
    }

    /**
     * ����tryout���Ե�ֵ��
     * 
     */
    public void setTryout(int value) {
        this.tryout = value;
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
     *     {@link String }
     *     
     */
    public String getPort() {
        return port;
    }

    /**
     * ����port���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPort(String value) {
        this.port = value;
    }

    /**
     * ��ȡdebug���Ե�ֵ��
     * 
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * ����debug���Ե�ֵ��
     * 
     */
    public void setDebug(boolean value) {
        this.debug = value;
    }

}
