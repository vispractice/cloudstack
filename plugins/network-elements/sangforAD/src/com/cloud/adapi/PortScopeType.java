
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * ����˿�����
 * 
 * <p>PortScopeType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="PortScopeType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="from" type="{urn:sangfor:ad:api:soap:v1:base}SPortType"/&gt;
 *         &lt;element name="to" type="{urn:sangfor:ad:api:soap:v1:base}SPortType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PortScopeType", propOrder = {
    "from",
    "to"
})
public class PortScopeType {

    @XmlSchemaType(name = "integer")
    protected int from;
    @XmlSchemaType(name = "integer")
    protected Integer to;

    /**
     * ��ȡfrom���Ե�ֵ��
     * 
     */
    public int getFrom() {
        return from;
    }

    /**
     * ����from���Ե�ֵ��
     * 
     */
    public void setFrom(int value) {
        this.from = value;
    }

    /**
     * ��ȡto���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTo() {
        return to;
    }

    /**
     * ����to���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTo(Integer value) {
        this.to = value;
    }

}
