
package com.cloud.adapi;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * ���������Ϣ�б�����
 * 
 * <p>VsBaseInfoListType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="VsBaseInfoListType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="vs_base_info" type="{urn:sangfor:ad:api:soap:v1:base}VsBaseInfoType" maxOccurs="50" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VsBaseInfoListType", propOrder = {
    "vsBaseInfo"
})
public class VsBaseInfoListType {

    @XmlElement(name = "vs_base_info")
    protected List<VsBaseInfoType> vsBaseInfo;

    /**
     * Gets the value of the vsBaseInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the vsBaseInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVsBaseInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VsBaseInfoType }
     * 
     * 
     */
    public List<VsBaseInfoType> getVsBaseInfo() {
        if (vsBaseInfo == null) {
            vsBaseInfo = new ArrayList<VsBaseInfoType>();
        }
        return this.vsBaseInfo;
    }

}
