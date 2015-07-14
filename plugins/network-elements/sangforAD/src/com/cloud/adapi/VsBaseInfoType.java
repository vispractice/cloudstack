
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * ����������Ϣ���ͣ��鿴��������б�ʱʹ�ã�
 * 
 * <p>VsBaseInfoType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="VsBaseInfoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="name" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType"/&gt;
 *         &lt;element name="enable" type="{urn:sangfor:ad:api:soap:v1:base}BoolType"/&gt;
 *         &lt;element name="mode" type="{urn:sangfor:ad:api:soap:v1:base}VsModeType"/&gt;
 *         &lt;element name="service_name" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType"/&gt;
 *         &lt;element name="ip_group_name" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType"/&gt;
 *         &lt;element name="node_pool_name" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VsBaseInfoType", propOrder = {
    "name",
    "enable",
    "mode",
    "serviceName",
    "ipGroupName",
    "nodePoolName"
})
public class VsBaseInfoType {

    @XmlElement(required = true)
    protected byte[] name;
    protected boolean enable;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected VsModeType mode;
    @XmlElement(name = "service_name", required = true)
    protected byte[] serviceName;
    @XmlElement(name = "ip_group_name", required = true)
    protected byte[] ipGroupName;
    @XmlElement(name = "node_pool_name", required = true)
    protected byte[] nodePoolName;

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
     * ��ȡenable���Ե�ֵ��
     * 
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * ����enable���Ե�ֵ��
     * 
     */
    public void setEnable(boolean value) {
        this.enable = value;
    }

    /**
     * ��ȡmode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link VsModeType }
     *     
     */
    public VsModeType getMode() {
        return mode;
    }

    /**
     * ����mode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link VsModeType }
     *     
     */
    public void setMode(VsModeType value) {
        this.mode = value;
    }

    /**
     * ��ȡserviceName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getServiceName() {
        return serviceName;
    }

    /**
     * ����serviceName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setServiceName(byte[] value) {
        this.serviceName = value;
    }

    /**
     * ��ȡipGroupName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getIpGroupName() {
        return ipGroupName;
    }

    /**
     * ����ipGroupName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setIpGroupName(byte[] value) {
        this.ipGroupName = value;
    }

    /**
     * ��ȡnodePoolName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getNodePoolName() {
        return nodePoolName;
    }

    /**
     * ����nodePoolName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setNodePoolName(byte[] value) {
        this.nodePoolName = value;
    }

}
