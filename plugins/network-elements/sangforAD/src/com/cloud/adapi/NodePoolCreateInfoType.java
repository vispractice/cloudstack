
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
 * �ڵ����ϸ��Ϣ���ͣ�����ʱʹ�ã�
 * 
 * <p>NodePoolCreateInfoType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="NodePoolCreateInfoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="node_pool_name" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType"/&gt;
 *         &lt;element name="node_lb_method" type="{urn:sangfor:ad:api:soap:v1:base}NodeLbMethodType" minOccurs="0"/&gt;
 *         &lt;element name="hash_type" type="{urn:sangfor:ad:api:soap:v1:base}NodeHashType" minOccurs="0"/&gt;
 *         &lt;element name="persist1_name" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType"/&gt;
 *         &lt;element name="persist2_name" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType"/&gt;
 *         &lt;element name="monitors" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType" maxOccurs="5"/&gt;
 *         &lt;element name="min_monitors" type="{urn:sangfor:ad:api:soap:v1:base}IntegerType"/&gt;
 *         &lt;element name="recover_interval" type="{urn:sangfor:ad:api:soap:v1:base}NodePoolIntervalType"/&gt;
 *         &lt;element name="stepper_interval" type="{urn:sangfor:ad:api:soap:v1:base}NodePoolIntervalType"/&gt;
 *         &lt;element name="sched_method" type="{urn:sangfor:ad:api:soap:v1:base}SchedMethodType"/&gt;
 *         &lt;element name="conn_stat_all" type="{urn:sangfor:ad:api:soap:v1:base}BoolType"/&gt;
 *         &lt;element name="queue_length" type="{urn:sangfor:ad:api:soap:v1:base}IntegerType" minOccurs="0"/&gt;
 *         &lt;element name="queue_timeout" type="{urn:sangfor:ad:api:soap:v1:base}IntegerType" minOccurs="0"/&gt;
 *         &lt;element name="node_info" type="{urn:sangfor:ad:api:soap:v1:base}NodeInfoType" maxOccurs="500"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NodePoolCreateInfoType", propOrder = {
    "nodePoolName",
    "nodeLbMethod",
    "hashType",
    "persist1Name",
    "persist2Name",
    "monitors",
    "minMonitors",
    "recoverInterval",
    "stepperInterval",
    "schedMethod",
    "connStatAll",
    "queueLength",
    "queueTimeout",
    "nodeInfo"
})
public class NodePoolCreateInfoType {

    @XmlElement(name = "node_pool_name", required = true)
    protected byte[] nodePoolName;
    @XmlElement(name = "node_lb_method")
    @XmlSchemaType(name = "string")
    protected NodeLbMethodType nodeLbMethod;
    @XmlElement(name = "hash_type")
    @XmlSchemaType(name = "string")
    protected NodeHashType hashType;
    @XmlElement(name = "persist1_name", required = true)
    protected byte[] persist1Name;
    @XmlElement(name = "persist2_name", required = true)
    protected byte[] persist2Name;
    //节点监视器名称
    @XmlElement(required = true)
    protected List<byte[]> monitors;
    @XmlElement(name = "min_monitors", required = true)
    protected BigInteger minMonitors;
    //恢复时间
    @XmlElement(name = "recover_interval")
    @XmlSchemaType(name = "integer")
    protected int recoverInterval;
    
