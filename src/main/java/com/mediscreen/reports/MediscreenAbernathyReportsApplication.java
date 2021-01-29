package com.mediscreen.reports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MediscreenAbernathyReportsApplication {

    public static void main(final String[] args) {
        SpringApplication.run(MediscreenAbernathyReportsApplication.class,
                args);
    }

//    @Bean
//    public AgeCalculator getAgeCalculator() {
//        return new AgeCalculator();
//    }

}
