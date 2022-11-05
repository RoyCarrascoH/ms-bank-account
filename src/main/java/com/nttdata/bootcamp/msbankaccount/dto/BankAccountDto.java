package com.nttdata.bootcamp.msbankaccount.dto;

import java.util.List;
import com.nttdata.bootcamp.msbankaccount.dto.bean.CheckingAccount;
import com.nttdata.bootcamp.msbankaccount.dto.bean.FixedTermAccount;
import com.nttdata.bootcamp.msbankaccount.dto.bean.SavingAccount;
import com.nttdata.bootcamp.msbankaccount.model.Headline;
import com.nttdata.bootcamp.msbankaccount.model.Movement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Class BankAccountDto.
 * BankAccount microservice class BankAccountDto.
 */
@Getter
@Setter
@NoArgsConstructor
@Slf4j
@SuperBuilder
@ToString
public class BankAccountDto {
    private String idBankAccount;

    private String documentNumber;

    private String accountType;

    private String cardNumber;

    private DebitCardDto debitCard;

    private String accountNumber;

    private Double commission;

    private Integer movementDate;

    private Integer maximumMovement;

    private Double startingAmount;

    private List<Headline> listHeadline;

    private List<Headline> listAuthorizedSignatories;

    private String currency;

    private Double minimumAmount;

    private Double transactionLimit;

    private Double commissionTransaction;

    private List<Movement> movements;

    public Mono<SavingAccount> mapperToSavingAccount() {
        log.info("ini mapperToSaving-------this: " + this.toString());
        SavingAccount savingAccount = SavingAccount.builder()
                .idBankAccount(this.getIdBankAccount())
                .documentNumber(this.getDocumentNumber())
                .accountType(this.getAccountType())
                //.cardNumber(this.getCardNumber())
                .debitCard(this.getDebitCard())
                .accountNumber(this.getAccountNumber())
                //.commission(this.getCommission()) // Se setea
                .maximumMovement(this.getMaximumMovement())
                .startingAmount(this.getStartingAmount())
                .currency(this.getCurrency())
                .minimumAmount(this.getMinimumAmount())
                .transactionLimit(this.getTransactionLimit())
                .commissionTransaction(this.getCommissionTransaction())
                .build();
        log.info("fn mapperToSaving-------: ");
        log.info("fn mapperToSaving-------savingAccount: " + savingAccount.toString());
        return Mono.just(savingAccount);
    }
    public Mono<FixedTermAccount> mapperToFixedTermAccount() {
        log.info("ini mapperToFixedTermAccount-------: ");
        FixedTermAccount fixedTermAccount = FixedTermAccount.builder()
                .idBankAccount(this.getIdBankAccount())
                .documentNumber(this.getDocumentNumber())
                .accountType(this.getAccountType())
                //.cardNumber(this.getCardNumber())
                .debitCard(this.getDebitCard())
                .accountNumber(this.getAccountNumber())
                //.commission(this.getCommission()) // Se setea
                .movementDate(this.getMovementDate())
                //.maximumMovement(this.getMaximumMovement())// Se setea
                .startingAmount(this.getStartingAmount())
                .currency(this.getCurrency())
                .minimumAmount(this.getMinimumAmount())
                .transactionLimit(this.getTransactionLimit())
                .commissionTransaction(this.getCommissionTransaction())
                .build();
        log.info("fn mapperToFixedTermAccount-------: ");
        return Mono.just(fixedTermAccount);
    }
    public Mono<CheckingAccount> mapperToCheckingAccount() {
        log.info("ini mapperToCheckingAccount-------: ");
        CheckingAccount checkingAccount = CheckingAccount.builder()
                .idBankAccount(this.getIdBankAccount())
                .documentNumber(this.getDocumentNumber())
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
        log.info("fn mapperToCheckingAccount-------: ");
        return Mono.just(checkingAccount);
    }

}
