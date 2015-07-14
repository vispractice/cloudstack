
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>RadiusReqType�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="RadiusReqType"&gt;
 *   &lt;restriction base="{urn:sangfor:ad:api:soap:v1:base}StringType"&gt;
 *     &lt;enumeration value="ACCESS_REQUEST"/&gt;
 *     &lt;enumeration value="ACCOUNTING_REQUEST"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RadiusReqType")
@XmlEnum
public enum RadiusReqType {

    ACCESS_REQUEST,
    ACCOUNTING_REQUEST;

    public String value() {
        return name();
    }

    public static RadiusReqType fromValue(String v) {
        return valueOf(v);
    }

}
