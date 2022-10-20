package com.nttdata.bootcamp.msbankaccount.dto;

import com.nttdata.bootcamp.msbankaccount.exception.ResourceNotFoundException;
import com.nttdata.bootcamp.msbankaccount.model.BankAccount;
import com.nttdata.bootcamp.msbankaccount.model.Client;
import com.nttdata.bootcamp.msbankaccount.model.Headline;
import com.nttdata.bootcamp.msbankaccount.model.Movement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class BankAccountDto {
    private String idBankAccount;

    private String documentNumber;
    @NotEmpty(message = "no debe estar vacío")
    private String accountType;

    private String cardNumber;

    @NotEmpty(message = "no debe estar vacío")
    private String accountNumber;

    private Double commission;

    private Integer movementDate;

    private Integer maximumMovement;

    private Double startingAmount;

    private List<Headline> listHeadline;

    private List<Headline> listAuthorizedSignatories;

    @NotEmpty(message = "no debe estar vacío")
    private String currency;

    private Double minimumAmount;

    private Double transactionLimit;

    private Double commissionTransaction;

    private List<Movement> movements;

    public Mono<Boolean> validateFields() {
        log.info("validateFields-------: ");
        return Mono.when(validateAccountType(), validateCommissionByAccountType(), validateMovementsByAccountType())
                .then(Mono.just(true));
    }

    public Mono<Boolean> validateAccountType() {
        log.info("ini validateAccountType-------: ");
        return Mono.just(this.getAccountType()).flatMap(ct -> {
            Boolean isOk = false;
            if (this.getAccountType().equals("Savings-account")) { // cuenta de ahorros.
                isOk = true;
            } else if (this.getAccountType().equals("FixedTerm-account")) { // cuenta plazos fijos.
                isOk = true;
            } else if (this.getAccountType().equals("Checking-account")) { // current account.
                isOk = true;
            } else if (this.getAccountType().equals("") || this.getAccountType() == null) { // credit card.
                isOk = true;
            } else {
                return Mono.error(new ResourceNotFoundException("Tipo Cuenta", "AccountType", this.getAccountType()));
            }
            log.info("fn validateAccountType-------: ");
            return Mono.just(isOk);
        });
    }

    public Mono<Boolean> validateCommissionByAccountType() {
        log.info("ini validateCommissionByAccountType-------: ");
        return Mono.just(this.getAccountType()).flatMap(ct -> {
            Boolean isOk = false;
            if (this.getAccountType().equals("Savings-account")) { // cuenta de ahorros.
                this.setCommission(0.0);
                isOk = true;
            } else if (this.getAccountType().equals("FixedTerm-account")) { // cuenta plazos fijos.
                this.setCommission(0.0);
                isOk = true;
            } else if (this.getAccountType().equals("Checking-account")) { // current account.
                if (this.getCommission() == null || !(this.getCommission() > 0)) {
                    return Mono.error(new ResourceNotFoundException("comision", "comision", this.getCommission() == null ? "" : this.getCommission().toString()));
                }
                isOk = true;
            } else {
                return Mono.error(new ResourceNotFoundException("Tipo Cuenta", "AccountType", this.getAccountType()));
            }
            log.info("fn validateCommissionByAccountType-------: ");
            return Mono.just(isOk);
        });
    }

    public Mono<Boolean> validateMovementsByAccountType() {
        log.info("ini validateMovementsByAccountType-------: ");
        return Mono.just(this.getAccountType()).flatMap(ct -> {
            Boolean isOk = false;
            if (this.getAccountType().equals("Savings-account")) { // cuenta de ahorros.
                if (this.getMaximumMovement() == null || !(this.getMaximumMovement() > 0)) {
                    return Mono.error(new ResourceNotFoundException("Máximo de movimientos", "MaximumMovement", this.getMaximumMovement() == null ? "" : this.getMaximumMovement().toString()));
                }
                isOk = true;
            } else if (this.getAccountType().equals("FixedTerm-account")) { // cuenta plazos fijos.
                log.info("-- validateMovementsByAccountType------- set setMaximumMovement: ");
                this.setMaximumMovement(1);
                if (this.getMovementDate() == null || !(this.getMovementDate() > 0)) {
                    return Mono.error(new ResourceNotFoundException("Fecha de movimientos", "MovementDate", this.getMovementDate() == null ? "" : this.getMovementDate().toString()));
                }
                isOk = true;
            } else if (this.getAccountType().equals("Checking-account")) { // cuenta corriente.
                this.setMaximumMovement(null);
                isOk = true;
            } else {
                return Mono.error(new ResourceNotFoundException("Tipo Cuenta", "AccountType", this.getAccountType()));
            }
            log.info("fin validateMovementsByAccountType-------: ");
            return Mono.just(isOk);
        });
    }

    public Mono<BankAccount> MapperToBankAccount(Client client) {
        log.info("ini MapperToBankAccount-------: ");
        BankAccount bankAccount = BankAccount.builder()
                //.idBankAccount(this.getIdBankAccount())
                .client(client)
                .accountType(this.getAccountType())
                .cardNumber(this.getCardNumber())
                .accountNumber(this.getAccountNumber())
                .commission(this.getCommission())
                .movementDate(this.getMovementDate())
                .maximumMovement(this.getMaximumMovement())
                .listHeadline(this.getListHeadline())
                .listAuthorizedSignatories(this.getListAuthorizedSignatories())
                .startingAmount(this.getStartingAmount())
                .currency(this.getCurrency())
                .minimumAmount(this.getMinimumAmount())
                .transactionLimit(this.getTransactionLimit())
                .commissionTransaction(this.getCommissionTransaction())
                .build();
        log.info("fn MapperToBankAccount-------: ");
        return Mono.just(bankAccount);
    }
}
