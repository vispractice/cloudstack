
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>NodeCurrentStatusType�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="NodeCurrentStatusType"&gt;
 *   &lt;restriction base="{urn:sangfor:ad:api:soap:v1:base}StringType"&gt;
 *     &lt;enumeration value="NODE_NORMAL"/&gt;
 *     &lt;enumeration value="NODE_BUSY"/&gt;
 *     &lt;enumeration value="NODE_FAIL"/&gt;
 *     &lt;enumeration value="NODE_UNAVAILABLE"/&gt;
 *     &lt;enumeration value="NODE_DISABLE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "NodeCurrentStatusType")
@XmlEnum
public enum NodeCurrentStatusType {

    NODE_NORMAL,
    NODE_BUSY,
    NODE_FAIL,
    NODE_UNAVAILABLE,
    NODE_DISABLE;

    public String value() {
        return name();
    }

    public static NodeCurrentStatusType fromValue(String v) {
        return valueOf(v);
    }

}
