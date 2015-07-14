
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>RadiusAttrType�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="RadiusAttrType"&gt;
 *   &lt;restriction base="{urn:sangfor:ad:api:soap:v1:base}StringType"&gt;
 *     &lt;enumeration value="RADIUS_ATTR_UINT32"/&gt;
 *     &lt;enumeration value="RADIUS_ATTR_ADDR"/&gt;
 *     &lt;enumeration value="RADIUS_ATTR_STRING"/&gt;
 *     &lt;enumeration value="RADIUS_ATTR_BYTE_STRING"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RadiusAttrType")
@XmlEnum
public enum RadiusAttrType {

    @XmlEnumValue("RADIUS_ATTR_UINT32")
    RADIUS_ATTR_UINT_32("RADIUS_ATTR_UINT32"),
    RADIUS_ATTR_ADDR("RADIUS_ATTR_ADDR"),
    RADIUS_ATTR_STRING("RADIUS_ATTR_STRING"),
    RADIUS_ATTR_BYTE_STRING("RADIUS_ATTR_BYTE_STRING");
    private final String value;

    RadiusAttrType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RadiusAttrType fromValue(String v) {
        for (RadiusAttrType c: RadiusAttrType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
