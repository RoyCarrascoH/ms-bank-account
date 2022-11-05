package com.nttdata.bootcamp.msbankaccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Class MsBankAccountApplication main.
 * BankAccount microservice class MsBankAccountApplication.
 */
@SpringBootApplication
@EnableEurekaClient
public class MsBankAccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsBankAccountApplication.class, args);
    }

}