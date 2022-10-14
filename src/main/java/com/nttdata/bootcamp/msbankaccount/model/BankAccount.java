package com.nttdata.bootcamp.msbankaccount.model;

import java.util.Date;
import java.util.List;
import lombok.Setter;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Document(collection = "BankAccount")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {

    @Id
    private String idBankAccount;

    @NotEmpty(message = "no debe estar vacío")
    private String accountType;

    private Integer cardNumber;

    @NotEmpty(message = "no debe estar vacío")
    private Integer accountNumber;

    private Double commission;

    private Date movementDate;

    private Integer maximumMovement;

    private List<Headline> listHeadline;

    private List<Headline> listAuthorizedSignatories;

    private Double startingAmount;

    @NotEmpty(message = "no debe estar vacío")
    private String currency;

}
