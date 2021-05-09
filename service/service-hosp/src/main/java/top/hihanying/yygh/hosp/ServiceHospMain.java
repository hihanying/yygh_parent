package top.hihanying.yygh.hosp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description: 启动类
 * 访问：http://localhost:8201/swagger-ui.html#/
 * @author ying Han
 * @date 23:17 - 2021/4/11
 */
@SpringBootApplication
public class ServiceHospMain {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospMain.class, args);
    }
}
