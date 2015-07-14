
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>PersistType�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="PersistType"&gt;
 *   &lt;restriction base="{urn:sangfor:ad:api:soap:v1:base}StringType"&gt;
 *     &lt;enumeration value="PERSIST_SOURCE_IP"/&gt;
 *     &lt;enumeration value="PERSIST_COOKIE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "PersistType")
@XmlEnum
public enum PersistType {

    PERSIST_SOURCE_IP,
    PERSIST_COOKIE;

    public String value() {
        return name();
    }

    public static PersistType fromValue(String v) {
        return valueOf(v);
    }

}
