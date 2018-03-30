package be.ictdynamic.ES_GEO_POC;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class ESTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(ESTestApplication.class, args);
    }
}
