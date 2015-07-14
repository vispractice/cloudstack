
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * ftp�ڵ��������Ϣ����
 * 
 * <p>NodeMonitorFtpInfoExType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="NodeMonitorFtpInfoExType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="uname" type="{urn:sangfor:ad:api:soap:v1:base}StringType" minOccurs="0"/&gt;
 *         &lt;element name="upwd" type="{urn:sangfor:ad:api:soap:v1:base}FtpPwdType" minOccurs="0"/&gt;
 *         &lt;element name="path" type="{urn:sangfor:ad:api:soap:v1:base}FtpPathType" minOccurs="0"/&gt;
 *         &lt;element name="mode" type="{urn:sangfor:ad:api:soap:v1:base}FtpModeType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NodeMonitorFtpInfoExType", propOrder = {
    "uname",
    "upwd",
    "path",
    "mode"
})
public class NodeMonitorFtpInfoExType {

    protected String uname;
    protected String upwd;
    protected String path;
    @XmlSchemaType(name = "string")
    protected FtpModeType mode;

    /**
     * ��ȡuname���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUname() {
        return uname;
    }

    /**
     * ����uname���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUname(String value) {
        this.uname = value;
    }

    /**
     * ��ȡupwd���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUpwd() {
        return upwd;
    }

    /**
     * ����upwd���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUpwd(String value) {
        this.upwd = value;
    }

    /**
     * ��ȡpath���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPath() {
        return path;
    }

    /**
     * ����path���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPath(String value) {
        this.path = value;
    }

    /**
     * ��ȡmode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link FtpModeType }
     *     
     */
    public FtpModeType getMode() {
        return mode;
    }

    /**
     * ����mode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link FtpModeType }
     *     
     */
    public void setMode(FtpModeType value) {
        this.mode = value;
    }

}
