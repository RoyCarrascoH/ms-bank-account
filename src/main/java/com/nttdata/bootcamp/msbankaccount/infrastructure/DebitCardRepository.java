package com.nttdata.bootcamp.msbankaccount.infrastructure;

import com.nttdata.bootcamp.msbankaccount.config.WebClientConfig;
import com.nttdata.bootcamp.msbankaccount.model.DebitCard;
import com.nttdata.bootcamp.msbankaccount.util.BankAccountUtil;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class DebitCardRepository {
    @Value("${local.property.host.ms-debit-card}")
    private String propertyHostMsDebitCard;

    @Autowired
    ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

	@CircuitBreaker(name = BankAccountUtil.DEBITCARD_CB, fallbackMethod = "getDefaultByCardNumber")
    public Mono<DebitCard> findByCardNumber(String cardNumber) {
        log.info("ini----findByCardNumber-------: " + propertyHostMsDebitCard);
        WebClientConfig webconfig = new WebClientConfig();
        return webconfig.setUriData("http://" + propertyHostMsDebitCard + ":8086")
                .flatMap(d -> webconfig.getWebclient().get().uri("/api/debitcard/cardNumber/" + cardNumber).retrieve()
                        .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new Exception("Error 400")))
                        .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Exception("Error 500")))
                        .bodyToMono(DebitCard.class)
                        // .transform(it -> reactiveCircuitBreakerFactory.create("parameter-service").run(it, throwable -> Mono.just(new DebitCard())))
                );
    }
    
    public Mono<DebitCard> getDefaultByCardNumber(String cardNumber, Exception e) {
	    return Mono.empty();
    }
}
