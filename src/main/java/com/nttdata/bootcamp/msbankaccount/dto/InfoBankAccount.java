package com.nttdata.bootcamp.msbankaccount.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Class ErrorDetail.
 * BankAccount microservice class ErrorDetail.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@ToString
@Builder
public class InfoBankAccount {

    private String accountType;
    private String accountNumber;
    private Double averageDailyBalance;

}
