package com.nttdata.bootcamp.msbankaccount.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import com.nttdata.bootcamp.msbankaccount.dto.DebitCardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Class DebitCard.
 * BankAccount microservice class DebitCard.
 */
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

    // private String cardNumber;
    private DebitCardDto debitCard;

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

    private Double transactionLimit;

    private Double commissionTransaction;

    private Double balance;
}
