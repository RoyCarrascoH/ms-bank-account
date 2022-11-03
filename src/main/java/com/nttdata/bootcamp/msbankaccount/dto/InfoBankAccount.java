package com.nttdata.bootcamp.msbankaccount.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

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
