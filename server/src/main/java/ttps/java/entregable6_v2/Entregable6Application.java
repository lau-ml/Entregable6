package ttps.java.entregable6_v2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class}, scanBasePackages = {"ttps.java.entregable6_v2"})
public class Entregable6Application {

    public static void main(String[] args) {
        SpringApplication.run(Entregable6Application.class, args);
    }

}
