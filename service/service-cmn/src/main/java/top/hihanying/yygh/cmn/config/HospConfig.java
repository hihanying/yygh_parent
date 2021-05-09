package top.hihanying.yygh.cmn.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "top.hihanying.yygh") // 扫描swagger
@EnableDiscoveryClient
public class HospConfig {

}
