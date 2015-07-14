
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>RadiusAccessType�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="RadiusAccessType"&gt;
 *   &lt;restriction base="{urn:sangfor:ad:api:soap:v1:base}StringType"&gt;
 *     &lt;enumeration value="ACCESS_TYPE_PAP"/&gt;
 *     &lt;enumeration value="ACCESS_TYPE_CHAP"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RadiusAccessType")
@XmlEnum
public enum RadiusAccessType {

    ACCESS_TYPE_PAP,
    ACCESS_TYPE_CHAP;

    public String value() {
        return name();
    }

    public static RadiusAccessType fromValue(String v) {
        return valueOf(v);
    }

}
