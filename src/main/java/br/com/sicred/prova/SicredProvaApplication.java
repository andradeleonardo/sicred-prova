package br.com.sicred.prova;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@OpenAPIDefinition
public class SicredProvaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SicredProvaApplication.class, args);
    }

}
