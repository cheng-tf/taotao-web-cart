package com.taotao.springboot.web.cart.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * <p>Title: JacksonUtils</p>
 * <p>Description: JSON工具类（基于Jackson工具包）</p>
 * <p>Company: bupt.edu.cn</p>
 * <p>Created: 2018-05-05 16:33</p>
 * @author ChengTengfei
 * @version 1.0
 */
public class JacksonUtils {

    // 定义Jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 对象-->JSON字符串
     */
    public static String objectToJson(Object data) {
        try {
            return MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * JSON数据-->对象
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            return MAPPER.readValue(jsonData, beanType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * JSON数据-->对象List
     */
    public static <T>List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

