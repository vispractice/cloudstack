
package com.cloud.adapi;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * ��������
 * 
 * <p>ErrType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="ErrType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="err_code" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *         &lt;element name="err_info" type="{urn:sangfor:ad:api:soap:v1:base}StringType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ErrType", propOrder = {
    "errCode",
    "errInfo"
})
public class ErrType {

    @XmlElement(name = "err_code", required = true)
    protected BigInteger errCode;
    @XmlElement(name = "err_info", required = true)
    protected String errInfo;

    /**
     * ��ȡerrCode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getErrCode() {
        return errCode;
    }

    /**
     * ����errCode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setErrCode(BigInteger value) {
        this.errCode = value;
    }

    /**
     * ��ȡerrInfo���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrInfo() {
        return errInfo;
    }

    /**
     * ����errInfo���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrInfo(String value) {
        this.errInfo = value;
    }

}
