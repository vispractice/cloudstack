
package com.cloud.adapi;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * �ڵ�ػ���Ϣ����
 * 
 * <p>NodePoolInfoType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="NodePoolInfoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="node_pool_name" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType"/&gt;
 *         &lt;element name="node_number" type="{urn:sangfor:ad:api:soap:v1:base}IntegerType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NodePoolInfoType", propOrder = {
    "nodePoolName",
    "nodeNumber"
})
public class NodePoolInfoType {

    @XmlElement(name = "node_pool_name", required = true)
    protected byte[] nodePoolName;
    @XmlElement(name = "node_number", required = true)
    protected BigInteger nodeNumber;

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
     * ��ȡnodeNumber���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNodeNumber() {
        return nodeNumber;
    }

    /**
     * ����nodeNumber���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNodeNumber(BigInteger value) {
        this.nodeNumber = value;
    }

}
