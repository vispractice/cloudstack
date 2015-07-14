
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>HttpSchedModeType�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="HttpSchedModeType"&gt;
 *   &lt;restriction base="{urn:sangfor:ad:api:soap:v1:base}StringType"&gt;
 *     &lt;enumeration value="HTTP_SCHED_MODE_FIRST_REQ"/&gt;
 *     &lt;enumeration value="HTTP_SCHED_MODE_EVERY_REQ"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "HttpSchedModeType")
@XmlEnum
public enum HttpSchedModeType {
	//首个请求
    HTTP_SCHED_MODE_FIRST_REQ,
    //每一个请求
    HTTP_SCHED_MODE_EVERY_REQ;

    public String value() {
        return name();
    }

    public static HttpSchedModeType fromValue(String v) {
        return valueOf(v);
    }

}
