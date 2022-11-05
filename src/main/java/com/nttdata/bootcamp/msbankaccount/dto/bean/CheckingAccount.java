package com.nttdata.bootcamp.msbankaccount.dto.bean;

import com.nttdata.bootcamp.msbankaccount.dto.BankAccountDto;
import com.nttdata.bootcamp.msbankaccount.exception.ResourceNotFoundException;
import com.nttdata.bootcamp.msbankaccount.infrastructure.BankAccountRepository;
import com.nttdata.bootcamp.msbankaccount.model.BankAccount;
import com.nttdata.bootcamp.msbankaccount.model.Client;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SuperBuilder
@Slf4j
@Getter
@Setter
@ToString
public class CheckingAccount extends BankAccountBean {

    @Override
    public Mono<Boolean> validateFields() {
        log.info("CheckingAccount validateFields-------: ");
        return Mono.when(validateCommissionByAccountType(), validateMovementsByAccountType()).then(Mono.just(true));
    }
    @Override
    public Mono<Boolean> validateCommissionByAccountType() {
        log.info("ini CheckingAccount validateCommissionByAccountType-------: ");
        return Mono.just(this.getAccountType()).flatMap(ct -> {
            if (this.getCommission() == null || !(this.getCommission() > 0)) {
                return Mono.error(new ResourceNotFoundException("comision", "comision", this.getCommission() == null ? "" : this.getCommission().toString()));
            }
            log.info("fn CheckingAccount validateCommissionByAccountType-------: ");
            return Mono.just(true);
        });
    }
    @Override
    public Mono<Boolean> validateMovementsByAccountType() {
        log.info("ini CheckingAccount validateMovementsByAccountType-------: ");
        return Mono.just(this.getAccountType()).flatMap(ct -> {
            this.setMaximumMovement(null);
            log.info("fin CheckingAccount validateMovementsByAccountType-------: ");
            return Mono.just(true);
        });
    }
    @Override
    public Mono<BankAccount> mapperToBankAccount(Client client) {
        log.info("ini CheckingAccount mapperToBankAccount-------: ");
        BankAccount bankAccount = BankAccount.builder()
                .idBankAccount(this.getIdBankAccount())
                .client(client)
                .accountType(this.getAccountType())
                //.cardNumber(this.getCardNumber())
                .debitCard(this.getDebitCard())
                .accountNumber(this.getAccountNumber())
                .commission(this.getCommission())
                .startingAmount(this.getStartingAmount())
                .currency(this.getCurrency())
                .minimumAmount(this.getMinimumAmount())
                .transactionLimit(this.getTransactionLimit())
                .commissionTransaction(this.getCommissionTransaction())
                .listHeadline(this.getListHeadline())
                .listAuthorizedSignatories(this.getListAuthorizedSignatories())
                .build();
        log.info("fn CheckingAccount mapperToBankAccount-------: ");
        return Mono.just(bankAccount);
    }
}