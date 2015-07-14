
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * �ڵ����������Ϣ����(����ʱʹ��)
 * 
 * <p>NodeMonitorBaseUpdateInfoType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="NodeMonitorBaseUpdateInfoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="interval" type="{urn:sangfor:ad:api:soap:v1:base}MonitorTimeType" minOccurs="0"/&gt;
 *         &lt;element name="timeout" type="{urn:sangfor:ad:api:soap:v1:base}MonitorTimeType" minOccurs="0"/&gt;
 *         &lt;element name="tryout" type="{urn:sangfor:ad:api:soap:v1:base}MonitorTimeType" minOccurs="0"/&gt;
 *         &lt;element name="addr" type="{urn:sangfor:ad:api:soap:v1:base}IpType" minOccurs="0"/&gt;
 *         &lt;element name="port" type="{urn:sangfor:ad:api:soap:v1:base}SPortExtType" minOccurs="0"/&gt;
 *         &lt;element name="debug" type="{urn:sangfor:ad:api:soap:v1:base}BoolType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NodeMonitorBaseUpdateInfoType", propOrder = {
    "interval",
    "timeout",
    "tryout",
    "addr",
    "port",
    "debug"
})
public class NodeMonitorBaseUpdateInfoType {

    @XmlSchemaType(name = "integer")
    protected Integer interval;
    @XmlSchemaType(name = "integer")
    protected Integer timeout;
    @XmlSchemaType(name = "integer")
    protected Integer tryout;
    protected String addr;
    protected String port;
    protected Boolean debug;

    /**
     * ��ȡinterval���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInterval() {
        return interval;
    }

    /**
     * ����interval���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInterval(Integer value) {
        this.interval = value;
    }

    /**
     * ��ȡtimeout���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTimeout() {
        return timeout;
    }

    /**
     * ����timeout���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTimeout(Integer value) {
        this.timeout = value;
    }

    /**
     * ��ȡtryout���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTryout() {
        return tryout;
    }

    /**
     * ����tryout���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTryout(Integer value) {
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
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDebug() {
        return debug;
    }

    /**
     * ����debug���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDebug(Boolean value) {
        this.debug = value;
    }

}
