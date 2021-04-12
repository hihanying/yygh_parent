package top.hihanying.yygh.hosp.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("top.hihanying.yygh.hosp.mapper")
@ComponentScan("top.hihanying") // 扫描swagger
public class HospConfig {

}
