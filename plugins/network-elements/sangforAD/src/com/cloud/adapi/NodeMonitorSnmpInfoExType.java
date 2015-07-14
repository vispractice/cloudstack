
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Snmp�ڵ��������Ϣ����
 * 
 * <p>NodeMonitorSnmpInfoExType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="NodeMonitorSnmpInfoExType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="community" type="{urn:sangfor:ad:api:soap:v1:base}StringType" minOccurs="0"/&gt;
 *         &lt;element name="cpu_threshold" type="{urn:sangfor:ad:api:soap:v1:base}ThresholdType" minOccurs="0"/&gt;
 *         &lt;element name="cpu_ratio" type="{urn:sangfor:ad:api:soap:v1:base}ThresholdType" minOccurs="0"/&gt;
 *         &lt;element name="mem_threshold" type="{urn:sangfor:ad:api:soap:v1:base}ThresholdType" minOccurs="0"/&gt;
 *         &lt;element name="mem_ratio" type="{urn:sangfor:ad:api:soap:v1:base}ThresholdType" minOccurs="0"/&gt;
 *         &lt;element name="disk_threshold" type="{urn:sangfor:ad:api:soap:v1:base}ThresholdType" minOccurs="0"/&gt;
 *         &lt;element name="disk_ratio" type="{urn:sangfor:ad:api:soap:v1:base}ThresholdType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NodeMonitorSnmpInfoExType", propOrder = {
    "community",
    "cpuThreshold",
    "cpuRatio",
    "memThreshold",
    "memRatio",
    "diskThreshold",
    "diskRatio"
})
public class NodeMonitorSnmpInfoExType {

    protected String community;
    @XmlElement(name = "cpu_threshold")
    @XmlSchemaType(name = "integer")
    protected Integer cpuThreshold;
    @XmlElement(name = "cpu_ratio")
    @XmlSchemaType(name = "integer")
    protected Integer cpuRatio;
    @XmlElement(name = "mem_threshold")
    @XmlSchemaType(name = "integer")
    protected Integer memThreshold;
    @XmlElement(name = "mem_ratio")
    @XmlSchemaType(name = "integer")
    protected Integer memRatio;
    @XmlElement(name = "disk_threshold")
    @XmlSchemaType(name = "integer")
    protected Integer diskThreshold;
    @XmlElement(name = "disk_ratio")
    @XmlSchemaType(name = "integer")
    protected Integer diskRatio;

    /**
     * ��ȡcommunity���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommunity() {
        return community;
    }

    /**
     * ����community���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommunity(String value) {
        this.community = value;
    }

    /**
     * ��ȡcpuThreshold���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCpuThreshold() {
        return cpuThreshold;
    }

    /**
     * ����cpuThreshold���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCpuThreshold(Integer value) {
        this.cpuThreshold = value;
    }

    /**
     * ��ȡcpuRatio���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCpuRatio() {
        return cpuRatio;
    }

    /**
     * ����cpuRatio���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCpuRatio(Integer value) {
        this.cpuRatio = value;
    }

    /**
     * ��ȡmemThreshold���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMemThreshold() {
        return memThreshold;
    }

    /**
     * ����memThreshold���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMemThreshold(Integer value) {
        this.memThreshold = value;
    }

    /**
     * ��ȡmemRatio���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMemRatio() {
        return memRatio;
    }

    /**
     * ����memRatio���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMemRatio(Integer value) {
        this.memRatio = value;
    }

    /**
     * ��ȡdiskThreshold���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDiskThreshold() {
        return diskThreshold;
    }

    /**
     * ����diskThreshold���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDiskThreshold(Integer value) {
        this.diskThreshold = value;
    }

    /**
     * ��ȡdiskRatio���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDiskRatio() {
        return diskRatio;
    }

    /**
     * ����diskRatio���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDiskRatio(Integer value) {
        this.diskRatio = value;
    }

}
