
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * AD��֤����
 * 
 * <p>ADAuthType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="ADAuthType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="username" type="{urn:sangfor:ad:api:soap:v1:base}UsernameType"/&gt;
 *         &lt;element name="passwd" type="{urn:sangfor:ad:api:soap:v1:base}PasswdType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ADAuthType", propOrder = {
    "username",
    "passwd"
})
public class ADAuthType {

    @XmlElement(required = true)
    protected byte[] username;
    @XmlElement(required = true)
    protected byte[] passwd;
    
    public ADAuthType(){}
    
    public ADAuthType(byte[] username,byte[] passwd){
    	this.username = username;
    	this.passwd = passwd;
    }
    /**
     * ��ȡusername���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getUsername() {
        return username;
    }

    /**
     * ����username���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setUsername(byte[] value) {
        this.username = value;
    }

    /**
     * ��ȡpasswd���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getPasswd() {
        return passwd;
    }

    /**
     * ����passwd���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setPasswd(byte[] value) {
        this.passwd = value;
    }

}
