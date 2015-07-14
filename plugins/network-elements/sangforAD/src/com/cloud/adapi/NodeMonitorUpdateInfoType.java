
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * �ڵ��������Ϣ���ͣ�����ʱʹ�ã�
 * 
 * <p>NodeMonitorUpdateInfoType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="NodeMonitorUpdateInfoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="monitor_name" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType" minOccurs="0"/&gt;
 *         &lt;element name="base_info" type="{urn:sangfor:ad:api:soap:v1:base}NodeMonitorBaseUpdateInfoType" minOccurs="0"/&gt;
 *         &lt;element name="connect_ext" type="{urn:sangfor:ad:api:soap:v1:base}NodeMonitorConnectInfoExType" minOccurs="0"/&gt;
 *         &lt;element name="snmp_ext" type="{urn:sangfor:ad:api:soap:v1:base}NodeMonitorSnmpInfoExType" minOccurs="0"/&gt;
 *         &lt;element name="dns_ext" type="{urn:sangfor:ad:api:soap:v1:base}NodeMonitorDnsInfoExType" minOccurs="0"/&gt;
 *         &lt;element name="radius_ext" type="{urn:sangfor:ad:api:soap:v1:base}NodeMonitorRadiusInfoExType" minOccurs="0"/&gt;
 *         &lt;element name="ftp_ext" type="{urn:sangfor:ad:api:soap:v1:base}NodeMonitorFtpInfoExType" minOccurs="0"/&gt;
 *         &lt;element name="tcphalf_ext" type="{urn:sangfor:ad:api:soap:v1:base}NodeMonitorTcpHalfInfoExType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NodeMonitorUpdateInfoType", propOrder = {
    "monitorName",
    "baseInfo",
    "connectExt",
    "snmpExt",
    "dnsExt",
    "radiusExt",
    "ftpExt",
    "tcphalfExt"
})
public class NodeMonitorUpdateInfoType {

    @XmlElement(name = "monitor_name")
    protected byte[] monitorName;
    @XmlElement(name = "base_info")
    protected NodeMonitorBaseUpdateInfoType baseInfo;
    @XmlElement(name = "connect_ext")
    protected NodeMonitorConnectInfoExType connectExt;
    @XmlElement(name = "snmp_ext")
    protected NodeMonitorSnmpInfoExType snmpExt;
    @XmlElement(name = "dns_ext")
    protected NodeMonitorDnsInfoExType dnsExt;
    @XmlElement(name = "radius_ext")
    protected NodeMonitorRadiusInfoExType radiusExt;
    @XmlElement(name = "ftp_ext")
    protected NodeMonitorFtpInfoExType ftpExt;
    @XmlElement(name = "tcphalf_ext")
    protected NodeMonitorTcpHalfInfoExType tcphalfExt;

    /**
     * ��ȡmonitorName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getMonitorName() {
        return monitorName;
    }

    /**
     * ����monitorName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setMonitorName(byte[] value) {
        this.monitorName = value;
    }

    /**
     * ��ȡbaseInfo���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link NodeMonitorBaseUpdateInfoType }
     *     
     */
    public NodeMonitorBaseUpdateInfoType getBaseInfo() {
        return baseInfo;
    }

    /**
     * ����baseInfo���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link NodeMonitorBaseUpdateInfoType }
     *     
     */
    public void setBaseInfo(NodeMonitorBaseUpdateInfoType value) {
        this.baseInfo = value;
    }

    /**
     * ��ȡconnectExt���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link NodeMonitorConnectInfoExType }
     *     
     */
    public NodeMonitorConnectInfoExType getConnectExt() {
        return connectExt;
    }

    /**
     * ����connectExt���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link NodeMonitorConnectInfoExType }
     *     
     */
    public void setConnectExt(NodeMonitorConnectInfoExType value) {
        this.connectExt = value;
    }

    /**
     * ��ȡsnmpExt���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link NodeMonitorSnmpInfoExType }
     *     
     */
    public NodeMonitorSnmpInfoExType getSnmpExt() {
        return snmpExt;
    }

    /**
     * ����snmpExt���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link NodeMonitorSnmpInfoExType }
     *     
     */
    public void setSnmpExt(NodeMonitorSnmpInfoExType value) {
        this.snmpExt = value;
    }

    /**
     * ��ȡdnsExt���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link NodeMonitorDnsInfoExType }
     *     
     */
    public NodeMonitorDnsInfoExType getDnsExt() {
        return dnsExt;
    }

    /**
     * ����dnsExt���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link NodeMonitorDnsInfoExType }
     *     
     */
    public void setDnsExt(NodeMonitorDnsInfoExType value) {
        this.dnsExt = value;
    }

    /**
     * ��ȡradiusExt���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link NodeMonitorRadiusInfoExType }
     *     
     */
    public NodeMonitorRadiusInfoExType getRadiusExt() {
        return radiusExt;
    }

    /**
     * ����radiusExt���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link NodeMonitorRadiusInfoExType }
     *     
     */
    public void setRadiusExt(NodeMonitorRadiusInfoExType value) {
        this.radiusExt = value;
    }

    /**
     * ��ȡftpExt���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link NodeMonitorFtpInfoExType }
     *     
     */
    public NodeMonitorFtpInfoExType getFtpExt() {
        return ftpExt;
    }

    /**
     * ����ftpExt���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link NodeMonitorFtpInfoExType }
     *     
     */
    public void setFtpExt(NodeMonitorFtpInfoExType value) {
        this.ftpExt = value;
    }

    /**
     * ��ȡtcphalfExt���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link NodeMonitorTcpHalfInfoExType }
     *     
     */
    public NodeMonitorTcpHalfInfoExType getTcphalfExt() {
        return tcphalfExt;
    }

    /**
     * ����tcphalfExt���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link NodeMonitorTcpHalfInfoExType }
     *     
     */
    public void setTcphalfExt(NodeMonitorTcpHalfInfoExType value) {
        this.tcphalfExt = value;
    }

}
