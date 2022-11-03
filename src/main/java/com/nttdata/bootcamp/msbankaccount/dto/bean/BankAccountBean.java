package com.nttdata.bootcamp.msbankaccount.dto.bean;

import com.nttdata.bootcamp.msbankaccount.dto.DebitCardDto;
import com.nttdata.bootcamp.msbankaccount.model.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Slf4j
@SuperBuilder
@ToString
public abstract class BankAccountBean {

    private String idBankAccount;
    private String documentNumber;
    private String accountType;
    //private String cardNumber;
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

    public abstract Mono<Boolean> validateFields();
    public abstract Mono<Boolean> validateCommissionByAccountType();
    public abstract Mono<Boolean> validateMovementsByAccountType();
    public abstract Mono<BankAccount> MapperToBankAccount(Client client);
}