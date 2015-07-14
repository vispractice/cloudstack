
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>NodeHashType�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="NodeHashType"&gt;
 *   &lt;restriction base="{urn:sangfor:ad:api:soap:v1:base}StringType"&gt;
 *     &lt;enumeration value="HASH_SRC_IP"/&gt;
 *     &lt;enumeration value="HASH_SRC_IPPORT"/&gt;
 *     &lt;enumeration value="HASH_URI"/&gt;
 *     &lt;enumeration value="HASH_HOST"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "NodeHashType")
@XmlEnum
public enum NodeHashType {

    HASH_SRC_IP,
    HASH_SRC_IPPORT,
    HASH_URI,
    HASH_HOST;

    public String value() {
        return name();
    }

    public static NodeHashType fromValue(String v) {
        return valueOf(v);
    }

}
