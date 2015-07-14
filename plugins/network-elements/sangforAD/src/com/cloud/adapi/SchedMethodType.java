
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>SchedMethodType�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="SchedMethodType"&gt;
 *   &lt;restriction base="{urn:sangfor:ad:api:soap:v1:base}StringType"&gt;
 *     &lt;enumeration value="SCHED_METHOD_FAIELD"/&gt;
 *     &lt;enumeration value="SCHED_METHOD_FORCE"/&gt;
 *     &lt;enumeration value="SCHED_METHOD_QUEUE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "SchedMethodType")
@XmlEnum
public enum SchedMethodType {

    SCHED_METHOD_FAIELD,
    SCHED_METHOD_FORCE,
    SCHED_METHOD_QUEUE;

    public String value() {
        return name();
    }

    public static SchedMethodType fromValue(String v) {
        return valueOf(v);
    }

}
