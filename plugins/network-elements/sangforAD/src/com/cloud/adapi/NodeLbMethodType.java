
package com.cloud.adapi;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>NodeLbMethodType�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="NodeLbMethodType"&gt;
 *   &lt;restriction base="{urn:sangfor:ad:api:soap:v1:base}StringType"&gt;
 *     &lt;enumeration value="NODE_LB_RR"/&gt;
 *     &lt;enumeration value="NODE_LB_WRR"/&gt;
 *     &lt;enumeration value="NODE_LB_LEAST_CONN"/&gt;
 *     &lt;enumeration value="NODE_LB_FAST_RESP"/&gt;
 *     &lt;enumeration value="NODE_LB_FEED_BACK"/&gt;
 *     &lt;enumeration value="NODE_LB_PRIOR"/&gt;
 *     &lt;enumeration value="NODE_LB_HASH"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "NodeLbMethodType")
@XmlEnum
public enum NodeLbMethodType {
    NODE_LB_RR,//轮询
    NODE_LB_WRR,//加权轮询
    NODE_LB_LEAST_CONN,//最少连接(包含权值)
    NODE_LB_FAST_RESP,//最快响应时间
    NODE_LB_FEED_BACK,//动态反馈方式
    NODE_LB_PRIOR,//优先级
    NODE_LB_HASH;//哈希

    public String value() {
        return name();
    }

    public static NodeLbMethodType fromValue(String v) {
        return valueOf(v);
    }

}
