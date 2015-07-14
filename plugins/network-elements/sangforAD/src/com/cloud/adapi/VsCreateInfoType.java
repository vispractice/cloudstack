
package com.cloud.adapi;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * ���������Ϣ���ͣ�����ʱʹ�ã�
 * 
 * <p>VsCreateInfoType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="VsCreateInfoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="name" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType"/&gt;
 *         &lt;element name="enable" type="{urn:sangfor:ad:api:soap:v1:base}BoolType" minOccurs="0"/&gt;
 *         &lt;element name="mode" type="{urn:sangfor:ad:api:soap:v1:base}VsModeType"/&gt;
 *         &lt;element name="http_sched_mode" type="{urn:sangfor:ad:api:soap:v1:base}HttpSchedModeType" minOccurs="0"/&gt;
 *         &lt;element name="tcp_cache_stream" type="{urn:sangfor:ad:api:soap:v1:base}BoolType" minOccurs="0"/&gt;
 *         &lt;element name="tcp_cache_num" type="{urn:sangfor:ad:api:soap:v1:base}IntegerType" minOccurs="0"/&gt;
 *         &lt;element name="end_str" type="{urn:sangfor:ad:api:soap:v1:base}StringType" minOccurs="0"/&gt;
 *         &lt;element name="pre_rule" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType" maxOccurs="100" minOccurs="0"/&gt;
 *         &lt;element name="service_name" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType"/&gt;
 *         &lt;element name="ip_group_name" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType"/&gt;
 *         &lt;element name="node_pool_name" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType"/&gt;
 *         &lt;element name="auto_snat" type="{urn:sangfor:ad:api:soap:v1:base}BoolType" minOccurs="0"/&gt;
 *         &lt;element name="snat_pool" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType" minOccurs="0"/&gt;
 *         &lt;element name="three_transfer" type="{urn:sangfor:ad:api:soap:v1:base}BoolType" minOccurs="0"/&gt;
 *         &lt;element name="ssl" type="{urn:sangfor:ad:api:soap:v1:base}SSLAttrType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VsCreateInfoType", propOrder = {
    "name",
    "enable",
    "mode",
    "httpSchedMode",
    "tcpCacheStream",
    "tcpCacheNum",
    "endStr",
    "preRule",
    "serviceName",
    "ipGroupName",
    "nodePoolName",
    "autoSnat",
    "snatPool",
    "threeTransfer",
    "ssl"
})
public class VsCreateInfoType {

    @XmlElement(required = true)
    protected byte[] name;
    protected Boolean enable;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected VsModeType mode;
    @XmlElement(name = "http_sched_mode")
    @XmlSchemaType(name = "string")
    protected HttpSchedModeType httpSchedMode;
    @XmlElement(name = "tcp_cache_stream")
    protected Boolean tcpCacheStream;
    @XmlElement(name = "tcp_cache_num")
    protected BigInteger tcpCacheNum;
    @XmlElement(name = "end_str")
    protected String endStr;
    @XmlElement(name = "pre_rule")
    protected List<byte[]> preRule;
    @XmlElement(name = "service_name", required = true)
    protected byte[] serviceName;
    @XmlElement(name = "ip_group_name", required = true)
    protected byte[] ipGroupName;
    @XmlElement(name = "node_pool_name", required = true)
    protected byte[] nodePoolName;
    @XmlElement(name = "auto_snat")
    protected Boolean autoSnat;
    @XmlElement(name = "snat_pool")
    protected byte[] snatPool;
    @XmlElement(name = "three_transfer")
    protected Boolean threeTransfer;
    protected SSLAttrType ssl;

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
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEnable() {
        return enable;
    }

    /**
     * ����enable���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEnable(Boolean value) {
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
     * ��ȡhttpSchedMode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link HttpSchedModeType }
     *     
     */
    public HttpSchedModeType getHttpSchedMode() {
        return httpSchedMode;
    }

    /**
     * ����httpSchedMode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link HttpSchedModeType }
     *     
     */
    public void setHttpSchedMode(HttpSchedModeType value) {
        this.httpSchedMode = value;
    }

    /**
     * ��ȡtcpCacheStream���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTcpCacheStream() {
        return tcpCacheStream;
    }

    /**
     * ����tcpCacheStream���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTcpCacheStream(Boolean value) {
        this.tcpCacheStream = value;
    }

    /**
     * ��ȡtcpCacheNum���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTcpCacheNum() {
        return tcpCacheNum;
    }

    /**
     * ����tcpCacheNum���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTcpCacheNum(BigInteger value) {
        this.tcpCacheNum = value;
    }

    /**
     * ��ȡendStr���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndStr() {
        return endStr;
    }

    /**
     * ����endStr���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndStr(String value) {
        this.endStr = value;
    }

    /**
     * Gets the value of the preRule property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the preRule property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPreRule().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * byte[]
     * 
     */
    public List<byte[]> getPreRule() {
        if (preRule == null) {
            preRule = new ArrayList<byte[]>();
        }
        return this.preRule;
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

    /**
     * ��ȡautoSnat���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAutoSnat() {
        return autoSnat;
    }

    /**
     * ����autoSnat���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAutoSnat(Boolean value) {
        this.autoSnat = value;
    }

    /**
     * ��ȡsnatPool���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getSnatPool() {
        return snatPool;
    }

    /**
     * ����snatPool���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setSnatPool(byte[] value) {
        this.snatPool = value;
    }

    /**
     * ��ȡthreeTransfer���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isThreeTransfer() {
        return threeTransfer;
    }

    /**
     * ����threeTransfer���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setThreeTransfer(Boolean value) {
        this.threeTransfer = value;
    }

    /**
     * ��ȡssl���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link SSLAttrType }
     *     
     */
    public SSLAttrType getSsl() {
        return ssl;
    }

    /**
     * ����ssl���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link SSLAttrType }
     *     
     */
    public void setSsl(SSLAttrType value) {
        this.ssl = value;
    }

	public Boolean getEnable() {
		return enable;
	}

	public Boolean getTcpCacheStream() {
		return tcpCacheStream;
	}

	public Boolean getAutoSnat() {
		return autoSnat;
	}

	public Boolean getThreeTransfer() {
		return threeTransfer;
	}

	public void setPreRule(List<byte[]> preRule) {
		this.preRule = preRule;
	}

}
