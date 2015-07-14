
package com.cloud.adapi;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * ������Ϣ���ͣ���������ʱʹ�ã�
 * 
 * <p>ServiceInfoType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="ServiceInfoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="name" type="{urn:sangfor:ad:api:soap:v1:base}CommonNameType"/&gt;
 *         &lt;element name="type" type="{urn:sangfor:ad:api:soap:v1:base}ServiceType"/&gt;
 *         &lt;element name="port_scope" type="{urn:sangfor:ad:api:soap:v1:base}PortScopeType" maxOccurs="16"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceInfoType", propOrder = {
    "name",
    "type",
    "portScope"
})
public class ServiceInfoType {

    @XmlElement(required = true)
    protected byte[] name;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected ServiceType type;
    @XmlElement(name = "port_scope", required = true)
    protected List<PortScopeType> portScope;

    /**
     * ��ȡname���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getName() {
        return name;
    }

    /**
     * ����name���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setName(byte[] value) {
        this.name = value;
    }

    /**
     * ��ȡtype���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ServiceType }
     *     
     */
    public ServiceType getType() {
        return type;
    }

    /**
     * ����type���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceType }
     *     
     */
    public void setType(ServiceType value) {
        this.type = value;
    }

    /**
     * Gets the value of the portScope property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the portScope property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPortScope().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PortScopeType }
     * 
     * 
     */
    public List<PortScopeType> getPortScope() {
        if (portScope == null) {
            portScope = new ArrayList<PortScopeType>();
        }
        return this.portScope;
    }

	public void setPortScope(List<PortScopeType> portScope) {
		this.portScope = portScope;
	}

}
