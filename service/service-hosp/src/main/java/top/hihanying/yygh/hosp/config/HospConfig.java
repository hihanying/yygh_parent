package top.hihanying.yygh.hosp.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDiscoveryClient // 开启Nacas注册发现
@EnableFeignClients(basePackages = "top.hihanying.yygh")
@ComponentScan(basePackages = "top.hihanying.yygh") // 扫描swagger
public class HospConfig {

}
