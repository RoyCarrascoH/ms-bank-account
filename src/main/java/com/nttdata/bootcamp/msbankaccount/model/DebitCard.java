package com.nttdata.bootcamp.msbankaccount.model;

import org.springframework.data.annotation.Id;
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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DebitCard {

    @Id
    private String idDebitCard;
    private String cardNumber;
    private Boolean state;

}
