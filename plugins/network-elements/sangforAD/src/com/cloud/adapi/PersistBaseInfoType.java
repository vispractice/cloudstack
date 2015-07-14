
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * �Ự���ֻ���Ϣ���ͣ��鿴�б�ʱʹ�ã�
 * 
 * <p>PersistBaseInfoType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="PersistBaseInfoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="persist_name" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType"/&gt;
 *         &lt;element name="p_type" type="{urn:sangfor:ad:api:soap:v1:base}PersistType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersistBaseInfoType", propOrder = {
    "persistName",
    "pType"
})
public class PersistBaseInfoType {

    @XmlElement(name = "persist_name", required = true)
    protected byte[] persistName;
    @XmlElement(name = "p_type", required = true)
    @XmlSchemaType(name = "string")
    protected PersistType pType;

    /**
     * ��ȡpersistName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getPersistName() {
        return persistName;
    }

    /**
     * ����persistName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setPersistName(byte[] value) {
        this.persistName = value;
    }

    /**
     * ��ȡpType���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link PersistType }
     *     
     */
    public PersistType getPType() {
        return pType;
    }

    /**
     * ����pType���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link PersistType }
     *     
     */
    public void setPType(PersistType value) {
        this.pType = value;
    }

}
