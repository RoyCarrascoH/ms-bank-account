package com.nttdata.bootcamp.msbankaccount.model;

import java.util.Date;
import java.util.List;

import com.nttdata.bootcamp.msbankaccount.exception.ResourceNotFoundException;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotEmpty;

@Document(collection = "BankAccount")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BankAccount {

    @Id
    private String idBankAccount;

    private Client client;

    @NotEmpty(message = "no debe estar vacío")
    private String accountType;

    private String cardNumber;

    @NotEmpty(message = "no debe estar vacío")
    private String accountNumber;

    private Double commission;

    private Integer movementDate;

    private Integer maximumMovement;

    private List<Headline> listHeadline;

    private List<Headline> listAuthorizedSignatories;

    private Double startingAmount;

    @NotEmpty(message = "no debe estar vacío")
    private String currency;

    private Double minimumAmount;

    private Double  Balance;
}