    @XmlElement(name = "stepper_interval")
    @XmlSchemaType(name = "integer")
    protected int stepperInterval;
    //节点池繁忙处理策略
    @XmlElement(name = "sched_method", required = true)
    @XmlSchemaType(name = "string")
    protected SchedMethodType schedMethod;
    @XmlElement(name = "conn_stat_all")
    protected boolean connStatAll;
    @XmlElement(name = "queue_length")
    protected BigInteger queueLength;
    @XmlElement(name = "queue_timeout")
    protected BigInteger queueTimeout;
    @XmlElement(name = "node_info", required = true)
    protected List<NodeInfoType> nodeInfo;

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
     * ��ȡnodeLbMethod���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link NodeLbMethodType }
     *     
     */
    public NodeLbMethodType getNodeLbMethod() {
        return nodeLbMethod;
    }

    /**
     * ����nodeLbMethod���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link NodeLbMethodType }
     *     
     */
    public void setNodeLbMethod(NodeLbMethodType value) {
        this.nodeLbMethod = value;
    }

    /**
     * ��ȡhashType���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link NodeHashType }
     *     
     */
    public NodeHashType getHashType() {
        return hashType;
    }

    /**
     * ����hashType���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link NodeHashType }
     *     
     */
    public void setHashType(NodeHashType value) {
        this.hashType = value;
    }

    /**
     * ��ȡpersist1Name���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getPersist1Name() {
        return persist1Name;
    }

    /**
     * ����persist1Name���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setPersist1Name(byte[] value) {
        this.persist1Name = value;
    }

    /**
     * ��ȡpersist2Name���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getPersist2Name() {
        return persist2Name;
    }

    /**
     * ����persist2Name���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setPersist2Name(byte[] value) {
        this.persist2Name = value;
    }

    /**
     * Gets the value of the monitors property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the monitors property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMonitors().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * byte[]
     * 
     */
    public List<byte[]> getMonitors() {
        if (monitors == null) {
            monitors = new ArrayList<byte[]>();
        }
        return this.monitors;
    }

    /**
     * ��ȡminMonitors���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMinMonitors() {
        return minMonitors;
    }

    /**
     * ����minMonitors���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMinMonitors(BigInteger value) {
        this.minMonitors = value;
    }

    /**
     * ��ȡrecoverInterval���Ե�ֵ��
     * 
     */
    public int getRecoverInterval() {
        return recoverInterval;
    }

    /**
     * ����recoverInterval���Ե�ֵ��
     * 
     */
    public void setRecoverInterval(int value) {
        this.recoverInterval = value;
    }

    /**
     * ��ȡstepperInterval���Ե�ֵ��
     * 
     */
    public int getStepperInterval() {
        return stepperInterval;
    }

    /**
     * ����stepperInterval���Ե�ֵ��
     * 
     */
    public void setStepperInterval(int value) {
        this.stepperInterval = value;
    }

    /**
     * ��ȡschedMethod���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link SchedMethodType }
     *     
     */
    public SchedMethodType getSchedMethod() {
        return schedMethod;
    }

    /**
     * ����schedMethod���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link SchedMethodType }
     *     
     */
    public void setSchedMethod(SchedMethodType value) {
        this.schedMethod = value;
    }

    /**
     * ��ȡconnStatAll���Ե�ֵ��
     * 
     */
    public boolean isConnStatAll() {
        return connStatAll;
    }

    /**
     * ����connStatAll���Ե�ֵ��
     * 
     */
    public void setConnStatAll(boolean value) {
        this.connStatAll = value;
    }

    /**
     * ��ȡqueueLength���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getQueueLength() {
        return queueLength;
    }

    /**
     * ����queueLength���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setQueueLength(BigInteger value) {
        this.queueLength = value;
    }

    /**
     * ��ȡqueueTimeout���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getQueueTimeout() {
        return queueTimeout;
    }

    /**
     * ����queueTimeout���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setQueueTimeout(BigInteger value) {
        this.queueTimeout = value;
    }

    /**
     * Gets the value of the nodeInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nodeInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNodeInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NodeInfoType }
     * 
     * 
     */
    public List<NodeInfoType> getNodeInfo() {
        if (nodeInfo == null) {
            nodeInfo = new ArrayList<NodeInfoType>();
        }
        return this.nodeInfo;
    }

	public void setMonitors(List<byte[]> monitors) {
		this.monitors = monitors;
	}

	public void setNodeInfo(List<NodeInfoType> nodeInfo) {
		this.nodeInfo = nodeInfo;
	}

}
