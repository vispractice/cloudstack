
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>NodeConfStatusType�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="NodeConfStatusType"&gt;
 *   &lt;restriction base="{urn:sangfor:ad:api:soap:v1:base}StringType"&gt;
 *     &lt;enumeration value="NODE_ENABLE"/&gt;
 *     &lt;enumeration value="NODE_DISABLE"/&gt;
 *     &lt;enumeration value="NODE_OFFLINE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "NodeConfStatusType")
@XmlEnum
public enum NodeConfStatusType {

    NODE_ENABLE,
    NODE_DISABLE,
    NODE_OFFLINE;

    public String value() {
        return name();
    }

    public static NodeConfStatusType fromValue(String v) {
        return valueOf(v);
    }

}
