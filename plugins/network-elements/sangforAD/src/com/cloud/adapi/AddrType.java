
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>AddrType�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="AddrType"&gt;
 *   &lt;restriction base="{urn:sangfor:ad:api:soap:v1:base}StringType"&gt;
 *     &lt;enumeration value="ADDR_ALL"/&gt;
 *     &lt;enumeration value="ADDR_ONE"/&gt;
 *     &lt;enumeration value="ADDR_SCOPE"/&gt;
 *     &lt;enumeration value="ADDR_SUBNET"/&gt;
 *     &lt;enumeration value="ADDR_ISP"/&gt;
 *     &lt;enumeration value="ADDR_INSIDE_GROUP"/&gt;
 *     &lt;enumeration value="ADDR_DOMAIN"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AddrType")
@XmlEnum
public enum AddrType {

    ADDR_ALL,
    ADDR_ONE,
    ADDR_SCOPE,
    ADDR_SUBNET,
    ADDR_ISP,
    ADDR_INSIDE_GROUP,
    ADDR_DOMAIN;

    public String value() {
        return name();
    }

    public static AddrType fromValue(String v) {
        return valueOf(v);
    }

}
