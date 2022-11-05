package com.nttdata.bootcamp.msbankaccount.dto;

import java.util.List;
import com.nttdata.bootcamp.msbankaccount.model.Movement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Class BalanceSummaryDto.
 * BankAccount microservice class BalanceSummaryDto.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@ToString
@Builder
public class BalanceSummaryDto {

    private String documentNumber;
    private List<InfoBankAccount> objBankAccountInfo;
    private List<Movement> movements;

}
