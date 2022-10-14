package com.nttdata.bootcamp.msbankaccount.application;

import com.nttdata.bootcamp.msbankaccount.model.BankAccount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankAccountService {

    public Flux<BankAccount> findAll();

    public Mono<BankAccount> findById(String idBankAccount);

    public Mono<BankAccount> save(BankAccount bankAccount);

    public Mono<Void> delete(BankAccount bankAccount);
}
