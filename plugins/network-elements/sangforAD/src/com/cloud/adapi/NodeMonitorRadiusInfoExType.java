
package com.cloud.adapi;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Radius�ڵ��������Ϣ����
 * 
 * <p>NodeMonitorRadiusInfoExType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="NodeMonitorRadiusInfoExType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="req_type" type="{urn:sangfor:ad:api:soap:v1:base}RadiusReqType" minOccurs="0"/&gt;
 *         &lt;element name="access_type" type="{urn:sangfor:ad:api:soap:v1:base}RadiusAccessType" minOccurs="0"/&gt;
 *         &lt;element name="user_name" type="{urn:sangfor:ad:api:soap:v1:base}RadiusUsernameType" minOccurs="0"/&gt;
 *         &lt;element name="password" type="{urn:sangfor:ad:api:soap:v1:base}RadiusPasswdType" minOccurs="0"/&gt;
 *         &lt;element name="secret" type="{urn:sangfor:ad:api:soap:v1:base}RadiusSecretType" minOccurs="0"/&gt;
 *         &lt;element name="attrs" type="{urn:sangfor:ad:api:soap:v1:base}RadiusMonitorAttrType" maxOccurs="32" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NodeMonitorRadiusInfoExType", propOrder = {
    "reqType",
    "accessType",
    "userName",
    "password",
    "secret",
    "attrs"
})
public class NodeMonitorRadiusInfoExType {

    @XmlElement(name = "req_type")
    @XmlSchemaType(name = "string")
    protected RadiusReqType reqType;
    @XmlElement(name = "access_type")
    @XmlSchemaType(name = "string")
    protected RadiusAccessType accessType;
    @XmlElement(name = "user_name")
    protected String userName;
    protected String password;
    protected String secret;
    protected List<RadiusMonitorAttrType> attrs;

    /**
     * ��ȡreqType���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link RadiusReqType }
     *     
     */
    public RadiusReqType getReqType() {
        return reqType;
    }

    /**
     * ����reqType���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link RadiusReqType }
     *     
     */
    public void setReqType(RadiusReqType value) {
        this.reqType = value;
    }

    /**
     * ��ȡaccessType���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link RadiusAccessType }
     *     
     */
    public RadiusAccessType getAccessType() {
        return accessType;
    }

    /**
     * ����accessType���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link RadiusAccessType }
     *     
     */
    public void setAccessType(RadiusAccessType value) {
        this.accessType = value;
    }

    /**
     * ��ȡuserName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserName() {
        return userName;
    }

    /**
     * ����userName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * ��ȡpassword���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * ����password���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * ��ȡsecret���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecret() {
        return secret;
    }

    /**
     * ����secret���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecret(String value) {
        this.secret = value;
    }

    /**
     * Gets the value of the attrs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attrs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttrs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RadiusMonitorAttrType }
     * 
     * 
     */
    public List<RadiusMonitorAttrType> getAttrs() {
        if (attrs == null) {
            attrs = new ArrayList<RadiusMonitorAttrType>();
        }
        return this.attrs;
    }

}
