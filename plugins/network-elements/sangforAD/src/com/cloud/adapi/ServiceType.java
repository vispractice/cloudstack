
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ServiceType�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="ServiceType"&gt;
 *   &lt;restriction base="{urn:sangfor:ad:api:soap:v1:base}StringType"&gt;
 *     &lt;enumeration value="SRV_UDP"/&gt;
 *     &lt;enumeration value="SRV_TCP"/&gt;
 *     &lt;enumeration value="SRV_HTTP"/&gt;
 *     &lt;enumeration value="SRV_SSL"/&gt;
 *     &lt;enumeration value="SRV_HTTPS"/&gt;
 *     &lt;enumeration value="SRV_RADIUS"/&gt;
 *     &lt;enumeration value="SRV_DNS"/&gt;
 *     &lt;enumeration value="SRV_FTP"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ServiceType")
@XmlEnum
public enum ServiceType {

    SRV_UDP,
    SRV_TCP,
    SRV_HTTP,
    SRV_SSL,
    SRV_HTTPS,
    SRV_RADIUS,
    SRV_DNS,
    SRV_FTP;

    public String value() {
        return name();
    }

    public static ServiceType fromValue(String v) {
        return valueOf(v);
    }

}
