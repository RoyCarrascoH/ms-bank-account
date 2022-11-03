package com.nttdata.bootcamp.msbankaccount.dto.bean;

import com.nttdata.bootcamp.msbankaccount.exception.ResourceNotFoundException;
import com.nttdata.bootcamp.msbankaccount.model.BankAccount;
import com.nttdata.bootcamp.msbankaccount.model.Client;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@SuperBuilder
@Slf4j
@Getter
@Setter
@ToString
public class SavingAccount extends BankAccountBean {

    @Override
    public Mono<Boolean> validateFields() {
        log.info("SavingAccount validateFields-------: ");
        return Mono.when(validateCommissionByAccountType(), validateMovementsByAccountType()).then(Mono.just(true));
    }
    @Override
    public Mono<Boolean> validateCommissionByAccountType() {
        log.info("ini SavingAccount validateCommissionByAccountType-------: ");
        return Mono.just(this.getAccountType()).flatMap(ct -> {
            this.setCommission(0.0);
            log.info("fn SavingAccount validateCommissionByAccountType-------: ");
            return Mono.just(true);
        });
    }
    @Override
    public Mono<Boolean> validateMovementsByAccountType() {
        log.info("ini SavingAccount validateMovementsByAccountType-------: ");
        return Mono.just(this.getAccountType()).flatMap(ct -> {
            if (this.getMaximumMovement() == null || !(this.getMaximumMovement() > 0)) {
                return Mono.error(new ResourceNotFoundException("Máximo de movimientos", "MaximumMovement", this.getMaximumMovement() == null ? "" : this.getMaximumMovement().toString()));
            }
            log.info("fin SavingAccount validateMovementsByAccountType-------: ");
            return Mono.just(true);
        });
    }
    @Override
    public Mono<BankAccount> MapperToBankAccount(Client client) {
        log.info("ini SavingAccount MapperToBankAccount-------: ");
        BankAccount bankAccount = BankAccount.builder()
                .idBankAccount(this.getIdBankAccount())
                .client(client)
                .accountType(this.getAccountType())
                //.cardNumber(this.getCardNumber())
                .debitCard(this.getDebitCard())
                .accountNumber(this.getAccountNumber())
                .commission(this.getCommission()) // Se setea
                .maximumMovement(this.getMaximumMovement())
                .startingAmount(this.getStartingAmount())
                .currency(this.getCurrency())
                .minimumAmount(this.getMinimumAmount())
                .transactionLimit(this.getTransactionLimit())
                .commissionTransaction(this.getCommissionTransaction())
                .build();
        log.info("fn SavingAccount MapperToBankAccount-------: ");
        return Mono.just(bankAccount);
    }
}
