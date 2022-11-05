package com.nttdata.bootcamp.msbankaccount.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class Headline.
 * BankAccount microservice class Headline.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Headline {
    private String names;
    private String surnames;
    private String documentType;
    private String documentNumber;
}
