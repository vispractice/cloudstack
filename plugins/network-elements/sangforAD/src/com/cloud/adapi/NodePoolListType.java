
package com.cloud.adapi;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * �ڵ���б�����
 * 
 * <p>NodePoolListType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="NodePoolListType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="node_pool" type="{urn:sangfor:ad:api:soap:v1:base}NodePoolInfoType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NodePoolListType", propOrder = {
    "nodePool"
})
public class NodePoolListType {

    @XmlElement(name = "node_pool")
    protected List<NodePoolInfoType> nodePool;

    /**
     * Gets the value of the nodePool property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nodePool property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNodePool().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NodePoolInfoType }
     * 
     * 
     */
    public List<NodePoolInfoType> getNodePool() {
        if (nodePool == null) {
            nodePool = new ArrayList<NodePoolInfoType>();
        }
        return this.nodePool;
    }

}
