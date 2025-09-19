package com.snapp.billsplitter.infrastructure;

import com.snapp.billsplitter.infrastructure.config.property.AuthorizationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AuthorizationProperties.class)
public class BillSplitterApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillSplitterApplication.class, args);
    }

}
