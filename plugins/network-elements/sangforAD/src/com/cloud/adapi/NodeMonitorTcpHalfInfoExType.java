
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * tcp�����ӽڵ��������Ϣ����
 * 
 * <p>NodeMonitorTcpHalfInfoExType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="NodeMonitorTcpHalfInfoExType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="src_addr" type="{urn:sangfor:ad:api:soap:v1:base}StringType" minOccurs="0"/&gt;
 *         &lt;element name="interface" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NodeMonitorTcpHalfInfoExType", propOrder = {
    "srcAddr",
    "_interface"
})
public class NodeMonitorTcpHalfInfoExType {

    @XmlElement(name = "src_addr")
    protected String srcAddr;
    @XmlElement(name = "interface")
    protected byte[] _interface;

    /**
     * ��ȡsrcAddr���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrcAddr() {
        return srcAddr;
    }

    /**
     * ����srcAddr���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrcAddr(String value) {
        this.srcAddr = value;
    }

    /**
     * ��ȡinterface���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getInterface() {
        return _interface;
    }

    /**
     * ����interface���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setInterface(byte[] value) {
        this._interface = value;
    }

}
