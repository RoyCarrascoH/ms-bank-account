package com.nttdata.bootcamp.msbankaccount.application;

import com.nttdata.bootcamp.msbankaccount.dto.BankAccountDto;
import com.nttdata.bootcamp.msbankaccount.model.BankAccount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Class BankAccountService.
 * BankAccount microservice class BankAccountService.
 */
public interface BankAccountService {
    public Flux<BankAccount> findAll();

    public Mono<BankAccount> findById(String idBankAccount);

    public Mono<BankAccount> findByAccountNumber(String accountNumber);

    public Flux<BankAccount> findByDocumentNumber(String accountNumber, String accountType);

    public Mono<BankAccount> save(BankAccountDto bankAccountDto);

    public Mono<BankAccount> update(BankAccountDto bankAccountDto, String idBankAccount);

    public Mono<Void> delete(String idBankAccount);

    public Mono<BankAccountDto> findMovementsByDocumentNumber(
            String documentNumber, String accountNumber);

    public Flux<BankAccount> findByDocumentNumberAndWithdrawalAmount(
            String accountNumber, String cardNumber, Double withdrawalAmount);

    public Mono<BankAccount> updateBalanceById(String idBankAccount, Double balance);

    public Mono<BankAccount> findByDebitCardNumberAndIsMainAccount(String debitCardNumber);

    public Flux<BankAccount> findBankAccountBalanceByDocumentNumber(String documentNumber);

}
