
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>VsModeType�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="VsModeType"&gt;
 *   &lt;restriction base="{urn:sangfor:ad:api:soap:v1:base}StringType"&gt;
 *     &lt;enumeration value="VS_MODE_L4"/&gt;
 *     &lt;enumeration value="VS_MODE_L7"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "VsModeType")
@XmlEnum
public enum VsModeType {

    @XmlEnumValue("VS_MODE_L4")
    VS_MODE_L_4("VS_MODE_L4"),
    @XmlEnumValue("VS_MODE_L7")
    VS_MODE_L_7("VS_MODE_L7");
    private final String value;

    VsModeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VsModeType fromValue(String v) {
        for (VsModeType c: VsModeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
