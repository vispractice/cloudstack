
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>CookieType�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="CookieType"&gt;
 *   &lt;restriction base="{urn:sangfor:ad:api:soap:v1:base}StringType"&gt;
 *     &lt;enumeration value="PERSIST_COOKIE_INSERT"/&gt;
 *     &lt;enumeration value="PERSIST_COOKIE_STUDY"/&gt;
 *     &lt;enumeration value="PERSIST_COOKIE_REWRITE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "CookieType")
@XmlEnum
public enum CookieType {

    PERSIST_COOKIE_INSERT,
    PERSIST_COOKIE_STUDY,
    PERSIST_COOKIE_REWRITE;

    public String value() {
        return name();
    }

    public static CookieType fromValue(String v) {
        return valueOf(v);
    }

}
