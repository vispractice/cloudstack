
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>MonitorType�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="MonitorType"&gt;
 *   &lt;restriction base="{urn:sangfor:ad:api:soap:v1:base}StringType"&gt;
 *     &lt;enumeration value="MONITOR_ICMPV4"/&gt;
 *     &lt;enumeration value="MONITOR_ICMPV6"/&gt;
 *     &lt;enumeration value="MONITOR_CONNECT_TCP"/&gt;
 *     &lt;enumeration value="MONITOR_CONNECT_UDP"/&gt;
 *     &lt;enumeration value="MONITOR_SNMP"/&gt;
 *     &lt;enumeration value="MONITOR_DNS"/&gt;
 *     &lt;enumeration value="MONITOR_RADIUS"/&gt;
 *     &lt;enumeration value="MONITOR_FTP"/&gt;
 *     &lt;enumeration value="MONITOR_TCP_HALF_OPEN"/&gt;
 *     &lt;enumeration value="MONITOR_CONNECT_HTTP"/&gt;
 *     &lt;enumeration value="MONITOR_CONNECT_SSL"/&gt;
 *     &lt;enumeration value="MONITOR_CONNECT_HTTPS"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "MonitorType")
@XmlEnum
public enum MonitorType {

    @XmlEnumValue("MONITOR_ICMPV4")
    MONITOR_ICMPV_4("MONITOR_ICMPV4"),
    @XmlEnumValue("MONITOR_ICMPV6")
    MONITOR_ICMPV_6("MONITOR_ICMPV6"),
    MONITOR_CONNECT_TCP("MONITOR_CONNECT_TCP"),
    MONITOR_CONNECT_UDP("MONITOR_CONNECT_UDP"),
    MONITOR_SNMP("MONITOR_SNMP"),
    MONITOR_DNS("MONITOR_DNS"),
    MONITOR_RADIUS("MONITOR_RADIUS"),
    MONITOR_FTP("MONITOR_FTP"),
    MONITOR_TCP_HALF_OPEN("MONITOR_TCP_HALF_OPEN"),
    MONITOR_CONNECT_HTTP("MONITOR_CONNECT_HTTP"),
    MONITOR_CONNECT_SSL("MONITOR_CONNECT_SSL"),
    MONITOR_CONNECT_HTTPS("MONITOR_CONNECT_HTTPS");
    private final String value;

    MonitorType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MonitorType fromValue(String v) {
        for (MonitorType c: MonitorType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
