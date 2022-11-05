package com.nttdata.bootcamp.msbankaccount.infrastructure;

import com.nttdata.bootcamp.msbankaccount.config.WebClientConfig;
import com.nttdata.bootcamp.msbankaccount.model.Client;
import com.nttdata.bootcamp.msbankaccount.util.BankAccountUtil;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class ClientRepository {

    @Value("${local.property.host.ms-client}")
    private String propertyHostMsClient;

    @Autowired
    ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

    @CircuitBreaker(name = BankAccountUtil.CLIENT_CB, fallbackMethod = "getDefaultClientByDni")
    public Mono<Client> findClientByDni(String documentNumber) {
        WebClientConfig webconfig = new WebClientConfig();
        return webconfig.setUriData("http://" + propertyHostMsClient + ":8080")
                .flatMap(d -> webconfig.getWebclient().get().uri("/api/clients/documentNumber/" + documentNumber).retrieve()
                                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new Exception("Error 400")))
                                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Exception("Error 500")))
                                .bodyToMono(Client.class)
                        // .transform(it -> reactiveCircuitBreakerFactory.create("parameter-service").run(it, throwable -> Mono.just(new Client())))
                );
    }

    @CircuitBreaker(name = BankAccountUtil.CLIENT_CB, fallbackMethod = "getDefaultUpdateProfileClient")
    public Mono<Void> updateProfileClient(String documentNumber, String profile) {
        WebClientConfig webconfig = new WebClientConfig();
        return webconfig.setUriData("http://" + propertyHostMsClient + ":8080")
                .flatMap(d -> webconfig.getWebclient().put()
                                .uri("/api/clients/documentNumber/" + documentNumber + "/profile/" + profile)
                                .accept(MediaType.APPLICATION_JSON).retrieve()
                                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new Exception("Error 400")))
                                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Exception("Error 500")))
                                .bodyToMono(Void.class)
                        // .transform(it -> reactiveCircuitBreakerFactory.create("parameter-service").run(it, throwable -> Mono.empty()))
                );
    }

    public Mono<Client> getDefaultClientByDni(String documentNumber, Exception e) {
        return Mono.empty();
    }

    public Mono<Void> getDefaultUpdateProfileClient(String documentNumber, String profile, Exception e) {
        return Mono.empty();
    }

}
