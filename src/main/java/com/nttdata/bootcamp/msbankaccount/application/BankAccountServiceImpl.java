package com.nttdata.bootcamp.msbankaccount.application;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;
import com.nttdata.bootcamp.msbankaccount.model.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import com.nttdata.bootcamp.msbankaccount.infrastructure.BankAccountRepository;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Override
    public Flux<BankAccount> findAll() {
        return bankAccountRepository.findAll();
    }

    @Override
    public Mono<BankAccount> findById(String IdClient) {
        return bankAccountRepository.findById(IdClient);
    }

    @Override
    public Mono<BankAccount> save(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public Mono<Void> delete(BankAccount bankAccount) {
        return bankAccountRepository.delete(bankAccount);
    }

}
