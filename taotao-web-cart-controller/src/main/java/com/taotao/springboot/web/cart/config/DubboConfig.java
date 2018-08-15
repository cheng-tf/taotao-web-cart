package com.taotao.springboot.web.cart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * <p>Title: DubboConfig</p>
 * <p>Description: </p>
 * <p>Company: bupt.edu.cn</p>
 * <p>Created: 2018-08-11 21:43</p>
 * @author ChengTengfei
 * @version 1.0
 */
@Configuration
//@PropertySource("classpath:dubbo/dubbo.properties")
@ImportResource({"classpath:/dubbo.xml"})
public class DubboConfig {
}
