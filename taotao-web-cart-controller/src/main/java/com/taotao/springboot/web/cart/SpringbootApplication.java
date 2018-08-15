package com.taotao.springboot.web.cart;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * <p>Title: SpringbootApplication</p>
 * <p>Description: </p>
 * <p>Company: bupt.edu.cn</p>
 * <p>Created: 2018-05-11 23:59</p>
 * @author ChengTengfei
 * @version 1.0
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})//取消Druid数据源自动配置
@ComponentScan(basePackages = "com.taotao.springboot.web.cart.*")
@ImportResource({"classpath:/dubbo.xml"})//开启Dubbo原生XML配置
//@EnableDubboConfiguration//开启Dubbo注解配置
public class SpringbootApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringbootApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }

}
