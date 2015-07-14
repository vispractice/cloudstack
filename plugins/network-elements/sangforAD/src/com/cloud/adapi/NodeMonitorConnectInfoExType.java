
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Connect�ڵ��������Ϣ����
 * 
 * <p>NodeMonitorConnectInfoExType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="NodeMonitorConnectInfoExType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="recv_buf_max" type="{urn:sangfor:ad:api:soap:v1:base}RecvBufMaxType" minOccurs="0"/&gt;
 *         &lt;element name="send_msg" type="{urn:sangfor:ad:api:soap:v1:base}SendMsgType" minOccurs="0"/&gt;
 *         &lt;element name="recv_include" type="{urn:sangfor:ad:api:soap:v1:base}RecvCloseMsgType" minOccurs="0"/&gt;
 *         &lt;element name="close_send_msg" type="{urn:sangfor:ad:api:soap:v1:base}RecvCloseMsgType" minOccurs="0"/&gt;
 *         &lt;element name="resp_code" type="{urn:sangfor:ad:api:soap:v1:base}StringType" minOccurs="0"/&gt;
 *         &lt;element name="url_msg" type="{urn:sangfor:ad:api:soap:v1:base}StringType" minOccurs="0"/&gt;
 *         &lt;element name="ssl_cert_id" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType" minOccurs="0"/&gt;
 *         &lt;element name="ssl_ciphers" type="{urn:sangfor:ad:api:soap:v1:base}SslCiphersType" minOccurs="0"/&gt;
 *         &lt;element name="hex" type="{urn:sangfor:ad:api:soap:v1:base}BoolType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NodeMonitorConnectInfoExType", propOrder = {
    "recvBufMax",
    "sendMsg",
    "recvInclude",
    "closeSendMsg",
    "respCode",
    "urlMsg",
    "sslCertId",
    "sslCiphers",
    "hex"
})
public class NodeMonitorConnectInfoExType {

    @XmlElement(name = "recv_buf_max")
    @XmlSchemaType(name = "integer")
    protected Integer recvBufMax;
    @XmlElement(name = "send_msg")
    protected String sendMsg;
    @XmlElement(name = "recv_include")
    protected String recvInclude;
    @XmlElement(name = "close_send_msg")
    protected String closeSendMsg;
    @XmlElement(name = "resp_code")
    protected String respCode;
    @XmlElement(name = "url_msg")
    protected String urlMsg;
    @XmlElement(name = "ssl_cert_id")
    protected byte[] sslCertId;
    @XmlElement(name = "ssl_ciphers")
    protected String sslCiphers;
    protected Boolean hex;

    /**
     * ��ȡrecvBufMax���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRecvBufMax() {
        return recvBufMax;
    }

    /**
     * ����recvBufMax���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRecvBufMax(Integer value) {
        this.recvBufMax = value;
    }

    /**
     * ��ȡsendMsg���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSendMsg() {
        return sendMsg;
    }

    /**
     * ����sendMsg���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSendMsg(String value) {
        this.sendMsg = value;
    }

    /**
     * ��ȡrecvInclude���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecvInclude() {
        return recvInclude;
    }

    /**
     * ����recvInclude���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecvInclude(String value) {
        this.recvInclude = value;
    }

    /**
     * ��ȡcloseSendMsg���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCloseSendMsg() {
        return closeSendMsg;
    }

    /**
     * ����closeSendMsg���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCloseSendMsg(String value) {
        this.closeSendMsg = value;
    }

    /**
     * ��ȡrespCode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRespCode() {
        return respCode;
    }

    /**
     * ����respCode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRespCode(String value) {
        this.respCode = value;
    }

    /**
     * ��ȡurlMsg���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrlMsg() {
        return urlMsg;
    }

    /**
     * ����urlMsg���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrlMsg(String value) {
        this.urlMsg = value;
    }

    /**
     * ��ȡsslCertId���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getSslCertId() {
        return sslCertId;
    }

    /**
     * ����sslCertId���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setSslCertId(byte[] value) {
        this.sslCertId = value;
    }

    /**
     * ��ȡsslCiphers���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSslCiphers() {
        return sslCiphers;
    }

    /**
     * ����sslCiphers���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSslCiphers(String value) {
        this.sslCiphers = value;
    }

    /**
     * ��ȡhex���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isHex() {
        return hex;
    }

    /**
     * ����hex���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHex(Boolean value) {
        this.hex = value;
    }

}
