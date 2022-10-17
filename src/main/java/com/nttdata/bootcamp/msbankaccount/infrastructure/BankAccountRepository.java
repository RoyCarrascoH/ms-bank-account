package com.nttdata.bootcamp.msbankaccount.infrastructure;

import com.nttdata.bootcamp.msbankaccount.model.BankAccount;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankAccountRepository extends ReactiveMongoRepository<BankAccount, String> {

    @Query(value = "{'client.documentNumber' : ?0, accountType: ?1 }")
    Flux<BankAccount> findByAccountClient(String documentNumber, String accountType);
    Mono<BankAccount> findByAccountNumber(String accountNumber);

}
