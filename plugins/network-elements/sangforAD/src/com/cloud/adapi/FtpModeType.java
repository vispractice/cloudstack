
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>FtpModeType�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="FtpModeType"&gt;
 *   &lt;restriction base="{urn:sangfor:ad:api:soap:v1:base}StringType"&gt;
 *     &lt;enumeration value="PASSIVE_MODE_TYPE"/&gt;
 *     &lt;enumeration value="PORT_MODE_TYPE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "FtpModeType")
@XmlEnum
public enum FtpModeType {

    PASSIVE_MODE_TYPE,
    PORT_MODE_TYPE;

    public String value() {
        return name();
    }

    public static FtpModeType fromValue(String v) {
        return valueOf(v);
    }

}
