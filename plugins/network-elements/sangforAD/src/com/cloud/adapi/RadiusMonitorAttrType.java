
package com.cloud.adapi;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * radius��������
 * 
 * <p>RadiusMonitorAttrType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="RadiusMonitorAttrType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="attr_id" type="{urn:sangfor:ad:api:soap:v1:base}IntegerType" minOccurs="0"/&gt;
 *         &lt;element name="attr_type" type="{urn:sangfor:ad:api:soap:v1:base}RadiusAttrType" minOccurs="0"/&gt;
 *         &lt;element name="attr_int" type="{urn:sangfor:ad:api:soap:v1:base}IntegerType" minOccurs="0"/&gt;
 *         &lt;element name="attr_string" type="{urn:sangfor:ad:api:soap:v1:base}StringType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RadiusMonitorAttrType", propOrder = {
    "attrId",
    "attrType",
    "attrInt",
    "attrString"
})
public class RadiusMonitorAttrType {

    @XmlElement(name = "attr_id")
    protected BigInteger attrId;
    @XmlElement(name = "attr_type")
    @XmlSchemaType(name = "string")
    protected RadiusAttrType attrType;
    @XmlElement(name = "attr_int")
    protected BigInteger attrInt;
    @XmlElement(name = "attr_string")
    protected String attrString;

    /**
     * ��ȡattrId���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAttrId() {
        return attrId;
    }

    /**
     * ����attrId���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAttrId(BigInteger value) {
        this.attrId = value;
    }

    /**
     * ��ȡattrType���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link RadiusAttrType }
     *     
     */
    public RadiusAttrType getAttrType() {
        return attrType;
    }

    /**
     * ����attrType���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link RadiusAttrType }
     *     
     */
    public void setAttrType(RadiusAttrType value) {
        this.attrType = value;
    }

    /**
     * ��ȡattrInt���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAttrInt() {
        return attrInt;
    }

    /**
     * ����attrInt���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAttrInt(BigInteger value) {
        this.attrInt = value;
    }

    /**
     * ��ȡattrString���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttrString() {
        return attrString;
    }

    /**
     * ����attrString���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttrString(String value) {
        this.attrString = value;
    }

}
